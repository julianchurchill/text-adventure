package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.UserInventory;

public class UseWith implements Action {
    private List<Action> followUpActions = new ArrayList<Action>();
    private Item item;
    private UserInventory inventory;
    private ModelLocation location;
    private ActionFactory actionFactory;

    public UseWith( Item item, UserInventory inventory,
                    ModelLocation location, ActionFactory factory ) {
        this.item = item;
        this.inventory = inventory;
        this.location = location;
        this.actionFactory = factory;
        if( factory != null ) {
            extractActionsForInventoryItems();
            extractActionsForLocationItems();
        }
    }

    public ActionFactory actionFactory() {
        return actionFactory;
    }

    private void extractActionsForInventoryItems() {
        if( inventory != null )
            for( Item target : inventory.inventoryItems() )
                if( target.visible() )
                    followUpActions.add(
                        actionFactory.createUseWithSpecificItemAction( item, target ) );
    }

    private void extractActionsForLocationItems() {
        if( location != null )
            for( Item target : location.items() )
                if( target.visible() )
                    followUpActions.add(
                        actionFactory.createUseWithSpecificItemAction( item, target ) );
    }

    public Item item() {
        return item;
    }

    public UserInventory inventory() {
        return inventory;
    }

    public ModelLocation location() {
        return location;
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

    public String name() {
        return "use with";
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

