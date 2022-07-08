import br.unicamp.cst.core.entities.*;
import codelets.*;

import java.io.IOException;

import entities.CodeletContainerCodelet;
import environment.Environment;


public class AgentMind extends Mind {

    public AgentMind(Environment env) throws IOException {
    super();
    // Declare Memory Objects
    Memory rawDataBufferMO;
    Memory detectedObjectsMO;
    Memory objectsBufferMO;
    Memory propertyCategoriesMO;
    MemoryContainer eventsMC;

    createMemoryGroup("EpisodeTrackerMemoryGroup");
    createCodeletGroup("EpisodeTrackerCodeletGroup");


    // Initialize Memory Objects
    rawDataBufferMO = createMemoryObject("RAW_DATA_BUFFER", "");
    detectedObjectsMO = createMemoryObject("DETECTED_OBJECTS", "");
    objectsBufferMO = createMemoryObject("OBJECTS_BUFFER", "");
    propertyCategoriesMO = createMemoryObject("PROPERTY_CATEGORIES", "");
    eventsMC = createMemoryContainer("EVENTS");

    registerMemory(rawDataBufferMO, "EpisodeTrackerMemoryGroup");
    registerMemory(detectedObjectsMO, "EpisodeTrackerMemoryGroup");
    registerMemory(objectsBufferMO, "EpisodeTrackerMemoryGroup");
    registerMemory(propertyCategoriesMO, "EpisodeTrackerMemoryGroup");


    // Create Codelets
    Codelet rawDataBufferizerCodelet = new RAWDataBufferizerCodelet(env);
    rawDataBufferizerCodelet.addOutput(rawDataBufferMO);
    rawDataBufferizerCodelet.setName("RAWDataBufferizer");
    insertCodelet(rawDataBufferizerCodelet);


    Codelet objectProposerCodelet = new ObjectProposerCodelet();
    objectProposerCodelet.addInput(rawDataBufferMO);
    objectProposerCodelet.addOutput(detectedObjectsMO);
    objectProposerCodelet.setName("ObjectProposer");
    insertCodelet(objectProposerCodelet);

    Codelet objectBufferizerCodelet = new ObjectBufferizerCodelet();
    objectBufferizerCodelet.addInput(detectedObjectsMO);
    objectBufferizerCodelet.addOutput(objectsBufferMO);
    objectBufferizerCodelet.setName("ObjectBufferizer");
    insertCodelet(objectBufferizerCodelet);

    Codelet propertyCategoryLearnerCodelet = new PropertyCategoryLearnerCodelet();
    propertyCategoryLearnerCodelet.addInput(detectedObjectsMO);
    propertyCategoryLearnerCodelet.addOutput(propertyCategoriesMO);
    propertyCategoryLearnerCodelet.setName("PropertyCategoryLearner");
    insertCodelet(propertyCategoryLearnerCodelet);

    CodeletContainer EventTrackerCodeletContainer = new CodeletContainer();

    Codelet eventCategoryLearner = new EventCategoryLearnerCodelet(this, objectsBufferMO, eventsMC);
    eventCategoryLearner.addInput(propertyCategoriesMO);
    eventCategoryLearner.setName("EventCategoryLearner");
    insertCodelet(eventCategoryLearner);


    registerCodelet(rawDataBufferizerCodelet, "EpisodeTrackerCodeletGroup");
    registerCodelet(objectProposerCodelet, "EpisodeTrackerCodeletGroup");
    registerCodelet(objectBufferizerCodelet, "EpisodeTrackerCodeletGroup");
    registerCodelet(propertyCategoryLearnerCodelet, "EpisodeTrackerCodeletGroup");
    registerCodelet(eventCategoryLearner, "EpisodeTrackerCodeletGroup");


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
        c.setTimeStep(1);

    // Start Cognitive Cycle
    start();

    }
}
