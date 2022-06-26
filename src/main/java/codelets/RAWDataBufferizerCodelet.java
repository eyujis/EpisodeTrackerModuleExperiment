package codelets;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.wme.Idea;
import environment.Environment;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;




public class RAWDataBufferizerCodelet extends Codelet {

    private Environment e;
    private Memory rawDataMO;
    private int buffer_size = 5;
    private int frame_size = 0;
    private List<Double> timestamp = new ArrayList<Double>();
    private List<Double> latitude = new ArrayList<Double>();
    private List<Double> longitude = new ArrayList<Double>();

    // idea_buffer contains "frames" that have an ArrayList of Ideas;
    private Idea idea_buffer = initializeBuffer(buffer_size);



    public RAWDataBufferizerCodelet(Environment env) {
        e = env;
    }

    @Override
    public void accessMemoryObjects() {
        rawDataMO=(MemoryObject)this.getOutput("RAWDATA");
    }

    @Override
    public void proc() {
        String[] raw_data = null;
        // Get next line from the environment.
        // Each line corresponds to a frame.
        try {
            raw_data = e.nextLine();
        } catch(IOException ex){
            System.out.println (ex.toString());
        }

        synchronized (rawDataMO) {

            List<Idea> frames = (List<Idea>) idea_buffer.get("frames").getValue();

            addFrame(frames, raw_data, buffer_size);

            rawDataMO.setI((Idea) idea_buffer);

////          Print that checks if buffer correctly shifts value positions
//            for(int i=0; i<buffer_size; i++) {
//                System.out.println(((List<Idea>) ((Idea) rawDataMO.getI()).get("frames").getValue()).get(i).toStringFull());
//            }
//            System.out.println("-------------------");
        }
    }

    @Override
    public void calculateActivation() {

    }

    public Idea initializeBuffer(int buffer_size) {
        Idea idea_buffer = new Idea("buffer","",0);
        List<Idea> frames = new ArrayList<Idea>();
        for(int i=0; i<buffer_size; i++)    {
            Idea frame = new Idea("frame","",0);
            frame.add(new Idea("timestamp",null));
            frame.add(new Idea("latitude", null));
            frame.add(new Idea("longitude", null));
            frames.add(frame);
        }
        idea_buffer.add(new Idea("frames",frames));
        idea_buffer.add(new Idea("buffer_size", buffer_size));
        return idea_buffer;
    }

    private void addFrame(List<Idea> frames, String [] raw_data, int buffer_size) {

        // shift right position from frames i=buffer_size-1 to i=0
        for(int i=buffer_size-2; i>=0; i--)    {
            // get ith values
            Double ith_timestamp = (Double) frames.get(i).get("timestamp").getValue();
            Double ith_latitude = (Double) frames.get(i).get("latitude").getValue();
            Double ith_longitude = (Double) frames.get(i).get("longitude").getValue();
            // set i+1th values
            frames.get(i+1).get("timestamp").setValue(ith_timestamp);
            frames.get(i+1).get("latitude").setValue(ith_latitude);
            frames.get(i+1).get("longitude").setValue(ith_longitude);
        }

        frames.get(0).get("timestamp").setValue(Double.valueOf(raw_data[0]));
        frames.get(0).get("latitude").setValue(Double.valueOf(raw_data[1]));
        frames.get(0).get("longitude").setValue(Double.valueOf(raw_data[2]));


    }

}
