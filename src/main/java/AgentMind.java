import br.unicamp.cst.core.entities.*;
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
    Memory eventsMO;
//    MemoryContainer eventsMC;
    Memory eventsBufferMO;
    Memory episodeMO;

    createMemoryGroup("EpisodeTrackerMemoryGroup");
    createCodeletGroup("EpisodeTrackerCodeletGroup");


    // Initialize Memory Objects
    rawDataBufferMO = createMemoryObject("RAW_DATA_BUFFER", "");
    detectedObjectsMO = createMemoryObject("DETECTED_OBJECTS", "");
    objectsBufferMO = createMemoryObject("OBJECTS_BUFFER", "");
    propertyCategoriesMO = createMemoryObject("PROPERTY_CATEGORIES", "");
    eventsMO = createMemoryObject("EVENTS", "");
    eventsBufferMO = createMemoryObject("EVENTS_BUFFER");
    episodeMO = createMemoryObject("EPISODE");

    registerMemory(rawDataBufferMO, "EpisodeTrackerMemoryGroup");
    registerMemory(detectedObjectsMO, "EpisodeTrackerMemoryGroup");
    registerMemory(objectsBufferMO, "EpisodeTrackerMemoryGroup");
    registerMemory(propertyCategoriesMO, "EpisodeTrackerMemoryGroup");
    registerMemory(eventsMO, "EpisodeTrackerMemoryGroup");
    registerMemory(eventsBufferMO, "EpisodeTrackerMemoryGroup");
    registerMemory(episodeMO, "EpisodeTrackerMemoryGroup");


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

    Codelet eventCategoryLearner = new EventCategoryLearnerCodelet(this, objectsBufferMO, eventsMO);
    eventCategoryLearner.addInput(propertyCategoriesMO);
    eventCategoryLearner.setName("EventCategoryLearner");
    insertCodelet(eventCategoryLearner);

    Codelet eventsBufferizerCodelet = new EventsBufferizerCodelet();
    eventsBufferizerCodelet.addInput(eventsMO);
    eventsBufferizerCodelet.addOutput(eventsBufferMO);
    eventsBufferizerCodelet.setIsMemoryObserver(true);
    eventsMO.addMemoryObserver(eventsBufferizerCodelet);
    eventCategoryLearner.setName("EventBufferizer");
    insertCodelet(eventsBufferizerCodelet);

    Codelet episodeTrackerCodelet = new EpisodeTrackerCodelet();
    episodeTrackerCodelet.addInput(eventsBufferMO);
    episodeTrackerCodelet.addOutput(episodeMO);
    episodeTrackerCodelet.setIsMemoryObserver(true);
    eventsBufferMO.addMemoryObserver(episodeTrackerCodelet);
    episodeTrackerCodelet.setName("EpisodeTracker");
    insertCodelet(episodeTrackerCodelet);



    registerCodelet(rawDataBufferizerCodelet, "EpisodeTrackerCodeletGroup");
    registerCodelet(objectProposerCodelet, "EpisodeTrackerCodeletGroup");
    registerCodelet(objectBufferizerCodelet, "EpisodeTrackerCodeletGroup");
    registerCodelet(propertyCategoryLearnerCodelet, "EpisodeTrackerCodeletGroup");
    registerCodelet(eventCategoryLearner, "EpisodeTrackerCodeletGroup");
    registerCodelet(eventsBufferizerCodelet, "EpisodeTrackerCodeletGroup");
    registerCodelet(episodeTrackerCodelet, "EpisodeTrackerCodeletGroup");


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
