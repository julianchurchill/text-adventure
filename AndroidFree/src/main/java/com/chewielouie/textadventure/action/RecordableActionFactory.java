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
        return new RecordableAction(
            wrappedFactory.createShowInventoryAction( inventory, model ) );
    }

    public Action createInventoryItemAction( Item item,
                                             UserInventory inventory,
                                             ModelLocation location ) {
        return new RecordableAction(
            wrappedFactory.createInventoryItemAction( item, inventory, location ) );
    }

    public Action createExamineAction( Item item ) {
        return new RecordableAction( wrappedFactory.createExamineAction( item ) );
    }

    public Action createUseWithAction( Item item,
                                       UserInventory inventory,
                                       ModelLocation location ) {
        return new RecordableAction(
            wrappedFactory.createUseWithAction( item, inventory, location ) );
    }

    public Action createExamineAnItemAction( List<Item> items ) {
        return new RecordableAction(
            wrappedFactory.createExamineAnItemAction( items ) );
    }

    public Action createTakeAnItemAction( List<Item> items,
                                          UserInventory inventory,
                                          ModelLocation location ) {
        return new RecordableAction(
            wrappedFactory.createTakeAnItemAction( items, inventory, location ) );
    }

    public Action createTakeSpecificItemAction( Item item,
                                          UserInventory inventory,
                                          ModelLocation location ) {
        return new RecordableAction(
            wrappedFactory.createTakeSpecificItemAction( item, inventory, location ) );
    }

    public Action createUseWithSpecificItemAction( Item actionOwner,
                                                   Item target ) {
        return new RecordableAction(
            wrappedFactory.createUseWithSpecificItemAction( actionOwner, target ) );
    }

    public Action createExitAction( Exit exit, TextAdventureModel model ) {
        return new RecordableAction( wrappedFactory.createExitAction( exit, model ) );
    }
}
