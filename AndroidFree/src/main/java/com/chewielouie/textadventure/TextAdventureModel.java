package com.chewielouie.textadventure;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.MovementEventSubscriber;

public interface TextAdventureModel {
    public String contextualText();
    public String currentLocationDescription();
    public String availableItemsText();
    public void addLocationArea( String id, String name );
    public String currentLocationAreaName();
    public Set<String> areaIDs();
    public void setCurrentLocation( String id );
    public void addLocation( ModelLocation location );
    public List<Exit> currentLocationExits();
    public void moveThroughExit( Exit exit );
    public List<Item> inventoryItems();
    public ModelLocation currentLocation();
    public Collection<ModelLocation> locations();
    public Exit findExitByID( String id );
    public void destroyItem( String id );
    public Item findItemByID( String id );
    public int currentScore();
    public int maximumScore();
    public void setCurrentScore( int score );
    public void setMaximumScore( int score );
    public void subscribeForEvents( MovementEventSubscriber subscriber );
    public ModelLocation findLocationByID( String id );
    public void moveItemToInventory( String itemID );
}

