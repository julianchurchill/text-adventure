package com.chewielouie.textadventure;

import java.util.List;
import com.chewielouie.textadventure.action.Action;

public interface TextAdventureView {
    public void showLocationDescription( String s );
    public void showLocationExits( List<Exit> exits );
    public void setActions( List<Action> actions );
    public void giveUserImmediateActionChoice( List<Action> actions );
}

