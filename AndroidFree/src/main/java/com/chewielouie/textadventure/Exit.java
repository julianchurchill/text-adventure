package com.chewielouie.textadventure;

public class Exit {
    private String label = new String();
    private String destination = new String();

    public Exit( String label ) {
        this.label = label;
    }

    public Exit( String label, String destinationLabel ) {
        this.label = label;
        this.destination = destinationLabel;
    }

    public String label() {
        return this.label;
    }

    public String destination() {
        return this.destination;
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

