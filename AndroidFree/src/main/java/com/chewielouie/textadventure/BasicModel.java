package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.ModelEventSubscriber;

public class BasicModel implements TextAdventureModel, UserInventory {
    Map<String,ModelLocation> locations = new HashMap<String,ModelLocation>();
    Map<String,Exit> exits = new HashMap<String,Exit>();
    Map<String,Item> items = new HashMap<String,Item>();
    ModelLocation currentLocation = new NullLocation();
    private List<Item> inventoryItems = new ArrayList<Item>();
    private int currentScore = 0;
    private int maximumScore = 7;
    private List<ModelEventSubscriber> eventSubscribers =
                                    new ArrayList<ModelEventSubscriber>();

    public BasicModel() {
    }

    public String currentLocationDescription() {
        return currentLocation().description();
    }

    public String availableItemsText() {
        return currentLocation().availableItemsText();
    }

    public void addLocationArea( String id, String name ) {
    }

    public void setCurrentLocation( String id ) {
        currentLocation = locations.get( id );
        for( ModelEventSubscriber s : eventSubscribers )
            s.currentLocationChanged();
    }

    public void addLocation( ModelLocation location ) {
        if( currentLocation instanceof NullLocation )
            currentLocation = location;
        locations.put( location.id(), location );
        addExitsToCache( location );
        addItemsToCache( location );
    }

    private void addItemsToCache( ModelLocation location ) {
        for( Item item : location.items() )
            items.put( item.id(), item );
    }

    private void addExitsToCache( ModelLocation location ) {
        for( Exit exit : location.exitsIncludingInvisibleOnes() )
            exits.put( exit.id(), exit );
    }

    public void moveThroughExit( Exit exit ) {
        if( currentLocation.exitable( exit ) ) {
            currentLocation = locations.get(
                    currentLocation.exitDestinationFor( exit ) );
            for( ModelEventSubscriber s : eventSubscribers )
                s.currentLocationChanged();
        }
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
        items.put( item.id(), item );
    }

    public Collection<ModelLocation> locations() {
        return locations.values();
    }

    public Exit findExitByID( String id ) {
        return exits.get( id );
    }

    public void destroyItem( String id ) {
        items.remove( id );
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
        if( items.containsKey( id ) )
            return items.get( id );
        // Acceptance tests fail if we don't check the inventory here, not sure why...
        Item item = findItemInInventory( id );
        if( item != null ) {
            items.put( id, item );
            return item;
        }
        // If item has been added to the location _after_ it was added to the model
        // we won't have cached it yet so look again...
        item = findItemInAnyLocation( id );
        if( item != null )
            items.put( id, item );
        return item;
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
        return currentScore;
    }

    public int maximumScore() {
        return maximumScore;
    }

    public void setCurrentScore( int score ) {
        currentScore = score;
    }

    public void setMaximumScore( int score ) {
        maximumScore = score;
    }

    public void subscribeForEvents( ModelEventSubscriber subscriber ) {
        eventSubscribers.add( subscriber );
    }

    public ModelLocation findLocationByID( String id ) {
        return locations.get( id );
    }
}

