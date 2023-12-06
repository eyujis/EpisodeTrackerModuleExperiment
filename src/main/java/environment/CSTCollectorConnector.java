package environment;

import br.unicamp.cst.representation.idea.Idea;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CSTCollectorConnector {
    String apiUrl = "http://vm.hiaac.ic.unicamp.br:8089/getValues";

    public Idea getCurrentPosition() {
        try {

            URL url = new URL(this.apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder responseContent = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                responseContent.append(inputLine);
            }

            in.close();

            JsonObject responseJsonObject = JsonParser.parseString(responseContent.toString()).getAsJsonObject();
            String jsonString = responseJsonObject.get("body").getAsString();

            // Parse the JSON string to a JsonArray
            JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();

            // Assuming there is only one object in the array, you can get the JsonObject at index 0
            JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

            // Now you can access individual fields within the JsonObject
            double latitude = jsonObject.get("latitude").getAsDouble();
            double longitude = jsonObject.get("longitude").getAsDouble();
            long timestamp = jsonObject.get("timestamp").getAsLong();


            Idea position = new Idea("position", "", 0);
            position.add(new Idea("latitude", latitude));
            position.add(new Idea("longitude", longitude));
            position.add(new Idea("timestamp", timestamp));

            return position;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Idea("empty");
    }
}


