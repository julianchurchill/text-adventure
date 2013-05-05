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
    }

    public Action createShowInventoryAction( UserInventory inventory,
                                             TextAdventureModel model ) {
        return wrappedFactory.createShowInventoryAction( inventory, model );
    }

    public Action createInventoryItemAction( Item item,
                                             UserInventory inventory,
                                             ModelLocation location ) {
        // return new InventoryItem( item, inventory, location, this );
        return null;
    }

    public Action createExamineAction( Item item ) {
        // return new Examine( item );
        return null;
    }

    public Action createUseWithAction( Item item,
                                       UserInventory inventory,
                                       ModelLocation location ) {
        // return new UseWith( item, inventory, location, this );
        return null;
    }

    public Action createExamineAnItemAction( List<Item> items ) {
        // return new ExamineAnItem( items, this );
        return null;
    }

    public Action createTakeAnItemAction( List<Item> items,
                                          UserInventory inventory,
                                          ModelLocation location ) {
        // return new TakeAnItem( items, inventory, location, this );
        return null;
    }

    public Action createTakeSpecificItemAction( Item item,
                                          UserInventory inventory,
                                          ModelLocation location ) {
        // return new TakeSpecificItem( item, inventory, location );
        return null;
    }

    public Action createUseWithSpecificItemAction( Item actionOwner,
                                                   Item target ) {
        // return new UseWithSpecificItem( actionOwner, target );
        return null;
    }

    public Action createExitAction( Exit exit, TextAdventureModel model ) {
        // return new ExitAction( exit, model );
        return null;
    }
}
