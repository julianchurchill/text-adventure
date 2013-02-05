package com.chewielouie.textadventure.action;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.Item;
import com.chewielouie.textadventure.UserInventory;

@RunWith(JMock.class)
public class TakeAnItemTests {

    private Mockery mockery = new Mockery();

    TakeAnItem createAction() {
        return new TakeAnItem( new ArrayList<Item>(), null );
    }

    @Test
    public void label_is_take_an_item() {
        assertEquals( "Take an item", createAction().label() );
    }

    @Test
    public void user_must_choose_follow_up_action_is_always_true() {
        assertTrue( createAction().userMustChooseFollowUpAction() );
    }

    @Test
    public void follow_up_actions_contains_TakeSpecificItem_actions_for_each_location_item() {
        final List<Item> items = new ArrayList<Item>();
        Item item1 = mockery.mock( Item.class, "item 1" );
        Item item2 = mockery.mock( Item.class, "item 2" );
        items.add( item1 );
        items.add( item2 );
        TakeAnItem action = new TakeAnItem( items, null );

        List<Action> actions = action.followUpActions();
        assertEquals( 2, actions.size() );
        assertTrue( actions.get(0) instanceof TakeSpecificItem );
        assertEquals( item1, ((TakeSpecificItem)actions.get(0)).item() );
        assertTrue( actions.get(1) instanceof TakeSpecificItem );
        assertEquals( item2, ((TakeSpecificItem)actions.get(1)).item() );
    }

    @Test
    public void user_inventory_is_passed_to_TakeSpecificItem_follow_up_actions() {
        final List<Item> items = new ArrayList<Item>();
        Item item = mockery.mock( Item.class, "item 1" );
        items.add( item );
        UserInventory inventory = mockery.mock( UserInventory.class );
        TakeAnItem action = new TakeAnItem( items, inventory );

        List<Action> actions = action.followUpActions();
        assertEquals( inventory, ((TakeSpecificItem)actions.get(0)).inventory() );
    }

    @Test
    public void two_objects_with_the_same_value_should_be_equal() {
        TakeAnItem object1 = createAction();
        TakeAnItem object2 = createAction();

        assertEquals( createAction(), createAction() );
    }

    @Test
    public void a_show_inventory_object_is_not_equal_to_a_non_object() {
        TakeAnItem object = createAction();
        Object notATakeAnItem = new Object();

        assertNotEquals( object, notATakeAnItem );
    }

    @Test
    public void two_objects_with_the_same_value_should_have_the_same_hashcode() {
        TakeAnItem object1 = createAction();
        TakeAnItem object2 = createAction();

        assertEquals( object1.hashCode(), object2.hashCode() );
    }
}

