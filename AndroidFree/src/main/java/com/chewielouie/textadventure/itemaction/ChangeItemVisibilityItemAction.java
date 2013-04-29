package com.chewielouie.textadventure.itemaction;

import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.item.Item;
import java.util.ArrayList;
import java.util.List;

public class ChangeItemVisibilityItemAction implements ItemAction {
    private TextAdventureModel model;
    private String itemID = "";
    private boolean visibility = true;

    public ChangeItemVisibilityItemAction( String arguments, TextAdventureModel model ) {
        this.model = model;
        if( arguments != null ) {
            this.itemID = extractItemID( arguments );
            this.visibility = extractVisibility( arguments );
        }
    }

    private String extractItemID( String arguments ) {
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
        Item item = model.findItemByID( itemID );
        if( item != null )
            item.setVisible( visibility );
    }

    public String name() {
        return "change item visibility";
    }

    public List<String> arguments() {
        List<String> args = new ArrayList<String>();
        args.add( this.itemID );
        if( this.visibility )
            args.add( "visible" );
        else
            args.add( "invisible" );
        return args;
    }
}


