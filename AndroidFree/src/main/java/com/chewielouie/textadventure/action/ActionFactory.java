package com.chewielouie.textadventure.action;

import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;

public interface ActionFactory {
    public Action createShowInventoryAction( UserInventory inventory,
                                             TextAdventureModel model );
}

