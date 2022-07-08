package pheromone;
import br.unicamp.cst.representation.idea.Idea;

import java.util.ArrayList;
import java.lang.Math;

public class CircleRegion {
    private ArrayList<Circle> circles = new ArrayList<Circle>();
    private double relevance;
    private String name;

    public CircleRegion(Circle circle, int ithRegion)   {
        addCircleToRegion(circle);
        this.relevance = 1;
        name = String.valueOf(ithRegion).concat(" Region");
    }

    public void decayRelevance(double decayRate) {
        this.relevance = relevance*decayRate;
    }

    public void increaseRelevance() {
        this.relevance = this.relevance+1;
    }

    public double getRelevance() {
        return this.relevance;
    }

    public String getName() {
        return this.name;
    }

    // add circled

    public void addCircleToRegion(Circle circle) {
        this.circles.add(circle);
    }

    public boolean doesCircleIntersectRegion(Circle circle)  {
        for(int i=0; i<this.circles.size(); i++) {
            if (doesCirclesIntersect(circle, this.circles.get(i)))   {
                return true;
            }
        }
        return false;
    }

    public boolean doesCirclesIntersect(Circle circle1, Circle circle2) {
        double x1 = circle1.getX();
        double y1 = circle1.getY();
        double r1 = circle1.getRadius();

        double x2 = circle2.getX();
        double y2 = circle2.getY();
        double r2 = circle2.getRadius();

//        double center_distance = Math.pow((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2), 0.5);
        double center_distance = calculateDistance(x1, y1, x2, y2);

        double radius_sum = r1 + r2;

        if (center_distance <= radius_sum)  {
            return true;
        } else {
            return false;
        }
    }

    public boolean doesPositionIntersectRegion(Position position)  {
        for(int i=0; i<this.circles.size(); i++) {
            if (doesPositionIntersectCircle(position, this.circles.get(i)))   {
                return true;
            }
        }
        return false;
    }

    public boolean doesPositionIntersectCircle(Position position, Circle circle) {
        double x1 = position.getX();
        double y1 = position.getY();

        double x2 = circle.getX();
        double y2 = circle.getY();
        double r2 = circle.getRadius();

        double center_distance = calculateDistance(x1, y1, x2, y2);

        double radius_sum = r2;

        if (center_distance <= radius_sum)  {
            return true;
        } else {
            return false;
        }
    }

    public Double calculateDistance(double p1Lat, double p1Lon, double p2Lat, double p2Lon) {
        Double R = 6371.0;
        Double distance;
        double lat1 = Math.toRadians(p1Lat);
        double lon1 = Math.toRadians(p1Lon);
        double lat2 = Math.toRadians(p2Lat);
        double lon2 = Math.toRadians(p2Lon);

        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        double a = Math.pow(Math.sin(dlat /2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        distance = R*c;

        return distance;
    }

    public Idea getRegionCenterIdea() {
        ArrayList<Double> latitudeList = new ArrayList<Double> ();
        ArrayList<Double> longitudeList = new ArrayList<Double> ();
        Idea circleRegionsIdea = new Idea("regionCenter","",0);

        for(int i=0; i<circles.size(); i++) {
            latitudeList.add(circles.get(i).getX());
            longitudeList.add(circles.get(i).getY());
        }
        double latitudeMean = listMean(latitudeList);
        double longitudeMean = listMean(longitudeList);

        circleRegionsIdea.add(new Idea("latitudeMean", latitudeMean));
        circleRegionsIdea.add(new Idea("longitudeMean", longitudeMean));

        return circleRegionsIdea;
    }

    public double listMean(ArrayList<Double> li) {
        double sum=0;
        for(int i=0; i<li.size(); i++)  {
            sum = sum+li.get(i);
        }
        double average = sum/li.size();
        return average;
    }

    public ArrayList<Circle> getCircles()  {
        return this.circles;
    }



}