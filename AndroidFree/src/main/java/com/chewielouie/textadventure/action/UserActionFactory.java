package com.chewielouie.textadventure.action;

import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;
import com.chewielouie.textadventure.item.Item;
import java.util.List;

public class UserActionFactory implements ActionFactory {
    private ActionFactory factoryForChildActionsToUse = this;

    public void setFactoryForChildActionsToUse( ActionFactory f ) {
        this.factoryForChildActionsToUse = f;
    }

    private ActionFactory factoryForChildActionsToUse() {
        return factoryForChildActionsToUse;
    }

    public Action createShowInventoryAction( UserInventory inventory,
                                             TextAdventureModel model ) {
        return new ShowInventory( inventory, model, factoryForChildActionsToUse() );
    }

    public Action createInventoryItemAction( Item item,
                                             UserInventory inventory,
                                             ModelLocation location ) {
        return new InventoryItem( item, inventory, location, factoryForChildActionsToUse() );
    }

    public Action createExamineAction( Item item ) {
        return new Examine( item );
    }

    public Action createUseWithAction( Item item,
                                       UserInventory inventory,
                                       ModelLocation location ) {
        return new UseWith( item, inventory, location, factoryForChildActionsToUse() );
    }

    public Action createExamineAnItemAction( List<Item> items ) {
        return new ExamineAnItem( items, factoryForChildActionsToUse() );
    }

    public Action createTakeAnItemAction( List<Item> items,
                                          UserInventory inventory,
                                          ModelLocation location ) {
        return new TakeAnItem( items, inventory, location, factoryForChildActionsToUse() );
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

    public Action createTalkToAction( Item item ) {
        return new TalkToAction( item, factoryForChildActionsToUse() );
    }

    public Action createSayAction( String phraseId, Item item ) {
        return new SayAction( phraseId, item, factoryForChildActionsToUse() );
    }
}
