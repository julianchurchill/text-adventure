package com.chewielouie.textadventure.serialisation;

import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.TextAdventureModel;

public class PlainTextModelSerialiser {
    private ItemSerialiser itemSerialiser;

    public PlainTextModelSerialiser( ItemSerialiser itemSerialiser ) {
        this.itemSerialiser = itemSerialiser;
    }

    public String serialise( TextAdventureModel model ) {
        StringBuffer text = new StringBuffer();
        for( Item item : model.inventoryItems() ) {
            text.append( "INVENTORY ITEM\n" );
            text.append( itemSerialiser.serialise( item ) );
        }
        return text.toString();
    }
}

