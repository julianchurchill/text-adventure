package com.chewielouie.textadventure.serialisation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionHistory;
import com.chewielouie.textadventure.action.ActionParameters;
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
        when( action.name() ).thenReturn( "name" );
        ActionRecord actionRecord = mock( ActionRecord.class );
        when( actionRecord.action() ).thenReturn( action );
        ActionHistory history = mock( ActionHistory.class );
        when( history.size() ).thenReturn( 1 );
        when( history.getRecord( 0 ) ).thenReturn( actionRecord );

        assertThat( new ActionHistorySerialiser( history ).serialise(),
                    is( "action name:name:\n" ) );
    }

    @Test
    public void serialises_string_parameter_if_present() {
        ActionParameters params = mock( ActionParameters.class );
        when( params.string() ).thenReturn( "string value" );
        ActionRecord actionRecord = mock( ActionRecord.class );
        when( actionRecord.params() ).thenReturn( params );
        ActionHistory history = mock( ActionHistory.class );
        when( history.size() ).thenReturn( 1 );
        when( history.getRecord( 0 ) ).thenReturn( actionRecord );

        assertThat( new ActionHistorySerialiser( history ).serialise(),
                    is( "string:string value:\n" ) );
    }

    @Test
    public void serialises_item_parameter_if_present() {
        Item item = mock( Item.class );
        when( item.id() ).thenReturn( "itemid" );
        ActionParameters params = mock( ActionParameters.class );
        when( params.item() ).thenReturn( item );
        ActionRecord actionRecord = mock( ActionRecord.class );
        when( actionRecord.params() ).thenReturn( params );
        ActionHistory history = mock( ActionHistory.class );
        when( history.size() ).thenReturn( 1 );
        when( history.getRecord( 0 ) ).thenReturn( actionRecord );

        assertThat( new ActionHistorySerialiser( history ).serialise(),
                    is( "item id:itemid:\n" ) );
    }

    @Test
    public void serialises_extra_item_parameter_if_present() {
        Item item = mock( Item.class );
        when( item.id() ).thenReturn( "extraitemid" );
        ActionParameters params = mock( ActionParameters.class );
        when( params.extraItem() ).thenReturn( item );
        ActionRecord actionRecord = mock( ActionRecord.class );
        when( actionRecord.params() ).thenReturn( params );
        ActionHistory history = mock( ActionHistory.class );
        when( history.size() ).thenReturn( 1 );
        when( history.getRecord( 0 ) ).thenReturn( actionRecord );

        assertThat( new ActionHistorySerialiser( history ).serialise(),
                    is( "extra item id:extraitemid:\n" ) );
    }

    @Test
    public void serialises_exit_parameter_if_present() {
        Exit exit = mock( Exit.class );
        when( exit.id() ).thenReturn( "exitid" );
        ActionParameters params = mock( ActionParameters.class );
        when( params.exit() ).thenReturn( exit );
        ActionRecord actionRecord = mock( ActionRecord.class );
        when( actionRecord.params() ).thenReturn( params );
        ActionHistory history = mock( ActionHistory.class );
        when( history.size() ).thenReturn( 1 );
        when( history.getRecord( 0 ) ).thenReturn( actionRecord );

        assertThat( new ActionHistorySerialiser( history ).serialise(),
                    is( "exit id:exitid:\n" ) );
    }

    @Test
    public void serialises_location_parameter_if_present() {
        ModelLocation location = mock( ModelLocation.class );
        when( location.id() ).thenReturn( "locationid" );
        ActionParameters params = mock( ActionParameters.class );
        when( params.location() ).thenReturn( location );
        ActionRecord actionRecord = mock( ActionRecord.class );
        when( actionRecord.params() ).thenReturn( params );
        ActionHistory history = mock( ActionHistory.class );
        when( history.size() ).thenReturn( 1 );
        when( history.getRecord( 0 ) ).thenReturn( actionRecord );

        assertThat( new ActionHistorySerialiser( history ).serialise(),
                    is( "location id:locationid:\n" ) );
    }

    @Test
    public void serialises_all_parameters_at_the_same_time() {
        Item item = mock( Item.class );
        when( item.id() ).thenReturn( "itemid" );
        Item extraItem = mock( Item.class );
        when( extraItem.id() ).thenReturn( "extraitemid" );
        Exit exit = mock( Exit.class );
        when( exit.id() ).thenReturn( "exitid" );
        ActionParameters params = mock( ActionParameters.class );
        when( params.string() ).thenReturn( "string value" );
        when( params.item() ).thenReturn( item );
        when( params.extraItem() ).thenReturn( extraItem );
        when( params.exit() ).thenReturn( exit );
        ActionRecord actionRecord = mock( ActionRecord.class );
        when( actionRecord.params() ).thenReturn( params );
        ActionHistory history = mock( ActionHistory.class );
        when( history.size() ).thenReturn( 1 );
        when( history.getRecord( 0 ) ).thenReturn( actionRecord );

        assertThat( new ActionHistorySerialiser( history ).serialise(),
                    is( "string:string value:item id:itemid:extra item id:extraitemid:exit id:exitid:\n" ) );
    }

    @Test
    public void serialises_parameters_and_action_type_together() {
        Action action = mock( Action.class );
        when( action.name() ).thenReturn( "name" );
        Item item = mock( Item.class );
        when( item.id() ).thenReturn( "itemid" );
        ActionParameters params = mock( ActionParameters.class );
        when( params.item() ).thenReturn( item );
        ActionRecord actionRecord = mock( ActionRecord.class );
        when( actionRecord.action() ).thenReturn( action );
        when( actionRecord.params() ).thenReturn( params );
        ActionHistory history = mock( ActionHistory.class );
        when( history.size() ).thenReturn( 1 );
        when( history.getRecord( 0 ) ).thenReturn( actionRecord );

        assertThat( new ActionHistorySerialiser( history ).serialise(),
                    is( "action name:name:item id:itemid:\n" ) );
    }
}
