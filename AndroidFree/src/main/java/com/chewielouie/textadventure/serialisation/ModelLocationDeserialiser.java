package com.chewielouie.textadventure.serialisation;

import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.ExitFactory;
import com.chewielouie.textadventure.Item;
import com.chewielouie.textadventure.ItemFactory;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.LocationExit;

public class ModelLocationDeserialiser {
    private final String locationIDTag = "location id:";
    private final String locationDescriptionTag = "location description:";
    private final String exitLabelTag = "exit label:";
    private final String exitDestinationTag = "exit destination:";
    private final String exitDirectionHintTag = "exit direction hint:";
    private final String exitIsNotVisibleTag = "exit is not visible:";
    private final String exitIDTag = "exit id:";
    private final String exitTag = "EXIT\n";
    private final String itemTag = "ITEM\n";
    private String content;
    private int startOfLastFoundTag = -1;
    private ModelLocation location;
    private ItemFactory itemFactory;
    private ExitFactory exitFactory;

    public ModelLocationDeserialiser( ModelLocation location,
           ItemFactory itemFactory, ExitFactory exitFactory ) {
        this.location = location;
        this.itemFactory = itemFactory;
        this.exitFactory = exitFactory;
    }

    public void deserialise( String content ) {
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
            while( exitAvailable() ) {
                int startOfExit = content.indexOf( exitTag, startOfLastFoundTag+1 );
                int endOfExit = content.indexOf( exitTag, startOfExit+1 );
                if( endOfExit == -1 ) {
                    endOfExit = content.indexOf( locationIDTag, startOfExit+1 );
                    if( endOfExit == -1 )
                        endOfExit = content.length();
                }
                Exit exit = exitFactory.create();
                exit.deserialise(
                    content.substring( startOfExit + exitTag.length(), endOfExit ) );
                location.addExit( exit );
                startOfLastFoundTag = startOfExit;
            }
        }
    }

    private boolean exitAvailable() {
        return content.indexOf( exitTag, startOfLastFoundTag+1 ) != -1;
    }

    private boolean exitNotVisibleIsSpecifiedDiscardIt() {
        int startOfTag = content.indexOf( exitIsNotVisibleTag, startOfLastFoundTag + 1 );
        if( indexIsInCurrentExit( startOfTag ) ) {
            extractNewlineDelimitedValueFor( exitIsNotVisibleTag );
            return true;
        }
        return false;
    }

    private String extractExitID() {
        int startOfTag = content.indexOf( exitIDTag, startOfLastFoundTag + 1 );
        if( indexIsInCurrentExit( startOfTag ) )
            return extractNewlineDelimitedValueFor( exitIDTag );
        return "";
    }

    private boolean indexIsInCurrentExit( int index ) {
        int nextExit = content.indexOf( exitLabelTag, startOfLastFoundTag + 1 );
        if( index != -1 && ( nextExit == -1 || nextExit > index ) )
            return true;
        return false;
    }

    private void deserialiseItems() {
        if( itemFactory != null ) {
            while( itemAvailable() ) {
                int startOfItem = content.indexOf( itemTag, startOfLastFoundTag+1 );
                int endOfItem = content.indexOf( itemTag, startOfItem+1 );
                if( endOfItem == -1 ) {
                    endOfItem = content.indexOf( locationIDTag, startOfItem+1 );
                    if( endOfItem == -1 )
                        endOfItem = content.length();
                }
                Item item = itemFactory.create();
                item.deserialise(
                        content.substring( startOfItem + itemTag.length(), endOfItem ) );
                location.addItem( item );
                startOfLastFoundTag = startOfItem;
            }
        }
    }

    private boolean itemAvailable() {
        return content.indexOf( itemTag, startOfLastFoundTag+1 ) != -1;
    }

    private Exit.DirectionHint stringToDirectionHint( String hint ) {
        if( hint.equals( "North" ) )
            return Exit.DirectionHint.North;
        if( hint.equals( "South" ) )
            return Exit.DirectionHint.South;
        if( hint.equals( "East" ) )
            return Exit.DirectionHint.East;
        if( hint.equals( "West" ) )
            return Exit.DirectionHint.West;
        return Exit.DirectionHint.DontCare;
    }
}

