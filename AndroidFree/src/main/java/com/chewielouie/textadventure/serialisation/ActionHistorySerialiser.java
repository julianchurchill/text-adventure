package com.chewielouie.textadventure.serialisation;

import static com.chewielouie.textadventure.serialisation.ActionHistoryTextFormat.*;

import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionHistory;
import com.chewielouie.textadventure.action.ActionParameters;
import com.chewielouie.textadventure.action.ActionRecord;
import com.chewielouie.textadventure.item.Item;
import java.lang.StringBuilder;

public class ActionHistorySerialiser {
    private ActionHistory history;
    private StringBuilder stringBuilder = null;
    // These are to avoid doing the concatentaion while serialising. Reduces the appends the
    // string builder must do by 1. At the time this was written a large game save takes 3s
    // and this optimisation can save about 30% of that.
    private static final String ACTION_NAME_TAG_AND_SEPERATOR = ACTION_NAME_TAG + SEPERATOR;
    private static final String STRING_TAG_AND_SEPERATOR = STRING_TAG + SEPERATOR;
    private static final String ITEM_ID_TAG_AND_SEPERATOR = ITEM_ID_TAG + SEPERATOR;
    private static final String EXTRA_ITEM_ID_TAG_AND_SEPERATOR = EXTRA_ITEM_ID_TAG + SEPERATOR;
    private static final String EXIT_ID_TAG_AND_SEPERATOR = EXIT_ID_TAG + SEPERATOR;
    private static final String LOCATION_ID_TAG_AND_SEPERATOR = LOCATION_ID_TAG + SEPERATOR;

    public ActionHistorySerialiser( ActionHistory history ) {
        this.history = history;
    }

    public String serialise() {
        stringBuilder = new StringBuilder();
        for( int i = 0; i < history.size(); ++i ) {
            ActionRecord record = history.getRecord( i );
            serialiseActionType( record.action() );
            serialiseActionParameters( record.params() );
            stringBuilder.append( "\n" );
        }
        return stringBuilder.toString();
    }

    private void serialiseActionType( Action action ) {
        if( action != null ) {
            stringBuilder.append( ACTION_NAME_TAG_AND_SEPERATOR );
            stringBuilder.append( action.name() );
            stringBuilder.append( SEPERATOR );
        }
    }

    private void serialiseActionParameters( ActionParameters params ) {
        if( params != null ) {
            serialiseStringParam( params.string() );
            serialiseItemParam( params.item() );
            serialiseExtraItemParam( params.extraItem() );
            serialiseExitParam( params.exit() );
            serialiseLocationParam( params.location() );
        }
    }

    private void serialiseStringParam( String string ) {
        if( string != null ) {
            stringBuilder.append( STRING_TAG_AND_SEPERATOR );
            stringBuilder.append( string );
            stringBuilder.append( SEPERATOR );
        }
    }

    private void serialiseItemParam( Item item ) {
        if( item != null ) {
            stringBuilder.append( ITEM_ID_TAG_AND_SEPERATOR );
            stringBuilder.append( item.id() );
            stringBuilder.append( SEPERATOR );
        }
    }

    private void serialiseExtraItemParam( Item item ) {
        if( item != null ) {
            stringBuilder.append( EXTRA_ITEM_ID_TAG_AND_SEPERATOR );
            stringBuilder.append( item.id() );
            stringBuilder.append( SEPERATOR );
        }
    }

    private void serialiseExitParam( Exit exit ) {
        if( exit != null ) {
            stringBuilder.append( EXIT_ID_TAG_AND_SEPERATOR );
            stringBuilder.append( exit.id() );
            stringBuilder.append( SEPERATOR );
        }
    }

    private void serialiseLocationParam( ModelLocation location ) {
        if( location != null ) {
            stringBuilder.append( LOCATION_ID_TAG_AND_SEPERATOR );
            stringBuilder.append( location.id() );
            stringBuilder.append( SEPERATOR );
        }
    }
}
