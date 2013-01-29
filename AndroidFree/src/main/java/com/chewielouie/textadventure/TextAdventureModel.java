package com.chewielouie.textadventure;

import java.util.List;

public interface TextAdventureModel {
    public String currentLocationDescription();
    public List<String> currentLocationExits();
    public List<Exit> currentLocationExitsNew();
    public void moveThroughExit( String exitLabel );
}

