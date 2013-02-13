package com.chewielouie.textadventure;

public class PlainTextModelPopulator {
    private final String locationNameTag = "LOCATION\n";
    private final String inventoryItemNameTag = "INVENTORY ITEM\n";
    private int nextCharToParse = 0;
    private TextAdventureModel model = null;
    private ModelLocationFactory locationFactory = null;
    private UserInventory inventory = null;
    private ItemFactory itemFactory = null;
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
        if( itemFactory != null ) {
            while( moreContentToParse() && nextSectionIsAnInventoryItem() ) {
                Item item = itemFactory.create();
                item.deserialise( extractInventoryItemContent() );
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
                l.deserialise( extractLocationContent() );
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

