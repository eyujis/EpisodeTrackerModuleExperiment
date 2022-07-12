package codelets;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.idea.Idea;
import environment.Environment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class EventBufferizerCodelet extends Codelet {

    private Environment e;
    private Memory eventsMC;
    private Memory eventsBufferMO;
    private int buffer_size = 5;
    private int frame_size = 0;
    private List<Double> timestamp = new ArrayList<Double>();
    private List<Double> latitude = new ArrayList<Double>();
    private List<Double> longitude = new ArrayList<Double>();

    // idea_buffer contains "frames" that have an ArrayList of Ideas;
    private Idea eventsBuffer = initializeBuffer(buffer_size);



    @Override
    public void accessMemoryObjects() {
        eventsMC=(MemoryContainer)this.getInput("EVENTS");
        eventsBufferMO=(MemoryObject)this.getOutput("EVENTS BUFFER");
    }

    @Override
    public void proc() {
        if (eventsMC.getI()=="")    {
            return;
        }

//        Idea eventFrame = (Idea) eventsMC.getI();
//        List<Idea> eventFrames = (List<Idea>) eventsBuffer.get("frames").getValue();
//        addFrame(eventFrames, eventFrame, buffer_size);
//        eventsBufferMO.setI((Idea) eventsBuffer);

//          Print that checks if buffer correctly shifts value positions
//        for(int i=0; i<buffer_size; i++) {
//            List<Idea> testIdeaList = (List<Idea>) ((Idea) eventsBufferMO.getI()).get("frames").getValue();
//            Idea testFrame = testIdeaList.get(i);
//            Idea frameValue = (Idea) testFrame.getValue();
//            if(frameValue != null)
//                System.out.println(frameValue.getValue());
//
//        }
//        System.out.println("-------------------");

    }

    @Override
    public void calculateActivation() {

    }

    public Idea initializeBuffer(int buffer_size) {
        Idea eventBuffer = new Idea("buffer","",0);
        List<Idea> frames = new ArrayList<Idea>();
        for(int i=0; i<buffer_size; i++)    {
            Idea frame = new Idea("frame", new Idea("event", ""),0);
            frames.add(frame);
        }
        eventBuffer.add(new Idea("frames",frames));
        eventBuffer.add(new Idea("buffer_size", buffer_size));
        return eventBuffer;

    }

    private void addFrame(List<Idea> frames, Idea frame, int buffer_size) {

        // shift right position from frames i=buffer_size-1 to i=0
        for(int i=buffer_size-2; i>=0; i--)    {
            // get ith values
            Idea ith_event = (Idea) ((Idea) frames.get(i)).getValue();

            // set i+1th values
            ((Idea)frames.get(i+1)).setValue(ith_event);
        }

        ((Idea) frames.get(0)).setValue(frame);

    }

}
