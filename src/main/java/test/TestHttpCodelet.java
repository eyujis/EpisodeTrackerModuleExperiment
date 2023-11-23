package test;

import br.unicamp.cst.io.rest.HttpCodelet;
import br.unicamp.cst.representation.idea.Idea;
import com.google.gson.*;
import memory_storage.MemoryInstance;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TestHttpCodelet extends HttpCodelet {
    String apiURI = "http://127.0.0.1:5000";
    String postURI = apiURI + "/set_idea/";
    String getURI = apiURI + "/get_idea/memory1";
    HashMap<String, Object> params = new HashMap<>();

    @Override
    public void accessMemoryObjects() {
        params.put("value", "");
        params.put("field", "value");
        params.put("name", "memory1");
    }

    @Override
    public void calculateActivation() {

    }

    @Override
    public void proc() {
        MemoryInstance memoryInstance = new MemoryInstance("memoryInst");
        Idea myIdea = createIdea();
            System.out.println(memoryInstance.postIdea(myIdea));
            System.out.println(memoryInstance.getIdea().toStringFull());
    }


    public Idea createIdea() {
        Idea father = new Idea("father", "", 0);
        Idea child1 = new Idea("child1", "", 0);
        Idea child2 = new Idea("child2", 3);
        Idea grandchild11 = new Idea("grandchild11", 4);
        child1.add(grandchild11);
        father.add(child1);
        father.add(child2);
        return father;
    }


}
