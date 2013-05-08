package com.chewielouie.textadventure.action;

import java.util.List;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.ActionHistory;
import com.chewielouie.textadventure.Exit;

public class RecordableAction implements Action {
    private Action wrappedAction;
    private ActionHistory actionHistory;
    private Item item;
    private Item targetItem;
    private Exit exit;

    public RecordableAction( Action toWrap, ActionHistory actionHistory ) {
        this.wrappedAction = toWrap;
        this.actionHistory = actionHistory;
    }

    public String label() {
        return wrappedAction.label();
    }

    public void trigger() {
        if( actionHistory != null )
            actionHistory.addActionWithParameters( wrappedAction, item,
                                                   targetItem, exit );
        wrappedAction.trigger();
    }

    public boolean userMustChooseFollowUpAction() {
        return wrappedAction.userMustChooseFollowUpAction();
    }

    public List<Action> followUpActions() {
        return wrappedAction.followUpActions();
    }

    public boolean userTextAvailable() {
        return wrappedAction.userTextAvailable();
    }

    public String userText() {
        return wrappedAction.userText();
    }

    public ActionFactory actionFactory() {
        return wrappedAction.actionFactory();
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
