package com.chewielouie.textadventure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import com.chewielouie.textadventure.itemaction.ItemAction;

public class LocationExitTests {

    @Test
    public void exit_is_visible_by_default() {
        assertTrue( new LocationExit().visible() );
    }

    @Test
    public void exit_can_be_made_invisible() {
        LocationExit exit = new LocationExit();
        exit.setInvisible();
        assertFalse( exit.visible() );
    }

    @Test
    public void exit_can_be_made_visible_again() {
        LocationExit exit = new LocationExit();
        exit.setInvisible();
        exit.setVisible();
        assertTrue( exit.visible() );
    }

    @Test
    public void exit_id_is_blank_by_default() {
        LocationExit exit = new LocationExit();
        assertEquals( "", exit.id() );
    }

    @Test
    public void exit_id_can_be_set() {
        LocationExit exit = new LocationExit();
        exit.setID( "exit id" );
        assertEquals( "exit id", exit.id() );
    }

    @Test
    public void exit_has_a_default_direction_hint_of_dont_care() {
        LocationExit exit = new LocationExit();

        assertEquals( Exit.DirectionHint.DontCare, exit.directionHint() );
    }

    @Test
    public void can_set_a_direction_hint() {
        LocationExit exit = new LocationExit();
        exit.setDirectionHint( Exit.DirectionHint.North );

        assertEquals( Exit.DirectionHint.North, exit.directionHint() );
    }

    @Test
    public void two_exits_with_the_same_value_should_be_equal() {
        LocationExit exit1 = new LocationExit();
        exit1.setLabel( "label" );
        exit1.setDestination( "destination" );
        LocationExit exit2 = new LocationExit();
        exit2.setLabel( "label" );
        exit2.setDestination( "destination" );

        assertEquals( exit1, exit2 );
    }

    @Test
    public void two_exits_with_different_labels_should_not_be_equal() {
        LocationExit exit1 = new LocationExit();
        exit1.setLabel( "label 1" );
        LocationExit exit2 = new LocationExit();
        exit2.setLabel( "label 2" );

        assertNotEquals( exit1, exit2 );
    }


    @Test
    public void two_exits_with_different_destinations_should_not_be_equal() {
        LocationExit exit1 = new LocationExit();
        exit1.setDestination( "destination 1" );
        LocationExit exit2 = new LocationExit();
        exit2.setDestination( "destination 2" );

        assertNotEquals( exit1, exit2 );
    }


    @Test
    public void two_exits_with_different_direction_hints_should_not_be_equal() {
        LocationExit exit1 = new LocationExit();
        exit1.setDirectionHint( Exit.DirectionHint.North );
        LocationExit exit2 = new LocationExit();
        exit2.setDirectionHint( Exit.DirectionHint.South );

        assertNotEquals( exit1, exit2 );
    }

    @Test
    public void a_exit_is_not_equal_to_a_non_exit() {
        LocationExit exit = new LocationExit();
        Object notAnExit = new Object();

        assertNotEquals( exit, notAnExit );
    }

    @Test
    public void two_exits_with_the_same_value_should_have_the_same_hashcode() {
        LocationExit exit1 = new LocationExit();
        exit1.setLabel( "label" );
        LocationExit exit2 = new LocationExit();
        exit2.setLabel( "label" );

        assertEquals( exit1.hashCode(), exit2.hashCode() );
    }

    @Test
    public void two_exits_with_different_labels_should_have_different_hashcodes() {
        LocationExit exit1 = new LocationExit();
        exit1.setLabel( "label 1" );
        LocationExit exit2 = new LocationExit();
        exit2.setLabel( "label 2" );

        assertNotEquals( exit1.hashCode(), exit2.hashCode() );
    }

    @Test
    public void two_exits_with_different_destinations_should_have_different_hashcodes() {
        LocationExit exit1 = new LocationExit();
        exit1.setDestination( "destination 1" );
        LocationExit exit2 = new LocationExit();
        exit2.setDestination( "destination 2" );

        assertNotEquals( exit1.hashCode(), exit2.hashCode() );
    }

    @Test
    public void two_exits_with_different_direction_hints_should_have_different_hashcodes() {
        LocationExit exit1 = new LocationExit();
        exit1.setDirectionHint( Exit.DirectionHint.North );
        LocationExit exit2 = new LocationExit();
        exit2.setDirectionHint( Exit.DirectionHint.South );

        assertNotEquals( exit1.hashCode(), exit2.hashCode() );
    }

    @Test
    public void onUse_enacts_all_added_on_use_actions() {
        LocationExit exit = new LocationExit();
        ItemAction action1 = mock( ItemAction.class );
        ItemAction action2 = mock( ItemAction.class );
        exit.addOnUseAction( action1 );
        exit.addOnUseAction( action2 );

        exit.use();

        verify( action1 ).enact();
        verify( action2 ).enact();
    }
}

