package com.chewielouie.textadventure;

public class LocationExit implements Exit {
    private String label = new String();
    private String destination = new String();
    private DirectionHint directionHint = DirectionHint.DontCare;
    private boolean visible = true;

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

    public String destination() {
        return this.destination;
    }

    public DirectionHint directionHint() {
        return this.directionHint;
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
}

