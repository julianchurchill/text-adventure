package com.chewielouie.textadventure;

import com.chewielouie.textadventure.action.Action;

public interface UserActionHandler {
    public void moveThroughExit( Exit exit );
    public void enact( Action action );
}

