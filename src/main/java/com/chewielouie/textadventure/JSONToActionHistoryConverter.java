package com.chewielouie.textadventure;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.content.Context;
import com.cedarsoftware.util.io.JsonReader;
import com.chewielouie.textadventure.action.ActionHistory;

public class JSONToActionHistoryConverter {
    private Context context;
    private String jsonFileName;

    public JSONToActionHistoryConverter( Context context, String jsonFileName ) {
        this.context = context;
        this.jsonFileName = jsonFileName;
    }

    public ActionHistory convert() {
        BasicModel jsonBasedModel = loadJSONModel();

        if( jsonBasedModel != null ) {
            // inspect the state of the model and fill in the action history with guesses
                // 1. Figure out what has been picked up
                    // a. if skeleton key is in inventory do 'take specific item:clocktowerskeletonkey:townentrance'
                    // b... if xxx is in inventory do 'take specific item:item id:location id'
                // 2. Figure out what has been used
                // 3. Figure out what has been examined
                // 4. Figure out where the player is and get them there by using exits
        }
        return null;
    }

    private BasicModel loadJSONModel() {
        BasicModel jsonBasedModel = new BasicModel();
        try {
            FileInputStream inputStream = context.openFileInput( jsonFileName );
            JsonReader jr = new JsonReader( inputStream );
            jsonBasedModel = (BasicModel) jr.readObject();
            jr.close();
            return jsonBasedModel;
        } catch( FileNotFoundException e ) {
            System.err.println("exception thrown: " + e.toString() );
        } catch( IOException e ) {
            System.err.println("exception thrown: " + e.toString() );
        }
        return null;
    }
}

