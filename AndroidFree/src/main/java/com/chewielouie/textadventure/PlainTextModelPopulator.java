package com.chewielouie.textadventure;

public class PlainTextModelPopulator {
    private final String locationNameTag = "LOCATION\n";
    private final String inventoryItemNameTag = "INVENTORY ITEM\n";
    private int nextCharToParse = 0;
    private TextAdventureModel model = null;
    private ModelLocationFactory locationFactory;
    private UserInventory inventory = null;
    private ItemFactory itemFactory;
    private String content;

    public PlainTextModelPopulator( TextAdventureModel model,
                                    ModelLocationFactory locationFactory,
                                    UserInventory inventory,
                                    ItemFactory itemFactory,
                                    String content ) {
        this.model = model;
        this.locationFactory = locationFactory;
        this.inventory = inventory;
        this.itemFactory = itemFactory;
        this.content = content;

        extractInventory();
        extractLocations();
    }

    private void extractInventory() {
        if( content.indexOf( inventoryItemNameTag, nextCharToParse ) != -1 ) {
            while( moreContentToParse() ) {
                Item item = itemFactory.create();
                item.deserialise( extractInventoryItemContent() );
                if( inventory != null )
                    inventory.addToInventory( item );
            }
        }
    }

    private String extractInventoryItemContent() {
        String itemContent = "";
        int itemStart = content.indexOf( inventoryItemNameTag, nextCharToParse );
        if( itemStart != -1 ) {
            nextCharToParse = content.indexOf( inventoryItemNameTag, itemStart+1 );
            if( nextCharToParse == -1 )
                nextCharToParse = content.length();

            itemContent = content.substring(
                    itemStart + inventoryItemNameTag.length(), nextCharToParse );
        }
        else
            nextCharToParse = content.length();
        return itemContent;
    }

    private void extractLocations() {
        while( moreContentToParse() ) {
            ModelLocation l = locationFactory.create();
            l.deserialise( extractLocationContent() );
            if( model != null )
                model.addLocation( l );
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

