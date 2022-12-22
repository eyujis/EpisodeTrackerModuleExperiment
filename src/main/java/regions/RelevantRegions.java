package regions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RelevantRegions {
    BufferedReader csvReader = new BufferedReader(new FileReader("src/main/regions/relevant_regions.csv"));
    String[] rawLineData = null;
    private ArrayList<RelevantRegionPC> relevantRegionPCArrayList = new ArrayList<RelevantRegionPC>();
    public RelevantRegions() throws IOException {
        rawLineData = this.readLine();
        rawLineData = this.readLine();
        while(rawLineData!=null) {
            Double currentLatitude = Double.valueOf(rawLineData[1]);
            Double currentLongitude = Double.valueOf(rawLineData[2]);
            String currentRegionName = rawLineData[3];

            RelevantRegionPC relevantRegionPC = new RelevantRegionPC(currentRegionName, currentLatitude, currentLongitude);

            relevantRegionPCArrayList.add(relevantRegionPC);

            rawLineData = this.readLine();
        }
    }

    public String[] readLine() throws IOException {
        String row;
        if ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            return data;
        } else {
            csvReader.close();
            return null;
        }
    }

    public ArrayList<RelevantRegionPC> getRelevantRegionPCArrayList() {
        return relevantRegionPCArrayList;
    }
}
