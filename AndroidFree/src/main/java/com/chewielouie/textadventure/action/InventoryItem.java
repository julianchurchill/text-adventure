package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.Item;
import com.chewielouie.textadventure.TextAdventureModel;

public class InventoryItem implements Action {
    private List<Action> followUpActions = new ArrayList<Action>();
    private Item item;
    private TextAdventureModel model;

    public InventoryItem( Item item, TextAdventureModel model ) {
        this.item = item;
        this.model = model;
        followUpActions.add( new Examine( item ) );
        followUpActions.add( new UseWith( item, model ) );
    }

    public TextAdventureModel model() {
        return model;
    }

    public Item item() {
        return item;
    }

    public String label() {
        return item.name();
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
        if( !(o instanceof InventoryItem) )
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}




