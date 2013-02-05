package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.Item;
import com.chewielouie.textadventure.TextAdventureModel;

public class TakeAnItem implements Action {
    private TextAdventureModel model;

    public TakeAnItem( TextAdventureModel model ) {
        this.model = model;
    }

    public String label() {
        return "Take an item";
    }

    public void trigger() {
        //items = model.inventoryItems();
    }

    public boolean userMustChooseFollowUpAction() {
        return true;
    }

    public List<Action> followUpActions() {
        //List<Action> actions = new ArrayList<Action>();
        //for( Item item : items )
            //actions.add( new Examine( item ) );
        //return actions;
        return new ArrayList<Action>();
    }

    public boolean userTextAvailable() {
        return false;
    }

    public String userText() {
        return "";
    }

    @Override
    public boolean equals( Object o ) {
        if( !(o instanceof TakeAnItem) )
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}


