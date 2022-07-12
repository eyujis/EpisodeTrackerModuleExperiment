package entities;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.idea.Idea;

import java.util.ArrayList;
import java.util.List;

public abstract class EventTrackerPropertyCategoryCodelet extends Codelet {

    PropertyCategory propertyCategory;

    public Memory objectsBufferMO;
    public Memory eventsMO;
    public double initialTime;
    public double finalTime;
    private List<Idea> objectsBufferIdeaList;
    private Idea objectInitialState;
    private Idea objectFinalState;

    public EventTrackerPropertyCategoryCodelet()    {
        this.propertyCategory = propertyCategory;
    }

    @Override
    public void accessMemoryObjects() {
        this.objectsBufferMO=(Memory)this.getInput("OBJECTS_BUFFER");
        this.eventsMO=(Memory)this.getOutput("EVENTS");
    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {
        if  (this.objectsBufferMO.getI()=="")   {
            return;
        }
        this.objectsBufferIdeaList = (List<Idea>) ((Idea) this.objectsBufferMO.getI()).get("timeSteps").getValue();
        updateObjectInitialState();
        updateObjectFinalState();
        this.initialTime = getObjectTime(this.objectInitialState);
        this.finalTime = getObjectTime(this.objectFinalState);

        if(eventTracked(this.objectInitialState, this.objectFinalState))  {
            this.eventsMO.setI(this.buildEventIdea());
//            System.out.println("---------------");
//            System.out.println(((Idea) this.eventsMC.getI()).toStringFull());
//            System.out.println(((List<Idea>) ((Idea) this.eventsMC.getI()).get("timeSteps").getValue()).get(0).toStringFull());
//            System.out.println(((List<Idea>) ((Idea) this.eventsMC.getI()).get("timeSteps").getValue()).get(1).toStringFull());
        }
    }

    public abstract boolean eventTracked(Idea objectInitialState, Idea objectFinalState);

    public void updateObjectInitialState() {
        int initialStateIdx = this.objectsBufferIdeaList.size()-1;
        Idea objectInitialState = this.objectsBufferIdeaList.get(initialStateIdx);
        this.objectInitialState = objectInitialState.get("object");
    }
    public void updateObjectFinalState() {
        int finalStateIdx = 0;
        Idea objectFinalState = this.objectsBufferIdeaList.get(finalStateIdx);
        this.objectFinalState = objectFinalState.get("object");
    }

    public double getObjectTime(Idea objectState)    {
        double time = (double) objectState.get("time").getValue();
        return time;
    }


    public Idea buildEventIdea()   {
        Idea event = new Idea(this.getName(), "", 0);
        List<Idea> timeSteps = new ArrayList<Idea>();
        timeSteps.add(this.objectInitialState);
        timeSteps.add(this.objectFinalState);
        event.add(new Idea("timeSteps",timeSteps));
        return event;
    }



}
