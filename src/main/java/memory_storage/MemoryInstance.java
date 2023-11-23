package memory_storage;

import br.unicamp.cst.io.rest.HttpCodelet;
import br.unicamp.cst.representation.idea.Idea;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;

public class MemoryInstance {
    String apiURL = "http://127.0.0.1:5000";
    String postURL = apiURL + "/set_idea/";
    String getURL = apiURL + "/get_idea/";
    HashMap<String, Object> params = new HashMap<>();

    HttpCodelet httpCodelet = new HttpCodelet() {
        @Override
        public void accessMemoryObjects() {

        }

        @Override
        public void calculateActivation() {

        }

        @Override
        public void proc() {

        }
    };

    public MemoryInstance(String memoryName) {
        getURL = getURL + memoryName;
        params.put("value", "");
        params.put("field", "value");
        params.put("name", memoryName);
        initializeMemory();
    }

    public Idea getIdea() {
        String getJson = null;

        try {
            getJson = httpCodelet.sendGET(this.getURL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonObject responseJsonObject = (JsonObject) JsonParser.parseString(getJson);
        String jsonStringLiteral = (String) responseJsonObject.get("value").toString();

        String jsonString = new Gson().fromJson(jsonStringLiteral, String.class);
        JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);
        Idea myIdea = new Gson().fromJson(jsonObject, Idea.class);
        return myIdea;
    }


    public String postIdea(Idea idea) {
        JsonObject myJson = ideaToJsonObject(idea);
        params.replace("value", myJson.toString());

        String response = " API POST request failed!";

        Gson gson = new Gson();
        try {
            response = httpCodelet.sendPOST(this.postURL, params, "application/json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    public JsonObject ideaToJsonObject(Idea myIdea) {
        Gson gson = new Gson();
        String myJSON = gson.toJson(myIdea);
        JsonObject myJsonObject = gson.fromJson(myJSON, JsonObject.class);
        return myJsonObject;
    }

    public void initializeMemory() {
        try {
            httpCodelet.sendPOST(this.postURL, params, "application/json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
