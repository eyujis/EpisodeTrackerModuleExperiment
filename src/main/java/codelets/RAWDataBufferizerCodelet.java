package codelets;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.idea.Idea;
import environment.CSTCollectorConnector;
import environment.Environment;
import memory_storage.MemoryInstance;

import java.io.IOException;

public class RAWDataBufferizerCodelet extends Codelet {
    private Environment e;

    private Memory rawDataMO;
    private MemoryInstance rawDataMI;

    private int buffer_size = 10;
    private double firstTimestamp;
    private boolean firstCall = true;

    private long lastConnectionTimestamp = -1;


    // idea_buffer contains "frames" that have an ArrayList of Ideas;
    private Idea idea_buffer = initializeBuffer(buffer_size);

    CSTCollectorConnector cstCollectorConnector = new CSTCollectorConnector();


    public RAWDataBufferizerCodelet(MemoryInstance memoryInstance) {
        this.rawDataMI = memoryInstance;
    }

    @Override
    public void accessMemoryObjects() {
        rawDataMO=(MemoryObject)this.getOutput("RAW_DATA_BUFFER");
    }

    @Override
    public void proc() {
        Idea currentPosition = cstCollectorConnector.getCurrentPosition();
        if(currentPosition.getName().equals("empty")){
            return;
        }

        long rvcTimestamp = (long) currentPosition.get("timestamp").getValue();

        if(rvcTimestamp != lastConnectionTimestamp) {
            Idea frames = idea_buffer.get("frames");

            addFrame(frames, currentPosition, buffer_size);
            rawDataMI.postIdea(idea_buffer);
            rawDataMO.setI("");

            System.out.println(currentPosition.toStringFull());

            lastConnectionTimestamp = rvcTimestamp;
        }
    }

    @Override
    public void calculateActivation() {

    }

    public Idea initializeBuffer(int buffer_size) {
        Idea idea_buffer = new Idea("buffer","",0);
        Idea frames = new Idea("frames", "", 0);
        for(int i=0; i<buffer_size; i++)    {
            Idea frame = new Idea("frame","",0);
            frame.add(new Idea("timestamp",null));
            frame.add(new Idea("latitude", null));
            frame.add(new Idea("longitude", null));
            frames.getL().add(frame);
        }
        idea_buffer.add(frames);
        idea_buffer.add(new Idea("buffer_size", buffer_size));
        return idea_buffer;
    }

    private void addFrame(Idea frames, Idea currentPosition, int buffer_size) {

        // shift right position from frames i=buffer_size-1 to i=0
        for(int i=buffer_size-2; i>=0; i--)    {
            // get ith values
            Double ith_timestamp = (Double) frames.getL().get(i).get("timestamp").getValue();
            Double ith_latitude = (Double) frames.getL().get(i).get("latitude").getValue();
            Double ith_longitude = (Double) frames.getL().get(i).get("longitude").getValue();
            // set i+1th values
            frames.getL().get(i+1).get("timestamp").setValue(ith_timestamp);
            frames.getL().get(i+1).get("latitude").setValue(ith_latitude);
            frames.getL().get(i+1).get("longitude").setValue(ith_longitude);
        }

        if (firstCall)  {
            frames.getL().get(0).get("timestamp").setValue(Double.valueOf((Long) currentPosition.get("timestamp").getValue()));
            this.firstTimestamp = Double.valueOf((Long) currentPosition.get("timestamp").getValue());
            firstCall = false;
        }   else {
            frames.getL().get(0).get("timestamp").setValue((Double.valueOf((Long) currentPosition.get("timestamp").getValue())-firstTimestamp)/1000);
        }
        frames.getL().get(0).get("latitude").setValue((Double) currentPosition.get("latitude").getValue());
        frames.getL().get(0).get("longitude").setValue((Double) currentPosition.get("longitude").getValue());

    }

}
