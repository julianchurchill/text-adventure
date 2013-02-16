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
        Location l = createLocation();
        l.addExit( new Exit( "north", "loc2" ) );
        assertTrue( l.exitable( new Exit( "north", "loc2" ) ) );
    }

    @Test
    public void exits_that_havent_been_added_are_not_exitable() {
        Location l = createLocation();
        l.addExit( new Exit( "north", "loc2" ) );
        assertFalse( l.exitable( new Exit( "south", "loc1" ) ) );
    }

    @Test
    public void exit_destination_is_retrievable() {
        Exit north = new Exit( "north", "loc2" );
        Location l = createLocation();
        l.addExit( new Exit( "north", "loc2" ) );
        assertEquals( "loc2", l.exitDestinationFor( north ) );
    }

    @Test
    public void all_valid_exits_are_retrieveable() {
        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "north", "loc2" ) );
        exits.add( new Exit( "south", "loc3" ) );

        Location l = createLocation();
        l.addExit( new Exit( "north", "loc2" ) );
        l.addExit( new Exit( "south", "loc3" ) );

        assertEquals( exits, l.exits() );
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

    @Test
    public void deserialise_finds_location_id() {
        Location l = createLocation();
        l.deserialise( "location id:name" );
        assertEquals( "name", l.id() );
    }

    @Test
    public void deserialise_strips_trailing_newlines_from_location_id() {
        Location l = createLocation();
        l.deserialise( "location id:name\n" );
        assertEquals( "name", l.id() );
    }

    @Test
    public void deserialise_finds_location_description() {
        Location l = createLocation();
        l.deserialise( "location id:name\n" +
                       "location description:You are in a room.\n" +
                       "It is a bit untidy." );
        assertEquals( "You are in a room.\n" +
                      "It is a bit untidy.", l.description() );
    }

    @Test
    public void deserialise_extracts_location_description_up_to_exit_label() {
        Location l = createLocation();
        l.deserialise( "location id:name\n" +
                       "location description:You are in a room.\n" +
                       "It is a bit untidy.\n" +
                       "exit label:label\n" );
        assertEquals( "You are in a room.\n" +
                      "It is a bit untidy.\n", l.description() );
    }

    @Test
    public void deserialise_extracts_location_description_up_to_item() {
        Location l = createLocation();
        l.deserialise( "location id:name\n" +
                       "location description:You are in a room.\n" +
                       "It is a bit untidy.\n" +
                       "ITEM\n" );
        assertEquals( "You are in a room.\n" +
                      "It is a bit untidy.\n", l.description() );
    }

    @Test
    public void deserialise_extracts_exit() {
        Location l = createLocation();
        l.deserialise( "location id:name\n" +
                       "exit label:label\n" +
                       "exit destination:destination\n" +
                       "exit direction hint:East" );
        assertEquals( "label", l.exits().get(0).label() );
        assertEquals( "destination", l.exits().get(0).destination() );
        assertEquals( Exit.DirectionHint.East, l.exits().get(0).directionHint() );
    }

    @Test
    public void deserialise_exit_direction_hint_is_optional() {
        Location l = createLocation();
        l.deserialise( "location id:name\n" +
                       "exit label:label\n" +
                       "exit destination:destination" );
        assertEquals( "label", l.exits().get(0).label() );
        assertEquals( "destination", l.exits().get(0).destination() );
        assertEquals( Exit.DirectionHint.DontCare, l.exits().get(0).directionHint() );
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
        assertEquals( "label2", l.exits().get(1).label() );
        assertEquals( "destination", l.exits().get(1).destination() );
        assertEquals( Exit.DirectionHint.North, l.exits().get(1).directionHint() );
    }

    @Test
    public void deserialise_extracts_item() {
        final Item item = mockery.mock( Item.class );
        final ItemFactory itemFactory = mockery.mock( ItemFactory.class );
        Location l = new Location( "", "", null, itemFactory );

        mockery.checking( new Expectations() {{
            allowing( itemFactory ).create();
            will( returnValue( item ) );
            ignoring( itemFactory );
            oneOf( item ).deserialise( "item name:item content\n" +
                                       "and more item content" );
            ignoring( item );
        }});

        l.deserialise( "location id:name\n" +
                       "ITEM\nitem name:item content\n" +
                       "and more item content" );
    }

    @Test
    public void deserialise_extracts_multiple_items() {
        final Item item1 = mockery.mock( Item.class, "item1" );
        final Item item2 = mockery.mock( Item.class, "item2" );
        final ItemFactory itemFactory = mockery.mock( ItemFactory.class );
        Location l = new Location( "", "", null, itemFactory );

        mockery.checking( new Expectations() {{
            atLeast( 1 ).of( itemFactory ).create();
                will( onConsecutiveCalls(
                      returnValue( item1 ),
                      returnValue( item2 ) ) );
            ignoring( itemFactory );
            oneOf( item1 ).deserialise( "item 1 content\n" +
                                        "and more item content\n" );
            ignoring( item1 );
            oneOf( item2 ).deserialise( "item 2 content\n" +
                                        "and more item content\n" );
            ignoring( item2 );
        }});

        l.deserialise( "location id:name\n" +
                       "ITEM\nitem 1 content\n" +
                       "and more item content\n" +
                       "ITEM\nitem 2 content\n" +
                       "and more item content\n" );
    }

    @Test
    public void deserialise_adds_extracted_items_to_location() {
        final Item item1 = mockery.mock( Item.class, "item1" );
        final Item item2 = mockery.mock( Item.class, "item2" );
        final ItemFactory itemFactory = mockery.mock( ItemFactory.class );
        Location l = new Location( "", "", null, itemFactory );

        mockery.checking( new Expectations() {{
            atLeast( 1 ).of( itemFactory ).create();
                will( onConsecutiveCalls(
                      returnValue( item1 ),
                      returnValue( item2 ) ) );
            ignoring( itemFactory );
            ignoring( item1 );
            ignoring( item2 );
        }});

        l.deserialise( "location id:name\n" +
                       "ITEM\nitem 1 content\n" +
                       "and more item content\n" +
                       "ITEM\nitem 2 content\n" +
                       "and more item content\n" );
        assertEquals( item1, l.items().get(0) );
        assertEquals( item2, l.items().get(1) );
    }
}

