package pheromone;

import br.unicamp.cst.representation.idea.Idea;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PheromoneAlgorithm {
    private ArrayList<CircleRegion> circleRegions = new ArrayList<CircleRegion>();
    private double circleRadius;
    private double decayRate;
    private double relevanceThreshold;
    private double relevanceMinimum;
    private int nthRegion = 0;
    private int nthRegionLongTerm = 0;


    private ArrayList<CircleRegion> circleRegionsLongTerm = new ArrayList<CircleRegion>();
    private double circleRadiusLongTerm;


    public PheromoneAlgorithm(double circleRadius,
                              double decayRate,
                              double relevanceThreshold,
                              double relevanceMinimum,
                              double circleRadiusLongTerm) {
        this.circleRadius = circleRadius;
        this.decayRate = decayRate;
        this.relevanceThreshold = relevanceThreshold;
        this.relevanceMinimum = relevanceMinimum;
        this.circleRadiusLongTerm = circleRadiusLongTerm;
    }

    public synchronized void updateRegions(double x, double y) {
        boolean intersected = false;
        Circle circle = new Circle(x, y, this.circleRadius);
        for (int i=0; i<this.circleRegions.size() && intersected == false; i++)  {
            CircleRegion circleRegion = this.circleRegions.get(i);
            if(circleRegion.doesCircleIntersectRegion(circle))   {
                circleRegion.addCircleToRegion(circle);
                circleRegion.increaseRelevance();
                intersected = true;
            }
        }

        removeRegionsBelowRelevanceMinimum();

        if (intersected==false) {
            CircleRegion newCircleRegion = new CircleRegion(circle, this.nthRegion);
            this.circleRegions.add(newCircleRegion);
            this.nthRegion = this.nthRegion+1;
        }

        decayRegionsRelevance();

    }

//    TODO | This second layer of the algorithm has not been validated yet. 
    public synchronized void updateLongTermRegions(double x, double y) {
        boolean intersected = false;
        Circle circle = new Circle(x, y, this.circleRadiusLongTerm);
        for (int i=0; i<this.circleRegionsLongTerm.size() && intersected == false; i++)  {
            CircleRegion circleRegion = this.circleRegionsLongTerm.get(i);
            if(circleRegion.doesCircleIntersectRegion(circle))   {
                circleRegion.addCircleToRegion(circle);
                circleRegion.increaseRelevance();
                intersected = true;
            }
        }

        if (intersected==false) {
            CircleRegion newCircleRegion = new CircleRegion(circle, this.nthRegionLongTerm);
            this.circleRegionsLongTerm.add(newCircleRegion);
            this.nthRegionLongTerm = this.nthRegionLongTerm+1;
        }

    }


    public void removeRegionsBelowRelevanceMinimum()    {
        ArrayList<Integer> removeRegionIdxList =  new ArrayList<Integer>();
        for (int i=0; i<this.circleRegions.size(); i++) {

            CircleRegion circleRegion = this.circleRegions.get(i);

            if(circleRegion.getRelevance()<this.relevanceMinimum)   {
                removeRegionIdxList.add(i);
            }
        }
        removeRegionIdxs(removeRegionIdxList);
    }

    public void removeRegionIdxs(ArrayList<Integer> removeRegionIdxList)  {
        for(int i=removeRegionIdxList.size()-1; i>=0; i--) {
            this.circleRegions.remove((int) removeRegionIdxList.get(i));
        }
    }

    public void decayRegionsRelevance() {
        for(int i=0; i<this.circleRegions.size(); i++) {
            CircleRegion circleRegion = this.circleRegions.get(i);
            if (circleRegion.getRelevance()<this.relevanceThreshold)    {
                circleRegion.decayRelevance(this.decayRate);
            }
        }
    }

    public Idea getCircleRegionsAsIdea() {
        Idea circleRegionsIdea = new Idea("circleRegions","",0);
        ArrayList<Idea> circleRegionsIdeaList = new ArrayList<Idea>();

        for(int i=0; i<this.circleRegions.size(); i++)   {
            String name = circleRegions.get(i).getName();
            Idea circleRegionIdea = new Idea(name,"",0);
            circleRegionIdea.add(circleRegions.get(i).getRegionCenterIdea());
            circleRegionsIdeaList.add(circleRegionIdea);
        }

        circleRegionsIdea.setValue(circleRegionsIdeaList);


        return circleRegionsIdea;
    }

    public Idea getLongTermCircleRegionsAsIdea() {
        Idea circleLongTermRegionsIdea = new Idea("circleLongTermRegions","",0);
        ArrayList<Idea> circleRegionsLongTermIdeaList = new ArrayList<Idea>();

        for(int i=0; i<this.circleRegionsLongTerm.size(); i++)   {
            String name = circleRegionsLongTerm.get(i).getName();
            Idea circleRegionIdea = new Idea(name,"",0);
            circleRegionIdea.add(circleRegionsLongTerm.get(i).getRegionCenterIdea());
            circleRegionsLongTermIdeaList.add(circleRegionIdea);
        }

        circleLongTermRegionsIdea.setValue(circleRegionsLongTermIdeaList);


        return circleLongTermRegionsIdea;
    }

}
