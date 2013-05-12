package com.chewielouie.textadventure.serialisation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.junit.Test;
import java.util.List;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionFactory;
import com.chewielouie.textadventure.action.ActionParameters;

public class ActionHistoryDeserialiserTests {

    @Test
    public void returns_empty_list_of_actions_for_empty_input() {
        List<Action> actions =
            new ActionHistoryDeserialiser( null, null, null ).deserialise( "" );
        assertThat( actions.size(), is( 0 ) );
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
    // public void deserialises_xxx_action_type() {

    @Test
    public void adds_deserialised_actions_to_list_in_order() {
        Action action1 = mock( Action.class );
        Action action2 = mock( Action.class );
        ActionFactory factory = mock( ActionFactory.class );
        when( factory.createShowInventoryAction( Mockito.any( UserInventory.class ),
                                                 Mockito.any( TextAdventureModel.class ) ) )
            .thenReturn( action1, action2 );
        List<Action> actions =
            new ActionHistoryDeserialiser( factory, null, null ).deserialise(
            "action name:show inventory:\n" +
            "action name:show inventory:\n" );

        assertThat( actions.size(), is( 2 ) );
        assertThat( actions.get( 0 ), is( action1 ) );
        assertThat( actions.get( 1 ), is( action2 ) );
    }

    // @Test
    // public void deserialises_action_item_parameter() {

    // @Test
    // public void deserialises_action_extra_item_parameter() {

    // @Test
    // public void deserialises_action_exit_parameter() {
}
