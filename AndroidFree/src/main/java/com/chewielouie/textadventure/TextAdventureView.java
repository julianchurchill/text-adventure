package com.chewielouie.textadventure;

import java.util.List;

public interface TextAdventureView {
    public void showLocationDescription( String s );
    public void showLocationExits( List<String> exits );
    public void showLocationExitsNew( List<Exit> exits );
}

