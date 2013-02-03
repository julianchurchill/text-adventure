package com.chewielouie.textadventure.action;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.Item;
import com.chewielouie.textadventure.TextAdventureModel;

@RunWith(JMock.class)
public class ShowInventoryTests {

    private Mockery mockery = new Mockery();

    @Test
    public void two_objects_with_the_same_value_should_be_equal() {
        ShowInventory object1 = new ShowInventory( null );
        ShowInventory object2 = new ShowInventory( null );

        assertEquals( object1, object2 );
    }

    @Test
    public void a_show_inventory_object_is_not_equal_to_a_non_object() {
        ShowInventory object = new ShowInventory( null );
        Object notAShowInventory = new Object();

        assertNotEquals( object, notAShowInventory );
    }

    @Test
    public void two_objects_with_the_same_value_should_have_the_same_hashcode() {
        ShowInventory object1 = new ShowInventory( null );
        ShowInventory object2 = new ShowInventory( null );

        assertEquals( object1.hashCode(), object2.hashCode() );
    }

    @Test
    public void trigger_action_gathers_inventory_items_from_model() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        ShowInventory action = new ShowInventory( model );
        final List<Item> items = new ArrayList<Item>();
        mockery.checking( new Expectations() {{
            oneOf( model ).inventoryItems();
            will( returnValue( items ) );
            ignoring( model );
        }});

        action.trigger();
    }
}

