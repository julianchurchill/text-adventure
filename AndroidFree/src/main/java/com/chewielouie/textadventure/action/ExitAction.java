package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.Exit;

public class ExitAction implements Action {
    private Exit exit;

    public ExitAction( Exit exit ) {
        this.exit = exit;
    }

    public String label() {
        return exit.label();
    }

    public void trigger() {
        // userText = "You examine the " + item.midSentenceCasedName() + ". " + item.description();
        // item.examine();
        // if( item.examineText().length() != 0 )
        //     userText = userText + "\n\n" + item.examineText();
    }

    public boolean userMustChooseFollowUpAction() {
        return false;
    }

    public List<Action> followUpActions() {
        return new ArrayList<Action>();
    }

    public String userText() {
        // return userText;
        return "";
    }

    public boolean userTextAvailable() {
        return false;
    }
}

