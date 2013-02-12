package com.chewielouie.textadventure;

public interface ItemFactory {
    public Item create();
    public Item create( String name,
                String description,
                String countableNounPrefix,
                String midSentenceCasedName );
}

