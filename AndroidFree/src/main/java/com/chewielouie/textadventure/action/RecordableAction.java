package com.chewielouie.textadventure.action;

import java.util.List;

public class RecordableAction implements Action {
    private Action wrappedAction;
    private ActionHistory actionHistory;
    private ActionParameters params;

    public RecordableAction( Action toWrap, ActionHistory actionHistory ) {
        this.wrappedAction = toWrap;
        this.actionHistory = actionHistory;
    }

    public String label() {
        return wrappedAction.label();
    }

    public void trigger() {
        if( actionHistory != null )
            actionHistory.addActionWithParameters( wrappedAction, params );
        wrappedAction.trigger();
    }

    public boolean userMustChooseFollowUpAction() {
        return wrappedAction.userMustChooseFollowUpAction();
    }

    public List<Action> followUpActions() {
        return wrappedAction.followUpActions();
    }

    public boolean userTextAvailable() {
        return wrappedAction.userTextAvailable();
    }

    public String userText() {
        return wrappedAction.userText();
    }

    public String name() {
        return wrappedAction.name();
    }

    public ActionFactory actionFactory() {
        return wrappedAction.actionFactory();
    }

    public ActionHistory actionHistory() {
        return actionHistory;
    }

    public void setActionParameters( ActionParameters params ) {
        this.params = params;
    }

    public ActionParameters actionParameters() {
        return params;
    }
}
