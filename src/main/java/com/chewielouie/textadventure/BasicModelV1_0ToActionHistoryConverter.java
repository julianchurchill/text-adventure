package com.chewielouie.textadventure;

import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionHistory;
import com.chewielouie.textadventure.action.ActionParameters;
import com.chewielouie.textadventure.action.BasicActionHistory;
import com.chewielouie.textadventure.action.ShowInventory;
import com.chewielouie.textadventure.action.TakeSpecificItem;
import com.chewielouie.textadventure.item.Item;

public class BasicModelV1_0ToActionHistoryConverter {
    private TextAdventureModel model;

    public BasicModelV1_0ToActionHistoryConverter( TextAdventureModel model ) {
        this.model = model;
    }

    public ActionHistory actionHistory() {
        // inspect the state of the model and fill in the action history with guesses
            // 1. Figure out what has been picked up
                // a. if skeleton key is in inventory do 'take specific item:clocktowerskeletonkey:townentrance'
                // b... if xxx is in inventory do 'take specific item:item id:location id'
            // 2. Figure out what has been used
            // 3. Figure out what has been examined
            // 4. Figure out where the player is and get them there by using exits
        ActionHistory history = new BasicActionHistory();
        Item skeletonKey = null;
        for( Item item : model.inventoryItems() )
            if( item.id().equals( "clocktowerskeletonkey" ) )
                skeletonKey = item;
        ModelLocation townEntrance = model.findLocationByID( "townentrance" );
        Action action = new TakeSpecificItem( skeletonKey, null, townEntrance );
        ActionParameters parameters = new ActionParameters( skeletonKey, townEntrance );
        history.addActionWithParameters( action, parameters );
        return history;
    }
}

