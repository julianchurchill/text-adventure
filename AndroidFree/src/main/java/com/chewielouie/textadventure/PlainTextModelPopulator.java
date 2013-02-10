package com.chewielouie.textadventure;

public class PlainTextModelPopulator {
    private final String locationNameTag = "location_name:";
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
}

