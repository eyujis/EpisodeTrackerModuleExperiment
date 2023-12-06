package codelets.EventCategoryCodelets;

import br.unicamp.cst.representation.idea.Idea;
import entities.EventTrackerPropertyCategoryCodelet;
import entities.PropertyCategory;
import regions.Position;

public class EventInCategoryCodelet extends EventTrackerPropertyCategoryCodelet {

    public EventInCategoryCodelet(PropertyCategory propertyCategory)   {
        this.propertyCategory = propertyCategory;
        this.setName(propertyCategory.getName()+" - IN");
    }

    @Override
    public boolean eventTracked(Idea objectInitialState, Idea objectFinalState) {
        Position initialPosition = getPositionFromObject(objectInitialState);
        Position finalPosition = getPositionFromObject(objectFinalState);
        if(this.propertyCategory.belongsToCategory(initialPosition) == false &&
           this.propertyCategory.belongsToCategory(finalPosition) == true) {
            return true;
        }
        return false;

    }

    public Position getPositionFromObject(Idea objectIdea) {
        double latitude = (double) objectIdea.get("latitude").getValue();
        double longitude = (double) objectIdea.get("longitude").getValue();
        Position position = new Position(latitude, longitude);
        return position;
    }


}
