package com.chewielouie.textadventure.action;

import com.chewielouie.textadventure.item.TalkPhraseSource;
import java.util.ArrayList;
import java.util.List;

public class SayAction implements Action {
    private String id;
    private TalkPhraseSource phraseSource;
    private ActionFactory actionFactory;

    public SayAction( String id, TalkPhraseSource phraseSource, ActionFactory factory ) {
        this.id = id;
        this.phraseSource = phraseSource;
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
            actions.add( actionFactory.createSayAction( followOnPhraseId, phraseSource ) );
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
