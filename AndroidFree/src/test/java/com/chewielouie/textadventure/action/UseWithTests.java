package com.chewielouie.textadventure.action;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.UserInventory;

@RunWith(JMock.class)
public class UseWithTests {

    private Mockery mockery = new Mockery();

    UseWith createAction() {
        return new UseWith( null, null, null );
    }

    @Test
    public void label_is_use_with() {
        assertEquals( "Use with", createAction().label() );
    }

    @Test
    public void user_must_choose_follow_up_action_is_always_true() {
        assertTrue( createAction().userMustChooseFollowUpAction() );
    }

    @Test
    public void follow_up_actions_contains_UseWithSpecificItem_actions_for_each_item_from_model_and_inventory() {
        final Item inventoryItem = mockery.mock( Item.class, "inv item" );
        final List<Item> inventoryItems = new ArrayList<Item>();
        inventoryItems.add( inventoryItem );
        final Item locationItem = mockery.mock( Item.class, "loc item" );
        final List<Item> locationItems = new ArrayList<Item>();
        locationItems.add( locationItem );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        mockery.checking( new Expectations() {{
            allowing( location ).items();
            will( returnValue( locationItems ) );
            ignoring( location );
            allowing( inventory ).inventoryItems();
            will( returnValue( inventoryItems ) );
            ignoring( inventory );
            ignoring( inventoryItem );
            allowing( locationItem ).visible();
            will( returnValue( true ) );
            ignoring( locationItem );
        }});
        UseWith action = new UseWith( null, inventory, location );

        List<Action> actions = action.followUpActions();
        assertEquals( 2, actions.size() );
        assertTrue( actions.get(0) instanceof UseWithSpecificItem );
        assertEquals( inventoryItem, ((UseWithSpecificItem)actions.get(0)).targetItem() );
        assertTrue( actions.get(1) instanceof UseWithSpecificItem );
        assertEquals( locationItem, ((UseWithSpecificItem)actions.get(1)).targetItem() );
    }

    @Test
    public void follow_up_actions_contains_UseWithSpecificItem_actions_for_each_item_from_location() {
        final Item locationItem = mockery.mock( Item.class, "loc item" );
        final List<Item> locationItems = new ArrayList<Item>();
        locationItems.add( locationItem );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            allowing( location ).items();
            will( returnValue( locationItems ) );
            ignoring( location );
            allowing( locationItem ).visible();
            will( returnValue( true ) );
            ignoring( locationItem );
        }});
        UseWith action = new UseWith( null, null, location );

        List<Action> actions = action.followUpActions();
        assertEquals( 1, actions.size() );
        assertTrue( actions.get(0) instanceof UseWithSpecificItem );
        assertEquals( locationItem, ((UseWithSpecificItem)actions.get(0)).targetItem() );
    }

    @Test
    public void UseWithSpecificItem_actions_contain_only_visible_location_items() {
        final Item visibleItem = mockery.mock( Item.class, "visible item" );
        final Item invisibleItem = mockery.mock( Item.class, "invisible item" );
        final List<Item> locationItems = new ArrayList<Item>();
        locationItems.add( visibleItem );
        locationItems.add( invisibleItem );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            allowing( location ).items();
            will( returnValue( locationItems ) );
            ignoring( location );
            allowing( visibleItem ).visible();
            will( returnValue( true ) );
            ignoring( visibleItem );
            allowing( invisibleItem ).visible();
            will( returnValue( false ) );
            ignoring( invisibleItem );
        }});
        UseWith action = new UseWith( null, null, location );

        List<Action> actions = action.followUpActions();
        assertEquals( 1, actions.size() );
        assertTrue( actions.get(0) instanceof UseWithSpecificItem );
        assertEquals( visibleItem, ((UseWithSpecificItem)actions.get(0)).targetItem() );
    }

    @Test
    public void follow_up_actions_contains_UseWithSpecificItem_actions_for_each_item_from_inventory() {
        final Item inventoryItem = mockery.mock( Item.class, "inv item" );
        final List<Item> inventoryItems = new ArrayList<Item>();
        inventoryItems.add( inventoryItem );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        mockery.checking( new Expectations() {{
            allowing( inventory ).inventoryItems();
            will( returnValue( inventoryItems ) );
            ignoring( inventory );
            ignoring( inventoryItem );
        }});
        UseWith action = new UseWith( null, inventory, null );

        List<Action> actions = action.followUpActions();
        assertEquals( 1, actions.size() );
        assertTrue( actions.get(0) instanceof UseWithSpecificItem );
        assertEquals( inventoryItem, ((UseWithSpecificItem)actions.get(0)).targetItem() );
    }

    @Test
    public void UseWithSpecificItem_location_actions_are_created_with_original_item() {
        final Item locationItem = mockery.mock( Item.class, "loc item" );
        final List<Item> locationItems = new ArrayList<Item>();
        locationItems.add( locationItem );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            allowing( location ).items();
            will( returnValue( locationItems ) );
            ignoring( location );
            allowing( locationItem ).visible();
            will( returnValue( true ) );
            ignoring( locationItem );
        }});
        Item originalItem = mockery.mock( Item.class, "item being used" );
        UseWith action = new UseWith( originalItem, null, location );

        List<Action> actions = action.followUpActions();
        assertEquals( 1, actions.size() );
        assertTrue( actions.get(0) instanceof UseWithSpecificItem );
        assertEquals( originalItem, ((UseWithSpecificItem)actions.get(0)).itemBeingUsed() );
    }

    @Test
    public void UseWithSpecificItem_inventory_actions_are_created_with_original_item() {
        final Item inventoryItem = mockery.mock( Item.class, "inv item" );
        final List<Item> inventoryItems = new ArrayList<Item>();
        inventoryItems.add( inventoryItem );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        mockery.checking( new Expectations() {{
            allowing( inventory ).inventoryItems();
            will( returnValue( inventoryItems ) );
            ignoring( inventory );
            ignoring( inventoryItem );
        }});
        Item originalItem = mockery.mock( Item.class, "item being used" );
        UseWith action = new UseWith( originalItem, inventory, null );

        List<Action> actions = action.followUpActions();
        assertEquals( 1, actions.size() );
        assertTrue( actions.get(0) instanceof UseWithSpecificItem );
        assertEquals( originalItem, ((UseWithSpecificItem)actions.get(0)).itemBeingUsed() );
    }

    @Test
    public void two_objects_with_the_same_value_should_be_equal() {
        UseWith object1 = createAction();
        UseWith object2 = createAction();

        assertEquals( createAction(), createAction() );
    }

    @Test
    public void a_show_inventory_object_is_not_equal_to_a_non_object() {
        UseWith object = createAction();
        Object notAUseWith = new Object();

        assertNotEquals( object, notAUseWith );
    }

    @Test
    public void two_objects_with_the_same_value_should_have_the_same_hashcode() {
        UseWith object1 = createAction();
        UseWith object2 = createAction();

        assertEquals( object1.hashCode(), object2.hashCode() );
    }
}


