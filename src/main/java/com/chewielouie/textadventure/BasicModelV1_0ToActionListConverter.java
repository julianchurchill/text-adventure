package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.TakeSpecificItem;
import com.chewielouie.textadventure.item.Item;

public class BasicModelV1_0ToActionListConverter {
    private TextAdventureModel model;

    public BasicModelV1_0ToActionListConverter( TextAdventureModel model ) {
        this.model = model;
    }

    public List<Action> actions() {
        // inspect the state of the model and fill in the action history with guesses
            // 1. Figure out what has been picked up
                // a. if skeleton key is in inventory do 'take specific item:clocktowerskeletonkey:townentrance'
                // b... if xxx is in inventory do 'take specific item:item id:location id'
            // 2. Figure out what has been used
            // 3. Figure out what has been examined
            // 4. Figure out where the player is and get them there by using exits
        List<Action> actions = new ArrayList<Action>();
        Item skeletonKey = null;
        for( Item item : model.inventoryItems() )
            if( item.id().equals( "clocktowerskeletonkey" ) )
                skeletonKey = item;
        ModelLocation townEntrance = model.findLocationByID( "townentrance" );
        actions.add( new TakeSpecificItem( skeletonKey, null, townEntrance ) );
        return actions;
    }
}

