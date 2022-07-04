package codelets;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.idea.Idea;

import java.util.List;


public class ObjectProposerCodelet extends Codelet {
    Memory rawDataMO;
    Memory detectedObjectsMO;

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
        try {
            Idea framesIdea = (Idea) rawDataMO.getI();
            Idea frameIdea = getFirstElementFromBuffer(framesIdea);
            Idea objectIdea = buildObject(frameIdea);
            detectedObjectsMO.setI(objectIdea);

//            System.out.println(((Idea) detectedObjectsMO.getI()).toStringFull());
        }
        catch (Exception e) {
        }
    }

    private Idea getFirstElementFromBuffer(Idea framesIdea)   {
        List<Idea> frameList = (List<Idea>) framesIdea.get("frames").getValue();
        return frameList.get(0);
    }

    // TODO | Adapt it for a list of objects.
    private Idea buildObject(Idea frameIdea) {
        Idea detectedObjectsIdea = new Idea("detected_objects","",0);
        Idea objectIdea = new Idea("object","",0);
        detectedObjectsIdea.add(objectIdea);
        objectIdea.add(new Idea("time", frameIdea.get("timestamp").getValue()));
        objectIdea.add(new Idea("latitude", frameIdea.get("latitude").getValue()));
        objectIdea.add(new Idea("longitude", frameIdea.get("longitude").getValue()));

        return detectedObjectsIdea;
    }
}
