package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.Item;

public class TakeSpecificItem implements Action {
    private Item item;

    public TakeSpecificItem( Item item ) {
        this.item = item;
    }

    public String label() {
        return "Take " + item.name();
    }

    public void trigger() {
    }

    public boolean userMustChooseFollowUpAction() {
        return false;
    }

    public List<Action> followUpActions() {
        return new ArrayList<Action>();
    }

    public boolean userTextAvailable() {
        return false;
    }

    public String userText() {
        return "";
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
}

