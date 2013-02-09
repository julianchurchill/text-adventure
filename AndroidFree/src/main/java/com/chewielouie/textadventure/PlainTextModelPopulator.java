package com.chewielouie.textadventure;

public class PlainTextModelPopulator {
    private final String locationNameTag = "location_name:";
    private int currentIndex = 0;
    private TextAdventureModel model;
    private ModelLocationFactory locationFactory;
    private String content;

    public PlainTextModelPopulator( TextAdventureModel model,
                                    ModelLocationFactory locationFactory,
                                    String content ) {
        this.model = model;
        this.locationFactory = locationFactory;
        this.content = content;
        while( moreContentToParse() )
            model.addLocation( locationFactory.create( extractName() ) );
    }

    private boolean moreContentToParse() {
        return currentIndex != -1 && currentIndex < content.length();
    }

    private String extractName() {
        currentIndex = content.indexOf( locationNameTag, currentIndex );
        int newlineIndex = content.indexOf( "\n", currentIndex );
        if( newlineIndex == -1 )
            newlineIndex = content.length();
        String name = "";
        if( currentIndex != -1 ) {
            name = content.substring( currentIndex+locationNameTag.length(), newlineIndex );
            currentIndex = currentIndex + 1;
        }
        return name;
    }
}

