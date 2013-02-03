package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.Item;

public class Examine implements Action {
    private Item item = null;

    public Examine( Item item ) {
        this.item = item;
    }

    public String label() {
        return "Examine " + item.name();
    }

    public void trigger() {
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
        return item.description();
    }

    public boolean userTextAvailable() {
        return true;
    }
}

