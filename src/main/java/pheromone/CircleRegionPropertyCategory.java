package pheromone;

import entities.PropertyCategory;

import java.util.Random;

public class CircleRegionPropertyCategory implements PropertyCategory {
    CircleRegion circleRegion;

    public CircleRegionPropertyCategory(CircleRegion circleRegion)  {
        this.circleRegion = circleRegion;
    }


    @Override
    public Object generateReplica() {
        Random random = new Random();
        int circleRandomIdx = random.nextInt(circleRegion.getCircles().size()-1);
        return this.circleRegion.getCircles().get(circleRandomIdx);
    }

    @Override
    public boolean belongsToCategory(Object position) {
        if(circleRegion.doesPositionIntersectRegion((Position) position))    {
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return circleRegion.getName();
    }
}
