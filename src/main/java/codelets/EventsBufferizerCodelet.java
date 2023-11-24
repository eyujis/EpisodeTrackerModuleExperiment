package codelets;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.idea.Idea;
import environment.Environment;
import memory_storage.MemoryInstance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class EventsBufferizerCodelet extends Codelet {
    private Memory eventsMO;
    private Memory eventsBufferMO;
    MemoryInstance eventsMI;
    MemoryInstance eventsBufferMI;

    private int buffer_size = 5;
    private Idea eventsBuffer =  new Idea("eventsBuffer","",0);

    public EventsBufferizerCodelet(MemoryInstance eventsMI, MemoryInstance eventsBufferMI) {
        this.eventsMI = eventsMI;
        this.eventsBufferMI = eventsBufferMI;
    }

    @Override
    public void accessMemoryObjects() {
        this.eventsMO=(Memory)this.getInput("EVENTS");
        this.eventsBufferMO=(MemoryObject)this.getOutput("EVENTS_BUFFER");
    }

    @Override
    public void proc() {
        Idea eventFrame = eventsMI.getIdea();
        addFrame(eventFrame);

        eventsBufferMI.postIdea(eventsBuffer);
        eventsBufferMO.setI("");
    }

    @Override
    public void calculateActivation() {

    }

    private void addFrame(Idea event) {
        if (eventsBuffer.getL().size()<this.buffer_size) {
            eventsBuffer.getL().add(event);
        } else {
            eventsBuffer.getL().remove(0);
            eventsBuffer.getL().add(event);
        }
    }
}
