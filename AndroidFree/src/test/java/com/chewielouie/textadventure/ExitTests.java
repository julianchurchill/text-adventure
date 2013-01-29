package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExitTests {

    @Test
    public void two_exits_with_the_same_value_should_be_equal() {
        Exit exit1 = new Exit( "label", "destination" );
        Exit exit2 = new Exit( "label", "destination" );

        assertEquals( exit1, exit2 );
    }

    @Test
    public void two_exits_with_different_values_should_not_be_equal() {
        Exit exit1 = new Exit( "label a", "destination a" );
        Exit exit2 = new Exit( "label b", "destination b" );

        assertNotEquals( exit1, exit2 );
    }

    @Test
    public void a_exit_is_not_equal_to_a_non_exit() {
        Exit exit = new Exit( "label", "destination" );
        Object notAnExit = new Object();

        assertNotEquals( exit, notAnExit );
    }

    @Test
    public void two_exits_with_the_same_value_should_have_the_same_hashcode() {
        Exit exit1 = new Exit( "label", "destination" );
        Exit exit2 = new Exit( "label", "destination" );

        assertEquals( exit1.hashCode(), exit2.hashCode() );
    }

    @Test
    public void two_exits_with_different_values_should_have_different_hashcodes() {
        Exit exit1 = new Exit( "label a", "destination a" );
        Exit exit2 = new Exit( "label b", "destination b" );

        assertNotEquals( exit1.hashCode(), exit2.hashCode() );
    }
}

