package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.TextAdventureModel;

public class ExitAction implements Action {
    private Exit exit;
    private TextAdventureModel model;

    public ExitAction( Exit exit, TextAdventureModel model ) {
        this.exit = exit;
        this.model = model;
    }

    public ActionFactory actionFactory() {
        return null;
    }

    public String label() {
        return exit.label();
    }

    public void trigger() {
        if( model != null )
            model.moveThroughExit( exit );
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
        return "exit";
    }
}

