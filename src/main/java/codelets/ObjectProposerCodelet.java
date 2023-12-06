package codelets;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.idea.Idea;
import memory_storage.MemoryInstance;

import java.util.List;


public class ObjectProposerCodelet extends Codelet {
    Memory rawDataMO;
    Memory detectedObjectsMO;
    MemoryInstance rawDataMI;
    MemoryInstance detectedObjectsMI;

    public ObjectProposerCodelet(MemoryInstance rawDataMI, MemoryInstance detectedObjectsMI) {
        this.rawDataMI = rawDataMI;
        this.detectedObjectsMI = detectedObjectsMI;
    }

    @Override
    public void accessMemoryObjects() {
        rawDataMO=(MemoryObject)this.getInput("RAW_DATA_BUFFER");
        detectedObjectsMO=(MemoryObject)this.getOutput("DETECTED_OBJECTS");
    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {
        Idea framesIdea = rawDataMI.getIdea();
        Idea frameIdea = getFirstElementFromBuffer(framesIdea);
        Idea objectIdea = buildObject(frameIdea);

        detectedObjectsMI.postIdea(objectIdea);
        detectedObjectsMO.setI("");
    }

    private Idea getFirstElementFromBuffer(Idea framesIdea)   {
        Idea frameList = framesIdea.get("frames");
        return frameList.getL().get(0);
    }

    private Idea buildObject(Idea frameIdea) {
        Idea detectedObjectsIdea = new Idea("detected_objects","",0);
        Idea objectIdea = new Idea("object","",0);
        objectIdea.add(new Idea("time", frameIdea.get("timestamp").getValue()));
        objectIdea.add(new Idea("latitude", frameIdea.get("latitude").getValue()));
        objectIdea.add(new Idea("longitude", frameIdea.get("longitude").getValue()));
        detectedObjectsIdea.getL().add(objectIdea);
        return detectedObjectsIdea;
    }
}
