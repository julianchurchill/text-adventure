package com.chewielouie.textadventure.serialisation;

import static com.chewielouie.textadventure.serialisation.ActionHistoryTextFormat.*;

import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionHistory;
import com.chewielouie.textadventure.action.ActionParameters;
import com.chewielouie.textadventure.action.ActionRecord;
import com.chewielouie.textadventure.item.Item;

public class ActionHistorySerialiser {
    private ActionHistory history;

    public ActionHistorySerialiser( ActionHistory history ) {
        this.history = history;
    }

    public String serialise() {
        String output = "";
        for( int i = 0; i < history.size(); ++i ) {
            ActionRecord record = history.getRecord( i );
            output += serialiseActionType( record.action() );
            output += serialiseActionParameters( record.params() );
            output += "\n";
        }
        return output;
    }

    private String serialiseActionType( Action action ) {
        if( action != null )
            return ACTION_NAME_TAG + SEPERATOR + action.name() + SEPERATOR;
        return "";
    }

    private String serialiseActionParameters( ActionParameters params ) {
        String output = "";
        if( params != null ) {
            output += serialiseStringParam( params.string() );
            output += serialiseItemParam( params.item() );
            output += serialiseExtraItemParam( params.extraItem() );
            output += serialiseExitParam( params.exit() );
            output += serialiseLocationParam( params.location() );
        }
        return output;
    }

    private String serialiseStringParam( String string ) {
        if( string != null )
            return STRING_TAG + SEPERATOR + string + SEPERATOR;
        return "";
    }

    private String serialiseItemParam( Item item ) {
        if( item != null )
            return ITEM_ID_TAG + SEPERATOR + item.id() + SEPERATOR;
        return "";
    }

    private String serialiseExtraItemParam( Item item ) {
        if( item != null )
            return EXTRA_ITEM_ID_TAG + SEPERATOR + item.id() + SEPERATOR;
        return "";
    }

    private String serialiseExitParam( Exit exit ) {
        if( exit != null )
            return EXIT_ID_TAG + SEPERATOR + exit.id() + SEPERATOR;
        return "";
    }

    private String serialiseLocationParam( ModelLocation location ) {
        if( location != null )
            return LOCATION_ID_TAG + SEPERATOR + location.id() + SEPERATOR;
        return "";
    }
}
