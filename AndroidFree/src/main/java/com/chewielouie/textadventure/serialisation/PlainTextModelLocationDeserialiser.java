package com.chewielouie.textadventure.serialisation;

import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.ExitFactory;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.ItemFactory;
import com.chewielouie.textadventure.ModelLocation;

public class PlainTextModelLocationDeserialiser implements ModelLocationDeserialiser {
    private final String locationIDTag = "location id:";
    private final String locationDescriptionTag = "location description:";
    private final String exitTag = "EXIT\n";
    private final String itemTag = "ITEM\n";
    private String content;
    private int startOfLastFoundTag;
    private ModelLocation location;
    private ItemFactory itemFactory;
    private ExitFactory exitFactory;
    private ItemDeserialiser itemDeserialiser;
    private ExitDeserialiser exitDeserialiser;

    public PlainTextModelLocationDeserialiser( ItemFactory itemFactory,
                                      ExitFactory exitFactory ) {
        this.itemFactory = itemFactory;
        this.exitFactory = exitFactory;
    }

    public PlainTextModelLocationDeserialiser( ItemFactory itemFactory,
                  ExitFactory exitFactory,
                  ItemDeserialiser itemDeserialiser,
                  ExitDeserialiser exitDeserialiser ) {
        this.itemFactory = itemFactory;
        this.exitFactory = exitFactory;
        this.itemDeserialiser = itemDeserialiser;
        this.exitDeserialiser = exitDeserialiser;
    }

    public void deserialise( ModelLocation location, String content ) {
        this.location = location;
        this.content = content;
        startOfLastFoundTag = -1;

        location.setId( extractNewlineDelimitedValueFor( locationIDTag ) );
        deserialiseDescription();
        deserialiseExits();
        deserialiseItems();
    }

    private String extractNewlineDelimitedValueFor( String tag ) {
        int startOfTag = content.indexOf( tag, startOfLastFoundTag + 1 );
        if( startOfTag == -1 )
            return "";
        startOfLastFoundTag = startOfTag;
        int endOfTag = content.indexOf( "\n", startOfTag );
        if( endOfTag == -1 )
            endOfTag = content.length();
        return content.substring( startOfTag + tag.length(), endOfTag );
    }

    private void deserialiseDescription() {
        int startOfDescription = content.indexOf( locationDescriptionTag );
        if( startOfDescription != -1 )
            location.setLocationDescription( content.substring(
                        startOfDescription + locationDescriptionTag.length(),
                        findEndOfDescription() ) );
    }

    private int findEndOfDescription() {
        int endOfDescription = content.indexOf( exitTag );
        if( endOfDescription == -1 ) {
            endOfDescription = content.indexOf( itemTag );
            if( endOfDescription == -1 )
                endOfDescription = content.length();
        }
        return endOfDescription;
    }

    private void deserialiseExits() {
        if( exitFactory != null ) {
            while( tagAvailable( exitTag ) ) {
                int startOfTag = findStartOfTag( exitTag );
                Exit exit = exitFactory.create();
                if( exitDeserialiser != null )
                    exitDeserialiser.deserialise( exit,
                                                  extractContentForTag( exitTag ) );
                location.addExit( exit );
                startOfLastFoundTag = startOfTag;
            }
        }
    }

    private String extractContentForTag( String tag ) {
        int startOfTag = findStartOfTag( tag );
        int endOfTag = findEndOfTag( tag, startOfTag );
        return content.substring( startOfTag + tag.length(), endOfTag );
    }

    private boolean tagAvailable( String tag ) {
        return content.indexOf( tag, startOfLastFoundTag+1 ) != -1;
    }

    private int findStartOfTag( String tag ) {
        return content.indexOf( tag, startOfLastFoundTag+1 );
    }

    private int findEndOfTag( String tag, int startOfTag ) {
        int endOfTag = content.indexOf( tag, startOfTag+1 );
        if( endOfTag == -1 ) {
            endOfTag = content.indexOf( itemTag, startOfTag+1 );
            if( endOfTag == -1 )
                endOfTag = content.length();
        }
        return endOfTag;
    }

    private void deserialiseItems() {
        if( itemFactory != null ) {
            while( tagAvailable( itemTag ) ) {
                int startOfTag = findStartOfTag( itemTag );
                Item item = itemFactory.create();
                if( itemDeserialiser != null )
                    itemDeserialiser.deserialise( item,
                                                  extractContentForTag( itemTag ) );
                location.addItem( item );
                startOfLastFoundTag = startOfTag;
            }
        }
    }
}

