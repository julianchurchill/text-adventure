package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import org.junit.Test;

public class ExitTests {

    @Test
    public void exit_has_a_default_direction_hint_of_dont_care() {
        Exit exit = new Exit( "label", "destination" );

        assertEquals( Exit.DirectionHint.DontCare, exit.directionHint() );
    }

    @Test
    public void can_set_a_direction_hint() {
        Exit exit = new Exit( "label", "destination", Exit.DirectionHint.North );

        assertEquals( Exit.DirectionHint.North, exit.directionHint() );
    }

    @Test
    public void two_exits_with_the_same_value_should_be_equal() {
        Exit exit1 = new Exit( "label", "destination", Exit.DirectionHint.North );
        Exit exit2 = new Exit( "label", "destination", Exit.DirectionHint.North );

        assertEquals( exit1, exit2 );
    }

    @Test
    public void two_exits_with_different_labels_should_not_be_equal() {
        Exit exit1 = new Exit( "label a", "destination", Exit.DirectionHint.North );
        Exit exit2 = new Exit( "label b", "destination", Exit.DirectionHint.North );

        assertNotEquals( exit1, exit2 );
    }


    @Test
    public void two_exits_with_different_destinations_should_not_be_equal() {
        Exit exit1 = new Exit( "label", "destination a", Exit.DirectionHint.North );
        Exit exit2 = new Exit( "label", "destination b", Exit.DirectionHint.North );

        assertNotEquals( exit1, exit2 );
    }


    @Test
    public void two_exits_with_different_direction_hints_should_not_be_equal() {
        Exit exit1 = new Exit( "label", "destination", Exit.DirectionHint.North );
        Exit exit2 = new Exit( "label", "destination", Exit.DirectionHint.South );

        assertNotEquals( exit1, exit2 );
    }

    @Test
    public void a_exit_is_not_equal_to_a_non_exit() {
        Exit exit = new Exit( "label", "destination", Exit.DirectionHint.North );
        Object notAnExit = new Object();

        assertNotEquals( exit, notAnExit );
    }

    @Test
    public void two_exits_with_the_same_value_should_have_the_same_hashcode() {
        Exit exit1 = new Exit( "label", "destination", Exit.DirectionHint.North );
        Exit exit2 = new Exit( "label", "destination", Exit.DirectionHint.North );

        assertEquals( exit1.hashCode(), exit2.hashCode() );
    }

    @Test
    public void two_exits_with_different_labels_should_have_different_hashcodes() {
        Exit exit1 = new Exit( "label a", "destination", Exit.DirectionHint.North );
        Exit exit2 = new Exit( "label b", "destination", Exit.DirectionHint.North );

        assertNotEquals( exit1.hashCode(), exit2.hashCode() );
    }

    @Test
    public void two_exits_with_different_destinations_should_have_different_hashcodes() {
        Exit exit1 = new Exit( "label", "destination a", Exit.DirectionHint.North );
        Exit exit2 = new Exit( "label", "destination b", Exit.DirectionHint.North );

        assertNotEquals( exit1.hashCode(), exit2.hashCode() );
    }

    @Test
    public void two_exits_with_different_direction_hints_should_have_different_hashcodes() {
        Exit exit1 = new Exit( "label", "destination", Exit.DirectionHint.North );
        Exit exit2 = new Exit( "label", "destination", Exit.DirectionHint.South );

        assertNotEquals( exit1.hashCode(), exit2.hashCode() );
    }
}

