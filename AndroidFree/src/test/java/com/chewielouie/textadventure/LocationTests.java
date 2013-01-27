package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class LocationTests {

    private Mockery mockery = new Mockery();

    @Test
    public void id_is_set_on_construction() {
        Location l = new Location( "id", "" );
        assertEquals( "id", l.id() );
    }

    @Test
    public void description_is_set_on_construction() {
        Location l = new Location( "", "description" );
        assertEquals( "description", l.description() );
    }

    @Test
    public void added_exit_makes_the_exit_exitable() {
        Location l = new Location( "", "" );
        l.addExit( "north", "loc2" );
        assertTrue( l.exitable( "north" ) );
    }

    @Test
    public void exits_that_havent_been_added_are_not_exitable() {
        Location l = new Location( "", "" );
        l.addExit( "north", "loc2" );
        assertFalse( l.exitable( "south" ) );
    }

    @Test
    public void exit_destination_is_retrievable() {
        Location l = new Location( "", "" );
        l.addExit( "north", "loc2" );
        assertEquals( "loc2", l.exitDestinationFor( "north" ) );
    }

    @Test
    public void all_valid_exits_are_retrieveable() {
        List<String> exits = new ArrayList<String>();
        exits.add( "north" );
        exits.add( "south" );

        Location l = new Location( "", "" );
        l.addExit( "north", "loc2" );
        l.addExit( "south", "loc3" );

        assertEquals( exits, l.exits() );
    }

    //@Test
    //public void first_location_added_is_the_starting_location() {
        //final ModelLocation loc1 = mockery.mock( ModelLocation.class, "loc1" );
        //final ModelLocation loc2 = mockery.mock( ModelLocation.class, "loc2" );
        //mockery.checking( new Expectations() {{
            //ignoring( loc1 );
            //ignoring( loc2 );
        //}});
        //BasicModel model = new BasicModel();
        //model.addLocation( loc1 );
        //model.addLocation( loc2 );

        //assertEquals( loc1, model.currentLocation() );
    //}

    //@Test
    //public void leaving_a_location_changes_the_current_location() {
        //final ModelLocation loc1 = mockery.mock( ModelLocation.class, "loc1" );
        //final ModelLocation loc2 = mockery.mock( ModelLocation.class, "loc2" );
        //mockery.checking( new Expectations() {{
            //oneOf( loc1 ).exitable( "north" );
            //will( returnValue( true ) );
            //oneOf( loc1 ).exitDestinationFor( "north" );
            //will( returnValue( "loc2" ) );
            //ignoring( loc1 );
            //allowing( loc2 ).id();
            //will( returnValue( "loc2" ) );
            //ignoring( loc2 );
        //}});
        //BasicModel model = new BasicModel();
        //model.addLocation( loc1 );
        //model.addLocation( loc2 );

        //model.moveThroughExit( "north" );

        //assertEquals( loc2, model.currentLocation() );
    //}

    //@Test
    //public void leaving_a_location_by_an_invalid_exit_does_not_change_the_current_location() {
        //final ModelLocation loc1 = mockery.mock( ModelLocation.class );
        //mockery.checking( new Expectations() {{
            //oneOf( loc1 ).exitable( "not an exit" );
            //will( returnValue( false ) );
            //ignoring( loc1 );
        //}});
        //BasicModel model = new BasicModel();
        //model.addLocation( loc1 );

        //model.moveThroughExit( "not an exit" );

        //assertEquals( loc1, model.currentLocation() );
    //}

    //@Test
    //public void current_location_description_is_taken_from_the_current_location() {
        //final ModelLocation location = mockery.mock( ModelLocation.class );
        //final String description = "description of this location";
        //mockery.checking( new Expectations() {{
            //oneOf( location ).description();
            //will( returnValue( description ) );
            //ignoring( location );
        //}});
        //BasicModel model = new BasicModel();
        //model.addLocation( location );

        //assertEquals( description, model.currentLocationDescription() );
    //}

    //@Test
    //public void current_location_exits_are_taken_from_the_current_location() {
        //final ModelLocation location = mockery.mock( ModelLocation.class );
        //final List<String> exits = new ArrayList<String>();
        //exits.add( "north" );
        //exits.add( "south" );
        //mockery.checking( new Expectations() {{
            //oneOf( location ).exits();
            //will( returnValue( exits ) );
            //ignoring( location );
        //}});
        //BasicModel model = new BasicModel();
        //model.addLocation( location );

        //assertEquals( exits, model.currentLocationExits() );
    //}
}

