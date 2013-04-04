package com.chewielouie.textadventure.itemaction;

import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.TextAdventureModel;

public class NormalItemActionFactory implements ItemActionFactory {
    private String changeItemDescriptionTag = "change item description:";
    private String changeItemNameTag = "change item name:";
    private String makeExitVisibleTag = "make exit visible:";
    private String destroyItemTag = "destroy item:";
    private String changeItemVisibilityTag = "change item visibility:";
    private String incrementScoreTag = "increment score:";
    private String changeLocationDescriptionTag = "change location description:";
    private TextAdventureModel model;

    public NormalItemActionFactory( TextAdventureModel model ) {
        this.model = model;
    }

    public ItemAction create( String content, Item item ) {
        if( content.startsWith( changeItemDescriptionTag ) )
            return new ChangeItemDescriptionItemAction(
                       content.substring( changeItemDescriptionTag.length() ),
                       item );
        else if( content.startsWith( changeItemNameTag ) )
            return new ChangeItemNameItemAction(
                       content.substring( changeItemNameTag.length() ),
                       item );
        else if( content.startsWith( makeExitVisibleTag ) )
            return new MakeExitVisibleItemAction(
                       content.substring( makeExitVisibleTag.length() ),
                       item,
                       model );
        else if( content.startsWith( destroyItemTag ) )
            return new DestroyItemItemAction(
                       content.substring( destroyItemTag.length() ),
                       model );
        else if( content.startsWith( changeItemVisibilityTag ) )
            return new ChangeItemVisibilityItemAction(
                       content.substring( changeItemVisibilityTag.length() ),
                       model );
        else if( content.startsWith( incrementScoreTag ) )
            return new IncrementScoreItemAction( model );
        else if( content.startsWith( changeLocationDescriptionTag ) )
            return new ChangeLocationDescriptionItemAction(
                       content.substring( changeLocationDescriptionTag.length() ),
                       model );
        return new NullItemAction( content );
    }
}


