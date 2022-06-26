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
    Memory rawDataMO;
    Memory categoriesMO;
    Memory eventsMC;
    Memory eventsBufferMO;

    // Initialize Memory Objects
    rawDataMO = createMemoryObject("RAWDATA", "");
    categoriesMO = createMemoryObject("CATEGORIES", "");
    eventsMC = createMemoryContainer("EVENTS");
    eventsBufferMO = createMemoryObject("EVENTS BUFFER", "");

    // Create Codelets
    Codelet rawDataCodelet = new RAWDataBufferizerCodelet(env);
    rawDataCodelet.addOutput(rawDataMO);
    insertCodelet(rawDataCodelet);

    Codelet categoryLearnerCodelet = new CategoryLearnerCodelet();
    categoryLearnerCodelet.addInput(rawDataMO);
    categoryLearnerCodelet.addInput(categoriesMO);
    categoryLearnerCodelet.addOutput(categoriesMO);
    insertCodelet(categoryLearnerCodelet);

    Codelet eventTrackerAtWorkCodelet = new EventTrackerAtWorkCodelet();
    eventTrackerAtWorkCodelet.addInput(rawDataMO);
    eventTrackerAtWorkCodelet.addInput(categoriesMO);
    eventTrackerAtWorkCodelet.addOutput(eventsMC);
    insertCodelet(eventTrackerAtWorkCodelet);

    Codelet eventTrackerAtHomeCodelet = new EventTrackerAtHomeCodelet();
    eventTrackerAtHomeCodelet.addInput(rawDataMO);
    eventTrackerAtHomeCodelet.addInput(categoriesMO);
    eventTrackerAtHomeCodelet.addOutput(eventsMC);
    insertCodelet(eventTrackerAtHomeCodelet);

    Codelet eventTrackerAtSchoolCodelet = new EventTrackerAtSchoolCodelet();
    eventTrackerAtSchoolCodelet.addInput(rawDataMO);
    eventTrackerAtSchoolCodelet.addInput(categoriesMO);
    eventTrackerAtSchoolCodelet.addOutput(eventsMC);
    insertCodelet(eventTrackerAtSchoolCodelet);

    Codelet eventTrackerAtBeachCodelet = new EventTrackerAtBeachCodelet();
    eventTrackerAtBeachCodelet.addInput(rawDataMO);
    eventTrackerAtBeachCodelet.addInput(categoriesMO);
    eventTrackerAtBeachCodelet.addOutput(eventsMC);
    insertCodelet(eventTrackerAtBeachCodelet);

    Codelet eventTrackerAtGymCodelet = new EventTrackerAtGymCodelet();
    eventTrackerAtGymCodelet.addInput(rawDataMO);
    eventTrackerAtGymCodelet.addInput(categoriesMO);
    eventTrackerAtGymCodelet.addOutput(eventsMC);
    insertCodelet(eventTrackerAtGymCodelet);

    Codelet eventTrackerAtRestaurantCodelet = new EventTrackerAtRestaurantCodelet();
    eventTrackerAtRestaurantCodelet.addInput(rawDataMO);
    eventTrackerAtRestaurantCodelet.addInput(categoriesMO);
    eventTrackerAtRestaurantCodelet.addOutput(eventsMC);
    insertCodelet(eventTrackerAtRestaurantCodelet);


    Codelet eventBufferizerCodelet = new EventBufferizerCodelet();
    eventBufferizerCodelet.addInput(eventsMC);
    eventBufferizerCodelet.addOutput(eventsBufferMO);
    insertCodelet(eventBufferizerCodelet);



    // Sets a time step for running the codelets to avoid heating too much your machine
    for (Codelet c : this.getCodeRack().getAllCodelets())
        c.setTimeStep(200);

    // Start Cognitive Cycle
    start();

    }
}
