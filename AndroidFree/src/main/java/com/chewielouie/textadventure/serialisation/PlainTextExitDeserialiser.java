package com.chewielouie.textadventure.serialisation;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.DeserialiserUtils;
import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.itemaction.ItemActionFactory;
import com.chewielouie.textadventure.itemaction.ItemAction;

public class PlainTextExitDeserialiser implements ExitDeserialiser {
    private final String exitLabelTag = "exit label:";
    private final String exitDestinationTag = "exit destination:";
    private final String exitDirectionHintTag = "exit direction hint:";
    private final String exitIsNotVisibleTag = "exit is not visible:";
    private final String exitIDTag = "exit id:";
    private final String exitOnUseActionTag = "exit on use action:";
    private String content = null;
    private Exit exit = null;
    private ItemActionFactory itemActionFactory = null;

    public PlainTextExitDeserialiser( ItemActionFactory f ) {
        itemActionFactory = f;
    }

    public void deserialise( Exit exit, String content ) {
        this.content = content;
        this.exit = exit;
        exit.setLabel(
            DeserialiserUtils.extractNewlineDelimitedValueFor( exitLabelTag, content ) );
        exit.setDestination(
            DeserialiserUtils.extractNewlineDelimitedValueFor( exitDestinationTag, content ) );
        exit.setDirectionHint( stringToDirectionHint(
            DeserialiserUtils.extractNewlineDelimitedValueFor( exitDirectionHintTag, content ) ) );
        if( exitNotVisibleIsSpecified() )
            exit.setInvisible();
        exit.setID( extractExitID() );
        extractOnUseActions();
    }

    private void extractOnUseActions() {
        List<ItemAction> actions = new ItemActionDeserialiser( content,
            exitOnUseActionTag, null, itemActionFactory ).extract();
        for( ItemAction action : actions )
            exit.addOnUseAction( action );
    }

    private boolean exitNotVisibleIsSpecified() {
        return content.indexOf( exitIsNotVisibleTag ) != DeserialiserUtils.NOT_FOUND;
    }

    private String extractExitID() {
        int startOfTag = content.indexOf( exitIDTag );
        if( startOfTag != -1 )
            return DeserialiserUtils.extractNewlineDelimitedValueFor( exitIDTag, content );
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

