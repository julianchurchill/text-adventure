package com.chewielouie.textadventure;

public class NormalItemActionFactory implements ItemActionFactory {
    public ItemAction create( String content ) {
        return new NullItemAction();
    }
}


