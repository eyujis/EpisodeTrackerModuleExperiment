package entities;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;

public abstract class EventTrackerPropertyCategoryCodelet extends Codelet {

    PropertyCategory propertyCategory;

    public Memory objectsBufferMO;
    public Memory eventsMC;

    public EventTrackerPropertyCategoryCodelet()    {
        this.propertyCategory = propertyCategory;
    }

    @Override
    public void accessMemoryObjects() {
        objectsBufferMO=(MemoryObject)this.getInput("OBJECTS_BUFFER");
        eventsMC=(MemoryContainer)this.getOutput("EVENTS");
    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {
        if(eventTracked())  {
            eventsMC.setI(this.getName() + ": Tracked");
        }

    }

    public abstract boolean eventTracked();

}
