package com.chewielouie.textadventure.action;

import java.util.List;
import java.util.ArrayList;

public class BasicActionHistory implements ActionHistory {
    private List<ActionRecord> history = new ArrayList<ActionRecord>();

    public void addActionWithParameters( Action action, ActionParameters params ) {
        history.add( new ActionRecord( action, params ) );
    }

    public int size() {
        return history.size();
    }

    public ActionRecord getRecord( int index ) {
        return history.get( index );
    }

    public void clear() {
        history.clear();
    }
}
