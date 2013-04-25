package com.chewielouie.textadventure.serialisation;

import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.TextAdventureModel;

public class PlainTextModelDeltaSerialiser {
    private LocationSerialiser locationSerialiser;

    public PlainTextModelDeltaSerialiser( LocationSerialiser locationSerialiser ) {
        this.locationSerialiser = locationSerialiser;
    }

    public String serialise( TextAdventureModel model ) {
        StringBuffer text = new StringBuffer();
        for( Item item : model.inventoryItems() ) {
            text.append( "inventory item:" );
            text.append( item.id() );
        }
        for( ModelLocation location : model.locations() ) {
            text.append( "LOCATION\n" );
            text.append( locationSerialiser.serialise( location ) );
        }
        return text.toString();
    }
}

