package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.Item;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.UserInventory;
import com.chewielouie.textadventure.TextAdventureModel;

public class UseWith implements Action {
    private List<Action> followUpActions = new ArrayList<Action>();
    private Item item;
    private UserInventory inventory;
    private ModelLocation location;
    private TextAdventureModel model;

    public UseWith( Item item, TextAdventureModel model ) {
        this( item,
              (model instanceof UserInventory ? (UserInventory)model : null),
              (model != null ? model.currentLocation() : null ) );
        this.model = model;
    }

    public UseWith( Item item, UserInventory inventory, ModelLocation location ) {
        this.item = item;
        this.inventory = inventory;
        this.location = location;
        if( inventory != null )
            for( Item target : inventory.inventoryItems() )
                followUpActions.add( new UseWithSpecificItem( item, target ) );
        if( location != null )
            for( Item target : location.items() )
                followUpActions.add( new UseWithSpecificItem( item, target ) );
    }

    public Item item() {
        return item;
    }

    public UserInventory inventory() {
        return inventory;
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

