package com.chewielouie.textadventure;

public class PlainTextModelPopulator {
    private final String locationNameTag = "location_name:";
    private final String locationDescriptionTag = "location description:";
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
            model.addLocation(
                    locationFactory.create( extractName(), extractDescription() ) );
    }

    private boolean moreContentToParse() {
        return currentIndex != -1 && currentIndex < content.length();
    }

    private String extractName() {
        currentIndex = content.indexOf( locationNameTag, currentIndex );
        String name = "";
        if( currentIndex != -1 ) {
            int endOfValue = endOfValue();
            name = content.substring( currentIndex+locationNameTag.length(), endOfValue );
            currentIndex = endOfValue + 1;
        }
        return name;
    }

    private int endOfValue() {
        int endOfValue = content.indexOf( "\n", currentIndex );
        if( endOfValue == -1 )
            return content.length();
        return endOfValue;
    }

    private String extractDescription() {
        int testIndex = content.indexOf( locationDescriptionTag, currentIndex );
        String value = "";
        if( testIndex != -1 ) {
            currentIndex = testIndex;
            int endOfValue = endOfValue();
            value = content.substring( currentIndex+locationDescriptionTag.length(), endOfValue );
            currentIndex = endOfValue + 1;
        }
        return value;
    }
}

