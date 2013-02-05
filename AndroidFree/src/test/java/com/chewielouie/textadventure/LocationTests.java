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
}

