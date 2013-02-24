package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class BasicModelTests {

    private Mockery mockery = new Mockery();

    @Test
    public void asking_for_the_current_location_before_one_is_set_returns_a_default_null_location() {
        BasicModel model = new BasicModel();

        assertTrue( model.currentLocation() instanceof NullLocation );
    }

    @Test
    public void first_location_added_is_the_starting_location() {
        final ModelLocation loc1 = mockery.mock( ModelLocation.class, "loc1" );
        final ModelLocation loc2 = mockery.mock( ModelLocation.class, "loc2" );
        mockery.checking( new Expectations() {{
            ignoring( loc1 );
            ignoring( loc2 );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( loc1 );
        model.addLocation( loc2 );

        assertEquals( loc1, model.currentLocation() );
    }

    @Test
    public void leaving_a_location_changes_the_current_location() {
        final ModelLocation loc1 = mockery.mock( ModelLocation.class, "loc1" );
        final ModelLocation loc2 = mockery.mock( ModelLocation.class, "loc2" );
        final Exit north = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            oneOf( loc1 ).exitable( north );
            will( returnValue( true ) );
            oneOf( loc1 ).exitDestinationFor( north );
            will( returnValue( "loc2" ) );
            ignoring( loc1 );
            allowing( loc2 ).id();
            will( returnValue( "loc2" ) );
            ignoring( loc2 );
            ignoring( north );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( loc1 );
        model.addLocation( loc2 );

        model.moveThroughExit( north );

        assertEquals( loc2, model.currentLocation() );
    }

    @Test
    public void leaving_a_location_by_an_invalid_exit_does_not_change_the_current_location() {
        final ModelLocation loc1 = mockery.mock( ModelLocation.class );
        final Exit notAValidExit = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            oneOf( loc1 ).exitable( notAValidExit );
            will( returnValue( false ) );
            ignoring( loc1 );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( loc1 );

        model.moveThroughExit( notAValidExit );

        assertEquals( loc1, model.currentLocation() );
    }

    @Test
    public void current_location_description_is_taken_from_the_current_location() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final String description = "description of this location";
        mockery.checking( new Expectations() {{
            oneOf( location ).description();
            will( returnValue( description ) );
            ignoring( location );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( location );

        assertEquals( description, model.currentLocationDescription() );
    }

    @Test
    public void current_location_exits_are_taken_from_the_current_location() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final List<Exit> exits = new ArrayList<Exit>();
        final Exit exit1 = mockery.mock( Exit.class, "exit1" );
        final Exit exit2 = mockery.mock( Exit.class, "exit2" );
        exits.add( exit1 );
        exits.add( exit2 );
        mockery.checking( new Expectations() {{
            oneOf( location ).visibleExits();
            will( returnValue( exits ) );
            ignoring( location );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( location );

        assertEquals( exits, model.currentLocationExits() );
    }

    @Test
    public void adding_an_item_to_the_inventory_actually_adds_it() {
        Item item = mockery.mock( Item.class );
        BasicModel model = new BasicModel();
        int sizeBeforeAdd = model.inventoryItems().size();
        model.addToInventory( item );

        assertEquals( sizeBeforeAdd+1, model.inventoryItems().size() );
        assertEquals( item, model.inventoryItems().get(sizeBeforeAdd) );
    }

    @Test
    public void find_exit_by_id_finds_the_exit() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final List<Exit> exits = new ArrayList<Exit>();
        final Exit exit = mockery.mock( Exit.class );
        exits.add( exit );
        mockery.checking( new Expectations() {{
            oneOf( location ).exitsIncludingInvisibleOnes();
            will( returnValue( exits ) );
            ignoring( location );
            oneOf( exit ).id();
            will( returnValue( "exit id" ) );
            ignoring( exit );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( location );

        assertEquals( exit, model.findExitByID( "exit id" ) );
    }

    @Test
    public void find_exit_by_id_returns_null_if_it_cant_find_the_exit() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final List<Exit> exits = new ArrayList<Exit>();
        final Exit exit = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            oneOf( location ).exitsIncludingInvisibleOnes();
            will( returnValue( exits ) );
            ignoring( location );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( location );

        assertEquals( null, model.findExitByID( "exit id" ) );
    }
}

