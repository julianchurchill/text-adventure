package com.chewielouie.textadventure.itemaction;

import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.ItemDecorator;
import com.chewielouie.textadventure.item.NullItemDecorator;
import com.chewielouie.textadventure.ModelDecorator;
import com.chewielouie.textadventure.NullModelDecorator;
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
    private ItemDecorator itemDecorator = new NullItemDecorator();
    private ModelDecorator modelDecorator = new NullModelDecorator();

    public NormalItemActionFactory( TextAdventureModel model ) {
        this.model = model;
    }

    public void setItemDecorator( ItemDecorator d ) {
        this.itemDecorator = d;
    }

    public void setModelDecorator( ModelDecorator d ) {
        this.modelDecorator = d;
    }

    public ItemAction create( String content, Item item ) {
        if( content.startsWith( changeItemDescriptionTag ) )
            return new ChangeItemDescriptionItemAction(
                       content.substring( changeItemDescriptionTag.length() ),
                       itemDecorator.decorate( item ) );
        else if( content.startsWith( changeItemNameTag ) )
            return new ChangeItemNameItemAction(
                       content.substring( changeItemNameTag.length() ),
                       itemDecorator.decorate( item ) );
        else if( content.startsWith( makeExitVisibleTag ) )
            return new MakeExitVisibleItemAction(
                       content.substring( makeExitVisibleTag.length() ),
                       modelDecorator.decorate( model ) );
        else if( content.startsWith( destroyItemTag ) )
            return new DestroyItemItemAction(
                       content.substring( destroyItemTag.length() ),
                       modelDecorator.decorate( model ) );
        else if( content.startsWith( changeItemVisibilityTag ) )
            return new ChangeItemVisibilityItemAction(
                       content.substring( changeItemVisibilityTag.length() ),
                       modelDecorator.decorate( model ) );
        else if( content.startsWith( incrementScoreTag ) )
            return new IncrementScoreItemAction( modelDecorator.decorate( model ) );
        else if( content.startsWith( changeLocationDescriptionTag ) )
            return new ChangeLocationDescriptionItemAction(
                       content.substring( changeLocationDescriptionTag.length() ),
                       modelDecorator.decorate( model ) );
        return new NullItemAction( content );
    }
}


