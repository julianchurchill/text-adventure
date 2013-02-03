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

    public Item item() {
        return item;
    }
}

