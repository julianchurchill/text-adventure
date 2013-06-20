package com.chewielouie.textadventure.serialisation;

import static com.chewielouie.textadventure.serialisation.ActionHistoryTextFormat.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionFactory;
import com.chewielouie.textadventure.item.Item;

public class ActionHistoryDeserialiser {
    private ActionFactory factory;
    private UserInventory inventory;
    private TextAdventureModel model;
    private String string;
    private Item item;
    private Item extraItem;
    private Exit exit;
    private ModelLocation location;

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

    private void createActionFromLine( String line, List<Action> actions ) {
        extractString( line );
        extractItem( line );
        extractExtraItem( line );
        extractExit( line );
        extractLocation( line );
        String actionName = findTagValue( line, ACTION_NAME_TAG );
        if( actionName != "" )
            actions.add( makeActionByName( actionName ) );
    }

    private void extractString( String line ) {
        string = findTagValue( line, STRING_TAG );
    }

    private void extractItem( String line ) {
        String id = findTagValue( line, ITEM_ID_TAG );
        if( id != "" )
            item = model.findItemByID( id );
        else
            item = null;
    }

    private void extractExtraItem( String line ) {
        String id = findTagValue( line, EXTRA_ITEM_ID_TAG );
        if( id != "" )
            extraItem = model.findItemByID( id );
        else
            extraItem = null;
    }

    private void extractExit( String line ) {
        String id = findTagValue( line, EXIT_ID_TAG );
        if( id != "" )
            exit = model.findExitByID( id );
        else
            exit = null;
    }

    private void extractLocation( String line ) {
        String id = findTagValue( line, LOCATION_ID_TAG );
        if( id != "" )
            location = model.findLocationByID( id );
        else
            location = null;
    }

    private String findTagValue( String line, String tag ) {
        int startOfTag = line.indexOf( tag );
        if( startOfTag != -1 ) {
            int startOfActionName = startOfTag + tag.length() + SEPERATOR.length();
            int endOfActionName = line.indexOf( SEPERATOR, startOfActionName );
            if( endOfActionName != -1 )
                return line.substring( startOfActionName, endOfActionName );
        }
        return "";
    }

    private Action makeActionByName( String actionName ) {
        Action action = null;
        if( actionName.equals( ACTION_NAME_EXAMINE_AN_ITEM ) )
            action = factory.createExamineAnItemAction( null );
        else if( actionName.equals( ACTION_NAME_EXAMINE ) )
            action = factory.createExamineAction( item );
        else if( actionName.equals( ACTION_NAME_EXIT ) )
            action = factory.createExitAction( exit, model );
        else if( actionName.equals( ACTION_NAME_INVENTORY_ITEM ) )
            action = factory.createInventoryItemAction( item, inventory, location );
        else if( actionName.equals( ACTION_NAME_SHOW_INVENTORY ) )
            action = factory.createShowInventoryAction( inventory, model );
        else if( actionName.equals( ACTION_NAME_TAKE_AN_ITEM ) )
            action = factory.createTakeAnItemAction( null, inventory, location );
        else if( actionName.equals( ACTION_NAME_TAKE_SPECIFIC_ITEM ) )
            action = factory.createTakeSpecificItemAction( item, inventory, location );
        else if( actionName.equals( ACTION_NAME_USE_WITH_SPECIFIC_ITEM ) )
            action = factory.createUseWithSpecificItemAction( item, extraItem );
        else if( actionName.equals( ACTION_NAME_USE_WITH ) )
            action = factory.createUseWithAction( item, inventory, location );
        else if( actionName.equals( ACTION_NAME_TALK_TO ) )
            action = factory.createTalkToAction( item );
        else if( actionName.equals( ACTION_NAME_SAY ) )
            action = factory.createSayAction( string, item );
        return action;
    }
}
