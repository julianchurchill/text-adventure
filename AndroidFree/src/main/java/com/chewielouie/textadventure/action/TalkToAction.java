package com.chewielouie.textadventure.action;

import com.chewielouie.textadventure.item.Item;
import java.util.ArrayList;
import java.util.List;

public class TalkToAction implements Action {
    private Item item;
    private ActionFactory actionFactory;

    public TalkToAction( Item item, ActionFactory factory ) {
        this.item = item;
        this.actionFactory = factory;
    }

    public ActionFactory actionFactory() {
        return actionFactory;
    }

    public String label() {
        return "Talk to " + item.midSentenceCasedName();
    }

    public void trigger() {
    }

    public boolean userMustChooseFollowUpAction() {
        return true;
    }

    public List<Action> followUpActions() {
        List<Action> actions = new ArrayList<Action>();
        if( item.getTalkable() != null )
            for( String id : item.getTalkable().initialPhraseIds() )
                actions.add( actionFactory.createSayAction( id, item.getTalkable() ) );
        return actions;
    }

    public String userText() {
        return "";
    }

    public boolean userTextAvailable() {
        return false;
    }

    public String name() {
        return "talk";
    }
}
