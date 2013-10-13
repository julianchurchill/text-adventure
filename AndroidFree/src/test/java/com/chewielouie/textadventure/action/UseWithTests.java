package com.chewielouie.textadventure.action;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static java.util.Arrays.asList;

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
        return new UseWith( null, null, null, null );
    }

    @Test
    public void name_is_correct() {
        assertEquals( "use with", createAction().name() );
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
    public void follow_up_actions_use_action_factory_to_get_UseWithSpecificItem_action_for_each_item_from_location() {
        final ActionFactory actionFactory = mockery.mock( ActionFactory.class );
        final Action locationAction = mockery.mock( Action.class );
        final Item item = mockery.mock( Item.class, "item" );
        final Item locationItem = mockery.mock( Item.class, "location item" );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            allowing( location ).items();
            will( returnValue( asList( locationItem ) ) );
            ignoring( location );
            allowing( locationItem ).visible(); will( returnValue( true ) );
            ignoring( locationItem );
            oneOf( actionFactory ).createUseWithSpecificItemAction( item, locationItem );
            will( returnValue( locationAction ) );
            ignoring( actionFactory );
        }});
        UseWith action = new UseWith( item, null, location, actionFactory );

        List<Action> actions = action.followUpActions();
        assertThat( actions.size(), is( greaterThan( 0 ) ) );
        assertThat( actions.get( 0 ), is( locationAction ) );
    }

    @Test
    public void follow_up_actions_include_only_visible_items_from_location() {
        final ActionFactory actionFactory = mockery.mock( ActionFactory.class );
        final Action locationAction = mockery.mock( Action.class );
        final Item item = mockery.mock( Item.class, "item" );
        final Item invisibleItem = mockery.mock( Item.class, "invisible item" );
        final Item visibleItem = mockery.mock( Item.class, "visible item" );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            allowing( location ).items();
            will( returnValue( asList( invisibleItem, visibleItem ) ) );
            ignoring( location );
            allowing( invisibleItem ).visible(); will( returnValue( false ) );
            ignoring( invisibleItem );
            allowing( visibleItem ).visible(); will( returnValue( true ) );
            ignoring( visibleItem );
            oneOf( actionFactory ).createUseWithSpecificItemAction( item, visibleItem );
            will( returnValue( locationAction ) );
            ignoring( actionFactory );
        }});
        new UseWith( item, null, location, actionFactory ).followUpActions();
    }

    @Test
    public void follow_up_actions_use_action_factory_to_get_UseWithSpecificItem_action_for_each_item_from_inventory() {
        final ActionFactory actionFactory = mockery.mock( ActionFactory.class );
        final Action inventoryAction = mockery.mock( Action.class );
        final Item item = mockery.mock( Item.class, "item" );
        final Item inventoryItem = mockery.mock( Item.class, "inventory item" );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        mockery.checking( new Expectations() {{
            allowing( inventory ).inventoryItems();
            will( returnValue( asList( inventoryItem ) ) );
            ignoring( inventory );
            oneOf( actionFactory ).createUseWithSpecificItemAction( item, inventoryItem );
            will( returnValue( inventoryAction ) );
            ignoring( actionFactory );
            allowing( inventoryItem ).visible();
            will( returnValue( true ) );
            ignoring( inventoryItem );
        }});
        UseWith action = new UseWith( item, inventory, null, actionFactory );

        List<Action> actions = action.followUpActions();
        assertThat( actions.size(), is( greaterThan( 0 ) ) );
        assertThat( actions.get( 0 ), is( inventoryAction ) );
    }

    Item makeVisibleItem() {
        Item item = mock( Item.class );
        when( item.visible() ).thenReturn( true );
        return item;
    }

    Item makeInvisibleItem() {
        Item item = mock( Item.class );
        when( item.visible() ).thenReturn( false );
        return item;
    }

    UserInventory makeInventoryWithItems( Item i1, Item i2 ) {
        UserInventory inventory = mock( UserInventory.class );
        when( inventory.inventoryItems() ).thenReturn( asList( i1, i2 ) );
        return inventory;
    }

    @Test
    public void follow_up_actions_include_only_visible_items_from_inventory() {
        Item item = mock( Item.class );
        Item invisibleItem = makeInvisibleItem();
        Item visibleItem = makeVisibleItem();
        UserInventory inventory = makeInventoryWithItems( invisibleItem, visibleItem );
        ActionFactory actionFactory = mock( ActionFactory.class );
        UseWith u = new UseWith( item, inventory, null, actionFactory );

        verify( actionFactory ).createUseWithSpecificItemAction( item, visibleItem );
        verify( actionFactory, never() ).createUseWithSpecificItemAction( item, invisibleItem );
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
