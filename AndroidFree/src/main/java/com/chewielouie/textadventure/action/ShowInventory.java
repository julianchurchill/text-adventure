package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.TextAdventureModel;

public class ShowInventory implements Action {
    private TextAdventureModel model;

    public ShowInventory( TextAdventureModel model ) {
        this.model = model;
    }

    public String label() {
        return "Show inventory";
    }

    public void trigger() {
        model.inventoryItems();
    }

    public boolean userMustChooseFollowUpAction() {
        return false;
    }

    public List<Action> followUpActions() {
        return new ArrayList<Action>();
    }

    @Override
    public boolean equals( Object o ) {
        if( !(o instanceof ShowInventory) )
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}

