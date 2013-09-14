package com.chewielouie.textadventure;

import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.ItemFactory;
import com.chewielouie.textadventure.serialisation.ModelLocationDeserialiser;
import com.chewielouie.textadventure.serialisation.ItemDeserialiser;

public class PlainTextModelPopulator {
    private final String propertiesTag = "PROPERTIES\n";
    private final String maximumScoreTag = "maximum score:";
    private final String locationNameTag = "LOCATION\n";
    private final String inventoryItemNameTag = "INVENTORY ITEM\n";
    private final String locationAreaTag = "LOCATION AREA\n";
    private final String locationAreaIdTag = "location area id:";
    private final String locationAreaNameTag = "location area name:";
    private final String[] orderedSections = { propertiesTag, inventoryItemNameTag, locationAreaTag, locationNameTag };
    private int nextCharToParse = 0;
    private TextAdventureModel model = new NullModel();
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
        if( model != null )
            this.model = model;
        this.locationFactory = locationFactory;
        this.inventory = inventory;
        this.itemFactory = itemFactory;
        this.locationDeserialiser = d;
        this.itemDeserialiser = i;
        this.content = content;

        extractProperties();
        extractInventory();
        extractLocationAreas();
        extractLocations();
    }

    private void extractProperties() {
        if( content.indexOf( propertiesTag ) != DeserialiserUtils.NOT_FOUND ) {
            String maxScore = DeserialiserUtils.extractNewlineDelimitedValueFor(
                maximumScoreTag, content );
            if( maxScore != "" ) {
                try {
                    model.setMaximumScore( Integer.parseInt( maxScore ) );
                } catch( NumberFormatException e ) {
                    System.out.println("Bad number format for maximum score property - '" + maxScore + "'");
                }
            }
            nextCharToParse = findEndOfCurrentSection();
        }
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
        nextCharToParse = findEndOfCurrentSection();
        return content.substring(
                itemStart + inventoryItemNameTag.length(), nextCharToParse );
    }

    private int findEndOfCurrentSection() {
        int endOfSection = -1;
        for( String section : orderedSections )
            if( endOfSection == - 1 )
                endOfSection = content.indexOf( section, nextCharToParse+1 );

        if( endOfSection == -1 )
            endOfSection = content.length();
        return endOfSection;
    }

    private void extractLocationAreas() {
        while( moreContentToParse() && nextSectionIsALocationArea() ) {
            model.addLocationArea( extractLocationAreaId(),
                                   extractLocationAreaName() );
            nextCharToParse++;
        }
    }

    private boolean nextSectionIsALocationArea() {
        return content.indexOf( locationAreaTag, nextCharToParse ) != -1;
    }

    private String extractLocationAreaId() {
        return DeserialiserUtils.extractNewlineDelimitedValueFor( locationAreaIdTag, content, nextCharToParse );
    }

    private String extractLocationAreaName() {
        return DeserialiserUtils.extractNewlineDelimitedValueFor( locationAreaNameTag, content, nextCharToParse );
    }

    private void extractLocations() {
        if( locationFactory != null ) {
            while( moreContentToParse() ) {
                ModelLocation l = locationFactory.create();
                String content = extractLocationContent();
                if( locationDeserialiser != null )
                    locationDeserialiser.deserialise( l, content );
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

