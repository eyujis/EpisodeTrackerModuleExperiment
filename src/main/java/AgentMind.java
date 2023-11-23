import br.unicamp.cst.core.entities.*;
import codelets.*;

import java.io.IOException;

import environment.Environment;
import test.TestHttpCodelet;


public class AgentMind extends Mind {

    public AgentMind(Environment env) throws IOException {
    super();
    // Declare Memory Objects
    Memory rawDataBufferMO;
    Memory detectedObjectsMO;
    Memory objectsBufferMO;
    Memory propertyCategoriesMO;
    Memory eventsMO;
    Memory eventsBufferMO;
    Memory episodeMO;
    Memory episodeBufferMO;

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
    episodeBufferMO = createMemoryObject("EPISODE_BUFFER");

    registerMemory(rawDataBufferMO, "EpisodeTrackerMemoryGroup");
    registerMemory(detectedObjectsMO, "EpisodeTrackerMemoryGroup");
    registerMemory(objectsBufferMO, "EpisodeTrackerMemoryGroup");
    registerMemory(propertyCategoriesMO, "EpisodeTrackerMemoryGroup");
    registerMemory(eventsMO, "EpisodeTrackerMemoryGroup");
    registerMemory(eventsBufferMO, "EpisodeTrackerMemoryGroup");
    registerMemory(episodeMO, "EpisodeTrackerMemoryGroup");
    registerMemory(episodeBufferMO, "EpisodeTrackerMemoryGroup");

    // Create Codelets
    Codelet rawDataBufferizerCodelet = new RAWDataBufferizerCodelet(env);
    rawDataBufferizerCodelet.addOutput(rawDataBufferMO);
    rawDataBufferizerCodelet.setName("RAWDataBufferizer");
    insertCodelet(rawDataBufferizerCodelet);


    Codelet objectProposerCodelet = new ObjectProposerCodelet();
    objectProposerCodelet.addInput(rawDataBufferMO);
    objectProposerCodelet.addOutput(detectedObjectsMO);
    objectProposerCodelet.setIsMemoryObserver(true);
    rawDataBufferMO.addMemoryObserver(objectProposerCodelet);
    objectProposerCodelet.setName("ObjectProposer");
    insertCodelet(objectProposerCodelet);

    Codelet objectBufferizerCodelet = new ObjectBufferizerCodelet();
    objectBufferizerCodelet.addInput(detectedObjectsMO);
    objectBufferizerCodelet.addOutput(objectsBufferMO);
    objectBufferizerCodelet.setIsMemoryObserver(true);
    detectedObjectsMO.addMemoryObserver(objectBufferizerCodelet);
    objectBufferizerCodelet.setName("ObjectBufferizer");
    insertCodelet(objectBufferizerCodelet);

    Codelet propertyCategoryLearnerCodelet = new PropertyCategoryLearnerCodelet();
    propertyCategoryLearnerCodelet.addInput(detectedObjectsMO);
    propertyCategoryLearnerCodelet.addOutput(propertyCategoriesMO);
    propertyCategoryLearnerCodelet.setIsMemoryObserver(true);
    detectedObjectsMO.addMemoryObserver(propertyCategoryLearnerCodelet);
    propertyCategoryLearnerCodelet.setName("PropertyCategoryLearner");
    insertCodelet(propertyCategoryLearnerCodelet);

    Codelet eventCategoryLearner = new EventCategoryLearnerCodelet(this, objectsBufferMO, eventsMO);
    eventCategoryLearner.addInput(propertyCategoriesMO);
    eventCategoryLearner.setIsMemoryObserver(true);
    propertyCategoriesMO.addMemoryObserver(eventCategoryLearner);
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

    Codelet episodeBufferizerCodelet = new EpisodeBufferizerCodelet();
    episodeBufferizerCodelet.addInput(episodeMO);
    episodeBufferizerCodelet.addOutput(episodeBufferMO);
    episodeBufferizerCodelet.setIsMemoryObserver(true);
    episodeMO.addMemoryObserver(episodeBufferizerCodelet);
    episodeBufferizerCodelet.setName("EpisodeBufferizer");
    insertCodelet(episodeBufferizerCodelet);

    Codelet testHttpCodelet = new TestHttpCodelet();
    insertCodelet(testHttpCodelet);


    registerCodelet(rawDataBufferizerCodelet, "EpisodeTrackerCodeletGroup");
    registerCodelet(objectProposerCodelet, "EpisodeTrackerCodeletGroup");
    registerCodelet(objectBufferizerCodelet, "EpisodeTrackerCodeletGroup");
//    registerCodelet(propertyCategoryLearnerCodelet, "EpisodeTrackerCodeletGroup");
    registerCodelet(eventCategoryLearner, "EpisodeTrackerCodeletGroup");
    registerCodelet(eventsBufferizerCodelet, "EpisodeTrackerCodeletGroup");
    registerCodelet(episodeTrackerCodelet, "EpisodeTrackerCodeletGroup");
    registerCodelet(episodeBufferizerCodelet, "EpisodeTrackerCodeletGroup");



    // Sets a time step for running the codelets to avoid heating too much your machine
    for (Codelet c : this.getCodeRack().getAllCodelets())
        c.setTimeStep(1);

    }
}
