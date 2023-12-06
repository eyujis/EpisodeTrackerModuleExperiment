package tests;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import br.unicamp.cst.representation.idea.Idea;
import com.google.gson.*;
import regions.RelevantRegionPC;

public class TestCluster {
    public static void main(String[] args) {

        try {
            // Set the API endpoint and headers
            String apiUrl = "http://localhost:8197/v0/clusters";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            // Get the response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder responseContent = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                responseContent.append(inputLine);
            }

            in.close();

            System.out.println(responseContent.toString());

            JsonObject responseJsonObject = JsonParser.parseString(responseContent.toString()).getAsJsonObject();
            JsonArray clusters = responseJsonObject.get("clusters").getAsJsonArray();

            for (int i=0; i<clusters.size(); i++) {
                JsonElement cluster = clusters.get(i);
                double longitude = cluster.getAsJsonObject().get("LongC").getAsDouble();
                double latitude = cluster.getAsJsonObject().get("LatC").getAsDouble();
                double radius = cluster.getAsJsonObject().get("raio").getAsDouble();
                new RelevantRegionPC("r"+i, latitude, longitude, radius);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
