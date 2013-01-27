package com.chewielouie.textadventure;

public class NullLocation implements ModelLocation {
    public void addExit( String exitLabel, String destinationId ) {
    }

    public boolean exitable( String exitLabel ) {
        return false;
    }

    public String exitDestinationFor( String exitLabel ) {
        return "";
    }

    public String id() {
        return "";
    }
}

