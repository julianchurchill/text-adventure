package com.chewielouie.textadventure.action;

import java.util.List;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.ActionHistory;
import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;

public class RecordableActionFactory implements ActionFactory {
    private ActionFactory wrappedFactory;

    public RecordableActionFactory( ActionFactory factoryToWrap,
                                    ActionHistory actionHistory ) {
        this.wrappedFactory = factoryToWrap;
        this.wrappedFactory.setFactoryForChildActionsToUse( this );
    }

    public void setFactoryForChildActionsToUse( ActionFactory f ) {
        wrappedFactory.setFactoryForChildActionsToUse( f );
    }

    public Action createShowInventoryAction( UserInventory inventory,
                                             TextAdventureModel model ) {
        return wrappedFactory.createShowInventoryAction( inventory, model );
    }

    public Action createInventoryItemAction( Item item,
                                             UserInventory inventory,
                                             ModelLocation location ) {
        return wrappedFactory.createInventoryItemAction( item, inventory, location );
    }

    public Action createExamineAction( Item item ) {
        return wrappedFactory.createExamineAction( item );
    }

    public Action createUseWithAction( Item item,
                                       UserInventory inventory,
                                       ModelLocation location ) {
        return wrappedFactory.createUseWithAction( item, inventory, location );
    }

    public Action createExamineAnItemAction( List<Item> items ) {
        return wrappedFactory.createExamineAnItemAction( items );
    }

    public Action createTakeAnItemAction( List<Item> items,
                                          UserInventory inventory,
                                          ModelLocation location ) {
        return wrappedFactory.createTakeAnItemAction( items, inventory, location );
    }

    public Action createTakeSpecificItemAction( Item item,
                                          UserInventory inventory,
                                          ModelLocation location ) {
        return wrappedFactory.createTakeSpecificItemAction( item, inventory, location );
    }

    public Action createUseWithSpecificItemAction( Item actionOwner,
                                                   Item target ) {
        return wrappedFactory.createUseWithSpecificItemAction( actionOwner, target );
    }

    public Action createExitAction( Exit exit, TextAdventureModel model ) {
        return wrappedFactory.createExitAction( exit, model );
    }
}
