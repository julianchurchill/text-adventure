package com.chewielouie.textadventure.action;

import static org.junit.Assert.*;

import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.Item;

@RunWith(JMock.class)
public class InventoryItemTests {

    private Mockery mockery = new Mockery();

    InventoryItem createAction() {
        return new InventoryItem( null );
    }

    @Test
    public void label_is_item_name() {
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            allowing( item ).name();
            will( returnValue( "Item name" ) );
            ignoring( item );
        }});
        InventoryItem action = new InventoryItem( item );

        assertEquals( "Item name", action.label() );
    }

    @Test
    public void user_must_choose_follow_up_action_is_always_true() {
        assertTrue( createAction().userMustChooseFollowUpAction() );
    }

    @Test
    public void follow_up_actions_contains_Examine_action_for_item() {
        Item item = mockery.mock( Item.class );
        InventoryItem action = new InventoryItem( item );

        List<Action> actions = action.followUpActions();
        assertEquals( 1, actions.size() );
        assertTrue( actions.get(0) instanceof Examine );
        assertEquals( item, ((Examine)actions.get(0)).item() );
    }

    @Test
    public void two_objects_with_the_same_value_should_be_equal() {
        InventoryItem object1 = createAction();
        InventoryItem object2 = createAction();

        assertEquals( createAction(), createAction() );
    }

    @Test
    public void a_show_inventory_object_is_not_equal_to_a_non_object() {
        InventoryItem object = createAction();
        Object notAInventoryItem = new Object();

        assertNotEquals( object, notAInventoryItem );
    }

    @Test
    public void two_objects_with_the_same_value_should_have_the_same_hashcode() {
        InventoryItem object1 = createAction();
        InventoryItem object2 = createAction();

        assertEquals( object1.hashCode(), object2.hashCode() );
    }
}

