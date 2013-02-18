package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.Item;
import com.chewielouie.textadventure.TextAdventureModel;

public class UseWith implements Action {
    private List<Action> followUpActions = new ArrayList<Action>();
    private Item item;
    private TextAdventureModel model;

    public UseWith( Item item, TextAdventureModel model ) {
        this.item = item;
        this.model = model;
        if( model != null ) {
            for( Item target : model.inventoryItems() )
                followUpActions.add( new UseWithSpecificItem( item, target ) );
            for( Item target : model.currentLocation().items() )
                followUpActions.add( new UseWithSpecificItem( item, target ) );
        }
    }

    public Item item() {
        return item;
    }

    public TextAdventureModel model() {
        return model;
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

