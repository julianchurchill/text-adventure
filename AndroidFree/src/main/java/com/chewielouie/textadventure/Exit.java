package com.chewielouie.textadventure;

public class Exit {
    private String label = new String();
    private String destination = new String();
    private DirectionHint directionHint = DirectionHint.DontCare;

    public enum DirectionHint { DontCare, North };

    public Exit( String label ) {
        this.label = label;
    }

    public Exit( String label, String destinationLabel ) {
        this( label );
        this.destination = destinationLabel;
    }

    public Exit( String label, String destinationLabel, DirectionHint d ) {
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

    @Override
    public boolean equals( Object o ) {
        if( !(o instanceof Exit) )
            return false;
        Exit other = (Exit)o;
        return label == other.label &&
               destination == other.destination;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + label.hashCode();
        result = prime * result + destination.hashCode();
        return result;
    }
}

