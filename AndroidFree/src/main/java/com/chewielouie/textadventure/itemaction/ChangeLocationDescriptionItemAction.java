package com.chewielouie.textadventure.itemaction;

import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.ModelLocation;
import java.util.ArrayList;
import java.util.List;

public class ChangeLocationDescriptionItemAction implements ItemAction {
    private TextAdventureModel model;
    private String locationID = "";
    private String newDescription = "";

    public ChangeLocationDescriptionItemAction( String arguments,
                                                TextAdventureModel model ) {
        this.model = model;
        if( arguments != null ) {
            extractLocationID( arguments );
            extractDescription( arguments );
        }
    }

    private void extractLocationID( String arguments ) {
        int endOfID = arguments.indexOf( ":" );
        if( endOfID != -1 )
            locationID = arguments.substring( 0, endOfID );
    }

    private void extractDescription( String arguments ) {
        int endOfID = arguments.indexOf( ":" );
        if( endOfID != -1 && (endOfID+1) < arguments.length() )
            newDescription = arguments.substring( endOfID+1 ) + "\n";
    }

    public TextAdventureModel model() {
        return model;
    }

    public void enact() {
        for( ModelLocation loc : model.locations() ) {
            if( loc.id().equals( locationID ) ) {
                loc.setLocationDescription( newDescription );
                break;
            }
        }
    }

    public String name() {
        return "change location description";
    }

    public List<String> arguments() {
        List<String> args = new ArrayList<String>();
        args.add( this.locationID );
        args.add( this.newDescription );
        return args;
    }
}




