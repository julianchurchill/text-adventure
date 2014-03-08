package com.chewielouie.textadventure.action;

import java.util.List;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.Logger;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;

public class LoggableActionFactory implements ActionFactory {
    private Logger logger;
    private ActionFactory wrappedFactory;

    public LoggableActionFactory( Logger logger, ActionFactory f ) {
        this.logger = logger;
        this.wrappedFactory = f;
    }

    public void setFactoryForChildActionsToUse( ActionFactory f ) {
        wrappedFactory.setFactoryForChildActionsToUse( f );
    }

    public Action createShowInventoryAction( UserInventory inventory,
                                             TextAdventureModel model ) {
        Action action = wrappedFactory.createShowInventoryAction( inventory, model );
        return new LoggableAction( action, logger );
    }

    public Action createInventoryItemAction( Item item,
                                             UserInventory inventory,
                                             ModelLocation location ) {
        Action action = wrappedFactory.createInventoryItemAction( item, inventory, location );
        return new LoggableAction( action, logger );
    }

    public Action createExamineAction( Item item ) {
        Action action = wrappedFactory.createExamineAction( item );
        return new LoggableAction( action, logger );
    }

    public Action createUseWithAction( Item item,
                                       UserInventory inventory,
                                       ModelLocation location ) {
        Action action = wrappedFactory.createUseWithAction( item, inventory, location );
        return new LoggableAction( action, logger );
    }

    public Action createExamineAnItemAction( List<Item> items ) {
        Action action = wrappedFactory.createExamineAnItemAction( items );
        return new LoggableAction( action, logger );
    }

    public Action createTakeAnItemAction( List<Item> items,
                                          UserInventory inventory,
                                          ModelLocation location ) {
        Action action = wrappedFactory.createTakeAnItemAction( items, inventory, location );
        return new LoggableAction( action, logger );
    }

    public Action createTakeSpecificItemAction( Item item,
                                          UserInventory inventory,
                                          ModelLocation location ) {
        Action action = wrappedFactory.createTakeSpecificItemAction( item, inventory, location );
        return new LoggableAction( action, logger );
    }

    public Action createUseWithSpecificItemAction( Item actionOwner,
                                                   Item target ) {
        Action action = wrappedFactory.createUseWithSpecificItemAction( actionOwner, target );
        return new LoggableAction( action, logger );
    }

    public Action createExitAction( Exit exit, TextAdventureModel model ) {
        Action action = wrappedFactory.createExitAction( exit, model );
        return new LoggableAction( action, logger );
    }

    public Action createTalkToAction( Item item ) {
        Action action = wrappedFactory.createTalkToAction( item );
        return new LoggableAction( action, logger );
    }

    public Action createSayAction( String phraseId, Item item ) {
        Action action = wrappedFactory.createSayAction( phraseId, item );
        return new LoggableAction( action, logger );
    }
}
