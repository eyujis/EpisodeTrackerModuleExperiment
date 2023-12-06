package entities;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.idea.Idea;
import memory_storage.MemoryInstance;

public abstract class EventTrackerPropertyCategoryCodelet extends Codelet {

    protected PropertyCategory propertyCategory;

    public Memory objectsBufferMO;
    public Memory eventsMO;
    protected MemoryInstance objectsBufferMI;
    protected MemoryInstance eventsMI;

    public double initialTime=-1;
    public double finalTime;
    private Idea objectsBufferIdeaList;
    private Idea objectInitialState;
    private Idea objectFinalState;

    private double deltaTime = 60;


    public EventTrackerPropertyCategoryCodelet()    {
        this.propertyCategory = propertyCategory;
    }

    @Override
    public void accessMemoryObjects() {
        this.objectsBufferMO=(MemoryObject)this.getInput("OBJECTS_BUFFER");
        this.eventsMO= (MemoryObject) this.getOutput("EVENTS");
    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {
        this.objectsBufferIdeaList = ((Idea) this.objectsBufferMI.getIdea()).get("timeSteps").clone();

        if(this.initialTime==-1)    {
            this.objectInitialState = getObjectCurrentState();
            this.initialTime = getObjectTime(this.objectInitialState);
        }

        this.objectFinalState = getObjectCurrentState();
        this.finalTime = getObjectTime(this.objectFinalState);

        if(this.finalTime-this.initialTime >= this.deltaTime)    {
            Idea objectInitialStateClone = this.objectInitialState.clone();
            Idea objectFinalStateClone = this.objectFinalState.clone();
            if(eventTracked(objectInitialStateClone, objectFinalStateClone))  {
                this.eventsMI.postIdea(this.buildEventIdea(objectInitialStateClone, objectFinalStateClone));
                this.eventsMO.setI("");
            }
            this.initialTime = this.finalTime;
            this.objectInitialState = this.objectFinalState.clone();

        }
    }

    public abstract boolean eventTracked(Idea objectInitialState, Idea objectFinalState);

    public Idea getObjectCurrentState() {
        int currentStateIdx = 0;
        Idea objectCurrentState = this.objectsBufferIdeaList.getL().get(currentStateIdx);
        return objectCurrentState.get("object").clone();
    }

    public double getObjectTime(Idea objectState)    {
        double time = (double) objectState.get("time").clone().getValue();
        return time;
    }

    public Idea buildEventIdea(Idea objectInitialState, Idea objectFinalState)   {
        Idea event = new Idea(this.getName(), "", 0);
        Idea timeSteps = new Idea("timeSteps", "", 0);
        timeSteps.getL().add(objectInitialState);
        timeSteps.getL().add(objectFinalState);
        event.getL().add(timeSteps);
        return event;
    }

    public void setObjectsBufferMI(MemoryInstance objectsBufferMI) {
        this.objectsBufferMI = objectsBufferMI;
    }

    public void setEventsMI(MemoryInstance eventsMI) {
        this.eventsMI = eventsMI;
    }

}

