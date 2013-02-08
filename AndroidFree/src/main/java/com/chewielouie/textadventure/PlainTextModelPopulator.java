package com.chewielouie.textadventure;

public class PlainTextModelPopulator {
    public PlainTextModelPopulator( TextAdventureModel model,
                                    ModelLocationFactory locationFactory,
                                    String content ) {
        model.addLocation( locationFactory.create( "name" ) );
    }
}

