package pheromone;

import br.unicamp.cst.representation.wme.Idea;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PheromoneAlgorithm {
    private ArrayList<CircleRegion> circleRegions = new ArrayList<CircleRegion>();
    private double circleRadius;
    private double decayRate;
    private double relevanceThreshold;
    private double relevanceMinimum;
    private int nthRegion = 0;


    public PheromoneAlgorithm(double circleRadius, double decayRate, double relevanceThreshold, double relevanceMinimum) {
        this.circleRadius = circleRadius;
        this.decayRate = decayRate;
        this.relevanceThreshold = relevanceThreshold;
        this.relevanceMinimum = relevanceMinimum;
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
            circleRegionsIdeaList.add(circleRegionIdea);
        }

        circleRegionsIdea.setValue(circleRegionsIdeaList);

        return circleRegionsIdea;
    }
}
