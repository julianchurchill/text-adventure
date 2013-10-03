package com.chewielouie.textadventure.action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.UserInventory;
import com.chewielouie.textadventure.TextAdventureModel;

@RunWith(JMock.class)
public class ShowInventoryTests {

    private Mockery mockery = new Mockery();

    @Test
    public void name_is_correct() {
        assertEquals( "show inventory", new ShowInventory( null, null, null ).name() );
    }

    @Test
    public void two_objects_with_the_same_value_should_be_equal() {
        ShowInventory object1 = new ShowInventory( null, null, null );
        ShowInventory object2 = new ShowInventory( null, null, null );

        assertEquals( object1, object2 );
    }

    @Test
    public void a_show_inventory_object_is_not_equal_to_a_non_object() {
        ShowInventory object = new ShowInventory( null, null, null );
        Object notAShowInventory = new Object();

        assertNotEquals( object, notAShowInventory );
    }

    @Test
    public void two_objects_with_the_same_value_should_have_the_same_hashcode() {
        ShowInventory object1 = new ShowInventory( null, null, null );
        ShowInventory object2 = new ShowInventory( null, null, null );

        assertEquals( object1.hashCode(), object2.hashCode() );
    }

    @Test
    public void user_must_choose_follow_up_action_is_always_true() {
        ShowInventory action = new ShowInventory( null, null, null );

        assertTrue( action.userMustChooseFollowUpAction() );
    }

    List<Item> list( Item item1, Item item2  ) {
        List<Item> items = new ArrayList<Item>();
        items.add( item1 );
        items.add( item2 );
        return items;
    }

    @Test
    public void only_visible_inventory_items_are_turned_into_inventory_item_actions() {
        Item visibleItem = mock( Item.class );
        when( visibleItem.visible() ).thenReturn( true );
        Item invisibleItem = mock( Item.class );
        when( invisibleItem.visible() ).thenReturn( false );
        UserInventory inventory = mock( UserInventory.class );
        when( inventory.inventoryItems() ).thenReturn( list( visibleItem, invisibleItem ) );
        ActionFactory actionFactory = mock( ActionFactory.class );
        ShowInventory showInventory =
            new ShowInventory( inventory, null, actionFactory );

        showInventory.trigger();
        showInventory.followUpActions();

        verify( actionFactory ).createInventoryItemAction( visibleItem, inventory, null );
        verify( actionFactory, never() ).createInventoryItemAction( invisibleItem, inventory, null );
    }

    @Test
    public void follow_up_actions_use_action_factory_to_get_inventory_item_actions() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final ActionFactory actionFactory = mockery.mock( ActionFactory.class );
        final Action action1 = mockery.mock( Action.class, "action 1" );
        final Action action2 = mockery.mock( Action.class, "action 2" );
        final Item item1 = mockery.mock( Item.class, "item 1" );
        final Item item2 = mockery.mock( Item.class, "item 2" );
        final List<Item> items = new ArrayList<Item>();
        items.add( item1 );
        items.add( item2 );
        mockery.checking( new Expectations() {{
            oneOf( actionFactory ).createInventoryItemAction( item1, inventory, location );
            will( returnValue( action1 ) );
            oneOf( actionFactory ).createInventoryItemAction( item2, inventory, location );
            will( returnValue( action2 ) );
            ignoring( actionFactory );
            allowing( inventory ).inventoryItems();
            will( returnValue( items ) );
            ignoring( inventory );
            allowing( model ).currentLocation();
            will( returnValue( location ) );
            ignoring( model );
            allowing( item1 ).visible();
            will( returnValue( true ) );
            ignoring( item1 );
            allowing( item2 ).visible();
            will( returnValue( true ) );
            ignoring( item2 );
            ignoring( location );
        }});
        ShowInventory showInventory =
            new ShowInventory( inventory, model, actionFactory );

        showInventory.trigger();

        List<Action> actions = showInventory.followUpActions();
        assertThat( actions.size(), is( 2 ) );
        assertThat( actions.get( 0 ), is( action1 ) );
        assertThat( actions.get( 1 ), is( action2 ) );
    }

    @Test
    public void user_text_is_not_available() {
        ShowInventory action = new ShowInventory( null, null, null );

        assertFalse( action.userTextAvailable() );
    }
}

