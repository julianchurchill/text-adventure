package com.chewielouie.textadventure;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionFactory;
import com.chewielouie.textadventure.action.TakeAnItem;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.NormalItem;

@RunWith(JMock.class)
public class LocationTests {

    private Mockery mockery = new Mockery();

    Location createLocation() {
        return new Location( "", "", null );
    }

    @Test
    public void id_is_set_on_construction() {
        Location l = new Location( "id", "", null );
        assertEquals( "id", l.id() );
    }

    @Test
    public void description_is_set_on_construction() {
        Location l = new Location( "", "description", null );
        assertEquals( "description", l.description() );
    }

    @Test
    public void added_exit_makes_the_exit_exitable() {
        final Exit exit = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            allowing( exit ).visible();
            will( returnValue( true ) );
            ignoring( exit );
        }});
        Location l = createLocation();
        l.addExit( exit );
        assertTrue( l.exitable( exit ) );
    }

    @Test
    public void non_visible_exits_are_not_exitable() {
        final Exit exit = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            allowing( exit ).visible();
            will( returnValue( false ) );
            ignoring( exit );
        }});
        Location l = createLocation();
        l.addExit( exit );
        assertFalse( l.exitable( exit ) );
    }

    @Test
    public void non_visible_exits_are_not_in_the_exits_list() {
        final Exit exit = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            allowing( exit ).visible();
            will( returnValue( false ) );
            ignoring( exit );
        }});
        Location l = createLocation();
        l.addExit( exit );
        assertEquals( 0, l.visibleExits().size() );
    }

    @Test
    public void exits_that_havent_been_added_are_not_exitable() {
        final Exit exit = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            ignoring( exit );
        }});
        Location l = createLocation();
        assertFalse( l.exitable( exit ) );
    }

    @Test
    public void exit_destination_is_retrievable() {
        final Exit exit = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            allowing( exit ).destination();
            will( returnValue( "destination" ) );
            ignoring( exit );
        }});
        Location l = createLocation();
        assertEquals( "destination", l.exitDestinationFor( exit ) );
    }

    @Test
    public void all_valid_exits_are_retrieveable() {
        final Exit exit1 = mockery.mock( Exit.class, "exit1" );
        final Exit exit2 = mockery.mock( Exit.class, "exit2" );
        mockery.checking( new Expectations() {{
            allowing( exit1 ).visible();
            will( returnValue( true ) );
            ignoring( exit1 );
            allowing( exit2 ).visible();
            will( returnValue( true ) );
            ignoring( exit2 );
        }});
        List<Exit> exits = new ArrayList<Exit>();
        exits.add( exit1 );
        exits.add( exit2 );

        Location l = createLocation();
        l.addExit( exit1 );
        l.addExit( exit2 );

        assertEquals( exits, l.visibleExits() );
    }

    @Test
    public void added_items_are_retrieveable() {
        List<Item> items = new ArrayList<Item>();
        items.add( new NormalItem() );

        Location l = createLocation();
        l.addItem( new NormalItem() );

        assertEquals( items, l.items() );
    }

    @Test
    public void location_actions_include_take_an_item_when_location_has_item() {
        Location l = createLocation();
        l.addItem( new NormalItem() );

        boolean actionsIncludeTakeAnItemAction = false;
        for( Action a : l.actions() )
            if( a instanceof TakeAnItem )
                actionsIncludeTakeAnItemAction = true;
        assertTrue( actionsIncludeTakeAnItemAction );
    }

    @Test
    public void location_action_to_take_an_item_is_created_with_location_items() {
        Location l = createLocation();
        l.addItem( new NormalItem() );
        List<Item> items = new ArrayList<Item>();
        items.add( new NormalItem() );

        for( Action a : l.actions() )
            if( a instanceof TakeAnItem )
                assertEquals( items, ((TakeAnItem)a).items() );
    }

    @Test
    public void location_action_to_take_an_item_does_not_include_untakeable_items() {
        Location l = createLocation();
        NormalItem item1 = new NormalItem();
        NormalItem item2 = new NormalItem();
        item1.setUntakeable();
        l.addItem( item1 );
        l.addItem( item2 );

        for( Action a : l.actions() )
            if( a instanceof TakeAnItem )
                assertEquals( 1, ((TakeAnItem)a).items().size() );
    }

    @Test
    public void location_action_to_take_an_item_does_not_include_invisible_items() {
        final Item visibleItem = mockery.mock( Item.class, "visible item" );
        final Item invisibleItem = mockery.mock( Item.class, "invisible item" );
        mockery.checking( new Expectations() {{
            allowing( visibleItem ).visible();
            will( returnValue( true ) );
            ignoring( visibleItem );
            allowing( invisibleItem ).visible();
            will( returnValue( false ) );
            ignoring( invisibleItem );
        }});
        Location l = createLocation();
        l.addItem( visibleItem );
        l.addItem( invisibleItem );

        List<Item> items = new ArrayList<Item>();
        items.add( visibleItem );

        for( Action a : l.actions() )
            if( a instanceof TakeAnItem )
                assertEquals( items, ((TakeAnItem)a).items() );
    }

    @Test
    public void location_action_to_take_an_item_is_not_included_if_only_untakeable_items_available() {
        Location l = createLocation();
        NormalItem item = new NormalItem();
        item.setUntakeable();
        l.addItem( item );

        for( Action a : l.actions() )
            if( a instanceof TakeAnItem )
                fail("TakeAnItem action is not needed by this location as it has no takeable items!");
    }

    @Test
    public void location_action_to_take_an_item_has_user_inventory_passed_to_it() {
        UserInventory inventory = mockery.mock( UserInventory.class );
        Location l = new Location( "", "", inventory );
        l.addItem( new NormalItem() );

        assertEquals( inventory, ((TakeAnItem)l.actions().get( 0 )).inventory() );
    }

    @Test
    public void location_action_to_take_an_item_has_location_passed_to_it() {
        UserInventory inventory = mockery.mock( UserInventory.class );
        Location l = new Location( "", "", inventory );
        l.addItem( new NormalItem() );

        assertEquals( l, ((TakeAnItem)l.actions().get( 0 )).location() );
    }

    @Test
    public void actions_uses_action_factory_to_create_ExamineAnItem_action_for_all_visible_items() {
        final Item visibleItem = mockery.mock( Item.class, "visible item" );
        final Item invisibleItem = mockery.mock( Item.class, "invisible item" );
        final ActionFactory actionFactory = mockery.mock( ActionFactory.class );
        final Action examineAnItemAction = mockery.mock( Action.class );
        final List<Item> visibleItems = new ArrayList<Item>();
        visibleItems.add( visibleItem );
        mockery.checking( new Expectations() {{
            allowing( visibleItem ).visible(); will( returnValue( true ) );
            ignoring( visibleItem );
            allowing( invisibleItem ).visible(); will( returnValue( false ) );
            ignoring( invisibleItem );
            oneOf( actionFactory ).createExamineAnItemAction( with( equal( visibleItems ) ) );
            will( returnValue( examineAnItemAction ) );
            ignoring( actionFactory );
        }});
        Location l = new Location( "", "", null, actionFactory );
        l.addItem( visibleItem );
        l.addItem( invisibleItem );

        List<Action> actions = l.actions();
        assertTrue( actions.size() > 0 );
        assertThat( actions.get( 0 ), is( examineAnItemAction ) );
    }

    @Test
    public void added_items_are_added_to_location_description() {
        Location l = new Location( "", "Location description.", null );
        NormalItem item1 = new NormalItem();
        item1.setName( "name" );
        l.addItem( item1 );
        NormalItem item2 = new NormalItem();
        item2.setName( "name2" );
        l.addItem( item2 );
        NormalItem item3 = new NormalItem();
        item3.setName( "name3" );
        l.addItem( item3 );

        assertEquals( "Location description.\nThere is a name, a name2 and a name3 here.", l.description() );
    }

    @Test
    public void description_includes_only_visible_items() {
        final Item visibleItem = mockery.mock( Item.class, "visible item" );
        final Item invisibleItem = mockery.mock( Item.class, "invisible item" );
        mockery.checking( new Expectations() {{
            allowing( visibleItem ).visible();
            will( returnValue( true ) );
            allowing( visibleItem ).countableNounPrefix();
            will( returnValue( "a" ) );
            allowing( visibleItem ).midSentenceCasedName();
            will( returnValue( "visible item" ) );
            ignoring( visibleItem );
            allowing( invisibleItem ).visible();
            will( returnValue( false ) );
            allowing( invisibleItem ).midSentenceCasedName();
            will( returnValue( "invisible item" ) );
            ignoring( invisibleItem );
        }});
        Location l = new Location( "", "Location description.", null );
        l.addItem( visibleItem );
        l.addItem( invisibleItem );

        assertEquals( "Location description.\nThere is a visible item here.", l.description() );
    }

    @Test
    public void removing_all_items_from_a_location_removes_TakeAnItem_action_from_action_list() {
        Item item = new NormalItem();
        Location l = createLocation();
        l.addItem( item );
        l.removeItem( item );

        for( Action a : l.actions() )
            if( a instanceof TakeAnItem )
                fail("TakeAnItem action is not needed by this location as it has no items!");
    }

    @Test
    public void a_location_without_items_does_not_need_a_TakeAnItem_action() {
        Location l = createLocation();

        for( Action a : l.actions() )
            if( a instanceof TakeAnItem )
                fail("TakeAnItem action is not needed by this location as it has no items!");
    }

    @Test
    public void location_coordinates_defaults_to_0_0() {
        Location l = createLocation();
        assertEquals( 0, l.x() );
        assertEquals( 0, l.y() );
    }

    @Test
    public void location_coordinates_can_be_set() {
        Location l = createLocation();
        l.setX( 5 );
        l.setY( 10 );
        assertEquals( 5,  l.x() );
        assertEquals( 10, l.y() );
    }
}

