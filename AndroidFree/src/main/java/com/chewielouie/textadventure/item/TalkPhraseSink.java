package com.chewielouie.textadventure.item;

import java.util.List;

public interface TalkPhraseSink {
    public void addInitialPhrase( String id, String phrase );
    public void addResponse( String id, String response );
}
