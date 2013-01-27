package com.chewielouie.textadventure;

public interface ModelLocation {
    public void addExit( String exitLabel, String destinationId );
    public boolean exitable( String exitLabel );
    public String exitDestinationFor( String exitLabel );
    public String id();
}


