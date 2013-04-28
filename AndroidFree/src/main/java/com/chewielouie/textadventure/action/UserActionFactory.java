package com.chewielouie.textadventure.action;

import java.util.List;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;

public class UserActionFactory implements ActionFactory {
    public Action createShowInventoryAction( UserInventory inventory,
                                             TextAdventureModel model ) {
        return new ShowInventory( inventory, model, this );
    }

    public Action createInventoryItemAction( Item item,
                                             UserInventory inventory,
                                             ModelLocation location ) {
        return new InventoryItem( item, inventory, location, this );
    }

    public Action createExamineAction( Item item ) {
        return new Examine( item );
    }

    public Action createUseWithAction( Item item,
                                       UserInventory inventory,
                                       ModelLocation location ) {
        return new UseWith( item, inventory, location );
    }

    public Action createExamineAnItemAction( List<Item> items ) {
        return new ExamineAnItem( items, this );
    }

    public Action createTakeAnItemAction( List<Item> items,
                                          UserInventory inventory,
                                          ModelLocation location ) {
        return new TakeAnItem( items, inventory, location );
    }

    public Action createTakeSpecificItemAction( Item item,
                                          UserInventory inventory,
                                          ModelLocation location ) {
        return null;
    }
}
