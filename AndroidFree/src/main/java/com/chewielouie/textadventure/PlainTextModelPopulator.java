package com.chewielouie.textadventure;

public class PlainTextModelPopulator {
    private final String locationNameTag = "location_name:";
    private final String locationDescriptionTag = "location description:";
    private int nextCharToParseIndex = 0;
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
        return nextCharToParseIndex < content.length();
    }

    private String extractName() {
        return extractValue( locationNameTag );
    }

    private String extractDescription() {
        return extractValue( locationDescriptionTag );
    }

    private String extractValue( String tag ) {
        int testIndex = content.indexOf( tag, nextCharToParseIndex );
        String value = "";
        if( testIndex != -1 ) {
            nextCharToParseIndex = testIndex;
            int endOfValue = endOfValue();
            value = content.substring( nextCharToParseIndex+tag.length(), endOfValue );
            nextCharToParseIndex = endOfValue + 1;
        }
        return value;
    }

    private int endOfValue() {
        int endOfValue = content.indexOf( "\n", nextCharToParseIndex );
        if( endOfValue == -1 )
            return content.length();
        return endOfValue;
    }
}

