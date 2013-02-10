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
        Location l = new Location( "", "", inventory );
        l.addItem( new NormalItem( "name", "description" ) );

        assertEquals( inventory, ((TakeAnItem)l.actions().get( 0 )).inventory() );
    }

    @Test
    public void location_action_to_take_an_item_has_location_passed_to_it() {
        UserInventory inventory = mockery.mock( UserInventory.class );
        Location l = new Location( "", "", inventory );
        l.addItem( new NormalItem( "name", "description" ) );

        assertEquals( l, ((TakeAnItem)l.actions().get( 0 )).location() );
    }

    @Test
    public void added_items_are_added_to_location_description() {
        Location l = new Location( "", "Location description.", null );
        l.addItem( new NormalItem( "name", "description" ) );
        l.addItem( new NormalItem( "name2", "description" ) );
        l.addItem( new NormalItem( "name3", "description" ) );

        assertEquals( "Location description. There is a name, a name2 and a name3 here.", l.description() );
    }

    @Test
    public void removing_all_items_from_a_location_removes_TakeAnItem_action_from_action_list() {
        Item item = new NormalItem( "name", "description" );
        Location l = new Location( "", "Location description.", null );
        l.addItem( item );
        l.removeItem( item );

        for( Action a : l.actions() )
            if( a instanceof TakeAnItem )
                fail("TakeAnItem action is not needed by this location as it has no items!");
    }

    @Test
    public void a_location_without_items_does_not_need_a_TakeAnItem_action() {
        Location l = new Location( "", "Location description.", null );

        for( Action a : l.actions() )
            if( a instanceof TakeAnItem )
                fail("TakeAnItem action is not needed by this location as it has no items!");
    }

    @Test
    public void deserialise_finds_location_id() {
        Location l = new Location( "", "Location description.", null );
        l.deserialise( "location_id:name" );
        assertEquals( "name", l.id() );
    }

    @Test
    public void deserialise_strips_trailing_newlines_from_location_id() {
        Location l = new Location( "", "", null );
        l.deserialise( "location_id:name\n" );
        assertEquals( "name", l.id() );
    }

    @Test
    public void deserialise_finds_location_description() {
        Location l = new Location( "", "", null );
        l.deserialise( "location_id:name\n" +
                       "location description:You are in a room.\n" +
                       "It is a bit untidy." );
        assertEquals( "You are in a room.\n" +
                      "It is a bit untidy.", l.description() );
    }

    @Test
    public void deserialise_extracts_exit() {
        Location l = new Location( "", "", null );
        l.deserialise( "location_id:name\n" +
                       "exit label:label\n" +
                       "exit destination:destination\n" +
                       "exit direction hint:East" );
        assertEquals( "label", l.exits().get(0).label() );
        assertEquals( "destination", l.exits().get(0).destination() );
        assertEquals( Exit.DirectionHint.East, l.exits().get(0).directionHint() );
    }

    @Test
    public void deserialise_exit_direction_hint_is_optional() {
        Location l = new Location( "", "", null );
        l.deserialise( "location_id:name\n" +
                       "exit label:label\n" +
                       "exit destination:destination" );
        assertEquals( "label", l.exits().get(0).label() );
        assertEquals( "destination", l.exits().get(0).destination() );
        assertEquals( Exit.DirectionHint.DontCare, l.exits().get(0).directionHint() );
    }

    //@Test
    //public void deserialise_extracts_multiple_exits() {
}

