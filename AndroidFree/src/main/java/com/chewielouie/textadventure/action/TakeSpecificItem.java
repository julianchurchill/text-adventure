package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.UserInventory;

public class TakeSpecificItem implements Action {
    private Item item;
    private UserInventory inventory;
    private ModelLocation location;

    public TakeSpecificItem( Item item, UserInventory inventory,
           ModelLocation location ) {
        this.item = item;
        this.inventory = inventory;
        this.location = location;
    }

    public ActionFactory actionFactory() {
        return null;
    }

    public String label() {
        return "Take " + item.midSentenceCasedName();
    }

    public void trigger() {
        location.removeItem( item );
        inventory.addToInventory( item );
    }

    public boolean userMustChooseFollowUpAction() {
        return false;
    }

    public List<Action> followUpActions() {
        return new ArrayList<Action>();
    }

    public boolean userTextAvailable() {
        return true;
    }

    public String userText() {
        return "You take the " + item.midSentenceCasedName() + ".";
    }

    public String name() {
        return "take specific item";
    }

    @Override
    public boolean equals( Object o ) {
        if( !(o instanceof TakeSpecificItem) )
            return false;
        TakeSpecificItem other = (TakeSpecificItem)o;
        return item.equals( other.item );
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + item.hashCode();
        return result;
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
}

