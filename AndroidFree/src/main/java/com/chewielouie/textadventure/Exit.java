package com.chewielouie.textadventure;

import com.chewielouie.textadventure.itemaction.ItemAction;

public interface Exit {
    public enum DirectionHint { DontCare, North, South, East, West };
    public String label();
    public void setLabel( String label );
    public String destination();
    public void setDestination( String destination );
    public DirectionHint directionHint();
    public void setDirectionHint( DirectionHint d );
    public boolean visible();
    public void setVisible();
    public void setInvisible();
    public String id();
    public void setID( String id );
    public void addOnUseAction( ItemAction action );
    public void use();
}

