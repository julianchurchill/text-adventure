package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.item.Item;

public class Examine implements Action {
    private Item item = null;

    public Examine( Item item ) {
        this.item = item;
    }

    public String label() {
        return "Examine " + item.midSentenceCasedName();
    }

    public void trigger() {
        item.examine();
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
        String text = "You examine the " + item.midSentenceCasedName() + ". " + item.description();
        if( item.examineText().length() != 0 )
            text = text + "\n\n" + item.examineText();
        return text;
    }

    public boolean userTextAvailable() {
        return true;
    }
}

