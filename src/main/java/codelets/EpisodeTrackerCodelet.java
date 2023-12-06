package codelets;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.idea.Idea;
import memory_storage.MemoryInstance;

import java.util.ArrayList;
import java.util.List;

public class EpisodeTrackerCodelet extends Codelet {

    Memory eventsBufferMO;
    Memory episodeMO;
    MemoryInstance eventsBufferMI;
    MemoryInstance episodeMI;

    Idea eventsBuffer;
    Idea currentEvent;
    Idea timeSteps;

    Idea initialStayTimeStep;
    Idea finalStayTimeStep;
    String stayEventSequenceName = "";
    boolean stayEventSequenceStarted = false;

    Idea initialMoveTimeStep;
    Idea finalMoveTimeStep;
    String moveEventName = "MOVE";
    boolean moveEventSequenceStarted = false;

    public EpisodeTrackerCodelet(MemoryInstance eventsBufferMI, MemoryInstance episodeMI) {
        this.eventsBufferMI = eventsBufferMI;
        this.episodeMI = episodeMI;
    }

    @Override
    public void accessMemoryObjects() {
        this.eventsBufferMO = (MemoryObject)this.getInput("EVENTS_BUFFER");
        this.episodeMO = (MemoryObject)this.getOutput("EPISODE");
    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {
        if (this.eventsBufferMI == null) {
            return;
        }
        try {
            eventsBuffer = this.eventsBufferMI.getIdea();
        }   catch (java.lang.NullPointerException e)   {return;}

        currentEvent = getLastPosition(eventsBuffer).clone();

        concatenateStayEventAndSetI(currentEvent);
        setIOutEvent(currentEvent);
        detectMoveEventAndSetI(currentEvent);
        setIInEvent(currentEvent);
    }

    private void detectMoveEventAndSetI(Idea currentEvent)  {
        if(isOutEvent(currentEvent) == true) {
            moveEventSequenceStarted = true;
            timeSteps = currentEvent.get("timeSteps");
            initialMoveTimeStep = timeSteps.getL().get(0).clone();
        }   else if(isInEvent(currentEvent) != true){
            moveEventSequenceStarted = false;
        }

        if(isInEvent(currentEvent) == true && moveEventSequenceStarted == true) {
            timeSteps = currentEvent.get("timeSteps");
            finalMoveTimeStep = timeSteps.getL().get(0).clone();

            Idea moveEventSequence = buildEventIdea(moveEventName, initialMoveTimeStep, finalMoveTimeStep);

            this.episodeMI.postIdea(moveEventSequence);
            this.episodeMO.setI("");

            moveEventSequenceStarted = false;
        }

    }
    private void concatenateStayEventAndSetI(Idea currentEvent)  {
        if (stayEventSequenceStarted==false)   {
            if(isStayEvent(currentEvent)==true) {
                stayEventSequenceStarted = true;
                stayEventSequenceName = currentEvent.getName();
            }
        }

        if (isStayEvent(currentEvent) == true) {
            if (stayEventSequenceStarted == false) {
                stayEventSequenceStarted = true;
                stayEventSequenceName = currentEvent.getName();

                timeSteps = currentEvent.get("timeSteps");
                initialStayTimeStep = timeSteps.getL().get(0).clone();
                finalStayTimeStep = timeSteps.getL().get(timeSteps.getL().size()-1).clone();
            }

            if (stayEventSequenceStarted == true) {
                timeSteps = currentEvent.get("timeSteps");
                finalStayTimeStep = timeSteps.getL().get(timeSteps.getL().size()-1).clone();
            }
        }   else   {
            if (stayEventSequenceStarted==true)  {
                stayEventSequenceStarted=false;
                Idea stayEventSequence = buildEventIdea(stayEventSequenceName, initialStayTimeStep, finalStayTimeStep);

                this.episodeMI.postIdea(stayEventSequence);
                this.episodeMO.setI("");
//                System.out.println( ((Idea) this.episodeMO.getI()).toStringFull());
            }
//            this.episodeMO.setI(currentEvent);
        }

    }

    private void setIOutEvent(Idea currentEvent) {
        if (isOutEvent(currentEvent) == true)   {
            this.episodeMI.postIdea(currentEvent);
            this.episodeMO.setI("");
        }
    }

    private void setIInEvent(Idea currentEvent) {
        if (isInEvent(currentEvent) == true)   {
            this.episodeMI.postIdea(currentEvent);
            this.episodeMO.setI("");
        }
    }

    private boolean isOutEvent(Idea event)   {
        if(event.getName().endsWith("OUT")) {
            return true;
        }
        return false;
    }

    private boolean isInEvent(Idea event)   {
        if(event.getName().endsWith("IN")) {
            return true;
        }
        return false;
    }

    private boolean isStayEvent(Idea event)   {
        if(event.getName().endsWith("STAY")) {
            return true;
        }
        return false;
    }
    private Idea getLastPosition(Idea eventsFrames)   {
        return eventsFrames.getL().get(eventsFrames.getL().size()-1);
    }

    public Idea buildEventIdea(String eventName, Idea initialTimeStep, Idea finalTimeStep)   {
        Idea event = new Idea(eventName, "", 0);
        List<Idea> timeSteps = new ArrayList<Idea>();
        timeSteps.add(initialTimeStep);
        timeSteps.add(finalTimeStep);
        event.add(new Idea("timeSteps",timeSteps));
        return event;
    }
}
