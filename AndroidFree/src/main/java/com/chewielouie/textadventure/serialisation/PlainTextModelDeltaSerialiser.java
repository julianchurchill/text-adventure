package com.chewielouie.textadventure.serialisation;

import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.TextAdventureModel;

public class PlainTextModelDeltaSerialiser {
    private ItemSerialiser itemSerialiser;
    private LocationSerialiser locationSerialiser;

    public PlainTextModelDeltaSerialiser( ItemSerialiser itemSerialiser,
           LocationSerialiser locationSerialiser ) {
        this.itemSerialiser = itemSerialiser;
        this.locationSerialiser = locationSerialiser;
    }

    public String serialise( TextAdventureModel model ) {
        StringBuffer text = new StringBuffer();
        for( Item item : model.inventoryItems() ) {
            text.append( "INVENTORY ITEM\n" );
            text.append( itemSerialiser.serialise( item ) );
        }
        for( ModelLocation location : model.locations() ) {
            text.append( "LOCATION\n" );
            text.append( locationSerialiser.serialise( location ) );
        }
        return text.toString();
    }
}

