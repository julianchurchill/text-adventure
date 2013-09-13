package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.itemaction.ItemAction;

public class LocationExit implements Exit {
    private String label = new String();
    private String destination = new String();
    private DirectionHint directionHint = DirectionHint.DontCare;
    private boolean visible = true;
    private String id = "";
    private List<ItemAction> onUseActions = new ArrayList<ItemAction>();

    public String label() {
        return this.label;
    }

    public void setLabel( String label ) {
        this.label = label;
    }

    public String destination() {
        return this.destination;
    }

    public void setDestination( String destination ) {
        this.destination = destination;
    }

    public DirectionHint directionHint() {
        return this.directionHint;
    }

    public void setDirectionHint( DirectionHint d ) {
        this.directionHint = d;
    }

    public boolean visible() {
        return visible;
    }

    public void setInvisible() {
        visible = false;
    }

    public void setVisible() {
        visible = true;
    }

    public String id() {
        return id;
    }

    public void setID( String id ) {
        this.id = id;
    }

    @Override
    public boolean equals( Object o ) {
        if( !(o instanceof LocationExit) )
            return false;
        LocationExit other = (LocationExit)o;
        return label == other.label &&
               destination == other.destination &&
               directionHint == other.directionHint;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + label.hashCode();
        result = prime * result + destination.hashCode();
        result = prime * result + directionHint.hashCode();
        return result;
    }

    public void addOnUseAction( ItemAction action ) {
        onUseActions.add( action );
    }

    public void use() {
        for( ItemAction action : onUseActions )
            action.enact();
    }
}

