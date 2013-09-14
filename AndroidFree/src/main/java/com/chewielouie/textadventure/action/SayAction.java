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
    private List<Action> followUpActions;

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
        if( followUpActions == null ) {
            followUpActions = new ArrayList<Action>();
            for( String followOnPhraseId : phraseSource.followOnPhrasesIdsForPhraseById( id ) )
                followUpActions.add( actionFactory.createSayAction( followOnPhraseId, item ) );
        }
        return followUpActions;
    }

    public String userText() {
        return phraseSource.phraseById( id ) + getResponse();
    }

    private String getResponse() {
        if( phraseSource.responseToPhraseById( id ).length() != 0 )
            return "\n\n" + phraseSource.responseToPhraseById( id );
        return "";
    }

    public boolean userTextAvailable() {
        return true;
    }

    public String name() {
        return "say";
    }
}
