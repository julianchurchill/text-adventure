package com.chewielouie.textadventure;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import android.content.Context;
import com.cedarsoftware.util.io.JsonReader;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionFactory;

public class JSONToActionListConverter {
    private Context context;
    private String jsonFileName;
    private BasicModelConverter converter;

    public JSONToActionListConverter( Context context, String jsonFileName,
                                      BasicModelConverter converter ) {
        this.context = context;
        this.jsonFileName = jsonFileName;
        this.converter = converter;
    }

    public List<Action> actions() {
        BasicModel jsonBasedModel = loadJSONModel();
        if( jsonBasedModel != null && converter != null )
            return converter.inferActionsFrom( jsonBasedModel );
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

