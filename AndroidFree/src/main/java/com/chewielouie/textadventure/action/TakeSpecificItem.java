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
        return "";
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

    //@Override
    //public boolean equals( Object o ) {
        //if( !(o instanceof TakeSpecificItem) )
            //return false;
        //return true;
    //}

    //@Override
    //public int hashCode() {
        //return 1;
    //}

    public Item item() {
        return item;
    }
}

