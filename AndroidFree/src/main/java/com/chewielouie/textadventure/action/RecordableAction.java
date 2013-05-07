package com.chewielouie.textadventure.action;

import java.util.List;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.ActionHistory;
import com.chewielouie.textadventure.Exit;

public class RecordableAction implements Action {
    private ActionHistory actionHistory;
    private Item item;
    private Item targetItem;
    private Exit exit;

    public RecordableAction( Action toWrap ) {
    }

    public RecordableAction( Action toWrap, ActionHistory actionHistory ) {
        this.actionHistory = actionHistory;
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

    public ActionHistory actionHistory() {
        return actionHistory;
    }

    public void setItem( Item item ) {
        this.item = item;
    }

    public Item item() {
        return item;
    }

    public void setTargetItem( Item item ) {
        this.targetItem = item;
    }

    public Item targetItem() {
        return targetItem;
    }

    public void setExit( Exit exit ) {
        this.exit = exit;
    }

    public Exit exit() {
        return exit;
    }
}
