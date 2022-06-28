package codelets;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.wme.Idea;
import pheromone.PheromoneAlgorithm;

import java.util.ArrayList;

public class PropertyCategoryLearnerCodelet extends Codelet {
    Memory detectedObjectsMO;
    Memory propertyCategoriesMO;
    double circleRadiusKm = 10 * Math.pow(10,-3);
    double decayRate = 0.8;
    double relevanceThreshold = 3.5;
    double relevanceMinimum = 1.0;
    int updateRate = 50;
    int ithUpdate = 0;

    PheromoneAlgorithm pheromoneAlgorithm = new PheromoneAlgorithm(circleRadiusKm, decayRate, relevanceThreshold, relevanceMinimum);

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
        try {
            if (ithUpdate % updateRate == 0)    {
                Idea detectedObjectsIdea = (Idea) detectedObjectsMO.getI();
                double latitude = (double) detectedObjectsIdea.get("object.latitude").getValue();
                double longitude = (double) detectedObjectsIdea.get("object.longitude").getValue();
                double time = (double) detectedObjectsIdea.get("object.time").getValue();

                pheromoneAlgorithm.updateRegions(latitude, longitude);

                // Print for testing
                System.out.println("=============" + time + "===============");
                ArrayList<Idea> circleRegionsIdeaList = (ArrayList<Idea>) pheromoneAlgorithm.getCircleRegionsAsIdea().getValue();
                for(int i=0; i<circleRegionsIdeaList.size(); i++)   {
                    System.out.println(circleRegionsIdeaList.get(i).toStringFull());
                }
            }
            ithUpdate = ithUpdate + 1;

        } catch (java.lang.ClassCastException e)    {

        }

    }
}
