package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.UserInventory;
import com.chewielouie.textadventure.TextAdventureModel;

public class ShowInventory implements Action {
    private TextAdventureModel model;
    private UserInventory inventory;
    private ActionFactory actionFactory;
    private List<Item> items = null;

    public ShowInventory( UserInventory inventory, TextAdventureModel model,
           ActionFactory factory ) {
        this.inventory = inventory;
        this.model = model;
        this.actionFactory = factory;
    }

    public ActionFactory actionFactory() {
        return actionFactory;
    }

    public String label() {
        return "Show inventory";
    }

    public void trigger() {
        if( inventory != null )
            items = inventory.inventoryItems();
    }

    public boolean userMustChooseFollowUpAction() {
        return true;
    }

    public List<Action> followUpActions() {
        List<Action> actions = new ArrayList<Action>();
        if( actionFactory != null )
            for( Item item : items )
                if( item.visible() )
                    actions.add(
                        actionFactory.createInventoryItemAction( item, inventory,
                            (model != null ? model.currentLocation() : null ) ) );
        return actions;
    }

    public boolean userTextAvailable() {
        return false;
    }

    public String userText() {
        return "";
    }

    public String name() {
        return "show inventory";
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

