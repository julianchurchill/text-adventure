package com.chewielouie.textadventure;

import java.util.HashMap;
import java.util.Map;

public class BasicModel implements TextAdventureModel {
    Map<String,ModelLocation> locations = new HashMap<String,ModelLocation>();
    ModelLocation currentLocation = new NullLocation();

    public String currentRoomText() {
        return "Some basic room text, please add more!";
    }

    public void addLocation( ModelLocation location ) {
        if( currentLocation instanceof NullLocation )
            currentLocation = location;
        locations.put( location.id(), location );
    }

    public void moveThroughExit( String exitLabel ) {
        if( currentLocation.exitable( exitLabel ) )
            currentLocation = locations.get(
                    currentLocation.exitDestinationFor( exitLabel ) );
    }

    public ModelLocation currentLocation() {
        return currentLocation;
    }
}


