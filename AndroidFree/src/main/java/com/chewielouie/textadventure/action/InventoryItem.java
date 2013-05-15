package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.UserInventory;

public class InventoryItem implements Action {
    private List<Action> followUpActions = new ArrayList<Action>();
    private Item item;
    private UserInventory inventory;
    private ModelLocation location;
    private ActionFactory actionFactory;

    public InventoryItem( Item item, UserInventory inventory,
                          ModelLocation location, ActionFactory factory ) {
        this.item = item;
        this.inventory = inventory;
        this.location = location;
        this.actionFactory = factory;
        if( factory != null && item != null ) {
            followUpActions.add( factory.createExamineAction( item ) );
            followUpActions.add(
                factory.createUseWithAction( item, inventory, location ) );
        }
    }

    public ActionFactory actionFactory() {
        return actionFactory;
    }

    public UserInventory inventory() {
        return inventory;
    }

    public ModelLocation location() {
        return location;
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

    public String name() {
        return "inventory item";
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




