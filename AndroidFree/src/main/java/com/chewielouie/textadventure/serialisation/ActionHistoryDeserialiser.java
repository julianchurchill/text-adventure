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
                createActionFromLine( scanner.nextLine(), actions );
        }
        return actions;
    }

    private void createActionFromLine( String input, List<Action> actions ) {
        int current = input.indexOf( ":" );
        if( current != -1 ) {
            int startOfActionName = current + 1;
            int endOfActionName = input.indexOf( ":", startOfActionName );
            if( endOfActionName != -1 ) {
                String actionName = input.substring( startOfActionName,
                                                     endOfActionName );
                actions.add( makeActionByName( actionName ) );
            }
        }
    }

    private Action makeActionByName( String actionName ) {
        Action action = null;
        if( actionName.equals( "examine an item" ) )
            action = factory.createExamineAnItemAction( null );
        else if( actionName.equals( "examine" ) )
            action = factory.createExamineAction( null );
        else if( actionName.equals( "exit" ) )
            action = factory.createExitAction( null, model );
        else if( actionName.equals( "inventory item" ) )
            action = factory.createInventoryItemAction( null, inventory, null );
        else if( actionName.equals( "show inventory" ) )
            action = factory.createShowInventoryAction( inventory, model );
        else if( actionName.equals( "take an item" ) )
            action = factory.createTakeAnItemAction( null, inventory, null );
        else if( actionName.equals( "take specific item" ) )
            action = factory.createTakeSpecificItemAction( null, inventory, null );
        else if( actionName.equals( "use with specific item" ) )
            action = factory.createUseWithSpecificItemAction( null, null );
        else if( actionName.equals( "use with" ) )
            action = factory.createUseWithAction( null, inventory, null );
        return action;
    }
}
