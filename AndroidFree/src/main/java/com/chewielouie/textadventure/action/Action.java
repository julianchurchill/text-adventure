package com.chewielouie.textadventure.action;

import java.util.List;

public interface Action {
    public String label();
    public void trigger();
    public boolean userMustChooseFollowUpAction();
    public List<Action> followUpActions();
    public boolean userTextAvailable();
    public String userText();
    public ActionFactory actionFactory();
    public String name();
}

