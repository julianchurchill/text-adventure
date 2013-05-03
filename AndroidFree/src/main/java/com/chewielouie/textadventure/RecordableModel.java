package com.chewielouie.textadventure;

import java.util.Collection;
import java.util.List;
import com.chewielouie.textadventure.ActionHistory;
import com.chewielouie.textadventure.item.Item;

public class RecordableModel implements TextAdventureModel {
    private TextAdventureModel modelToRecord;
    private ActionHistory actionHistory;

    public RecordableModel( TextAdventureModel modelToRecord, ActionHistory history ) {
        this.modelToRecord = modelToRecord;
        this.actionHistory = history;
    }

    public TextAdventureModel model() {
        return modelToRecord;
    }

    public ActionHistory actionHistory() {
        return actionHistory;
    }

    public String currentLocationDescription() {
        return "";
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
}
