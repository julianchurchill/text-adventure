package com.chewielouie.textadventure.action;

import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.TalkPhraseSource;
import java.util.ArrayList;
import java.util.List;

public class SayAction implements Action {
    private String id;
    private TalkPhraseSource phraseSource;
    private ActionFactory actionFactory;
    private Item item;

    public SayAction( String id, Item item, ActionFactory factory ) {
        this.id = id;
        this.item = item;
        if( item != null )
            this.phraseSource = item.getTalkPhraseSource();
        this.actionFactory = factory;
    }

    public ActionFactory actionFactory() {
        return actionFactory;
    }

    public String label() {
        return "Say \"" + phraseSource.shortPhraseById( id ) + "\"";
    }

    public void trigger() {
        phraseSource.executeActionsForPhraseById( id );
    }

    public boolean userMustChooseFollowUpAction() {
        return phraseSource.followOnPhrasesIdsForPhraseById( id ).size() > 0;
    }

    public List<Action> followUpActions() {
        List<Action> actions = new ArrayList<Action>();
        for( String followOnPhraseId : phraseSource.followOnPhrasesIdsForPhraseById( id ) )
            actions.add( actionFactory.createSayAction( followOnPhraseId, item ) );
        return actions;
    }

    public String userText() {
        return phraseSource.phraseById( id ) + "\n" + getResponse();
    }

    private String getResponse() {
        if( phraseSource.responseToPhraseById( id ).length() != 0 )
            return "\n" + phraseSource.responseToPhraseById( id ) + "\n";
        return "";
    }

    public boolean userTextAvailable() {
        return true;
    }

    public String name() {
        return "say";
    }
}
