package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;

public class TakeAnItem implements Action {
    private List<Action> followUpActions = new ArrayList<Action>();
    private List<Item> items;
    private UserInventory inventory;
    private ModelLocation location;
    private ActionFactory actionFactory;

    public TakeAnItem( List<Item> items, UserInventory inventory,
           ModelLocation location, ActionFactory factory ) {
        this.items = items;
        this.inventory = inventory;
        this.location = location;
        this.actionFactory = factory;
        if( items != null )
            for( Item item : items )
                followUpActions.add( factory.createTakeSpecificItemAction(
                                                item, inventory, location ) );
    }

    public ActionFactory actionFactory() {
        return actionFactory;
    }

    public List<Item> items() {
       return items;
    }

    public UserInventory inventory() {
        return inventory;
    }

    public ModelLocation location() {
        return location;
    }

    public String label() {
        return "Take an item";
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

    public String name() {
        return "take an item";
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


