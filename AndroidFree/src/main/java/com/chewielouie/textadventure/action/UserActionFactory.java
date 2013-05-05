package com.chewielouie.textadventure.action;

import java.util.List;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;

public class UserActionFactory implements ActionFactory {
    public void setFactoryForChildActionsToUse( ActionFactory f ) {
    }

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
        return new UseWith( item, inventory, location, this );
    }

    public Action createExamineAnItemAction( List<Item> items ) {
        return new ExamineAnItem( items, this );
    }

    public Action createTakeAnItemAction( List<Item> items,
                                          UserInventory inventory,
                                          ModelLocation location ) {
        return new TakeAnItem( items, inventory, location, this );
    }

    public Action createTakeSpecificItemAction( Item item,
                                          UserInventory inventory,
                                          ModelLocation location ) {
        return new TakeSpecificItem( item, inventory, location );
    }

    public Action createUseWithSpecificItemAction( Item actionOwner,
                                                   Item target ) {
        return new UseWithSpecificItem( actionOwner, target );
    }

    public Action createExitAction( Exit exit, TextAdventureModel model ) {
      return new ExitAction( exit, model );
    }
}
