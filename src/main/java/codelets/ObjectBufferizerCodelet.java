package codelets;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.idea.Idea;

import java.util.ArrayList;
import java.util.List;



public class ObjectBufferizerCodelet extends Codelet {

    private Memory detectedObjectsMO;
    private Memory objectBufferMO;

    private int buffer_size = 1;

    private Idea objectsBufferIdea = initializeBuffer(buffer_size);

    @Override
    public void accessMemoryObjects() {
        detectedObjectsMO=(MemoryObject)this.getInput("DETECTED_OBJECTS");
        objectBufferMO=(MemoryObject)this.getOutput("OBJECTS_BUFFER");
    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {
        try {
            List<Idea> objectsBufferIdeaList = (List<Idea>) objectsBufferIdea.get("timeSteps").getValue();
            Idea detectedObjectsIdea = ((Idea) detectedObjectsMO.getI()).get("object");
            addObjectToTimeStep(objectsBufferIdeaList, detectedObjectsIdea);
            objectBufferMO.setI((Idea) objectsBufferIdea);

//          Print that checks if buffer correctly shifts value positions
//            for(int i=0; i<buffer_size; i++) {
//                System.out.println(((List<Idea>) ((Idea) objectBufferMO.getI()).get("timeSteps").getValue()).get(i).toStringFull());
//            }
//            System.out.println("-------------------");

        } catch (java.lang.ClassCastException e)  {

        }
    }


    public Idea initializeBuffer(int buffer_size) {
        Idea objectsBufferIdea = new Idea("objects_buffer","",0);
        List<Idea> timeSteps = new ArrayList<Idea>();
        for(int i=0; i<buffer_size; i++)    {
            Idea timeStep = new Idea("timeStep","",0);
            Idea object = new Idea("object", "", 0);
            object.add(new Idea("time",null));
            object.add(new Idea("latitude", null));
            object.add(new Idea("longitude", null));
            timeStep.add(object);
            timeSteps.add(timeStep);
        }
        objectsBufferIdea.add(new Idea("timeSteps",timeSteps));
        return objectsBufferIdea;
    }

    // TODO | Adapt it for a list of objects.
    private void addObjectToTimeStep(List<Idea> objectsBufferIdeaList, Idea detectedObjectIdea) {

        // shift right position from frames i=buffer_size-1 to i=0
        for(int i=buffer_size-2; i>=0; i--)    {
            // get ith values
            Double ith_timestamp = (Double) objectsBufferIdeaList.get(i).get("object.time").getValue();
            Double ith_latitude = (Double) objectsBufferIdeaList.get(i).get("object.latitude").getValue();
            Double ith_longitude = (Double) objectsBufferIdeaList.get(i).get("object.longitude").getValue();
            // set i+1th values
            objectsBufferIdeaList.get(i+1).get("object.time").setValue(ith_timestamp);
            objectsBufferIdeaList.get(i+1).get("object.latitude").setValue(ith_latitude);
            objectsBufferIdeaList.get(i+1).get("object.longitude").setValue(ith_longitude);
        }

        Double time = (Double) detectedObjectIdea.get("time").getValue();
        Double latitude = (Double) detectedObjectIdea.get("latitude").getValue();
        Double longitude = (Double) detectedObjectIdea.get("longitude").getValue();

        objectsBufferIdeaList.get(0).get("object.time").setValue(time);
        objectsBufferIdeaList.get(0).get("object.latitude").setValue(latitude);
        objectsBufferIdeaList.get(0).get("object.longitude").setValue(longitude);

    }

}
