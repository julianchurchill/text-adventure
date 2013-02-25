package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.TakeAnItem;
import com.chewielouie.textadventure.action.ExamineAnItem;

@RunWith(JMock.class)
public class LocationTests {

    private Mockery mockery = new Mockery();

    Location createLocation() {
        return new Location( "", "", null, null );
    }

    @Test
    public void id_is_set_on_construction() {
        Location l = new Location( "id", "", null, null );
        assertEquals( "id", l.id() );
    }

    @Test
    public void description_is_set_on_construction() {
        Location l = new Location( "", "description", null, null );
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
        items.add( new NormalItem( "name", "description" ) );

        Location l = createLocation();
        l.addItem( new NormalItem( "name", "description" ) );

        assertEquals( items, l.items() );
    }

    @Test
    public void location_actions_include_take_an_item_when_location_has_item() {
        Location l = createLocation();
        l.addItem( new NormalItem( "name", "description" ) );

        boolean actionsIncludeTakeAnItemAction = false;
        for( Action a : l.actions() )
            if( a instanceof TakeAnItem )
                actionsIncludeTakeAnItemAction = true;
        assertTrue( actionsIncludeTakeAnItemAction );
    }

    @Test
    public void location_action_to_take_an_item_is_created_with_location_items() {
        Location l = createLocation();
        l.addItem( new NormalItem( "name", "description" ) );
        List<Item> items = new ArrayList<Item>();
        items.add( new NormalItem( "name", "description" ) );

        for( Action a : l.actions() )
            if( a instanceof TakeAnItem )
                assertEquals( items, ((TakeAnItem)a).items() );
    }

    @Test
    public void location_action_to_take_an_item_does_not_include_untakeable_items() {
        Location l = createLocation();
        NormalItem item1 = new NormalItem( "name", "description" );
        NormalItem item2 = new NormalItem( "name", "description" );
        item1.setUntakeable();
        l.addItem( item1 );
        l.addItem( item2 );

        for( Action a : l.actions() )
            if( a instanceof TakeAnItem )
                assertEquals( 1, ((TakeAnItem)a).items().size() );
    }

    @Test
    public void location_action_to_take_an_item_is_not_included_if_only_untakeable_items_available() {
        Location l = createLocation();
        NormalItem item = new NormalItem( "name", "description" );
        item.setUntakeable();
        l.addItem( item );

        for( Action a : l.actions() )
            if( a instanceof TakeAnItem )
                fail("TakeAnItem action is not needed by this location as it has no takeable items!");
    }

    @Test
    public void location_action_to_take_an_item_has_user_inventory_passed_to_it() {
        UserInventory inventory = mockery.mock( UserInventory.class );
        Location l = new Location( "", "", inventory, null );
        l.addItem( new NormalItem( "name", "description" ) );

        assertEquals( inventory, ((TakeAnItem)l.actions().get( 0 )).inventory() );
    }

    @Test
    public void location_action_to_take_an_item_has_location_passed_to_it() {
        UserInventory inventory = mockery.mock( UserInventory.class );
        Location l = new Location( "", "", inventory, null );
        l.addItem( new NormalItem( "name", "description" ) );

        assertEquals( l, ((TakeAnItem)l.actions().get( 0 )).location() );
    }

    @Test
    public void location_actions_include_examine_an_item_when_location_has_item() {
        Location l = createLocation();
        l.addItem( new NormalItem( "name", "description" ) );

        boolean actionsIncludeExamineAnItemAction = false;
        for( Action a : l.actions() )
            if( a instanceof ExamineAnItem )
                actionsIncludeExamineAnItemAction = true;
        assertTrue( actionsIncludeExamineAnItemAction );
    }

    @Test
    public void location_action_to_examine_an_item_is_created_with_location_items() {
        Location l = createLocation();
        l.addItem( new NormalItem( "name", "description" ) );
        List<Item> items = new ArrayList<Item>();
        items.add( new NormalItem( "name", "description" ) );

        for( Action a : l.actions() )
            if( a instanceof ExamineAnItem )
                assertEquals( items, ((ExamineAnItem)a).items() );
    }

    @Test
    public void added_items_are_added_to_location_description() {
        Location l = new Location( "", "Location description.", null, null );
        l.addItem( new NormalItem( "name", "description" ) );
        l.addItem( new NormalItem( "name2", "description" ) );
        l.addItem( new NormalItem( "name3", "description" ) );

        assertEquals( "Location description.\nThere is a name, a name2 and a name3 here.", l.description() );
    }

    @Test
    public void removing_all_items_from_a_location_removes_TakeAnItem_action_from_action_list() {
        Item item = new NormalItem( "name", "description" );
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
    public void removing_all_items_from_a_location_removes_ExamineAnItem_action_from_action_list() {
        Item item = new NormalItem( "name", "description" );
        Location l = createLocation();
        l.addItem( item );
        l.removeItem( item );

        for( Action a : l.actions() )
            if( a instanceof ExamineAnItem )
                fail("ExamineAnItem action is not needed by this location as it has no items!");
    }

    @Test
    public void a_location_without_items_does_not_need_a_ExamineAnItem_action() {
        Location l = createLocation();

        for( Action a : l.actions() )
            if( a instanceof ExamineAnItem )
                fail("ExamineAnItem action is not needed by this location as it has no items!");
    }

    // these Exit tests should be removed once the ModelLocationDeserialiser
    // uses a testable exit factory
    @Test
    public void deserialise_extracts_exit() {
        Location l = createLocation();
        l.deserialise( "location id:name\n" +
                       "exit label:label\n" +
                       "exit destination:destination\n" +
                       "exit direction hint:East" );
        assertEquals( "label", l.visibleExits().get(0).label() );
        assertEquals( "destination", l.visibleExits().get(0).destination() );
        assertEquals( Exit.DirectionHint.East, l.visibleExits().get(0).directionHint() );
    }

    @Test
    public void deserialise_exit_direction_hint_is_optional() {
        Location l = createLocation();
        l.deserialise( "location id:name\n" +
                       "exit label:label\n" +
                       "exit destination:destination" );
        assertEquals( "label", l.visibleExits().get(0).label() );
        assertEquals( "destination", l.visibleExits().get(0).destination() );
        assertEquals( Exit.DirectionHint.DontCare, l.visibleExits().get(0).directionHint() );
    }

    @Test
    public void deserialise_exit_is_visible_by_default() {
        Location l = createLocation();
        l.deserialise( "location id:name\n" +
                       "exit label:label\n" +
                       "exit destination:destination" );
        assertTrue( l.visibleExits().get(0).visible() );
    }

    @Test
    public void deserialise_extracts_exit_is_not_visible() {
        Location l = createLocation();
        l.deserialise( "location id:name\n" +
                       "exit label:label\n" +
                       "exit destination:destination\n" +
                       "exit is not visible:" );
        assertFalse( l.exitsIncludingInvisibleOnes().get(0).visible() );
    }

    @Test
    public void deserialise_extracts_exit_is_not_visible_for_relevant_exit_only() {
        Location l = createLocation();
        l.deserialise( "location id:name\n" +
                       "exit label:label1\n" +
                       "exit destination:destination1\n" +
                       "exit label:label2\n" +
                       "exit destination:destination2\n" +
                       "exit is not visible:" );
        assertTrue( l.exitsIncludingInvisibleOnes().get(0).visible() );
        assertFalse( l.exitsIncludingInvisibleOnes().get(1).visible() );
    }

    @Test
    public void deserialise_extracts_exit_id() {
        Location l = createLocation();
        l.deserialise( "location id:name\n" +
                       "exit label:label\n" +
                       "exit destination:destination\n" +
                       "exit id:exit id" );
        assertEquals( "exit id", l.visibleExits().get(0).id() );
    }

    @Test
    public void deserialise_extracts_multiple_exits() {
        Location l = createLocation();
        l.deserialise( "location id:name\n" +
                       "exit label:label1\n" +
                       "exit destination:destination\n" +
                       "exit direction hint:East\n" +
                       "exit label:label2\n" +
                       "exit destination:destination\n" +
                       "exit direction hint:North" );
        assertEquals( "label2", l.visibleExits().get(1).label() );
        assertEquals( "destination", l.visibleExits().get(1).destination() );
        assertEquals( Exit.DirectionHint.North, l.visibleExits().get(1).directionHint() );
    }
}

