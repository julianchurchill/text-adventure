package com.chewielouie.textadventure.action;

import static org.junit.Assert.*;

import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.Item;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.UserInventory;
import com.chewielouie.textadventure.TextAdventureModel;

@RunWith(JMock.class)
public class InventoryItemTests {

    private Mockery mockery = new Mockery();

    InventoryItem createAction() {
        return new InventoryItem( null, null );
    }

    @Test
    public void label_is_item_name() {
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            allowing( item ).name();
            will( returnValue( "Item name" ) );
            ignoring( item );
        }});
        InventoryItem action = new InventoryItem( item, null );

        assertEquals( "Item name", action.label() );
    }

    @Test
    public void user_must_choose_follow_up_action_is_always_true() {
        assertTrue( createAction().userMustChooseFollowUpAction() );
    }

    @Test
    public void follow_up_actions_contains_Examine_action_for_item() {
        Item item = mockery.mock( Item.class );
        InventoryItem action = new InventoryItem( item, null );

        List<Action> actions = action.followUpActions();
        assertTrue( actions.size() > 0 );
        assertTrue( actions.get(0) instanceof Examine );
        assertEquals( item, ((Examine)actions.get(0)).item() );
    }

    @Test
    public void follow_up_actions_contains_UseWith_action_for_item() {
        final Item item = mockery.mock( Item.class );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            ignoring( item );
            ignoring( inventory );
            ignoring( location );
        }});
        InventoryItem action = new InventoryItem( item, inventory, location );

        List<Action> actions = action.followUpActions();
        assertTrue( actions.size() > 1 );
        assertTrue( actions.get(1) instanceof UseWith );
        assertEquals( item, ((UseWith)actions.get(1)).item() );
        assertEquals( inventory, ((UseWith)actions.get(1)).inventory() );
        assertEquals( location, ((UseWith)actions.get(1)).location() );
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

