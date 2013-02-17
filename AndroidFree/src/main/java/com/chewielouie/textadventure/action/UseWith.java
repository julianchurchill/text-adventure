package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.Item;
import com.chewielouie.textadventure.TextAdventureModel;

public class UseWith implements Action {
    private List<Action> followUpActions = new ArrayList<Action>();

    public UseWith( TextAdventureModel model ) {
        for( Item item : model.inventoryItems() )
            followUpActions.add( new UseWithSpecificItem( item ) );
        for( Item item : model.currentLocation().items() )
            followUpActions.add( new UseWithSpecificItem( item ) );
    }

    public String label() {
        return "Use with";
    }

    public void trigger() {
    }

    public boolean userMustChooseFollowUpAction() {
        return true;
    }

    public List<Action> followUpActions() {
        return followUpActions;
    }

    public boolean userTextAvailable() {
        return false;
    }

    public String userText() {
        return "";
    }

    @Override
    public boolean equals( Object o ) {
        if( !(o instanceof UseWith) )
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}

