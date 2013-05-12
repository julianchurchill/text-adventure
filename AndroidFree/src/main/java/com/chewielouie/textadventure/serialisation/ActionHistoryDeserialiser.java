package com.chewielouie.textadventure.serialisation;

import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;
import com.chewielouie.textadventure.action.ActionFactory;
import com.chewielouie.textadventure.action.ActionHistory;

public class ActionHistoryDeserialiser {
    private ActionHistory history;
    private ActionFactory factory;
    private UserInventory inventory;
    private TextAdventureModel model;

    public ActionHistoryDeserialiser( ActionHistory history,
                                      ActionFactory factory,
                                      UserInventory inventory,
                                      TextAdventureModel model ) {
        this.history = history;
        this.factory = factory;
        this.inventory = inventory;
        this.model = model;
    }

    public void deserialise( String input ) {
        history.clear();
        if( factory != null )
            factory.createShowInventoryAction( inventory, model );
    }
}
