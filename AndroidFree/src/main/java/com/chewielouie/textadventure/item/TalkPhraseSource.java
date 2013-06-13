package com.chewielouie.textadventure.item;

import java.util.List;

public interface TalkPhraseSource {
    public List<String> initialPhraseIds();
    public String shortPhraseById( String id );
    public String phraseById( String id );
    public String responseToPhraseById( String id );
    public List<String> followOnPhrasesIdsForPhraseById( String id );
    public void executeActionsForPhraseById( String id );
}

