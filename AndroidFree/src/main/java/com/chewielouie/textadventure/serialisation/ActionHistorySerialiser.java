package com.chewielouie.textadventure.serialisation;

import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionHistory;

public class ActionHistorySerialiser {
    private ActionHistory history;

    public ActionHistorySerialiser( ActionHistory history ) {
        this.history = history;
    }

    public String serialise() {
        String output = "";
        for( int i = 0; i < history.size(); ++i ) {
            output += serialiseActionType( history.getRecord( i ).action() );
            output += "\n";
        }
        return output;
    }

    private String serialiseActionType( Action action ) {
        return action.name();
    }
}
