package com.chewielouie.textadventure;

public class NormalItemActionFactory implements ItemActionFactory {
    public ItemAction create( String content ) {
        if( content.startsWith( "change item description:" ) )
            return new ChangeItemDescriptionItemAction();
        return new NullItemAction();
    }
}


