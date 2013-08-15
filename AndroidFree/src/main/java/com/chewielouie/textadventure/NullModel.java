package com.chewielouie.textadventure;

import java.util.Collection;
import java.util.List;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.ModelEventSubscriber;

public class NullModel implements TextAdventureModel {
    public String currentLocationDescription() {
        return "";
    }

    public String availableItemsText() {
        return "";
    }

    public void addLocationArea( String id, String name ) {
    }

    public String currentLocationAreaName() {
        return "";
    }

    public void setCurrentLocation( String id ) {
    }

    public void addLocation( ModelLocation location ) {
    }

    public List<Exit> currentLocationExits() {
        return null;
    }

    public void moveThroughExit( Exit exit ) {
    }

    public List<Item> inventoryItems() {
        return null;
    }

    public ModelLocation currentLocation() {
        return null;
    }

    public Collection<ModelLocation> locations() {
        return null;
    }

    public Exit findExitByID( String id ) {
        return null;
    }

    public void destroyItem( String id ) {
    }

    public Item findItemByID( String id ) {
        return null;
    }

    public int currentScore() {
        return 0;
    }

    public int maximumScore() {
        return 0;
    }

    public void setCurrentScore( int score ) {
    }

    public void setMaximumScore( int score ) {
    }

    public void subscribeForEvents( ModelEventSubscriber subscriber ) {
    }

    public ModelLocation findLocationByID( String id ) {
        return null;
    }

}
