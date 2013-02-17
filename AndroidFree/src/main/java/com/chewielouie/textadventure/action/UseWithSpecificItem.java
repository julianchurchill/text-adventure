package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.Item;

public class UseWithSpecificItem implements Action {
    private List<Action> followUpActions = new ArrayList<Action>();
    private Item item;

    public UseWithSpecificItem( Item item ) {
        this.item = item;
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
        return false;
    }

    public List<Action> followUpActions() {
        return followUpActions;
    }

    public boolean userTextAvailable() {
        return true;
    }

    public String userText() {
        return "Nothing happens.";
    }

    @Override
    public boolean equals( Object o ) {
        if( !(o instanceof UseWithSpecificItem) )
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}

