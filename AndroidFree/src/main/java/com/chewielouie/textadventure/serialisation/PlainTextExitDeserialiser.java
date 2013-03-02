package com.chewielouie.textadventure.serialisation;

import com.chewielouie.textadventure.Exit;

public class PlainTextExitDeserialiser implements ExitDeserialiser {
    private final String exitLabelTag = "exit label:";
    private final String exitDestinationTag = "exit destination:";
    private final String exitDirectionHintTag = "exit direction hint:";
    private final String exitIsNotVisibleTag = "exit is not visible:";
    private final String exitIDTag = "exit id:";
    private String content;

    public void deserialise( Exit exit, String content ) {
        this.content = content;
        exit.setLabel( extractNewlineDelimitedValueFor( exitLabelTag ) );
        exit.setDestination(
                extractNewlineDelimitedValueFor( exitDestinationTag ) );
        exit.setDirectionHint( stringToDirectionHint(
            extractNewlineDelimitedValueFor( exitDirectionHintTag ) ) );
        if( exitNotVisibleIsSpecifiedDiscardIt() )
            exit.setInvisible();
        exit.setID( extractExitID() );
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

    private boolean exitNotVisibleIsSpecifiedDiscardIt() {
        int startOfTag = content.indexOf( exitIsNotVisibleTag );
        if( startOfTag != -1 ) {
            extractNewlineDelimitedValueFor( exitIsNotVisibleTag );
            return true;
        }
        return false;
    }

    private String extractExitID() {
        int startOfTag = content.indexOf( exitIDTag );
        if( startOfTag != -1 )
            return extractNewlineDelimitedValueFor( exitIDTag );
        return "";
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

