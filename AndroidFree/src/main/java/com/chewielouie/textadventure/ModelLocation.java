package com.chewielouie.textadventure;

import java.util.List;

public interface ModelLocation {
    public void addExit( String exitLabel, String destinationId );
    public boolean exitable( String exitLabel );
    public String exitDestinationFor( String exitLabel );
    public String id();
    public List<String> exits();
    public String description();
}


