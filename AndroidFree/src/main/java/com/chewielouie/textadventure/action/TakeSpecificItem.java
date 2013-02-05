package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.Item;
import com.chewielouie.textadventure.UserInventory;

public class TakeSpecificItem implements Action {
    private Item item;
    private UserInventory inventory;

    public TakeSpecificItem( Item item, UserInventory inventory ) {
        this.item = item;
        this.inventory = inventory;
    }

    public String label() {
        return "Take " + item.midSentenceCasedName();
    }

    public void trigger() {
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
}

