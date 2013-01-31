package com.chewielouie.textadventure.action;

public class ShowInventory implements Action {
    private String label = "Show inventory";

    public String label() {
        return label;
    }

    @Override
    public boolean equals( Object o ) {
        if( !(o instanceof ShowInventory) )
            return false;
        ShowInventory other = (ShowInventory)o;
        return label == other.label;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + label.hashCode();
        return result;
    }
}

