package com.chewielouie.textadventure;

import com.chewielouie.textadventure.action.Action;

public interface UserActionHandler {
    public void enact( Action action );
    public boolean inAnActionChain();
    public void cancelActionChain();
}

