package com.chewielouie.textadventure.action;

import com.chewielouie.textadventure.item.Item;
import java.util.ArrayList;
import java.util.List;

public class TalkToAction implements Action {
    private Item item;
    private ActionFactory actionFactory;
    private List<Action> followUpActions;

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
        if( followUpActions == null ) {
            followUpActions = new ArrayList<Action>();
            if( item.getTalkPhraseSource() != null )
                for( String id : item.getTalkPhraseSource().initialPhraseIds() )
                    followUpActions.add( actionFactory.createSayAction( id, item ) );
        }
        return followUpActions;
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
