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
        l.addExit( new Exit( "north", "loc2" ) );
        assertTrue( l.exitable( new Exit( "north", "loc2" ) ) );
    }

    @Test
    public void exits_that_havent_been_added_are_not_exitable() {
        Location l = new Location( "", "" );
        l.addExit( new Exit( "north", "loc2" ) );
        assertFalse( l.exitable( new Exit( "south", "loc1" ) ) );
    }

    @Test
    public void exit_destination_is_retrievable() {
        Exit north = new Exit( "north", "loc2" );
        Location l = new Location( "", "" );
        l.addExit( new Exit( "north", "loc2" ) );
        assertEquals( "loc2", l.exitDestinationFor( north ) );
    }

    @Test
    public void all_valid_exits_are_retrieveable() {
        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "north", "loc2" ) );
        exits.add( new Exit( "south", "loc3" ) );

        Location l = new Location( "", "" );
        l.addExit( new Exit( "north", "loc2" ) );
        l.addExit( new Exit( "south", "loc3" ) );

        assertEquals( exits, l.exits() );
    }
}

