package com.chewielouie.textadventure.action;

import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;
import com.chewielouie.textadventure.item.Item;
import java.util.List;

public class RecordableActionFactory implements ActionFactory {
    private ActionFactory wrappedFactory;
    private ActionHistory actionHistory;

    public RecordableActionFactory( ActionFactory factoryToWrap,
                                    ActionHistory actionHistory ) {
        this.wrappedFactory = factoryToWrap;
        this.actionHistory = actionHistory;
        this.wrappedFactory.setFactoryForChildActionsToUse( this );
    }

    public void setFactoryForChildActionsToUse( ActionFactory f ) {
        wrappedFactory.setFactoryForChildActionsToUse( f );
    }

    public Action createShowInventoryAction( UserInventory inventory,
                                             TextAdventureModel model ) {
        return new RecordableAction(
            wrappedFactory.createShowInventoryAction( inventory, model ),
            actionHistory );
    }

    public Action createInventoryItemAction( Item item,
                                             UserInventory inventory,
                                             ModelLocation location ) {
        RecordableAction action = new RecordableAction(
            wrappedFactory.createInventoryItemAction( item, inventory, location ),
            actionHistory );
        action.setActionParameters( new ActionParameters( item, location ) );
        return action;
    }

    public Action createExamineAction( Item item ) {
        RecordableAction action = new RecordableAction(
            wrappedFactory.createExamineAction( item ), actionHistory );
        action.setActionParameters( new ActionParameters( item ) );
        return action;
    }

    public Action createUseWithAction( Item item,
                                       UserInventory inventory,
                                       ModelLocation location ) {
        RecordableAction action = new RecordableAction(
            wrappedFactory.createUseWithAction( item, inventory, location ),
            actionHistory );
        action.setActionParameters( new ActionParameters( item, location ) );
        return action;
    }

    public Action createExamineAnItemAction( List<Item> items ) {
        return new RecordableAction(
            wrappedFactory.createExamineAnItemAction( items ),
            actionHistory );
    }

    public Action createTakeAnItemAction( List<Item> items,
                                          UserInventory inventory,
                                          ModelLocation location ) {
        RecordableAction action = new RecordableAction(
            wrappedFactory.createTakeAnItemAction( items, inventory, location ),
            actionHistory );
        action.setActionParameters( new ActionParameters( location ) );
        return action;
    }

    public Action createTakeSpecificItemAction( Item item,
                                          UserInventory inventory,
                                          ModelLocation location ) {
        RecordableAction action = new RecordableAction(
            wrappedFactory.createTakeSpecificItemAction( item, inventory, location ),
            actionHistory );
        action.setActionParameters( new ActionParameters( item, location ) );
        return action;
    }

    public Action createUseWithSpecificItemAction( Item actionOwner,
                                                   Item target ) {
        RecordableAction action = new RecordableAction(
            wrappedFactory.createUseWithSpecificItemAction( actionOwner, target ),
            actionHistory );
        action.setActionParameters( new ActionParameters( actionOwner, target ) );
        return action;
    }

    public Action createExitAction( Exit exit, TextAdventureModel model ) {
        RecordableAction action = new RecordableAction(
            wrappedFactory.createExitAction( exit, model ), actionHistory );
        action.setActionParameters( new ActionParameters( exit ) );
        return action;
    }

    public Action createTalkToAction( Item item ) {
        RecordableAction action = new RecordableAction(
            wrappedFactory.createTalkToAction( item ), actionHistory );
        action.setActionParameters( new ActionParameters( item ) );
        return action;
    }

    public Action createSayAction( String phraseId, Item item ) {
        RecordableAction action = new RecordableAction(
            wrappedFactory.createSayAction( phraseId, item ), actionHistory );
        action.setActionParameters( new ActionParameters( phraseId, item ) );
        return action;
    }
}
