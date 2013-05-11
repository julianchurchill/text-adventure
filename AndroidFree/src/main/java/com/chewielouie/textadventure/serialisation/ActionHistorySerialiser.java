package com.chewielouie.textadventure.serialisation;

import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionHistory;
import com.chewielouie.textadventure.action.ActionParameters;
import com.chewielouie.textadventure.action.ActionRecord;
import com.chewielouie.textadventure.item.Item;

public class ActionHistorySerialiser {
    private static final String SEPERATOR = ":";
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
            return "action name" + SEPERATOR + action.name() + SEPERATOR;
        return "";
    }

    private String serialiseActionParameters( ActionParameters params ) {
        String output = "";
        if( params != null ) {
            output += serialiseItemParam( params.item() );
            output += serialiseExtraItemParam( params.extraItem() );
            output += serialiseExitParam( params.exit() );
        }
        return output;
    }

    private String serialiseItemParam( Item item ) {
        if( item != null )
            return "item id" + SEPERATOR + item.id() + SEPERATOR;
        return "";
    }

    private String serialiseExtraItemParam( Item item ) {
        if( item != null )
            return "extra item id" + SEPERATOR + item.id() + SEPERATOR;
        return "";
    }

    private String serialiseExitParam( Exit exit ) {
        if( exit != null )
            return "exit id" + SEPERATOR + exit.id() + SEPERATOR;
        return "";
    }
}
