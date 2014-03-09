package com.chewielouie.textadventure.action;

import com.chewielouie.textadventure.Logger;
import java.util.List;

public class LoggableAction implements Action {
    private Action action;
    private Logger logger;

    public LoggableAction( Action action, Logger logger ) {
        this.action = action;
        this.logger = logger;
    }

    public String label() {
        return action.label();
    }

    public void trigger() {
        logger.log( "'" + action.name() + "' action enacted with label '" + action.label() + "'");
        action.trigger();
    }

    public boolean userMustChooseFollowUpAction() {
        return action.userMustChooseFollowUpAction();
    }

    public List<Action> followUpActions() {
        return action.followUpActions();
    }

    public boolean userTextAvailable() {
        return action.userTextAvailable();
    }

    public String userText() {
        return action.userText();
    }

    public ActionFactory actionFactory() {
        return action.actionFactory();
    }

    public String name() {
        return action.name();
    }
}
