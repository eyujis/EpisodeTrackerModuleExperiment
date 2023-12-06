package codelets;

import br.unicamp.cst.core.entities.*;
import codelets.EventCategoryCodelets.EventInCategoryCodelet;
import codelets.EventCategoryCodelets.EventOutCategoryCodelet;
import codelets.EventCategoryCodelets.EventStayCategoryCodelet;
import entities.PropertyCategory;
import memory_storage.MemoryInstance;

import java.util.ArrayList;
import java.util.HashMap;

public class EventCategoryLearnerCodelet extends Codelet {
    Mind mind;
    Memory propertyCategoriesMO;
    Memory objectsBufferMO;
    Memory eventsMO;
    MemoryInstance objectsBufferMI;
    MemoryInstance eventsMI;

    // CodeletContainer does not have methods for setting outputs, only inputs. Thus, it is not useful.
    // Used a Codelet ArrayList instead;
    ArrayList<Codelet> codeletContainer = new ArrayList<Codelet>();
    public EventCategoryLearnerCodelet(Mind mind, Memory objectsBufferMO, Memory eventsMO,
                                       MemoryInstance objectsBufferMI, MemoryInstance eventsMI)   {
        this.mind = mind;
        this.objectsBufferMO = objectsBufferMO;
        this.eventsMO = eventsMO;
        this.objectsBufferMI = objectsBufferMI;
        this.eventsMI = eventsMI;
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

//        System.out.println(((Idea) this.eventsMO.getI()).toStringFull());
//        System.out.println("-------------------------");
//        Idea event = ((Idea) this.eventsMO.getI());
//        ArrayList<Idea> timeSteps = (ArrayList<Idea>) event.get("timeSteps").getValue();
//        System.out.println(event.getName());
//        System.out.println(timeSteps.get(0).toStringFull());
//        System.out.println(timeSteps.get(1).toStringFull());


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

    public void addEventStayTrackerCodelet(PropertyCategory propertyCategory)  {
        EventStayCategoryCodelet eventStayCategoryCodelet = new EventStayCategoryCodelet(propertyCategory);
        eventStayCategoryCodelet.setObjectsBufferMI(objectsBufferMI);
        eventStayCategoryCodelet.setEventsMI(eventsMI);
        eventStayCategoryCodelet.addInput(this.objectsBufferMO);
        eventStayCategoryCodelet.addOutput(this.eventsMO);
        eventStayCategoryCodelet.setIsMemoryObserver(true);
        this.objectsBufferMO.addMemoryObserver(eventStayCategoryCodelet);
        this.codeletContainer.add(eventStayCategoryCodelet);
        this.mind.insertCodelet(eventStayCategoryCodelet);
        this.mind.getCodeRack().insertCodelet(eventStayCategoryCodelet);
        eventStayCategoryCodelet.start();
    }

    public void addEventInTrackerCodelet(PropertyCategory propertyCategory)  {
        EventInCategoryCodelet eventInCategoryCodelet = new EventInCategoryCodelet(propertyCategory);
        eventInCategoryCodelet.setObjectsBufferMI(objectsBufferMI);
        eventInCategoryCodelet.setEventsMI(eventsMI);
        eventInCategoryCodelet.addInput(this.objectsBufferMO);
        eventInCategoryCodelet.addOutput(this.eventsMO);
        eventInCategoryCodelet.setIsMemoryObserver(true);
        this.objectsBufferMO.addMemoryObserver(eventInCategoryCodelet);
        this.codeletContainer.add(eventInCategoryCodelet);
        this.mind.insertCodelet(eventInCategoryCodelet);
        this.mind.getCodeRack().insertCodelet(eventInCategoryCodelet);
        eventInCategoryCodelet.start();
    }

    public void addEventOutTrackerCodelet(PropertyCategory propertyCategory)  {
        EventOutCategoryCodelet eventOutCategoryCodelet = new EventOutCategoryCodelet(propertyCategory);
        eventOutCategoryCodelet.setObjectsBufferMI(objectsBufferMI);
        eventOutCategoryCodelet.setEventsMI(eventsMI);
        eventOutCategoryCodelet.addInput(this.objectsBufferMO);
        eventOutCategoryCodelet.addOutput(this.eventsMO);
        eventOutCategoryCodelet.setIsMemoryObserver(true);
        this.objectsBufferMO.addMemoryObserver(eventOutCategoryCodelet);
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




