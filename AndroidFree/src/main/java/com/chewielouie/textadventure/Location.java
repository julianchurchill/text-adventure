package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Location implements ModelLocation {
    private String id;
    private String description;
    private Map<String,Exit> exits = new HashMap<String,Exit>();
    private List<Exit> exitsNew = new ArrayList<Exit>();

    public Location( String locationId, String description ) {
        this.id = locationId;
        this.description = description;
    }

    public void addExit( String exitLabel, String destinationId ) {
        Exit exit = new Exit( exitLabel, destinationId );
        exits.put( exitLabel, exit );
        exitsNew.add( exit );
    }

    public boolean exitable( String exitLabel ) {
        return exits.containsKey( exitLabel );
    }

    public String exitDestinationFor( String exitLabel ) {
        return exits.get( exitLabel ).destination();
    }

    public String id() {
        return this.id;
    }

    public List<Exit> exits() {
        return exitsNew;
    }

    public String description() {
        return description;
    }
}

