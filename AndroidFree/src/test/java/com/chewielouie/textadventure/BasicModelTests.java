package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class BasicModelTests {

    private Mockery mockery = new Mockery();

    @Test
    public void leaving_a_location_changes_the_current_location() {
        Location loc1 = new Location( "loc1" ) {{
            addExit( "north", "loc2" );
        }};
        Location loc2 = new Location( "loc2" );
        BasicModel model = new BasicModel();
        model.addLocation( loc1 );
        model.addLocation( loc2 );

        model.moveThroughExit( "north" );

        assertEquals( loc2, model.currentLocation() );
    }

    //public void first_room_added_is_the_starting_room() {
    //public void leaving_a_room_by_an_invalid_exit_does_not_change_the_current_room() {
}

