package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicModel implements TextAdventureModel {
    Map<String,ModelLocation> locations = new HashMap<String,ModelLocation>();
    ModelLocation currentLocation = new NullLocation();
    private List<Item> inventoryItems = new ArrayList<Item>();

    public BasicModel() {
        inventoryItems.add( new NormalItem( "Pocket lint",
            "It's fluffy and shaped like an inverted belly button" ) );
    }

    public String currentLocationDescription() {
        return currentLocation().description();
    }

    public void addLocation( ModelLocation location ) {
        if( currentLocation instanceof NullLocation )
            currentLocation = location;
        locations.put( location.id(), location );
    }

    public void moveThroughExit( Exit exit ) {
        if( currentLocation.exitable( exit ) )
            currentLocation = locations.get(
                    currentLocation.exitDestinationFor( exit ) );
    }

    public ModelLocation currentLocation() {
        return currentLocation;
    }

    public List<Exit> currentLocationExits() {
        return currentLocation().exits();
    }

    public List<Item> inventoryItems() {
        return inventoryItems;
    }
}


