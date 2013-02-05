package com.chewielouie.textadventure.action;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class TakeAnItemTests {

    @Test
    public void two_objects_with_the_same_value_should_be_equal() {
        TakeAnItem object1 = new TakeAnItem();
        TakeAnItem object2 = new TakeAnItem();

        assertEquals( object1, object2 );
    }

    @Test
    public void a_show_inventory_object_is_not_equal_to_a_non_object() {
        TakeAnItem object = new TakeAnItem();
        Object notATakeAnItem = new Object();

        assertNotEquals( object, notATakeAnItem );
    }

    @Test
    public void two_objects_with_the_same_value_should_have_the_same_hashcode() {
        TakeAnItem object1 = new TakeAnItem();
        TakeAnItem object2 = new TakeAnItem();

        assertEquals( object1.hashCode(), object2.hashCode() );
    }
}

