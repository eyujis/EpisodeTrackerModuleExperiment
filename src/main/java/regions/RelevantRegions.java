package regions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

    public ArrayList<RelevantRegionPC> getRelevantRegionsFromMeanShift() {

        ArrayList<RelevantRegionPC> relevantRegionPCArrayList = new ArrayList<RelevantRegionPC>();

        try {
            // Set the API endpoint and headers
            String apiUrl = "http://localhost:8197/v0/clusters";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            // Get the response code
            int responseCode = connection.getResponseCode();
//            System.out.println("Response Code: " + responseCode);

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder responseContent = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                responseContent.append(inputLine);
            }

            in.close();

            JsonObject responseJsonObject = JsonParser.parseString(responseContent.toString()).getAsJsonObject();

            JsonArray clusters = responseJsonObject.get("clusters").getAsJsonArray();
            for (int i=0; i<clusters.size(); i++) {
                JsonElement cluster = clusters.get(i);
                double longitude = cluster.getAsJsonObject().get("LongC").getAsDouble();
                double latitude = cluster.getAsJsonObject().get("LatC").getAsDouble();
                double radius = cluster.getAsJsonObject().get("raio").getAsDouble();
                double relevance = cluster.getAsJsonObject().get("relevante").getAsDouble();
                if(relevance>0) {
                    //TODO fix id creating a new ID for every new region;
                    relevantRegionPCArrayList.add(new RelevantRegionPC("r"+i, latitude, longitude, radius));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return relevantRegionPCArrayList;
    }

}
