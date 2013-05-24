package com.chewielouie.textadventure;

import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionFactory;
import com.chewielouie.textadventure.item.Item;
import java.util.ArrayList;
import java.util.List;

public class BasicModelV1_0ToActionListConverter {
    private static final String KEY = "clocktowerskeletonkey";
    private static final String ENTRANCE = "townentrance";
    private static final String BANANA_PEEL = "bananapeel";
    private static final String DUST = "dustoftheancients";
    private static final String MAIN_STREET = "mainstreettown";
    private static final String SPADE = "spade";
    private static final String SHED = "smallshed";
    private static final String LOCKED_DOOR = "lockeddoor";
    private static final String OUT_BUILDINGS = "townoutbuildings";
    private static final String MOUND = "moundofearth";
    private static final String BAGS_OF_JUNK = "bagsofjunk";
    private static final String CLOCK_FACE = "clockface";
    private static final String CLOCK_HOUR_HAND = "clockhourhand";
    private static final String MECHANISM = "clockmechanism";
    private static final String MECHANISM_WITH_FACE = "clockmechanismwithface";
    private static final String MECHANISM_WITH_HOUR_HAND = "clockmechanismwithfaceandhourhand";

    private TextAdventureModel oldModel;
    private TextAdventureModel newModel;
    private UserInventory inventory;
    private ActionFactory actionFactory;
    private List<Action> actions;

    public BasicModelV1_0ToActionListConverter( TextAdventureModel oldModel,
                TextAdventureModel newModel, UserInventory inventory,
                ActionFactory factory ) {
        this.oldModel = oldModel;
        this.newModel = newModel;
        this.inventory = inventory;
        this.actionFactory = factory;
    }

    public List<Action> actions() {
        // inspect the state of the model and fill in the action history with guesses
            // 1. Figure out what has been picked up
                // a. if skeleton key is in inventory do 'take specific item:clocktowerskeletonkey:townentrance'
                // b... if xxx is in inventory do 'take specific item:item id:location id'
            // 2. Figure out what has been used
            // 3. Figure out what has been examined
            // 4. Figure out where the player is and get them there by using exits
        actions = new ArrayList<Action>();

        analyseImmediatelyTakeableItems();
        addUseKeyActionIfDoorIsUnlocked();
        generateClockFaceLifetimeActions();
        generateClockHourHandLifetimeActions();

        return actions;
    }

    private void analyseImmediatelyTakeableItems() {
        addTakeActionIfItemHasBeenPickedUp( KEY, ENTRANCE );
        addTakeActionIfItemHasBeenPickedUp( BANANA_PEEL, ENTRANCE );
        addTakeActionIfItemHasBeenPickedUp( DUST, MAIN_STREET );
        addTakeActionIfItemHasBeenPickedUp( SPADE, SHED );
    }

    private void addTakeActionIfItemHasBeenPickedUp( String itemId, String locationId ) {
        if( itemIsInOldInventory( itemId ) )
            addTakeAction( itemId, locationId );
    }

    private void addUseKeyActionIfDoorIsUnlocked() {
        Item lockedDoor = oldModel.findItemByID( LOCKED_DOOR );
        if( lockedDoor != null && lockedDoor.name().equals( "unlocked door" ) )
            addUseAction( LOCKED_DOOR, KEY );
    }

    private void generateClockFaceLifetimeActions() {
        if( itemIsInLocation( CLOCK_FACE, OUT_BUILDINGS ) )
            addUseAction( MOUND, SPADE );

        if( itemIsInOldInventory( CLOCK_FACE ) ) {
            addUseAction( MOUND, SPADE );
            addTakeAction( CLOCK_FACE, OUT_BUILDINGS );
        }

        Item mechanism = oldModel.findItemByID( MECHANISM_WITH_FACE );
        if( mechanism != null && mechanism.visible() ) {
            addUseAction( MOUND, SPADE );
            addTakeAction( CLOCK_FACE, OUT_BUILDINGS );
            addUseAction( MECHANISM, CLOCK_FACE );
        }
    }

    private void generateClockHourHandLifetimeActions() {
        if( itemIsInLocation( CLOCK_HOUR_HAND, SHED ) )
            addExamineAction( BAGS_OF_JUNK );

        if( itemIsInOldInventory( CLOCK_HOUR_HAND ) ) {
            addExamineAction( BAGS_OF_JUNK );
            addTakeAction( CLOCK_HOUR_HAND, SHED );
        }

        Item mechanism = oldModel.findItemByID( MECHANISM_WITH_HOUR_HAND );
        if( mechanism != null && mechanism.visible() ) {
            addExamineAction( BAGS_OF_JUNK );
            addTakeAction( CLOCK_HOUR_HAND, SHED );
            addUseAction( MECHANISM_WITH_FACE, CLOCK_HOUR_HAND );
        }
    }

    private void addTakeAction( String itemId, String locationId ) {
        actions.add( actionFactory.createTakeSpecificItemAction(
                                        findNewModelItem( itemId ),
                                        inventory,
                                        findNewModelLocation( locationId ) ) );
    }

    private void addUseAction( String actionOwnerItemID, String targetItemID ) {
        actions.add( actionFactory.createUseWithSpecificItemAction(
                                        findNewModelItem( actionOwnerItemID ),
                                        findNewModelItem( targetItemID ) ) );
    }

    private void addExamineAction( String itemID ) {
        actions.add( actionFactory.createExamineAction( findNewModelItem( itemID ) ) );
    }

    private Item findNewModelItem( String id ) {
        return newModel.findItemByID( id );
    }

    private ModelLocation findNewModelLocation( String id ) {
        return newModel.findLocationByID( id );
    }

    private boolean itemIsInOldInventory( String id ) {
        for( Item item : oldModel.inventoryItems() )
            if( item.id().equals( id ) )
                return true;
        return false;
    }

    private boolean itemIsInLocation( String itemID, String locationID ) {
        Item item = oldModel.findItemByID( itemID );
        ModelLocation location = oldModel.findLocationByID( locationID );
        if( location != null && item != null && location.items().contains( item ) )
            return true;
        return false;
    }
}

