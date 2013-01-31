package com.chewielouie.textadventure.action;

import static org.junit.Assert.*;

import org.junit.Test;

public class ShowInventoryTests {

    @Test
    public void two_objects_with_the_same_value_should_be_equal() {
        ShowInventory object1 = new ShowInventory();
        ShowInventory object2 = new ShowInventory();

        assertEquals( object1, object2 );
    }

    @Test
    public void a_show_inventory_object_is_not_equal_to_a_non_object() {
        ShowInventory object = new ShowInventory();
        Object notAShowInventory = new Object();

        assertNotEquals( object, notAShowInventory );
    }

    @Test
    public void two_objects_with_the_same_value_should_have_the_same_hashcode() {
        ShowInventory object1 = new ShowInventory();
        ShowInventory object2 = new ShowInventory();

        assertEquals( object1.hashCode(), object2.hashCode() );
    }
}

