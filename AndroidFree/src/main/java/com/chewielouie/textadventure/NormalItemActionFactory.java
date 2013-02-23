package com.chewielouie.textadventure;

public class NormalItemActionFactory implements ItemActionFactory {
    private String changeItemDescriptionTag = "change item description:";

    public ItemAction create( String content, Item item ) {
        if( content.startsWith( changeItemDescriptionTag ) )
            return new ChangeItemDescriptionItemAction(
                       content.substring( changeItemDescriptionTag.length() ),
                       item );
        return new NullItemAction();
    }
}


