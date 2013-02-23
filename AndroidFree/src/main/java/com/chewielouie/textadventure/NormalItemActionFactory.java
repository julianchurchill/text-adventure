package com.chewielouie.textadventure;

public class NormalItemActionFactory implements ItemActionFactory {
    private String changeItemDescriptionTag = "change item description:";
    private String changeItemNameTag = "change item name:";

    public ItemAction create( String content, Item item ) {
        if( content.startsWith( changeItemDescriptionTag ) )
            return new ChangeItemDescriptionItemAction(
                       content.substring( changeItemDescriptionTag.length() ),
                       item );
        else if( content.startsWith( changeItemNameTag ) )
            return new ChangeItemNameItemAction(
                       content.substring( changeItemNameTag.length() ),
                       item );
        return new NullItemAction();
    }
}


