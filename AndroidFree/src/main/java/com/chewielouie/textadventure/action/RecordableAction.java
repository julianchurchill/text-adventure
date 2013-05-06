package com.chewielouie.textadventure.action;

import java.util.List;
import com.chewielouie.textadventure.ActionHistory;

public class RecordableAction implements Action {
    private ActionHistory actionHistory;

    public RecordableAction( Action toWrap ) {
    }

    public RecordableAction( Action toWrap, ActionHistory actionHistory ) {
        this.actionHistory = actionHistory;
    }

    public String label() {
        return "";
    }

    public void trigger() {
    }

    public boolean userMustChooseFollowUpAction() {
        return false;
    }

    public List<Action> followUpActions() {
        return null;
    }

    public boolean userTextAvailable() {
        return false;
    }

    public String userText() {
        return "";
    }

    public ActionFactory actionFactory() {
        return null;
    }

    public ActionHistory actionHistory() {
        return actionHistory;
    }
}
