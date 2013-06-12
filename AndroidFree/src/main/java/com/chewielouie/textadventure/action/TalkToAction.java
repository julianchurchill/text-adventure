package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;

public class TalkToAction implements Action {
    public TalkToAction() {
    }

    public ActionFactory actionFactory() {
        return null;
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
        return new ArrayList<Action>();
    }

    public String userText() {
        return "";
    }

    public boolean userTextAvailable() {
        return false;
    }

    public String name() {
        return "talk";
    }

    public String phraseText() {
        return "";
    }
}
