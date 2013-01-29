package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.List;

public class Location implements ModelLocation {
    private String id;
    private String description;
    private List<Exit> exits = new ArrayList<Exit>();

    public Location( String locationId, String description ) {
        this.id = locationId;
        this.description = description;
    }

    public void addExit( String exitLabel, String destinationId ) {
        exits.add( new Exit( exitLabel, destinationId ) );
    }

    public boolean exitable( Exit exit ) {
        for( Exit e : exits )
            if( e == exit )
                return true;
        return false;
    }

    public boolean exitable( String exitLabel ) {
        for( Exit e : exits )
            if( e.label() == exitLabel )
                return true;
        return false;
    }

    public String exitDestinationFor( String exitLabel ) {
        for( Exit e : exits )
            if( e.label() == exitLabel )
                return e.destination();
        return "";
    }

    public String id() {
        return this.id;
    }

    public List<Exit> exits() {
        return exits;
    }

    public String description() {
        return description;
    }
}

