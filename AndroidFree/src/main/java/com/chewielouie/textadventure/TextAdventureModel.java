package com.chewielouie.textadventure;

import java.util.Collection;
import java.util.List;
import com.chewielouie.textadventure.item.Item;

public interface TextAdventureModel {
    public String currentLocationDescription();
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
}

