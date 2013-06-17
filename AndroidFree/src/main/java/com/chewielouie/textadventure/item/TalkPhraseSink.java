package com.chewielouie.textadventure.item;

import com.chewielouie.textadventure.itemaction.ItemAction;
import java.util.List;

public interface TalkPhraseSink {
    public void addInitialPhrase( String id, String phrase );
    public void addResponse( String id, String response );
    public void addFollowUpPhrase( String parentId, String newPhraseId, String phrase );
    public void addActionInResponseTo( String id, ItemAction action );
}
