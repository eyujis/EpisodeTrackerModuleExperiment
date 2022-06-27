import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.Mind;
import codelets.*;

import java.io.IOException;

import environment.Environment;


public class AgentMind extends Mind {

    public AgentMind(Environment env) throws IOException {
    super();
    // Declare Memory Objects
    Memory rawDataBufferMO;
    Memory detectedObjectsMO;
    Memory objectsBufferMO;
    Memory propertyCategoriesMO;

    // Initialize Memory Objects
    rawDataBufferMO = createMemoryObject("RAW_DATA_BUFFER", "");
    detectedObjectsMO = createMemoryObject("DETECTED_OBJECTS", "");
    objectsBufferMO = createMemoryObject("OBJECTS_BUFFER", "");
    propertyCategoriesMO = createMemoryObject("PROPERTY_CATEGORIES", "");
//    categoriesMO = createMemoryObject("CATEGORIES", "");
//    eventsMC = createMemoryContainer("EVENTS");
//    eventsBufferMO = createMemoryObject("EVENTS BUFFER", "");

    // Create Codelets
    Codelet rawDataBufferizerCodelet = new RAWDataBufferizerCodelet(env);
    rawDataBufferizerCodelet.addOutput(rawDataBufferMO);
    insertCodelet(rawDataBufferizerCodelet);

    Codelet objectProposerCodelet = new ObjectProposerCodelet();
    objectProposerCodelet.addInput(rawDataBufferMO);
    objectProposerCodelet.addOutput(detectedObjectsMO);
    insertCodelet(objectProposerCodelet);

    Codelet objectBufferizerCodelet = new ObjectBufferizerCodelet();
    objectBufferizerCodelet.addInput(detectedObjectsMO);
    objectBufferizerCodelet.addOutput(objectsBufferMO);
    insertCodelet(objectBufferizerCodelet);

    Codelet propertyCategoryLearnerCodelet = new PropertyCategoryLearnerCodelet();
    propertyCategoryLearnerCodelet.addInput(detectedObjectsMO);
    propertyCategoryLearnerCodelet.addOutput(propertyCategoriesMO);
    insertCodelet(propertyCategoryLearnerCodelet);




//    Codelet categoryLearnerCodelet = new CategoryLearnerCodelet();
//    categoryLearnerCodelet.addInput(rawDataMO);
//    categoryLearnerCodelet.addInput(categoriesMO);
//    categoryLearnerCodelet.addOutput(categoriesMO);
//    insertCodelet(categoryLearnerCodelet);
//
//
//
//    Codelet eventBufferizerCodelet = new EventBufferizerCodelet();
//    eventBufferizerCodelet.addInput(eventsMC);
//    eventBufferizerCodelet.addOutput(eventsBufferMO);
//    insertCodelet(eventBufferizerCodelet);



    // Sets a time step for running the codelets to avoid heating too much your machine
    for (Codelet c : this.getCodeRack().getAllCodelets())
        c.setTimeStep(200);

    // Start Cognitive Cycle
    start();

    }
}
