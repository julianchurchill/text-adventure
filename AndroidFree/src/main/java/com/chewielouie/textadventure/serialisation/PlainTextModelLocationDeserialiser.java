package com.chewielouie.textadventure.serialisation;

import com.chewielouie.textadventure.DeserialiserUtils;
import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.ExitFactory;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.ItemFactory;
import com.chewielouie.textadventure.ModelLocation;

public class PlainTextModelLocationDeserialiser implements ModelLocationDeserialiser {
    private final String xTag = "x:";
    private final String yTag = "y:";
    private final String locationIDTag = "location id:";
    private final String locationAreaIDTag = "location area id:";
    private final String locationDescriptionTag = "location description:";
    private final String textToShowOnFirstEntryTag = "text to show on first entry:";
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

        deserialiseCoordinates();
        location.setId( DeserialiserUtils.extractNewlineDelimitedValueFor( locationIDTag, content ) );
        location.setAreaID( DeserialiserUtils.extractNewlineDelimitedValueFor( locationAreaIDTag, content ) );
        deserialiseDescription();
        deserialiseTextForFirstEntry();
        deserialiseExits();
        deserialiseItems();
    }

    private void deserialiseCoordinates() {
        String x = DeserialiserUtils.extractNewlineDelimitedValueFor( xTag, content );
        if( x != "" )
            location.setX( Integer.parseInt( x ) );
        String y = DeserialiserUtils.extractNewlineDelimitedValueFor( yTag, content );
        if( y != "" )
            location.setY( Integer.parseInt( y ) );
    }

    private void deserialiseDescription() {
        int start = content.indexOf( locationDescriptionTag );
        if( start != -1 )
            location.setLocationDescription(
                findContentForTag( locationDescriptionTag, start ) );
    }

    private String findContentForTag( String tag, int start ) {
        return content.substring( start + tag.length(), findEndOfValue( start + 1 ) );
    }

    private int findEndOfValue( int start ) {
        int end = content.indexOf( textToShowOnFirstEntryTag, start );
        if( end == -1 ) {
            end = content.indexOf( exitTag, start );
            if( end == -1 ) {
                end = content.indexOf( itemTag, start );
                if( end == -1 )
                    end = content.length();
            }
        }
        return end;
    }

    private void deserialiseTextForFirstEntry() {
        int start = content.indexOf( textToShowOnFirstEntryTag );
        if( start != -1 )
            location.setTextForFirstEntry(
                findContentForTag( textToShowOnFirstEntryTag, start ) );
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

