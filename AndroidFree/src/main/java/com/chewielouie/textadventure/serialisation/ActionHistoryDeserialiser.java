package com.chewielouie.textadventure.serialisation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionFactory;

public class ActionHistoryDeserialiser {
    private ActionFactory factory;
    private UserInventory inventory;
    private TextAdventureModel model;

    public ActionHistoryDeserialiser( ActionFactory factory,
                                      UserInventory inventory,
                                      TextAdventureModel model ) {
        this.factory = factory;
        this.inventory = inventory;
        this.model = model;
    }

    public List<Action> deserialise( String input ) {
        List<Action> actions = new ArrayList<Action>();
        if( factory != null ) {
            Scanner scanner = new Scanner( input );
            while( scanner.hasNextLine() )
                extractActionFromLine( scanner.nextLine(), actions );
        }
        return actions;
    }

    private void extractActionFromLine( String input, List<Action> actions ) {
        int current = input.indexOf( ":" );
        if( current != -1 ) {
            int startOfActionName = current + 1;
            int endOfActionName = input.indexOf( ":", startOfActionName );
            if( endOfActionName != -1 ) {
                String actionName = input.substring( startOfActionName,
                                                     endOfActionName );
                actions.add( factory.createShowInventoryAction( inventory, model ) );
            }
        }
    }
}
