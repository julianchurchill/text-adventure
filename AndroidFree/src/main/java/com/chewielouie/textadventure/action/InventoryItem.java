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

    public InventoryItem( Item item, UserInventory inventory, ModelLocation location ) {
        this.item = item;
        this.inventory = inventory;
        this.location = location;
        followUpActions.add( new Examine( item ) );
        followUpActions.add( new UseWith( item, inventory, location ) );
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




