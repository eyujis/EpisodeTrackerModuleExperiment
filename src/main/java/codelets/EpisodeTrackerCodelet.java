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

    Idea initialTimeStep;
    Idea finalTimeStep;
    String stayEventSequenceName = "";
    boolean stayEventSequenceStarted = false;


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
        eventsBuffer = (Idea) eventsBufferMO.getI();
        try {
            eventsFrames = (ArrayList<Idea>) eventsBuffer.getValue();
        }   catch (java.lang.NullPointerException e)   {return;}

        currentEvent = getFirstPosition(eventsFrames);

        if (isStayEvent(currentEvent) == true) {
            if (stayEventSequenceStarted == false) {
                stayEventSequenceStarted = true;
                stayEventSequenceName = currentEvent.getName();

                timeSteps = (ArrayList<Idea>) currentEvent.get("timeSteps").getValue();
                initialTimeStep = timeSteps.get(0);
                finalTimeStep = timeSteps.get(timeSteps.size()-1);
            }

            if (stayEventSequenceStarted == true) {
                timeSteps = (ArrayList<Idea>) currentEvent.get("timeSteps").getValue();
                finalTimeStep = timeSteps.get(timeSteps.size()-1);
            }
        }   else   {
            if (stayEventSequenceStarted==true)  {
                stayEventSequenceStarted=false;
                Idea stayEventSequence = buildEventIdea();
                this.episodeMO.setI(stayEventSequence);
//                System.out.println( ((Idea) this.episodeMO.getI()).toStringFull());
            }
            this.episodeMO.setI(currentEvent);
//            System.out.println( ((Idea) this.episodeMO.getI()).toStringFull());
        }

    }

    private boolean isStayEvent(Idea event)   {
        if(event.getName().endsWith("STAY")) {
            return true;
        }
        return false;
    }
    private Idea getFirstPosition(ArrayList<Idea> eventsFrames)   {
        return eventsFrames.get(0);
    }

    public Idea buildEventIdea()   {
        Idea event = new Idea(stayEventSequenceName, "", 0);
        List<Idea> timeSteps = new ArrayList<Idea>();
        timeSteps.add(initialTimeStep);
        timeSteps.add(finalTimeStep);
        event.add(new Idea("timeSteps",timeSteps));
        return event;
    }
}
