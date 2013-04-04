package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.chewielouie.textadventure.item.Item;

public class BasicModel implements TextAdventureModel, UserInventory {
    Map<String,ModelLocation> locations = new HashMap<String,ModelLocation>();
    ModelLocation currentLocation = new NullLocation();
    private List<Item> inventoryItems = new ArrayList<Item>();

    public BasicModel() {
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
        return currentLocation().visibleExits();
    }

    public List<Item> inventoryItems() {
        return inventoryItems;
    }

    public void addToInventory( Item item ) {
        inventoryItems.add( item );
    }

    public Collection<ModelLocation> locations() {
        return locations.values();
    }

    public Exit findExitByID( String id ) {
        for( ModelLocation location : locations.values() )
            for( Exit exit : location.exitsIncludingInvisibleOnes() )
                if( exit.id().equals( id ) )
                    return exit;
        return null;
    }

    public void destroyItem( String id ) {
        if( removeItemFromInventory( id ) == false )
            if( removeItemFromCurrentLocation( id ) == false )
                removeItemFromAnyLocation( id );
    }

    private boolean removeItemFromInventory( String id ) {
        Item item = findItemInInventory( id );
        if( item != null ) {
            inventoryItems.remove( item );
            return true;
        }
        return false;
    }

    private boolean removeItemFromCurrentLocation( String id ) {
        return removeItemFromLocation( id, currentLocation );
    }

    private boolean removeItemFromAnyLocation( String id ) {
        for( ModelLocation loc : locations.values() )
            if( removeItemFromLocation( id, loc ) )
                return true;
        return false;
    }

    private boolean removeItemFromLocation( String id, ModelLocation loc ) {
        for( Item item : loc.items() ) {
            if( item.id().equals( id ) ) {
                loc.removeItem( item );
                return true;
            }
        }
        return false;
    }

    public Item findItemByID( String id ) {
        Item item = findItemInInventory( id );
        if( item != null )
            return item;
        return findItemInAnyLocation( id );
    }

    private Item findItemInInventory( String id ) {
        for( Item item : inventoryItems )
            if( item.id().equals( id ) )
                return item;
        return null;
    }

    private Item findItemInAnyLocation( String id ) {
        for( ModelLocation loc : locations.values() )
            for( Item item : loc.items() )
                if( item.id().equals( id ) )
                    return item;
        return null;
    }

    public int currentScore() {
        return 0;
    }

    public int maximumScore() {
        return 1;
    }
}

