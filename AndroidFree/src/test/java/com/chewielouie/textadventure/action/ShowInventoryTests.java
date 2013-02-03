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

    @Test
    public void user_must_choose_follow_up_action_is_always_true() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        ShowInventory action = new ShowInventory( model );

        assertTrue( action.userMustChooseFollowUpAction() );
    }

    @Test
    public void follow_up_actions_contains_Examine_actions_for_each_inventory_item() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        ShowInventory action = new ShowInventory( model );
        final List<Item> items = new ArrayList<Item>();
        Item item1 = mockery.mock( Item.class, "item 1" );
        Item item2 = mockery.mock( Item.class, "item 2" );
        items.add( item1 );
        items.add( item2 );
        mockery.checking( new Expectations() {{
            oneOf( model ).inventoryItems();
            will( returnValue( items ) );
            ignoring( model );
        }});

        action.trigger();
        List<Action> actions = action.followUpActions();
        assertEquals( 2, actions.size() );
        assertTrue( actions.get(0) instanceof Examine );
        assertEquals( item1, ((Examine)actions.get(0)).item() );
        assertTrue( actions.get(1) instanceof Examine );
        assertEquals( item2, ((Examine)actions.get(1)).item() );
    }
}

