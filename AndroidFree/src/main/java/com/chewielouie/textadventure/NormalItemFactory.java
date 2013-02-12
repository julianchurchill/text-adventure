package com.chewielouie.textadventure;

public class NormalItemFactory implements ItemFactory {

    public Item create() {
        return new NormalItem( "", "", "", "" );
    }

    public Item create( String name,
                String description,
                String countableNounPrefix,
                String midSentenceCasedName ) {
        return new NormalItem( name, description,
                countableNounPrefix, midSentenceCasedName );
    }
}

