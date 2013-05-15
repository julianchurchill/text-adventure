package com.chewielouie.textadventure.action;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.UserInventory;

@RunWith(JMock.class)
public class InventoryItemTests {

    private Mockery mockery = new Mockery();

    InventoryItem createAction() {
        return new InventoryItem( null, null, null, null );
    }

    @Test
    public void name_is_correct() {
        assertEquals( "inventory item", createAction().name() );
    }

    @Test
    public void label_is_item_name() {
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            allowing( item ).name();
            will( returnValue( "Item name" ) );
            ignoring( item );
        }});
        InventoryItem action = new InventoryItem( item, null, null, null );

        assertEquals( "Item name", action.label() );
    }

    @Test
    public void user_must_choose_follow_up_action_is_always_true() {
        assertTrue( createAction().userMustChooseFollowUpAction() );
    }

    @Test
    public void follow_up_actions_are_empty_if_item_is_null() {
        final ActionFactory actionFactory = mockery.mock( ActionFactory.class );
        mockery.checking( new Expectations() {{
            ignoring( actionFactory );
        }});
        InventoryItem action = new InventoryItem( null, null, null, actionFactory );

        assertEquals( 0, action.followUpActions().size() );
    }

    @Test
    public void follow_up_actions_use_action_factory_to_get_Examine_action() {
        final ActionFactory actionFactory = mockery.mock( ActionFactory.class );
        final Action examineAction = mockery.mock( Action.class );
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( actionFactory ).createExamineAction( item );
            will( returnValue( examineAction ) );
            ignoring( actionFactory );
            ignoring( examineAction );
        }});
        InventoryItem action = new InventoryItem( item, null, null, actionFactory );

        List<Action> actions = action.followUpActions();
        assertTrue( actions.size() > 1 );
        assertThat( actions.get( 0 ), is( examineAction ) );
    }

    @Test
    public void follow_up_actions_use_action_factory_to_get_UseWith_action() {
        final ActionFactory actionFactory = mockery.mock( ActionFactory.class );
        final Action useWithAction = mockery.mock( Action.class );
        final Item item = mockery.mock( Item.class );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            oneOf( actionFactory ).createUseWithAction( item, inventory, location );
            will( returnValue( useWithAction ) );
            ignoring( actionFactory );
            ignoring( useWithAction );
        }});
        InventoryItem action = new InventoryItem( item, inventory,
                                                  location, actionFactory );

        List<Action> actions = action.followUpActions();
        assertTrue( actions.size() > 1 );
        assertThat( actions.get( 1 ), is( useWithAction ) );
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

