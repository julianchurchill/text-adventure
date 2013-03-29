package com.chewielouie.textadventure.itemaction;

import com.chewielouie.textadventure.Logger;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.item.Item;

public class LoggableNormalItemActionFactory implements ItemActionFactory {
    private String changeItemDescriptionTag = "change item description:";
    private String changeItemNameTag = "change item name:";
    private String makeExitVisibleTag = "make exit visible:";
    private String destroyItemTag = "destroy item:";
    private Logger logger;
    private TextAdventureModel model;

    public LoggableNormalItemActionFactory( Logger logger, TextAdventureModel model ) {
        this.logger = logger;
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
        return new LoggableNullItemAction( logger, content );
    }
}



