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

        location.setId( extractNewlineDelimitedValueFor( locationIDTag ) );
        deserialiseDescription();
        deserialiseExits();
        deserialiseItems();
    }

    private String extractNewlineDelimitedValueFor( String tag ) {
        int startOfTag = content.indexOf( tag );
        if( startOfTag == -1 )
            return "";
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
            int nextTag = -1;
            while( (nextTag=findTagFrom( nextTag, exitTag )) != -1 ) {
                Exit exit = exitFactory.create();
                if( exitDeserialiser != null )
                    exitDeserialiser.deserialise( exit,
                                                  extractContentForTagFrom( nextTag, exitTag ) );
                location.addExit( exit );
            }
        }
    }

    private int findTagFrom( int start, String tag ) {
        return content.indexOf( tag, start+1 );
    }

    private String extractContentForTagFrom( int start, String tag ) {
        int endOfTag = findEndOfTag( tag, start );
        return content.substring( start + tag.length(), endOfTag );
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
            int nextTag = -1;
            while( (nextTag=findTagFrom( nextTag, itemTag )) != -1 ) {
                Item item = itemFactory.create();
                if( itemDeserialiser != null )
                    itemDeserialiser.deserialise( item,
                                                  extractContentForTagFrom( nextTag, itemTag ) );
                location.addItem( item );
            }
        }
    }
}

