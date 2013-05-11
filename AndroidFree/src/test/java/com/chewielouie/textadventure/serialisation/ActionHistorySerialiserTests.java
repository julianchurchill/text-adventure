package com.chewielouie.textadventure.serialisation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionHistory;
import com.chewielouie.textadventure.action.ActionRecord;

public class ActionHistorySerialiserTests {

    @Test
    public void serialises_empty_history_as_empty_string() {
        ActionHistory history = mock( ActionHistory.class );
        when( history.size() ).thenReturn( 0 );

        assertThat( new ActionHistorySerialiser( history ).serialise(), is( "" ) );
    }

    @Test
    public void serialises_action_name() {
        Action action = mock( Action.class );
        when( action.name() ).thenReturn( "action name" );
        ActionRecord actionRecord = mock( ActionRecord.class );
        when( actionRecord.action() ).thenReturn( action );
        ActionHistory history = mock( ActionHistory.class );
        when( history.size() ).thenReturn( 1 );
        when( history.getRecord( 0 ) ).thenReturn( actionRecord );

        assertThat( new ActionHistorySerialiser( history ).serialise(),
                    is( "action name\n" ) );
    }

    // @Test
    // public void serialises_item_parameter_if_present() {

    // @Test
    // public void serialises_extra_item_parameter_if_present() {

    // @Test
    // public void serialises_exit_parameter_if_present() {
}
