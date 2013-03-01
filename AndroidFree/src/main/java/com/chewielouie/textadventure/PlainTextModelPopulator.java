package com.chewielouie.textadventure;

import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.ItemFactory;
import com.chewielouie.textadventure.serialisation.ModelLocationDeserialiser;
import com.chewielouie.textadventure.serialisation.ItemDeserialiser;

public class PlainTextModelPopulator {
    private final String locationNameTag = "LOCATION\n";
    private final String inventoryItemNameTag = "INVENTORY ITEM\n";
    private int nextCharToParse = 0;
    private TextAdventureModel model = null;
    private ModelLocationFactory locationFactory = null;
    private UserInventory inventory = null;
    private ItemFactory itemFactory = null;
    private ModelLocationDeserialiser locationDeserialiser;
    private ItemDeserialiser itemDeserialiser;
    private String content;

    public PlainTextModelPopulator( TextAdventureModel model,
                                    ModelLocationFactory locationFactory,
                                    UserInventory inventory,
                                    ItemFactory itemFactory,
                                    ModelLocationDeserialiser d,
                                    ItemDeserialiser i,
                                    String content ) {
        this.model = model;
        this.locationFactory = locationFactory;
        this.inventory = inventory;
        this.itemFactory = itemFactory;
        this.locationDeserialiser = d;
        this.itemDeserialiser = i;
        this.content = content;

        extractInventory();
        extractLocations();
    }

    private void extractInventory() {
        if( itemFactory != null ) {
            while( moreContentToParse() && nextSectionIsAnInventoryItem() ) {
                Item item = itemFactory.create();
                String content = extractInventoryItemContent();
                if( itemDeserialiser != null )
                    itemDeserialiser.deserialise( item, content );
                if( inventory != null )
                    inventory.addToInventory( item );
            }
        }
    }

    private boolean nextSectionIsAnInventoryItem() {
        return content.indexOf( inventoryItemNameTag, nextCharToParse ) != -1;
    }

    private String extractInventoryItemContent() {
        int itemStart = content.indexOf( inventoryItemNameTag, nextCharToParse );
        findEndOfCurrentSection();
        return content.substring(
                itemStart + inventoryItemNameTag.length(), nextCharToParse );
    }

    private void findEndOfCurrentSection() {
        int endOfSection = content.indexOf( inventoryItemNameTag, nextCharToParse+1 );
        if( endOfSection == -1 ) {
            endOfSection = content.indexOf( locationNameTag, nextCharToParse+1 );
            if( endOfSection == -1 )
                endOfSection = content.length();
        }
        nextCharToParse = endOfSection;
    }

    private void extractLocations() {
        if( locationFactory != null ) {
            while( moreContentToParse() ) {
                ModelLocation l = locationFactory.create();
                String content = extractLocationContent();
                if( locationDeserialiser != null )
                    locationDeserialiser.deserialise( l, content );
                if( model != null )
                    model.addLocation( l );
            }
        }
    }

    private boolean moreContentToParse() {
        return nextCharToParse < content.length();
    }

    private String extractLocationContent() {
        String locationContent = "";
        int locationStart = content.indexOf( locationNameTag, nextCharToParse );
        if( locationStart != -1 ) {
            nextCharToParse = content.indexOf( locationNameTag, locationStart+1 );
            if( nextCharToParse == -1 )
                nextCharToParse = content.length();

            locationContent = content.substring(
                    locationStart + locationNameTag.length(), nextCharToParse );
        }
        else
            nextCharToParse = content.length();
        return locationContent;
    }
}

