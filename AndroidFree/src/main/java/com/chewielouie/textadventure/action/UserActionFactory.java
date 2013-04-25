package com.chewielouie.textadventure.action;

import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;

public class UserActionFactory implements ActionFactory {
    public Action createShowInventoryAction( UserInventory inventory,
                                             TextAdventureModel model ) {
        return new ShowInventory( inventory, model );
    }
}

