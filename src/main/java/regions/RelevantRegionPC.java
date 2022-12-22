package regions;

import entities.PropertyCategory;
import regions.Circle;
import regions.Position;

public class RelevantRegionPC implements PropertyCategory {
    String name;
    Double latitude;
    Double longitude;
    double circleRadiusKm = 10 * Math.pow(10,-3);


    public RelevantRegionPC(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public Object generateReplica() {
        return null;
    }

    @Override
    public boolean belongsToCategory(Object position) {
        if(this.doesPositionIntersectCircle((Position) position))    {
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public boolean doesPositionIntersectCircle(Position position) {
        double x1 = position.getX();
        double y1 = position.getY();

        double x2 = latitude;
        double y2 = longitude;
        double r2 = circleRadiusKm;

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

}
