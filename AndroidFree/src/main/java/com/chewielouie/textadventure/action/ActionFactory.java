package com.chewielouie.textadventure.action;

import java.util.List;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;

public interface ActionFactory {
    public void setFactoryForChildActionsToUse( ActionFactory f );
    public Action createShowInventoryAction( UserInventory inventory,
                                             TextAdventureModel model );
    public Action createInventoryItemAction( Item item,
                                             UserInventory inventory,
                                             ModelLocation location );
    public Action createExamineAction( Item item );
    public Action createUseWithAction( Item item,
                                       UserInventory inventory,
                                       ModelLocation location );
    public Action createExamineAnItemAction( List<Item> items );
    public Action createTakeAnItemAction( List<Item> items,
                                          UserInventory inventory,
                                          ModelLocation location );
    public Action createTakeSpecificItemAction( Item item,
                                          UserInventory inventory,
                                          ModelLocation location );
    public Action createUseWithSpecificItemAction( Item actionOwner,
                                                   Item target );
    public Action createExitAction( Exit exit, TextAdventureModel model );
    public Action createTalkToAction( Item item );
    public Action createSayAction( String phraseId, Item item );
}

