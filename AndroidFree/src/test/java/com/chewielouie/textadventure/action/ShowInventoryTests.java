package com.chewielouie.textadventure.action;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
public class ShowInventoryTests {

    private Mockery mockery = new Mockery();

    @Test
    public void two_objects_with_the_same_value_should_be_equal() {
        ShowInventory object1 = new ShowInventory( null, null );
        ShowInventory object2 = new ShowInventory( null, null );

        assertEquals( object1, object2 );
    }

    @Test
    public void a_show_inventory_object_is_not_equal_to_a_non_object() {
        ShowInventory object = new ShowInventory( null, null );
        Object notAShowInventory = new Object();

        assertNotEquals( object, notAShowInventory );
    }

    @Test
    public void two_objects_with_the_same_value_should_have_the_same_hashcode() {
        ShowInventory object1 = new ShowInventory( null, null );
        ShowInventory object2 = new ShowInventory( null, null );

        assertEquals( object1.hashCode(), object2.hashCode() );
    }

    @Test
    public void user_must_choose_follow_up_action_is_always_true() {
        ShowInventory action = new ShowInventory( null, null );

        assertTrue( action.userMustChooseFollowUpAction() );
    }

    @Test
    public void follow_up_actions_contains_InventoryItem_actions_for_each_inventory_item() {
        final UserInventory inventory = mockery.mock( UserInventory.class );
        ShowInventory action = new ShowInventory( inventory, null );
        final List<Item> items = new ArrayList<Item>();
        Item item1 = mockery.mock( Item.class, "item 1" );
        Item item2 = mockery.mock( Item.class, "item 2" );
        items.add( item1 );
        items.add( item2 );
        mockery.checking( new Expectations() {{
            oneOf( inventory ).inventoryItems();
            will( returnValue( items ) );
            ignoring( inventory );
        }});

        action.trigger();
        List<Action> actions = action.followUpActions();
        assertEquals( 2, actions.size() );
        assertTrue( actions.get(0) instanceof InventoryItem );
        assertEquals( item1, ((InventoryItem)actions.get(0)).item() );
        assertTrue( actions.get(1) instanceof InventoryItem );
        assertEquals( item2, ((InventoryItem)actions.get(1)).item() );
    }

    @Test
    public void follow_up_actions_contains_InventoryItem_actions_which_are_constructed_with_the_inventory() {
        final UserInventory inventory = mockery.mock( UserInventory.class );
        ShowInventory action = new ShowInventory( inventory, null );
        final List<Item> items = new ArrayList<Item>();
        Item item = mockery.mock( Item.class );
        items.add( item );
        mockery.checking( new Expectations() {{
            allowing( inventory ).inventoryItems();
            will( returnValue( items ) );
            ignoring( inventory );
        }});

        action.trigger();
        List<Action> actions = action.followUpActions();
        assertTrue( actions.size() > 0 );
        assertTrue( actions.get(0) instanceof InventoryItem );
        assertEquals( inventory, ((InventoryItem)actions.get(0)).inventory() );
    }

    @Test
    public void follow_up_actions_contains_InventoryItem_actions_which_are_constructed_with_the_current_location_from_the_model() {
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        ShowInventory action = new ShowInventory( inventory, model );
        final List<Item> items = new ArrayList<Item>();
        Item item = mockery.mock( Item.class );
        items.add( item );
        mockery.checking( new Expectations() {{
            allowing( inventory ).inventoryItems();
            will( returnValue( items ) );
            ignoring( inventory );
            allowing( model ).currentLocation();
            will( returnValue( location ) );
            ignoring( model );
            ignoring( location );
        }});

        action.trigger();
        List<Action> actions = action.followUpActions();
        assertTrue( actions.size() > 0 );
        assertTrue( actions.get(0) instanceof InventoryItem );
        assertEquals( location, ((InventoryItem)actions.get(0)).location() );
    }

    @Test
    public void user_text_is_not_available() {
        ShowInventory action = new ShowInventory( null, null );

        assertFalse( action.userTextAvailable() );
    }
}

