package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.item.Item;

public class Examine implements Action {
    private Item item = null;
    private String userText = "";

    public Examine( Item item ) {
        this.item = item;
    }

    public ActionFactory actionFactory() {
        return null;
    }

    public String label() {
        return "Examine " + item.midSentenceCasedName();
    }

    public void trigger() {
        userText = "You examine the " + item.midSentenceCasedName() + ". " + item.description();
        item.examine();
        if( item.examineText().length() != 0 )
            userText = userText + "\n\n" + item.examineText();
    }

    public boolean userMustChooseFollowUpAction() {
        return false;
    }

    public List<Action> followUpActions() {
        return new ArrayList<Action>();
    }

    public Item item() {
        return item;
    }

    public String userText() {
        return userText;
    }

    public boolean userTextAvailable() {
        return true;
    }
}

