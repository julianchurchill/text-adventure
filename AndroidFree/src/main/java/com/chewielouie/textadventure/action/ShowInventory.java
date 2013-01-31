package com.chewielouie.textadventure.action;

public class ShowInventory implements Action {
    public String label() {
        return "Show inventory";
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

