package com.chewielouie.textadventure;

import java.util.HashMap;
import java.util.Map;

public class Location {
    private String id;
    private Map<String,String> exits = new HashMap<String, String>();

    public Location( String locationId ) {
        this.id = locationId;
    }

    public void addExit( String exitLabel, String destinationId ) {
        exits.put( exitLabel, destinationId );
    }

    public boolean exitable( String exitLabel ) {
        return exits.containsKey( exitLabel );
    }

    public String exitDestinationFor( String exitLabel ) {
        return exits.get( exitLabel );
    }

    public String id() {
        return this.id;
    }
}

