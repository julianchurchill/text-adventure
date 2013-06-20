package com.chewielouie.textadventure.serialisation;

import static com.chewielouie.textadventure.serialisation.ActionHistoryTextFormat.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionFactory;
import com.chewielouie.textadventure.action.ActionParameters;
import com.chewielouie.textadventure.item.Item;
import java.util.List;
import org.junit.Test;
import org.mockito.Mockito;

public class ActionHistoryDeserialiserTests {

    @Test
    public void returns_empty_list_of_actions_for_empty_input() {
        List<Action> actions =
            new ActionHistoryDeserialiser( null, null, null ).deserialise( "" );
        assertThat( actions.size(), is( 0 ) );
    }

    @Test
    public void adds_deserialised_actions_to_list_in_order() {
        Action action1 = mock( Action.class );
        Action action2 = mock( Action.class );
        ActionFactory factory = mock( ActionFactory.class );
        when( factory.createExamineAnItemAction( Mockito.anyListOf( Item.class ) ) )
            .thenReturn( action1, action2 );

        List<Action> actions =
            new ActionHistoryDeserialiser( factory, null, null ).deserialise(
            ACTION_NAME_TAG + SEPERATOR + "examine an item" + SEPERATOR + "\n" +
            ACTION_NAME_TAG + SEPERATOR + "examine an item" + SEPERATOR + "\n" );

        assertThat( actions.size(), is( 2 ) );
        assertThat( actions.get( 0 ), is( action1 ) );
        assertThat( actions.get( 1 ), is( action2 ) );
    }

    @Test
    public void deserialises_ExamineAnItem_action_type() {
        ActionFactory factory = mock( ActionFactory.class );
        new ActionHistoryDeserialiser( factory, null, null ).deserialise(
            ACTION_NAME_TAG + SEPERATOR + "examine an item" + SEPERATOR + "\n" );

        verify( factory ).createExamineAnItemAction( Mockito.anyListOf( Item.class ) );
    }

    @Test
    public void deserialises_Examine_action_type_using_factory() {
        ActionFactory factory = mock( ActionFactory.class );
        TextAdventureModel model = mock( TextAdventureModel.class );
        Item item = mock( Item.class );
        when( model.findItemByID( "itemid" ) ).thenReturn( item );

        new ActionHistoryDeserialiser( factory, null, model ).deserialise(
            ACTION_NAME_TAG + SEPERATOR + "examine" + SEPERATOR +
            ITEM_ID_TAG + SEPERATOR + "itemid" + SEPERATOR + "\n" );

        verify( model ).findItemByID( "itemid" );
        verify( factory ).createExamineAction( item );
    }

    @Test
    public void deserialises_Exit_action_type_using_factory() {
        ActionFactory factory = mock( ActionFactory.class );
        TextAdventureModel model = mock( TextAdventureModel.class );
        Exit exit = mock( Exit.class );
        when( model.findExitByID( "exitid" ) ).thenReturn( exit );

        new ActionHistoryDeserialiser( factory, null, model ).deserialise(
            ACTION_NAME_TAG + SEPERATOR + "exit" + SEPERATOR +
            EXIT_ID_TAG + SEPERATOR + "exitid" + SEPERATOR + "\n" );

        verify( model ).findExitByID( "exitid" );
        verify( factory ).createExitAction( exit, model );
    }

    @Test
    public void deserialises_InventoryItem_action_type_using_factory() {
        ActionFactory factory = mock( ActionFactory.class );
        UserInventory inventory = mock( UserInventory.class );
        TextAdventureModel model = mock( TextAdventureModel.class );
        Item item = mock( Item.class );
        when( model.findItemByID( "itemid" ) ).thenReturn( item );
        ModelLocation location = mock( ModelLocation.class );
        when( model.findLocationByID( "locationid" ) ).thenReturn( location );

        new ActionHistoryDeserialiser( factory, inventory, model ).deserialise(
            ACTION_NAME_TAG + SEPERATOR + "inventory item" + SEPERATOR +
            ITEM_ID_TAG + SEPERATOR + "itemid" + SEPERATOR +
            LOCATION_ID_TAG + SEPERATOR + "locationid" + SEPERATOR + "\n" );

        verify( model ).findItemByID( "itemid" );
        verify( model ).findLocationByID( "locationid" );
        verify( factory ).createInventoryItemAction( Mockito.eq( item ),
                                                     Mockito.eq( inventory ),
                                                     Mockito.eq( location ) );
    }

    @Test
    public void deserialises_ShowInventory_action_type_using_factory() {
        ActionFactory factory = mock( ActionFactory.class );
        UserInventory inventory = mock( UserInventory.class );
        TextAdventureModel model = mock( TextAdventureModel.class );

        new ActionHistoryDeserialiser( factory, inventory, model ).deserialise(
            ACTION_NAME_TAG + SEPERATOR + "show inventory" + SEPERATOR + "\n" );

        verify( factory ).createShowInventoryAction( inventory, model );
    }

    @Test
    public void deserialises_TakeAnItem_action_type_using_factory() {
        ActionFactory factory = mock( ActionFactory.class );
        UserInventory inventory = mock( UserInventory.class );
        TextAdventureModel model = mock( TextAdventureModel.class );
        ModelLocation location = mock( ModelLocation.class );
        when( model.findLocationByID( "locationid" ) ).thenReturn( location );

        new ActionHistoryDeserialiser( factory, inventory, model ).deserialise(
            ACTION_NAME_TAG + SEPERATOR + "take an item" + SEPERATOR +
            LOCATION_ID_TAG + SEPERATOR + "locationid" + SEPERATOR + "\n" );

        verify( model ).findLocationByID( "locationid" );
        verify( factory ).createTakeAnItemAction( Mockito.anyListOf( Item.class ),
                                                  Mockito.eq( inventory ),
                                                  Mockito.eq( location ) );
    }

    @Test
    public void deserialises_TakeSpecificItem_action_type_using_factory() {
        ActionFactory factory = mock( ActionFactory.class );
        UserInventory inventory = mock( UserInventory.class );
        TextAdventureModel model = mock( TextAdventureModel.class );
        Item item = mock( Item.class );
        when( model.findItemByID( "itemid" ) ).thenReturn( item );
        ModelLocation location = mock( ModelLocation.class );
        when( model.findLocationByID( "locationid" ) ).thenReturn( location );

        new ActionHistoryDeserialiser( factory, inventory, model ).deserialise(
            ACTION_NAME_TAG + SEPERATOR + "take specific item" + SEPERATOR +
            ITEM_ID_TAG + SEPERATOR + "itemid" + SEPERATOR +
            LOCATION_ID_TAG + SEPERATOR + "locationid" + SEPERATOR + "\n" );

        verify( model ).findItemByID( "itemid" );
        verify( model ).findLocationByID( "locationid" );
        verify( factory ).createTakeSpecificItemAction( Mockito.eq( item ),
                                                        Mockito.eq( inventory ),
                                                        Mockito.eq( location ) );
    }

    @Test
    public void deserialises_UseWithSpecificItem_action_type_using_factory() {
        ActionFactory factory = mock( ActionFactory.class );
        TextAdventureModel model = mock( TextAdventureModel.class );
        Item item1 = mock( Item.class );
        when( model.findItemByID( "itemid1" ) ).thenReturn( item1 );
        Item item2 = mock( Item.class );
        when( model.findItemByID( "itemid2" ) ).thenReturn( item2 );

        new ActionHistoryDeserialiser( factory, null, model ).deserialise(
            ACTION_NAME_TAG + SEPERATOR + "use with specific item" + SEPERATOR +
            ITEM_ID_TAG + SEPERATOR + "itemid1" + SEPERATOR +
            EXTRA_ITEM_ID_TAG + SEPERATOR + "itemid2" + SEPERATOR + "\n" );

        verify( model ).findItemByID( "itemid1" );
        verify( model ).findItemByID( "itemid2" );
        verify( factory ).createUseWithSpecificItemAction( item1, item2 );
    }

    @Test
    public void deserialises_UseWith_action_type_using_factory() {
        ActionFactory factory = mock( ActionFactory.class );
        UserInventory inventory = mock( UserInventory.class );
        TextAdventureModel model = mock( TextAdventureModel.class );
        Item item = mock( Item.class );
        when( model.findItemByID( "itemid" ) ).thenReturn( item );
        ModelLocation location = mock( ModelLocation.class );
        when( model.findLocationByID( "locationid" ) ).thenReturn( location );

        new ActionHistoryDeserialiser( factory, inventory, model ).deserialise(
            ACTION_NAME_TAG + SEPERATOR + "use with" + SEPERATOR +
            ITEM_ID_TAG + SEPERATOR + "itemid" + SEPERATOR +
            LOCATION_ID_TAG + SEPERATOR + "locationid" + SEPERATOR + "\n" );

        verify( model ).findItemByID( "itemid" );
        verify( model ).findLocationByID( "locationid" );
        verify( factory ).createUseWithAction( Mockito.eq( item ),
                                               Mockito.eq( inventory ),
                                               Mockito.eq( location ) );
    }

    @Test
    public void deserialises_TalkTo_action_type_using_factory() {
        ActionFactory factory = mock( ActionFactory.class );
        TextAdventureModel model = mock( TextAdventureModel.class );
        Item item = mock( Item.class );
        when( model.findItemByID( "itemid" ) ).thenReturn( item );

        new ActionHistoryDeserialiser( factory, null, model ).deserialise(
            ACTION_NAME_TAG + SEPERATOR + "talk" + SEPERATOR +
            ITEM_ID_TAG + SEPERATOR + "itemid" + SEPERATOR + "\n" );

        verify( model ).findItemByID( "itemid" );
        verify( factory ).createTalkToAction( Mockito.eq( item ) );
    }

    @Test
    public void deserialises_Say_action_type_using_factory() {
        ActionFactory factory = mock( ActionFactory.class );
        TextAdventureModel model = mock( TextAdventureModel.class );
        Item item = mock( Item.class );
        when( model.findItemByID( "itemid" ) ).thenReturn( item );

        new ActionHistoryDeserialiser( factory, null, model ).deserialise(
            ACTION_NAME_TAG + SEPERATOR + "say" + SEPERATOR +
            STRING_TAG + SEPERATOR + "phraseid" + SEPERATOR +
            ITEM_ID_TAG + SEPERATOR + "itemid" + SEPERATOR + "\n" );

        verify( model ).findItemByID( "itemid" );
        verify( factory ).createSayAction( Mockito.eq( "phraseid" ), Mockito.eq( item ) );
    }
 }
