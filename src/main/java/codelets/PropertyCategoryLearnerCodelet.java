package codelets;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import regions.RelevantRegionPC;
import regions.RelevantRegions;

import java.io.IOException;
import java.util.ArrayList;

public class PropertyCategoryLearnerCodelet extends Codelet {
    Memory detectedObjectsMO;
    Memory propertyCategoriesMO;

    @Override
    public void accessMemoryObjects() {
        detectedObjectsMO=(MemoryObject)this.getInput("DETECTED_OBJECTS");
        propertyCategoriesMO=(MemoryObject)this.getOutput("PROPERTY_CATEGORIES");
    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {
        // TODO I need to modify this codelet. It should be updated by the relevant regions updated by Anderson's API
        //  instead of the CSV file.
        ArrayList<RelevantRegionPC> relevantRegionPCArrayList = null;
        try {
            relevantRegionPCArrayList = new RelevantRegions().getRelevantRegionsFromMeanShift();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        propertyCategoriesMO.setI(relevantRegionPCArrayList);
    }
}
