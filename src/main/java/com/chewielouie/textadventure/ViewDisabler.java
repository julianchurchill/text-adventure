package com.chewielouie.textadventure;

import java.util.List;
import com.chewielouie.textadventure.action.Action;

public class ViewDisabler implements TextAdventureView {
    private TextAdventureView originalView;
    private boolean disabled = false;

    public ViewDisabler( TextAdventureView view ) {
        this.originalView = view;
    }

    public void on() {
        disabled = true;
    }

    public void off() {
        disabled = false;
    }

    public void showMainText( String s ) {
        if( disabled == false )
            originalView.showMainText( s );
    }

    public void showLocationExits( List<Exit> exits ) {
        if( disabled == false )
            originalView.showLocationExits( exits );
    }

    public void setActions( List<Action> actions ) {
        if( disabled == false )
            originalView.setActions( actions );
    }

    public void currentScore( int score ) {
        if( disabled == false )
            originalView.currentScore( score );
    }

    public void maximumScore( int score ) {
        if( disabled == false )
            originalView.maximumScore( score );
    }
}

