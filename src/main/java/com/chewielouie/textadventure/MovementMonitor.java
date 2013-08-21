package com.chewielouie.textadventure;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MovementMonitor implements MovementEventSubscriber {
    private static final Map<String, Integer> areaIDsToMaskIDs;
    static {
        Map<String, Integer> aMap = new HashMap<String, Integer>();
        aMap.put( "church-area", R.drawable.church_mask );
        aMap.put( "mine-area", R.drawable.mine_mask );
        aMap.put( "town-area", R.drawable.town_mask );
        aMap.put( "friary-area", R.drawable.friary_mask );
        areaIDsToMaskIDs = Collections.unmodifiableMap( aMap );
    }

    private Set<String> exploredAreas = new HashSet<String>();
    private TextAdventureModel model = null;

    public MovementMonitor( TextAdventureModel model ) {
        this.model = model;
        exploredAreas.add( "church-area" );
        exploredAreas.add( "mine-area" );
    }

    public void currentLocationChanged() {
        exploredAreas.add( model.currentLocation().areaID() );
    }

    public int[] findExploredAreaMaskIDs() {
        int[] exploredMasks = new int[exploredAreas.size()];
        int i = 0;
        for( String areaID : exploredAreas )
            exploredMasks[i++] = areaIDsToMaskIDs.get( areaID );
        return exploredMasks;
    }
}
