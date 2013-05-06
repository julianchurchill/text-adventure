package com.chewielouie.textadventure.action;

import java.util.List;

public class RecordableAction implements Action {
    public RecordableAction( Action toWrap ) {
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
}

