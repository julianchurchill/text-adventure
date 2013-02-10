package com.chewielouie.textadventure;

public class PlainTextModelPopulator {
    private final String locationNameTag = "location_name:";
    //private final String locationDescriptionTag = "location description:";
    private int nextLocation = 0;
    private TextAdventureModel model;
    private ModelLocationFactory locationFactory;
    private String content;

    public PlainTextModelPopulator( TextAdventureModel model,
                                    ModelLocationFactory locationFactory,
                                    String content ) {
        this.model = model;
        this.locationFactory = locationFactory;
        this.content = content;
        while( moreContentToParse() ) {
            ModelLocation l = locationFactory.create();
            l.deserialise( extractContent() );
            model.addLocation( l );
        }

    }

    private boolean moreContentToParse() {
        return nextLocation < content.length();
    }

    private String extractContent() {
        String locationContent = "";
        int locationStart = content.indexOf( locationNameTag, nextLocation );
        if( locationStart != -1 ) {
            nextLocation = content.indexOf( locationNameTag, locationStart+1 );
            if( nextLocation == -1 )
                nextLocation = content.length();

            locationContent = content.substring( locationStart, nextLocation );
        }
        return locationContent;
    }

    //private String extractName() {
        //return extractValue( locationNameTag );
    //}

    //private String extractDescription() {
        //return extractValue( locationDescriptionTag );
    //}

    //private String extractValue( String tag ) {
        //int testIndex = content.indexOf( tag, nextLocation );
        //String value = "";
        //if( testIndex != -1 ) {
            //nextLocation = testIndex;
            //int endOfValue = endOfValue();
            //value = content.substring( nextLocation+tag.length(), endOfValue );
            //nextLocation = endOfValue + 1;
        //}
        //return value;
    //}

    //private int endOfValue() {
        //int endOfValue = content.indexOf( "\n", nextLocation );
        //if( endOfValue == -1 )
            //return content.length();
        //return endOfValue;
    //}
}

