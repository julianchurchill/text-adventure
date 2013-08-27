package com.chewielouie.textadventure_common;

import com.chewielouie.textadventure.Logger;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.NullLogger;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionFactory;
import com.chewielouie.textadventure.item.Item;
import java.util.ArrayList;
import java.util.List;

public class BasicModelV1_0ToActionListConverter implements BasicModelConverter{
    private static final String KEY = "clocktowerskeletonkey";
    private static final String ENTRANCE = "townentrance";
    private static final String BANANA_PEEL = "bananapeel";
    private static final String DUST = "dustoftheancients";
    private static final String MAIN_STREET = "mainstreettown";
    private static final String SPADE = "spade";
    private static final String SHED = "smallshed";
    private static final String LOCKED_DOOR = "lockeddoor";
    private static final String OUT_BUILDINGS = "townoutbuildings";
    private static final String CLOCK_TOWER = "clocktower";
    private static final String MOUND = "moundofearth";
    private static final String BAGS_OF_JUNK = "bagsofjunk";
    private static final String CLOCK_FACE = "clockface";
    private static final String CLOCK_HOUR_HAND = "clockhourhand";
    private static final String CLOCK_MINUTE_HAND = "clockminutehand";
    private static final String MECHANISM = "clockmechanism";
    private static final String MECHANISM_WITH_FACE = "clockmechanismwithface";
    private static final String MECHANISM_WITH_HOUR_HAND = "clockmechanismwithfaceandhourhand";
    private static final String MECHANISM_WITH_MINUTE_HAND = "clockmechanismwithfaceandhourhandandminutehand";
    private static final String AXE_HEAD = "axehead";
    private static final String DIMLY_LIT_ANNEX = "dimlylitannex";
    private static final String EVEN_SMALLER_ANNEX = "evensmallerannex";
    private static final String WOODEN_POLE = "woodenpole";
    private static final String PILE_OF_STRAW = "pileofstraw";
    private static final String BLUNT_PICK_AXE = "bluntpickaxe";
    private static final String PICK_AXE = "pickaxe";
    private static final String MINE_SMITHY = "minesmithy";
    private static final String SHARPENING_WHEEL = "sharpeningwheel";
    private static final String MAP = "minemap";
    private static final String TABLE = "table";
    private static final String CANDLE_LIT_CHAMBER = "candlelitchamber";
    private static final String SHOP_KEEPER = "shopkeeper";
    private static final String SMALL_MINE_CHAMBER = "smallminechamber";
    private static final String ROCK = "rock";

    private TextAdventureModel oldModel;
    private TextAdventureModel newModel;
    private UserInventory inventory;
    private ActionFactory actionFactory;
    private List<Action> actions;
    private Logger logger = new NullLogger();

    public BasicModelV1_0ToActionListConverter( TextAdventureModel newModel,
                                                UserInventory inventory,
                                                ActionFactory factory ) {
        this.newModel = newModel;
        this.inventory = inventory;
        this.actionFactory = factory;
    }

    public BasicModelV1_0ToActionListConverter( TextAdventureModel newModel,
                                                UserInventory inventory,
                                                ActionFactory factory,
                                                Logger logger ) {
        this( newModel, inventory, factory );
        this.logger = logger;
    }

    public List<Action> inferActionsFrom( TextAdventureModel model ) {
        this.oldModel = model;
        actions = new ArrayList<Action>();

        analyseImmediatelyTakeableItems();
        addUseKeyActionIfDoorIsUnlocked();
        generateClockFaceLifetimeActions();
        generateClockHourHandLifetimeActions();
        generateClockMinuteHandLifetimeActions();
        generateAxeHeadLifetimeActions();
        generateWoodenPoleLifetimeActions();
        generateBluntPickAxeLifetimeActions();
        generatePickAxeLifetimeActions();
        generateMapLifetimeActions();
        generateShopKeeperLifetimeActions();

        return actions;
    }

    private void analyseImmediatelyTakeableItems() {
        addTakeActionIfItemHasBeenPickedUp( KEY, ENTRANCE );
        addTakeActionIfItemHasBeenPickedUp( BANANA_PEEL, ENTRANCE );
        addTakeActionIfItemHasBeenPickedUp( DUST, MAIN_STREET );
        addTakeActionIfItemHasBeenPickedUp( SPADE, SHED );
    }

    private void addTakeActionIfItemHasBeenPickedUp( String itemId, String locationId ) {
        if( itemIsInInventory( itemId ) )
            addTakeAction( itemId, locationId );
    }

    private void addUseKeyActionIfDoorIsUnlocked() {
        Item lockedDoor = findOldModelItem( LOCKED_DOOR );
        if( lockedDoor != null && lockedDoor.name().equals( "unlocked door" ) )
            addUseAction( LOCKED_DOOR, KEY );
    }

    private void generateClockFaceLifetimeActions() {
        if( itemIsInLocationOrInventory( CLOCK_FACE, OUT_BUILDINGS ) )
            addUseAction( MOUND, SPADE );

        if( itemIsInInventory( CLOCK_FACE ) )
            addTakeAction( CLOCK_FACE, OUT_BUILDINGS );

        Item mechanism = findOldModelItem( MECHANISM_WITH_FACE );
        if( mechanism != null && mechanism.visible() )
            addAllClockFaceLifetimeActions();
    }

    private void addAllClockFaceLifetimeActions() {
        addUseAction( MOUND, SPADE );
        addTakeAction( CLOCK_FACE, OUT_BUILDINGS );
        addUseAction( MECHANISM, CLOCK_FACE );
    }

    private void generateClockHourHandLifetimeActions() {
        if( itemIsInLocationOrInventory( CLOCK_HOUR_HAND, SHED ) )
            addExamineAction( BAGS_OF_JUNK );

        if( itemIsInInventory( CLOCK_HOUR_HAND ) )
            addTakeAction( CLOCK_HOUR_HAND, SHED );

        Item mechanism = oldModel.findItemByID( MECHANISM_WITH_HOUR_HAND );
        if( mechanism != null && mechanism.visible() ) {
            addAllClockFaceLifetimeActions();
            addAllClockHourHandLifetimeActions();
        }
    }

    private void addAllClockHourHandLifetimeActions() {
        addExamineAction( BAGS_OF_JUNK );
        addTakeAction( CLOCK_HOUR_HAND, SHED );
        addUseAction( MECHANISM_WITH_FACE, CLOCK_HOUR_HAND );
    }

    private void generateClockMinuteHandLifetimeActions() {
        if( itemIsInInventory( CLOCK_MINUTE_HAND ) )
            addTakeAction( CLOCK_MINUTE_HAND, CLOCK_TOWER );

        Item mechanism = findOldModelItem( MECHANISM_WITH_MINUTE_HAND );
        if( mechanism != null && mechanism.visible() ) {
            addAllClockFaceLifetimeActions();
            addAllClockHourHandLifetimeActions();
            addTakeAction( CLOCK_MINUTE_HAND, CLOCK_TOWER );
            addUseAction( MECHANISM_WITH_HOUR_HAND, CLOCK_MINUTE_HAND );
        }
    }

    private void generateAxeHeadLifetimeActions() {
        Item axeHead = findOldModelItem( AXE_HEAD );
        if( axeHead != null && axeHead.name().equals( "Axe head" ) )
            addExamineAction( AXE_HEAD );
    }

    private void generateWoodenPoleLifetimeActions() {
        if( itemIsInLocationOrInventory( WOODEN_POLE, EVEN_SMALLER_ANNEX ) )
            addExamineAction( PILE_OF_STRAW );

        if( itemIsInInventory( WOODEN_POLE ) )
            addTakeAction( WOODEN_POLE, EVEN_SMALLER_ANNEX );
    }

    private void generateBluntPickAxeLifetimeActions() {
        if( itemIsInLocationOrInventory( BLUNT_PICK_AXE, DIMLY_LIT_ANNEX ) ) {
            addExamineAction( AXE_HEAD );
            addExamineAction( PILE_OF_STRAW );
            addTakeAction( WOODEN_POLE, EVEN_SMALLER_ANNEX );
            addUseAction( AXE_HEAD, WOODEN_POLE );
        }

        if( itemIsInInventory( BLUNT_PICK_AXE ) )
            addTakeAction( BLUNT_PICK_AXE, DIMLY_LIT_ANNEX );
    }

    private void generatePickAxeLifetimeActions() {
        if( itemIsInLocationOrInventory( PICK_AXE, MINE_SMITHY ) ) {
            addExamineAction( AXE_HEAD );
            addExamineAction( PILE_OF_STRAW );
            addTakeAction( WOODEN_POLE, EVEN_SMALLER_ANNEX );
            addUseAction( AXE_HEAD, WOODEN_POLE );
            addTakeAction( BLUNT_PICK_AXE, DIMLY_LIT_ANNEX );
            addUseAction( SHARPENING_WHEEL, BLUNT_PICK_AXE );
        }

        if( itemIsInInventory( PICK_AXE ) )
            addTakeAction( PICK_AXE, MINE_SMITHY );
    }

    private void generateMapLifetimeActions() {
        if( itemIsInLocationOrInventory( MAP, CANDLE_LIT_CHAMBER ) )
            addExamineAction( TABLE );

        if( itemIsInInventory( MAP ) )
            addTakeAction( MAP, CANDLE_LIT_CHAMBER );
    }

    private void generateShopKeeperLifetimeActions() {
        if( itemIsInLocation( SHOP_KEEPER, SMALL_MINE_CHAMBER ) )
            addUseAction( ROCK, PICK_AXE );

        if( oldModel.currentScore() == 2 ) {
            addUseAction( ROCK, PICK_AXE );
            addUseAction( SHOP_KEEPER, MAP );
        }
    }

    private void addTakeAction( String itemId, String locationId ) {
        logger.log( "adding take action for item '" + itemId + "' locationId '" +
                    locationId + "'" );
        actions.add( actionFactory.createTakeSpecificItemAction(
                                        findNewModelItem( itemId ),
                                        inventory,
                                        findNewModelLocation( locationId ) ) );
    }

    private void addUseAction( String actionOwnerItemID, String otherItemID ) {
        logger.log( "adding use action for owner item '" + actionOwnerItemID +
                    "' otherItemID '" + otherItemID + "'" );
        actions.add( actionFactory.createUseWithSpecificItemAction(
                                        findNewModelItem( otherItemID ),
                                        findNewModelItem( actionOwnerItemID ) ) );
    }

    private void addExamineAction( String itemID ) {
        logger.log( "adding examine action for item '" + itemID + "'" );
        actions.add( actionFactory.createExamineAction( findNewModelItem( itemID ) ) );
    }

    private Item findNewModelItem( String id ) {
        if( newModel.findItemByID( id ) == null )
            logger.log( "item not found in new model '" + id + "'" );
        return newModel.findItemByID( id );
    }

    private ModelLocation findNewModelLocation( String id ) {
        if( newModel.findLocationByID( id ) == null )
            logger.log( "location not found in new model '" + id + "'" );
        return newModel.findLocationByID( id );
    }

    private Item findOldModelItem( String id ) {
        if( oldModel.findItemByID( id ) == null )
            logger.log( "item not found in old model '" + id + "'" );
        return oldModel.findItemByID( id );
    }

    private boolean itemIsInInventory( String id ) {
        for( Item item : oldModel.inventoryItems() )
            if( item.id().equals( id ) )
                return true;
        return false;
    }

    private boolean itemIsInLocation( String itemID, String locationID ) {
        Item item = findOldModelItem( itemID );
        ModelLocation location = oldModel.findLocationByID( locationID );
        if( location == null )
            logger.log( "location not found in old model '" + locationID + "'" );
        if( location != null && item != null
            && location.items().contains( item ) && item.visible() )
            return true;
        return false;
    }

    private boolean itemIsInLocationOrInventory( String itemId, String locationId ) {
        return itemIsInLocation( itemId, locationId ) ||
               itemIsInInventory( itemId );
   }
}

