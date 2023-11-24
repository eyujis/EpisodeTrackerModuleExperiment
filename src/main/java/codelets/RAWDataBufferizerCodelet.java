package codelets;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.idea.Idea;
import environment.Environment;
import memory_storage.MemoryInstance;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class RAWDataBufferizerCodelet extends Codelet {
    private Environment e;

    private Memory rawDataMO;
    private MemoryInstance rawDataMI;

    private int buffer_size = 10;
    private double firstTimestamp;
    private boolean firstCall = true;


    // idea_buffer contains "frames" that have an ArrayList of Ideas;
    private Idea idea_buffer = initializeBuffer(buffer_size);


    public RAWDataBufferizerCodelet(Environment env, MemoryInstance memoryInstance) {
        e = env;
        this.rawDataMI = memoryInstance;
    }

    @Override
    public void accessMemoryObjects() {
        rawDataMO=(MemoryObject)this.getOutput("RAW_DATA_BUFFER");
    }

    @Override
    public void proc() {
        String[] raw_data = null;
        // Get next line from the environment.
        // Each line corresponds to a frame.
        try {
            raw_data = e.step();
        } catch(IOException ex){
            System.out.println (ex.toString());
        }

        Idea frames = idea_buffer.get("frames");

        addFrame(frames, raw_data, buffer_size);

        rawDataMI.postIdea(idea_buffer);
        rawDataMO.setI("");
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

    private void addFrame(Idea frames, String [] raw_data, int buffer_size) {

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
            frames.getL().get(0).get("timestamp").setValue(Double.valueOf(0));
            this.firstTimestamp = Double.valueOf(raw_data[0]);
            firstCall = false;
        }   else {
            frames.getL().get(0).get("timestamp").setValue((Double.valueOf(raw_data[0])-firstTimestamp)/1000);
        }
        frames.getL().get(0).get("latitude").setValue(Double.valueOf(raw_data[1]));
        frames.getL().get(0).get("longitude").setValue(Double.valueOf(raw_data[2]));

    }

}
