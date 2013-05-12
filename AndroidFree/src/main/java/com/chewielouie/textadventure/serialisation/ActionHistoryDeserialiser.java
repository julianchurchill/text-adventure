package com.chewielouie.textadventure.serialisation;

import com.chewielouie.textadventure.action.ActionHistory;

public class ActionHistoryDeserialiser {
    private ActionHistory history;

    public ActionHistoryDeserialiser( ActionHistory history ) {
        this.history = history;
    }

    public void deserialise() {
        history.clear();
    }
}
