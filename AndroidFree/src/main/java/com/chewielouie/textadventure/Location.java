package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Location implements ModelLocation {
    private String id;
    private String description;
    private Map<String,String> exits = new HashMap<String, String>();
    private List<String> exitLabels = new ArrayList<String>();

    public Location( String locationId, String description ) {
        this.id = locationId;
        this.description = description;
    }

    public void addExit( String exitLabel, String destinationId ) {
        exits.put( exitLabel, destinationId );
        exitLabels.add( exitLabel );
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

    public List<String> exits() {
        return exitLabels;
    }

    public String description() {
        return description;
    }
}

