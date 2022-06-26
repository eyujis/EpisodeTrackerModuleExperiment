package codelets;
import br.unicamp.cst.core.entities.Codelet;
import br.unicamp.cst.core.entities.Memory;
import br.unicamp.cst.core.entities.MemoryObject;
import br.unicamp.cst.representation.wme.Idea;
import environment.Environment;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;



public class CategoryLearnerCodelet extends Codelet {

    private Memory rawDataMO;
    private Memory categoriesMOInput;
    private Memory categoriesMOOutput;
    private Boolean initializedMO = true;

    @Override
    public void accessMemoryObjects() {
        rawDataMO=(MemoryObject)this.getInput("RAWDATA");
        categoriesMOInput=(MemoryObject)this.getInput("CATEGORIES");
        categoriesMOOutput=(MemoryObject)this.getOutput("CATEGORIES");
    }
    
    @Override
    public void calculateActivation() {


    }

    @Override
    public void proc() {

        if (initializedMO==true)   {
            categoriesMOOutput.setI(initializeCoordinates());
            initializedMO=false;
        }

    }



    public Idea initializeCoordinates() {
        Idea categories = new Idea("categories","",0);
        categories.add(new Idea("at_home", "", 0));
        categories.add(new Idea("at_work", "", 0));
        categories.add(new Idea("at_school", "", 0));
        categories.add(new Idea("at_beach", "", 0));
        categories.add(new Idea("at_gym", "", 0));
        categories.add(new Idea("at_restaurant", "", 0));

        // Values set based on https://colab.research.google.com/drive/1xKH5zHPqMYnQS1qYPbLEYf15IZb3Cl3E?usp=sharing
        // New dataset v_2 https://colab.research.google.com/drive/1YFazWuDq2CjxaszTfMUjv5UfoVsUWAUT?usp=sharing
        // The radius also changed from 0.05 to 0.08.
        // TODO Fine tune the radius and the latitude and longitude center.
        categories.get("at_home").add(new Idea("latitude")).setValue(32.866173289153245);
        categories.get("at_home").add(new Idea("longitude")).setValue(-117.22158849730943);
        categories.get("at_home").add(new Idea("radius")).setValue(0.08);

        categories.get("at_work").add(new Idea("latitude")).setValue(32.8759161844836);
        categories.get("at_work").add(new Idea("longitude")).setValue(-117.24122271777631);
        categories.get("at_work").add(new Idea("radius")).setValue(0.08);

        categories.get("at_school").add(new Idea("latitude")).setValue(32.87649460282245);
        categories.get("at_school").add(new Idea("longitude")).setValue(-117.24060468145133);
        categories.get("at_school").add(new Idea("radius")).setValue(0.08);

        categories.get("at_beach").add(new Idea("latitude")).setValue(32.864564117647056);
        categories.get("at_beach").add(new Idea("longitude")).setValue(-117.25425788235295);
        categories.get("at_beach").add(new Idea("radius")).setValue(0.08);

        categories.get("at_gym").add(new Idea("latitude")).setValue(32.88520140816324);
        categories.get("at_gym").add(new Idea("longitude")).setValue(-117.24005439795917);
        categories.get("at_gym").add(new Idea("radius")).setValue(0.08);

        categories.get("at_restaurant").add(new Idea("latitude")).setValue(33.30911052413792);
        categories.get("at_restaurant").add(new Idea("longitude")).setValue(-116.9665380827586);
        categories.get("at_restaurant").add(new Idea("radius")).setValue(0.08);


        return categories;
    }


}
