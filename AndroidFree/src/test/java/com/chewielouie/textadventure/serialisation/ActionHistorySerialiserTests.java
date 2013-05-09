package com.chewielouie.textadventure.serialisation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.action.ActionHistory;
import com.chewielouie.textadventure.action.ActionRecord;
import com.chewielouie.textadventure.action.ShowInventory;

public class ActionHistorySerialiserTests {

    @Test
    public void serialises_empty_history_as_empty_string() {
        ActionHistory history = mock( ActionHistory.class );
        when( history.size() ).thenReturn( 0 );

        assertThat( new ActionHistorySerialiser( history ).serialise(), is( "" ) );
    }

    @Test
    public void serialises_ShowInventory_action_type() {
        ActionRecord actionRecord = mock( ActionRecord.class );
        when( actionRecord.action() ).thenReturn( new ShowInventory( null, null, null ) );
        ActionHistory history = mock( ActionHistory.class );
        when( history.size() ).thenReturn( 1 );
        when( history.getRecord( 0 ) ).thenReturn( actionRecord );

        assertThat( new ActionHistorySerialiser( history ).serialise(),
                    is( "show inventory\n" ) );
    }

    // @Test
    // public void serialises_xxx_action_type() {
}
