package com.chewielouie.textadventure;

import com.chewielouie.textadventure.serialisation.PlainTextExitDeserialiser;

public class LocationExit implements Exit {
    private String label = new String();
    private String destination = new String();
    private DirectionHint directionHint = DirectionHint.DontCare;
    private boolean visible = true;
    private String id = "";
    private PlainTextExitDeserialiser deserialiser =
        new PlainTextExitDeserialiser();

    public LocationExit() {
    }

    public LocationExit( String label ) {
        this.label = label;
    }

    public LocationExit( String label, String destinationLabel ) {
        this( label );
        this.destination = destinationLabel;
    }

    public LocationExit( String label, String destinationLabel, DirectionHint d ) {
        this( label, destinationLabel );
        this.directionHint = d;
    }

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

    public void deserialise( String content ) {
        deserialiser.deserialise( this, content );
    }
}

