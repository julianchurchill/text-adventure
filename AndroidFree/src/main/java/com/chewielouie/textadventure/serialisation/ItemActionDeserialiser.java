package com.chewielouie.textadventure.serialisation;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.DeserialiserUtils;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.itemaction.ItemActionFactory;
import com.chewielouie.textadventure.itemaction.ItemAction;

public class ItemActionDeserialiser {
    private String content;
    private String triggerEventTag;
    private Item item;
    private ItemActionFactory itemActionFactory;

    public ItemActionDeserialiser( String content,
            String triggerEventTag, Item item, ItemActionFactory factory ) {
        this.content = content;
        this.triggerEventTag = triggerEventTag;
        this.item = item;
        this.itemActionFactory = factory;
    }

    public List<ItemAction> extract() {
        List<ItemAction> actions = new ArrayList<ItemAction>();
        if( itemActionFactory != null ) {
            int nextTag = DeserialiserUtils.NOT_FOUND;
            while( (nextTag=DeserialiserUtils.findTagFrom( nextTag, triggerEventTag, content )) != DeserialiserUtils.NOT_FOUND )
                actions.add(
                    createAction( nextTag ) );
        }
        return actions;
    }

    private ItemAction createAction( int startOfAction ) {
        return itemActionFactory.create(
                    DeserialiserUtils.extractValueUpToNewline( startOfAction +
                                             triggerEventTag.length(), content ),
                    item );
    }
}
