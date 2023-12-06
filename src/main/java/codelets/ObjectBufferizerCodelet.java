package codelets;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.idea.Idea;
import memory_storage.MemoryInstance;


public class ObjectBufferizerCodelet extends Codelet {

    private Memory detectedObjectsMO;
    private Memory objectBufferMO;

    private int buffer_size = 1;

    private Idea objectsBufferIdea = initializeBuffer(buffer_size);

    private MemoryInstance detectedObjectsMI;
    private MemoryInstance objectBufferMI;

    public ObjectBufferizerCodelet(MemoryInstance detectedObjectsMI, MemoryInstance objectBufferMI) {
        this.detectedObjectsMI = detectedObjectsMI;
        this.objectBufferMI = objectBufferMI;
    }

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
        Idea objectsBufferIdeaList = objectsBufferIdea.get("timeSteps");
        Idea detectedObjectsIdea = detectedObjectsMI.getIdea().get("object");
        addObjectToTimeStep(objectsBufferIdeaList, detectedObjectsIdea);

        objectBufferMI.postIdea((Idea) objectsBufferIdea);
        objectBufferMO.setI("");
    }


    public Idea initializeBuffer(int buffer_size) {
        Idea objectsBufferIdea = new Idea("objects_buffer","",0);
        Idea timeSteps = new Idea("timeSteps", "", 0);
        for(int i=0; i<buffer_size; i++)    {
            Idea timeStep = new Idea("timeStep","",0);
            Idea object = new Idea("object", "", 0);
            object.add(new Idea("time",null));
            object.add(new Idea("latitude", null));
            object.add(new Idea("longitude", null));
            timeStep.getL().add(object);
            timeSteps.getL().add(timeStep);
        }
        objectsBufferIdea.getL().add(timeSteps);
        return objectsBufferIdea;
    }

    private void addObjectToTimeStep(Idea objectsBufferIdeaList, Idea detectedObjectIdea) {

        // shift right position from frames i=buffer_size-1 to i=0
        for(int i=buffer_size-2; i>=0; i--)    {
            // get ith values
            Double ith_timestamp = (Double) objectsBufferIdeaList.getL().get(i).get("object.time").getValue();
            Double ith_latitude = (Double) objectsBufferIdeaList.getL().get(i).get("object.latitude").getValue();
            Double ith_longitude = (Double) objectsBufferIdeaList.getL().get(i).get("object.longitude").getValue();
            // set i+1th values
            objectsBufferIdeaList.getL().get(i+1).get("object.time").setValue(ith_timestamp);
            objectsBufferIdeaList.getL().get(i+1).get("object.latitude").setValue(ith_latitude);
            objectsBufferIdeaList.getL().get(i+1).get("object.longitude").setValue(ith_longitude);
        }

        Double time = (Double) detectedObjectIdea.get("time").getValue();
        Double latitude = (Double) detectedObjectIdea.get("latitude").getValue();
        Double longitude = (Double) detectedObjectIdea.get("longitude").getValue();

        objectsBufferIdeaList.getL().get(0).get("object.time").setValue(time);
        objectsBufferIdeaList.getL().get(0).get("object.latitude").setValue(latitude);
        objectsBufferIdeaList.getL().get(0).get("object.longitude").setValue(longitude);

    }

}
