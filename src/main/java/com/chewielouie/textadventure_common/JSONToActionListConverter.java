package com.chewielouie.textadventure_common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import android.content.Context;
import com.cedarsoftware.util.io.JsonReader;
import com.chewielouie.textadventure.BasicModel;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionFactory;
import com.chewielouie.textadventure.item.Item;

public class JSONToActionListConverter {
    private Context context;
    private String jsonFileName;
    private BasicModelConverter converter;
    private BasicModel jsonBasedModel;

    public JSONToActionListConverter( Context context, String jsonFileName,
                                      BasicModelConverter converter ) {
        this.context = context;
        this.jsonFileName = jsonFileName;
        this.converter = converter;
    }

    public BasicModel model() {
        return jsonBasedModel;
    }

    public List<Action> actions() {
        loadJSONModel();
        addMissingIDsToModel();
        if( jsonBasedModel != null && converter != null )
            return converter.inferActionsFrom( jsonBasedModel );
        return null;
    }

    private void loadJSONModel() {
        try {
            FileInputStream inputStream = context.openFileInput( jsonFileName );
            JsonReader jr = new JsonReader( inputStream );
            jsonBasedModel = (BasicModel) jr.readObject();
            jr.close();
        } catch( FileNotFoundException e ) {
            System.err.println("exception thrown: " + e.toString() );
        } catch( IOException e ) {
            System.err.println("exception thrown: " + e.toString() );
        }
    }

    private void addMissingIDsToModel() {
        if( jsonBasedModel != null ) {
            setItemIdForNamedItem( "unlocked door", "lockeddoor" );
            setItemIdForNamedItem( "locked door", "lockeddoor" );
            setItemIdForNamedItem( "Pocket lint", "pocketlint" );
            setItemIdForNamedItem( "Banana peel", "bananapeel" );
            setItemIdForNamedItem( "Dust of the Ancients", "dustoftheancients" );
            setItemIdForNamedItem( "Bags of junk", "bagsofjunk" );
        }
    }

    private void setItemIdForNamedItem( String name, String itemID ) {
        Item item = findItemWithName( name );
        if( item != null )
            item.setId( itemID );
    }

    private Item findItemWithName( String name ) {
        for( ModelLocation loc : jsonBasedModel.locations() )
            for( Item item : loc.items() )
                if( item.name().equals( name ) )
                    return item;
        for( Item item : jsonBasedModel.inventoryItems() )
            if( item.name().equals( name ) )
                return item;
        return null;
    }
}

