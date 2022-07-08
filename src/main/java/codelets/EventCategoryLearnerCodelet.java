package codelets;

import br.unicamp.cst.core.entities.*;
import entities.PropertyCategory;

import java.util.ArrayList;
import java.util.HashMap;

public class EventCategoryLearnerCodelet extends Codelet {
    Mind mind;
    Memory propertyCategoriesMO;
    Memory objectsBufferMO;
    Memory eventsMC;
    // CodeletContainer does not have methods for setting outputs, only inputs. Thus, it is not useful.
    // Used a Codelet ArrayList instead;
    //    CodeletContainer codeletContainer;
    ArrayList<Codelet> codeletContainer = new ArrayList<Codelet>();
    public EventCategoryLearnerCodelet(Mind mind, Memory objectsBufferMO, MemoryContainer eventsMC)   {
        this.mind = mind;
        this.objectsBufferMO = objectsBufferMO;
        this.eventsMC = eventsMC;

    }
    @Override
    public void accessMemoryObjects() {
        propertyCategoriesMO=(MemoryObject)this.getInput("PROPERTY_CATEGORIES");
    }

    @Override
    public void calculateActivation() {
    }

    @Override
    public void proc() {
        if(propertyCategoriesMO.getI()=="") {
            return;
        }
        ArrayList<PropertyCategory> propertyCategories = (ArrayList<PropertyCategory>) propertyCategoriesMO.getI();

        updateEventTrackers(propertyCategories);

        System.out.println(this.eventsMC.getI());

    }

    public void updateEventTrackers(ArrayList<PropertyCategory> propertyCategories) {
        addMissingEventCodelets(propertyCategories);
        removeUnnecessaryEventCodelets(propertyCategories);
    }

    public void addMissingEventCodelets(ArrayList<PropertyCategory> propertyCategories) {


        for (int i=0; i<propertyCategories.size(); i++)   {
            PropertyCategory ithPropertyCategory = propertyCategories.get(i);

            HashMap<String,Boolean> missingEvents = new HashMap<String,Boolean>();
            missingEvents.put("STAY", true);
            missingEvents.put("IN", true);
            missingEvents.put("OUT", true);


            updateWhichEventsAreMissing(ithPropertyCategory, missingEvents);

            if(missingEvents.get("STAY") == true)  {
                addEventStayTrackerCodelet(ithPropertyCategory);
            }

            if(missingEvents.get("IN") == true) {
                addEventInTrackerCodelet(ithPropertyCategory);
            }

            if(missingEvents.get("OUT") == true) {
                addEventOutTrackerCodelet(ithPropertyCategory);
            }

        }
    }

    public void updateWhichEventsAreMissing(PropertyCategory propertyCategory, HashMap<String,Boolean> missingEvents)    {
        for (int i=0; i<this.codeletContainer.size(); i++) {
            Codelet ithCodelet = this.codeletContainer.get(i);
            String ithCodeletName = ithCodelet.getName();
            String propertyCategoryName = propertyCategory.getName();

            if(ithCodeletName.startsWith(propertyCategoryName)) {
                for(String key : missingEvents.keySet())   {
                    if(ithCodeletName.endsWith(key))    {
                        missingEvents.put(key, false);
                    }
                }
            }
        }
    }

    public boolean isPropertyCategoriesInCodeletContainer(PropertyCategory propertyCategory)    {
        for (int i=0; i<this.codeletContainer.size(); i++)   {
            Codelet ithCodelet = this.codeletContainer.get(i);
            String ithCodeletName = ithCodelet.getName();
            String propertyCategoryName = propertyCategory.getName();

            if(ithCodeletName.startsWith(propertyCategoryName)) {
                return true;
            }
        }
        return false;
    }

    public void addEventStayTrackerCodelet(PropertyCategory propertyCategory)  {
        EventStayCategoryCodelet eventStayCategoryCodelet = new EventStayCategoryCodelet(propertyCategory);
        eventStayCategoryCodelet.addInput(this.objectsBufferMO);
        eventStayCategoryCodelet.addOutput(this.eventsMC);
        this.codeletContainer.add(eventStayCategoryCodelet);
        this.mind.insertCodelet(eventStayCategoryCodelet);
        this.mind.getCodeRack().insertCodelet(eventStayCategoryCodelet);
        eventStayCategoryCodelet.start();
    }

    public void addEventInTrackerCodelet(PropertyCategory propertyCategory)  {
        EventInCategoryCodelet eventInCategoryCodelet = new EventInCategoryCodelet(propertyCategory);
        eventInCategoryCodelet.addInput(this.objectsBufferMO);
        eventInCategoryCodelet.addOutput(this.eventsMC);
        this.codeletContainer.add(eventInCategoryCodelet);
        this.mind.insertCodelet(eventInCategoryCodelet);
        this.mind.getCodeRack().insertCodelet(eventInCategoryCodelet);
        eventInCategoryCodelet.start();
    }

    public void addEventOutTrackerCodelet(PropertyCategory propertyCategory)  {
        EventOutCategoryCodelet eventOutCategoryCodelet = new EventOutCategoryCodelet(propertyCategory);
        eventOutCategoryCodelet.addInput(this.objectsBufferMO);
        eventOutCategoryCodelet.addOutput(this.eventsMC);
        this.codeletContainer.add(eventOutCategoryCodelet);
        this.mind.insertCodelet(eventOutCategoryCodelet);
        this.mind.getCodeRack().insertCodelet(eventOutCategoryCodelet);
        eventOutCategoryCodelet.start();
    }




    public void removeUnnecessaryEventCodelets(ArrayList<PropertyCategory> propertyCategories) {
        for (int i=0; i<this.codeletContainer.size(); i++)   {
            Codelet ithCodelet = this.codeletContainer.get(i);
            if(isCodeletInPropertyCategories(ithCodelet, propertyCategories) == false)    {
                removeEventTrackerCodelet(i);
            }
        }
    }

    public boolean isCodeletInPropertyCategories(Codelet codelet, ArrayList<PropertyCategory> propertyCategories)    {
        for (int i=0; i<propertyCategories.size(); i++)   {
            PropertyCategory ithPropertyCategory = propertyCategories.get(i);
            String ithPropertyCategoryName = ithPropertyCategory.getName();
            String codeletName = codelet.getName();
            if(codeletName.startsWith(ithPropertyCategoryName)) {
                return true;
            }
        }
        return false;
    }

    public void removeEventTrackerCodelet(int codeletIdx) {
        Codelet codeletToRemove = this.codeletContainer.get(codeletIdx);
        this.codeletContainer.remove(codeletIdx);
        codeletToRemove.stop();
        this.mind.getCodeRack().destroyCodelet(codeletToRemove);
    }


}




