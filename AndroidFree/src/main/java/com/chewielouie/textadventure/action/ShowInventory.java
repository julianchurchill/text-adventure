package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;

public class ShowInventory implements Action {
    public String label() {
        return "Show inventory";
    }

    public void trigger() {
    }

    public boolean userMustChooseFollowUpAction() {
        return false;
    }

    public List<Action> followUpActions() {
        return new ArrayList<Action>();
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

