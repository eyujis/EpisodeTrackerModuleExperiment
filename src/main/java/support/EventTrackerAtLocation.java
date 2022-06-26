package support;

import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryContainer;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.wme.Idea;

import java.util.ArrayList;
import java.util.List;

public class EventTrackerAtLocation extends Codelet {
    private Memory rawDataMO;
    private Memory categoriesMO;
    private Memory eventsMC;
    private Idea bufferIdea;
    public String locationName;

    @Override
    public void accessMemoryObjects() {
        rawDataMO = (MemoryObject) this.getInput("RAWDATA");
        eventsMC = (MemoryContainer) this.getOutput("EVENTS");
        categoriesMO = (MemoryObject) this.getInput("CATEGORIES");

    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {
        if (rawDataMO.getI()=="" || categoriesMO.getI()=="")
            return;

        synchronized (rawDataMO) {
            bufferIdea = (Idea) rawDataMO.getI();
            List<Double> latitudeBuffer = getArrayListFromIdea(bufferIdea, "frames", "latitude");
            List<Double> longitudeBuffer = getArrayListFromIdea(bufferIdea, "frames", "longitude");
            List<Double> timestampBuffer = getArrayListFromIdea(bufferIdea, "frames", "timestamp");

            Double currentLatitude = getCurrentValue((ArrayList<Double>) latitudeBuffer);
            Double currrentLongitude = getCurrentValue((ArrayList<Double>) longitudeBuffer);
            Double currentTimestamp = getCurrentValue((ArrayList<Double>) timestampBuffer);

            // Provided by the Categories memory object.
            Idea categoryAtLocation = ((Idea)categoriesMO.getI()).get(locationName);

            Double latitude = (Double) categoryAtLocation.get("latitude").getValue();
            Double longitude = (Double) categoryAtLocation.get("longitude").getValue();
            Double radius = (Double) categoryAtLocation.get("radius").getValue();

            Double distance = calculateDistance(currentLatitude, currrentLongitude, latitude, longitude);

            if(distance<radius) {
                Idea atLocationEventIdea = new Idea("event", locationName, 0);
                atLocationEventIdea.add(new Idea ("timestamp", currentTimestamp));
                ((MemoryContainer)eventsMC).setI(atLocationEventIdea, 0.9);
            }

        }
    }

    public ArrayList<Double> getArrayListFromIdea(Idea idea, String pathToArrayList, String pathToData)    {
        //Get the ArrayList of Ideas from the Idea root.
        ArrayList<Idea> ideaArrayList;
        ideaArrayList = (ArrayList<Idea>) idea.get(pathToArrayList).getValue();

        ArrayList<Double> doubleArrayList = new ArrayList<Double> ();
        for(int i=0; i<ideaArrayList.size(); i++) {
            doubleArrayList.add((Double) ((Idea) ideaArrayList.get(i)).get(pathToData).getValue());
        }

        return doubleArrayList;

    }

    public Double getCurrentValue(ArrayList<Double> buffer) {
        return buffer.get(0);
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

