package com.chewielouie.textadventure.serialisation;

import static org.hamcrest.MatcherAssert.assertThat;
// import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionFactory;
import com.chewielouie.textadventure.action.ActionHistory;
import com.chewielouie.textadventure.action.ActionParameters;

public class ActionHistoryDeserialiserTests {

    @Test
    public void clears_history_if_nothing_to_deserialise() {
        ActionHistory history = mock( ActionHistory.class );
        new ActionHistoryDeserialiser( history, null, null, null ).deserialise( "" );

        verify( history ).clear();
        verify( history, never() ).addActionWithParameters( any( Action.class ),
                                                            any( ActionParameters.class ) );
    }

    @Test
    public void deserialises_ShowInventory_action_type_using_factory() {
        ActionFactory factory = mock( ActionFactory.class );
        ActionHistory history = mock( ActionHistory.class );
        UserInventory inventory = mock( UserInventory.class );
        TextAdventureModel model = mock( TextAdventureModel.class );
        new ActionHistoryDeserialiser( history, factory, inventory, model ).deserialise(
            "action name:show inventory:\n" );

        verify( factory ).createShowInventoryAction( inventory, model );
    }

    // @Test
    // public void adds_deserialised_action_to_history() {

    // @Test
    // public void deserialises_xxx_action_type() {

    // @Test
    // public void deserialises_action_item_parameter() {

    // @Test
    // public void deserialises_action_extra_item_parameter() {

    // @Test
    // public void deserialises_action_exit_parameter() {
}
