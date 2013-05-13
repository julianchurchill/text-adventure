package com.chewielouie.textadventure.serialisation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.junit.Test;
import java.util.List;
import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionFactory;
import com.chewielouie.textadventure.action.ActionParameters;
import com.chewielouie.textadventure.item.Item;

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
            "action name:examine an item:\n" +
            "action name:examine an item:\n" );

        assertThat( actions.size(), is( 2 ) );
        assertThat( actions.get( 0 ), is( action1 ) );
        assertThat( actions.get( 1 ), is( action2 ) );
    }

    @Test
    public void deserialises_ExamineAnItem_action_type() {
        ActionFactory factory = mock( ActionFactory.class );
        UserInventory inventory = mock( UserInventory.class );
        TextAdventureModel model = mock( TextAdventureModel.class );
        new ActionHistoryDeserialiser( factory, inventory, model ).deserialise(
            "action name:examine an item:\n" );

        verify( factory ).createExamineAnItemAction( Mockito.anyListOf( Item.class ) );
    }

    @Test
    public void deserialises_Examine_action_type() {
        ActionFactory factory = mock( ActionFactory.class );
        UserInventory inventory = mock( UserInventory.class );
        TextAdventureModel model = mock( TextAdventureModel.class );
        new ActionHistoryDeserialiser( factory, inventory, model ).deserialise(
            "action name:examine:\n" );

        verify( factory ).createExamineAction( Mockito.any( Item.class ) );
    }

    @Test
    public void deserialises_Exit_action_type_using_factory() {
        ActionFactory factory = mock( ActionFactory.class );
        UserInventory inventory = mock( UserInventory.class );
        TextAdventureModel model = mock( TextAdventureModel.class );
        new ActionHistoryDeserialiser( factory, inventory, model ).deserialise(
            "action name:exit:\n" );

        verify( factory ).createExitAction( Mockito.any( Exit.class ),
                                            Mockito.eq( model ) );
    }

    @Test
    public void deserialises_InventoryItem_action_type_using_factory() {
        ActionFactory factory = mock( ActionFactory.class );
        UserInventory inventory = mock( UserInventory.class );
        TextAdventureModel model = mock( TextAdventureModel.class );
        new ActionHistoryDeserialiser( factory, inventory, model ).deserialise(
            "action name:inventory item:\n" );

        verify( factory ).createInventoryItemAction( Mockito.any( Item.class ),
                                                     Mockito.eq( inventory ),
                                                     Mockito.any( ModelLocation.class ) );
    }

    @Test
    public void deserialises_ShowInventory_action_type_using_factory() {
        ActionFactory factory = mock( ActionFactory.class );
        UserInventory inventory = mock( UserInventory.class );
        TextAdventureModel model = mock( TextAdventureModel.class );
        new ActionHistoryDeserialiser( factory, inventory, model ).deserialise(
            "action name:show inventory:\n" );

        verify( factory ).createShowInventoryAction( inventory, model );
    }

    // @Test
    // public void deserialises_TakeAnItem_action_type_using_factory() {
    //     ActionFactory factory = mock( ActionFactory.class );
    //     UserInventory inventory = mock( UserInventory.class );
    //     TextAdventureModel model = mock( TextAdventureModel.class );
    //     new ActionHistoryDeserialiser( factory, inventory, model ).deserialise(
    //         "action name:take an item:\n" );

    //     verify( factory ).createTakeAnItemAction( Mockito.anyListOf( Item.class ),
    //                                               Mockito.eq( inventory ),
    //                                               Mockito.isNull() );
    // }


    // @Test
    // public void deserialises_TakeSpecificItem_action_type_using_factory() {

    // @Test
    // public void deserialises_UseWithSpecificItem_action_type_using_factory() {

    // @Test
    // public void deserialises_UseWith_action_type_using_factory() {

    // @Test
    // public void deserialises_action_item_parameter() {

    // @Test
    // public void deserialises_action_extra_item_parameter() {

    // @Test
    // public void deserialises_action_exit_parameter() {
}
