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
            BasicModelV1_0ToActionHistoryConverter converter =
                new BasicModelV1_0ToActionHistoryConverter( jsonBasedModel );
            return converter.actionHistory();
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

