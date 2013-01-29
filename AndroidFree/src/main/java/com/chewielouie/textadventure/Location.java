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

    public void addExit( Exit exit ) {
        exits.add( exit );
    }

    public boolean exitable( Exit exit ) {
        for( Exit e : exits )
            if( e.equals( exit ) )
                return true;
        return false;
    }

    public String exitDestinationFor( Exit exit ) {
        return exit.destination();
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

