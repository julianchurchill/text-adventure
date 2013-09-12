package com.chewielouie.textadventure.itemaction;

import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.TextAdventureModel;

public class NormalItemActionFactory implements ItemActionFactory {
    private final static String SEPERATOR = ":";

    private TextAdventureModel model;

    public NormalItemActionFactory( TextAdventureModel model ) {
        this.model = model;
    }

    public ItemAction create( String content, Item item ) {
        if( content.startsWith( changeItemDescriptionName() ) )
            return new ChangeItemDescriptionItemAction(
                       content.substring( changeItemDescriptionName().length() ),
                       item );
        else if( content.startsWith( changeItemNameName() ) )
            return new ChangeItemNameItemAction(
                       content.substring( changeItemNameName().length() ),
                       item );
        else if( content.startsWith( changeExitVisibilityName() ) )
            return new ChangeExitVisibilityItemAction(
                       content.substring( changeExitVisibilityName().length() ),
                       model );
        else if( content.startsWith( makeExitVisibleName() ) )
            return new MakeExitVisibleItemAction(
                       content.substring( makeExitVisibleName().length() ),
                       model );
        else if( content.startsWith( destroyItemName() ) )
            return new DestroyItemItemAction(
                       content.substring( destroyItemName().length() ),
                       model );
        else if( content.startsWith( changeItemVisibilityName() ) )
            return new ChangeItemVisibilityItemAction(
                       content.substring( changeItemVisibilityName().length() ),
                       model );
        else if( content.startsWith( incrementScoreName() ) )
            return new IncrementScoreItemAction( model );
        else if( content.startsWith( changeLocationDescriptionName() ) )
            return new ChangeLocationDescriptionItemAction(
                       content.substring( changeLocationDescriptionName().length() ),
                       model );
        else if( content.startsWith( takeItemName() ) )
            return new TakeItemItemAction(
                       content.substring( takeItemName().length() ), model );
        return new NullItemAction( content );
    }

    private String changeItemDescriptionName() {
        return new ChangeItemDescriptionItemAction( null, null ).name() + SEPERATOR;
    }

    private String changeItemNameName() {
        return new ChangeItemNameItemAction( null, null ).name() + SEPERATOR;
    }

    private String changeExitVisibilityName() {
        return new ChangeExitVisibilityItemAction( null, null ).name() + SEPERATOR;
    }

    private String makeExitVisibleName() {
        return new MakeExitVisibleItemAction( null, null ).name() + SEPERATOR;
    }

    private String destroyItemName() {
        return new DestroyItemItemAction( null, null ).name() + SEPERATOR;
    }

    private String changeItemVisibilityName() {
        return new ChangeItemVisibilityItemAction( null, null ).name() + SEPERATOR;
    }

    private String incrementScoreName() {
        return new IncrementScoreItemAction( null ).name() + SEPERATOR;
    }

    private String changeLocationDescriptionName() {
        return new ChangeLocationDescriptionItemAction( null, null ).name() + SEPERATOR;
    }

    private String takeItemName() {
        return new TakeItemItemAction( null, null ).name() + SEPERATOR;
    }
}


