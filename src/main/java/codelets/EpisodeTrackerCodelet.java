package codelets;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.idea.Idea;

import java.util.ArrayList;
import java.util.List;

public class EpisodeTrackerCodelet extends Codelet {

    Memory eventsBufferMO;
    Memory episodeMO;
    Idea eventsBuffer;
    Idea currentEvent;
    ArrayList<Idea> eventsFrames;
    ArrayList<Idea> timeSteps;

    Idea initialStayTimeStep;
    Idea finalStayTimeStep;
    String stayEventSequenceName = "";
    boolean stayEventSequenceStarted = false;

    Idea initialMoveTimeStep;
    Idea finalMoveTimeStep;
    String moveEventName = "MOVE";
    boolean moveEventSequenceStarted = false;


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
        if (this.eventsBufferMO.getI()=="") {
            return;
        }
        try {
            eventsBuffer = (Idea) this.eventsBufferMO.getI();
            eventsFrames = (ArrayList<Idea>) eventsBuffer.getValue();
        }   catch (java.lang.NullPointerException e)   {return;}

        currentEvent = getLastPosition(eventsFrames).clone();

        concatenateStayEventAndSetI(currentEvent);
        setIOutEvent(currentEvent);
        detectMoveEventAndSetI(currentEvent);
        setIInEvent(currentEvent);
//            System.out.println( ((Idea) this.episodeMO.getI()).toStringFull());

    }

    private void detectMoveEventAndSetI(Idea currentEvent)  {
        if(isOutEvent(currentEvent) == true) {
            moveEventSequenceStarted = true;
            timeSteps = (ArrayList<Idea>) currentEvent.get("timeSteps").getValue();
            initialMoveTimeStep = timeSteps.get(0).clone();
        }   else if(isInEvent(currentEvent) != true){
            moveEventSequenceStarted = false;
        }

        if(isInEvent(currentEvent) == true && moveEventSequenceStarted == true) {
            timeSteps = (ArrayList<Idea>) currentEvent.get("timeSteps").getValue();
            finalMoveTimeStep = timeSteps.get(0).clone();

            Idea moveEventSequence = buildEventIdea(moveEventName, initialMoveTimeStep, finalMoveTimeStep);
            this.episodeMO.setI(moveEventSequence);

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

                timeSteps = (ArrayList<Idea>) currentEvent.get("timeSteps").getValue();
                initialStayTimeStep = timeSteps.get(0).clone();
                finalStayTimeStep = timeSteps.get(timeSteps.size()-1).clone();
            }

            if (stayEventSequenceStarted == true) {
                timeSteps = (ArrayList<Idea>) currentEvent.get("timeSteps").getValue();
                finalStayTimeStep = timeSteps.get(timeSteps.size()-1).clone();
            }
        }   else   {
            if (stayEventSequenceStarted==true)  {
                stayEventSequenceStarted=false;
                Idea stayEventSequence = buildEventIdea(stayEventSequenceName, initialStayTimeStep, finalStayTimeStep);
                this.episodeMO.setI(stayEventSequence);
//                System.out.println( ((Idea) this.episodeMO.getI()).toStringFull());
            }
//            this.episodeMO.setI(currentEvent);
        }

    }

    private void setIOutEvent(Idea currentEvent) {
        if (isOutEvent(currentEvent) == true)   {
            this.episodeMO.setI(currentEvent);
        }
    }

    private void setIInEvent(Idea currentEvent) {
        if (isInEvent(currentEvent) == true)   {
            this.episodeMO.setI(currentEvent);
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
    private Idea getLastPosition(ArrayList<Idea> eventsFrames)   {
        return eventsFrames.get(eventsFrames.size()-1);
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
