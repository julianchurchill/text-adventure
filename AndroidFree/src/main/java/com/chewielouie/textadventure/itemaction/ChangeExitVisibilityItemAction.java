package com.chewielouie.textadventure.itemaction;

import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.Exit;
import java.util.ArrayList;
import java.util.List;

public class ChangeExitVisibilityItemAction implements ItemAction {
    private TextAdventureModel model;
    private String exitID = "";
    private boolean visibility = true;

    public ChangeExitVisibilityItemAction( String arguments, TextAdventureModel model ) {
        this.model = model;
        if( arguments != null ) {
            this.exitID = extractExitID( arguments );
            this.visibility = extractVisibility( arguments );
        }
    }

    private String extractExitID( String arguments ) {
        int endOfID = arguments.indexOf( ":" );
        if( endOfID != -1 )
            return arguments.substring( 0, endOfID );
        return arguments;
    }

    private boolean extractVisibility( String arguments ) {
        int endOfID = arguments.indexOf( ":" );
        if( endOfID != -1 && (endOfID+1) < arguments.length() ) {
            String visibilityString = arguments.substring( endOfID+1 );
            if( visibilityString.equals( "invisible" ) )
                return false;
        }
        return true;
    }

    public TextAdventureModel model() {
        return model;
    }

    public void enact() {
        Exit exit = model.findExitByID( exitID );
        if( exit != null )
            if( visibility )
                exit.setVisible();
            else
                exit.setInvisible();
    }

    public String name() {
        return "change exit visibility";
    }

    public List<String> arguments() {
        List<String> args = new ArrayList<String>();
        args.add( this.exitID );
        if( visibility )
            args.add( "visible" );
        else
            args.add( "invisible" );
        return args;
    }
}
