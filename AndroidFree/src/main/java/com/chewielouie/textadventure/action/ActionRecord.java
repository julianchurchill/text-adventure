package com.chewielouie.textadventure.action;

public class ActionRecord {

    private Action action;
    private ActionParameters params;

    public ActionRecord( Action action, ActionParameters params ) {
        this.action = action;
        this.params = params;
    }

    public Action action() {
        return action;
    }

    public ActionParameters params() {
        return params;
    }
}
