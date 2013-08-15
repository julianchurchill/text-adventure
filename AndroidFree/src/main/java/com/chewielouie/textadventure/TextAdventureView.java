package com.chewielouie.textadventure;

import java.util.List;
import com.chewielouie.textadventure.action.Action;

public interface TextAdventureView {
    public void showMainText( String s );
    public void showAvailableItemsText( String s );
    public void showLocationExits( List<Exit> exits );
    public void showAreaName( String s );
    public void setActions( List<Action> actions );
    public void currentScore( int score );
    public void maximumScore( int score );
}

