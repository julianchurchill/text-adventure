package com.chewielouie.textadventure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.item.Item;

@RunWith(JMock.class)
public class BasicModelTests {

    private Mockery mockery = new Mockery();

    @Test
    public void asking_for_the_current_location_before_one_is_set_returns_a_default_null_location() {
        BasicModel model = new BasicModel();

        assertTrue( model.currentLocation() instanceof NullLocation );
    }

    @Test
    public void set_current_location_by_id_changes_the_current_location() {
        ModelLocation loc1 = mock( ModelLocation.class );
        when( loc1.id() ).thenReturn( "loc1id" );
        ModelLocation loc2 = mock( ModelLocation.class );
        when( loc2.id() ).thenReturn( "loc2id" );
        BasicModel model = new BasicModel();
        model.addLocation( loc1 );
        model.addLocation( loc2 );

        model.setCurrentLocation( "loc2id" );

        assertThat( model.currentLocation(), is( loc2 ) );
    }

    @Test
    public void first_location_added_is_the_starting_location() {
        final ModelLocation loc1 = mockery.mock( ModelLocation.class, "loc1" );
        final ModelLocation loc2 = mockery.mock( ModelLocation.class, "loc2" );
        mockery.checking( new Expectations() {{
            ignoring( loc1 );
            ignoring( loc2 );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( loc1 );
        model.addLocation( loc2 );

        assertEquals( loc1, model.currentLocation() );
    }

    @Test
    public void leaving_a_location_changes_the_current_location() {
        final ModelLocation loc1 = mockery.mock( ModelLocation.class, "loc1" );
        final ModelLocation loc2 = mockery.mock( ModelLocation.class, "loc2" );
        final Exit north = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            oneOf( loc1 ).exitable( north );
            will( returnValue( true ) );
            oneOf( loc1 ).exitDestinationFor( north );
            will( returnValue( "loc2" ) );
            ignoring( loc1 );
            allowing( loc2 ).id();
            will( returnValue( "loc2" ) );
            ignoring( loc2 );
            ignoring( north );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( loc1 );
        model.addLocation( loc2 );

        model.moveThroughExit( north );

        assertEquals( loc2, model.currentLocation() );
    }

    @Test
    public void leaving_a_location_by_an_invalid_exit_does_not_change_the_current_location() {
        final ModelLocation loc1 = mockery.mock( ModelLocation.class );
        final Exit notAValidExit = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            oneOf( loc1 ).exitable( notAValidExit );
            will( returnValue( false ) );
            ignoring( loc1 );
            ignoring( notAValidExit );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( loc1 );

        model.moveThroughExit( notAValidExit );

        assertEquals( loc1, model.currentLocation() );
    }

    @Test
    public void leaving_a_location_notifies_it() {
        final ModelLocation loc1 = mockery.mock( ModelLocation.class, "loc1" );
        final ModelLocation loc2 = mockery.mock( ModelLocation.class, "loc2" );
        final Exit north = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            allowing( loc1 ).exitable( north );
            will( returnValue( true ) );
            allowing( loc1 ).exitDestinationFor( north );
            will( returnValue( "loc2" ) );
            oneOf( loc1 ).exited();
            ignoring( loc1 );
            allowing( loc2 ).id();
            will( returnValue( "loc2" ) );
            ignoring( loc2 );
            ignoring( north );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( loc1 );
        model.addLocation( loc2 );

        model.moveThroughExit( north );
    }

    @Test
    public void current_location_description_is_taken_from_the_current_location() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final String description = "description of this location";
        mockery.checking( new Expectations() {{
            oneOf( location ).description();
            will( returnValue( description ) );
            ignoring( location );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( location );

        assertEquals( description, model.currentLocationDescription() );
    }

    @Test
    public void if_location_description_changes_while_in_room_old_description_continues_to_be_used() {
        ModelLocation location = mock( ModelLocation.class );
        when( location.description() ).thenReturn( "old description", "new description" );
        BasicModel model = new BasicModel();
        model.addLocation( location );

        assertThat( model.currentLocationDescription(), is( "old description" ) );
        assertThat( model.currentLocationDescription(), is( "old description" ) );
    }

    @Test
    public void if_location_changes_new_location_description_is_used() {
        ModelLocation location1 = mock( ModelLocation.class );
        when( location1.description() ).thenReturn( "description 1" );
        ModelLocation location2 = mock( ModelLocation.class );
        when( location2.description() ).thenReturn( "description 2" );
        when( location2.id() ).thenReturn( "loc2id" );
        BasicModel model = new BasicModel();
        model.addLocation( location1 );
        model.addLocation( location2 );

        model.currentLocationDescription();
        model.setCurrentLocation( "loc2id" );

        assertThat( model.currentLocationDescription(), is( "description 2" ) );
    }

    @Test
    public void available_items_text_is_taken_from_the_current_location() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final String itemsText = "items text";
        mockery.checking( new Expectations() {{
            oneOf( location ).availableItemsText();
            will( returnValue( itemsText ) );
            ignoring( location );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( location );

        assertEquals( itemsText, model.availableItemsText() );
    }

    @Test
    public void current_location_exits_are_taken_from_the_current_location() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final List<Exit> exits = new ArrayList<Exit>();
        final Exit exit1 = mockery.mock( Exit.class, "exit1" );
        final Exit exit2 = mockery.mock( Exit.class, "exit2" );
        exits.add( exit1 );
        exits.add( exit2 );
        mockery.checking( new Expectations() {{
            oneOf( location ).visibleExits();
            will( returnValue( exits ) );
            ignoring( location );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( location );

        assertEquals( exits, model.currentLocationExits() );
    }

    @Test
    public void adding_an_item_to_the_inventory_actually_adds_it() {
        final Item item = mockery.mock( Item.class );
        BasicModel model = new BasicModel();
        int sizeBeforeAdd = model.inventoryItems().size();
        mockery.checking( new Expectations() {{
            ignoring( item );
        }});
        model.addToInventory( item );

        assertEquals( sizeBeforeAdd+1, model.inventoryItems().size() );
        assertEquals( item, model.inventoryItems().get(sizeBeforeAdd) );
    }

    @Test
    public void find_exit_by_id_finds_the_exit() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final List<Exit> exits = new ArrayList<Exit>();
        final Exit exit = mockery.mock( Exit.class );
        exits.add( exit );
        mockery.checking( new Expectations() {{
            oneOf( location ).exitsIncludingInvisibleOnes();
            will( returnValue( exits ) );
            ignoring( location );
            oneOf( exit ).id();
            will( returnValue( "exit id" ) );
            ignoring( exit );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( location );

        assertEquals( exit, model.findExitByID( "exit id" ) );
    }

    @Test
    public void find_exit_by_id_returns_null_if_it_cant_find_the_exit() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final List<Exit> exits = new ArrayList<Exit>();
        final Exit exit = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            oneOf( location ).exitsIncludingInvisibleOnes();
            will( returnValue( exits ) );
            ignoring( location );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( location );

        assertEquals( null, model.findExitByID( "exit id" ) );
    }

    @Test
    public void find_location_by_id_finds_the_location() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            oneOf( location ).id();
            will( returnValue( "location id" ) );
            ignoring( location );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( location );

        assertEquals( location, model.findLocationByID( "location id" ) );
    }

    @Test
    public void find_location_by_id_returns_null_if_it_cant_find_the_location() {
        BasicModel model = new BasicModel();

        assertEquals( null, model.findLocationByID( "location id" ) );
    }

    @Test
    public void destroy_item_removes_it_from_the_inventory() {
        final Item itemToDestroy = mockery.mock( Item.class, "item to destroy" );
        final Item itemToLeave = mockery.mock( Item.class, "item to leave" );
        mockery.checking( new Expectations() {{
            allowing( itemToDestroy ).id();
            will( returnValue( "itemid" ) );
            ignoring( itemToDestroy );
            allowing( itemToLeave ).id();
            will( returnValue( "otheritemid" ) );
            ignoring( itemToLeave );
        }});
        BasicModel model = new BasicModel();
        model.addToInventory( itemToDestroy );
        model.addToInventory( itemToLeave );

        model.destroyItem( "itemid" );

        assertEquals( 1, model.inventoryItems().size() );
        assertEquals( itemToLeave, model.inventoryItems().get( 0 ) );
    }

    @Test
    public void destroy_item_removes_it_from_the_current_location() {
        final Item itemToDestroy = mockery.mock( Item.class, "item to destroy" );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final ArrayList<Item> items = new ArrayList<Item>();
        items.add( itemToDestroy );
        mockery.checking( new Expectations() {{
            allowing( itemToDestroy ).id();
            will( returnValue( "itemid" ) );
            ignoring( itemToDestroy );
            allowing( location ).items();
            will( returnValue( items ) );
            oneOf( location ).removeItem( itemToDestroy );
            ignoring( location );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( location );

        model.destroyItem( "itemid" );
    }

    @Test
    public void destroy_item_removes_it_from_any_location() {
        final Item itemToDestroy = mockery.mock( Item.class, "item to destroy" );
        final ModelLocation currentLocation =
            mockery.mock( ModelLocation.class, "loc1" );
        final ModelLocation location2 = mockery.mock( ModelLocation.class, "loc2" );
        final ArrayList<Item> items = new ArrayList<Item>();
        items.add( itemToDestroy );
        mockery.checking( new Expectations() {{
            allowing( itemToDestroy ).id();
            will( returnValue( "itemid" ) );
            ignoring( itemToDestroy );
            ignoring( currentLocation );
            allowing( location2 ).items();
            will( returnValue( items ) );
            oneOf( location2 ).removeItem( itemToDestroy );
            ignoring( location2 );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( currentLocation );
        model.addLocation( location2 );

        model.destroyItem( "itemid" );
    }

    @Test
    public void find_item_by_id_finds_items_in_inventory() {
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            allowing( item ).id();
            will( returnValue( "itemid" ) );
            ignoring( item );
        }});
        BasicModel model = new BasicModel();
        model.addToInventory( item );

        assertEquals( item, model.findItemByID( "itemid" ) );
    }

    @Test
    public void find_item_by_id_finds_items_in_current_location() {
        final Item item = mockery.mock( Item.class );
        final ModelLocation currentLocation =
            mockery.mock( ModelLocation.class, "loc1" );
        final ArrayList<Item> items = new ArrayList<Item>();
        items.add( item );
        mockery.checking( new Expectations() {{
            allowing( item ).id();
            will( returnValue( "itemid" ) );
            ignoring( item );
            allowing( currentLocation ).items();
            will( returnValue( items ) );
            ignoring( currentLocation );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( currentLocation );

        assertEquals( item, model.findItemByID( "itemid" ) );
    }

    @Test
    public void find_item_by_id_finds_items_in_any_location() {
        final Item item = mockery.mock( Item.class );
        final ModelLocation currentLocation =
            mockery.mock( ModelLocation.class, "loc1" );
        final ModelLocation location2 = mockery.mock( ModelLocation.class, "loc2" );
        final ArrayList<Item> items = new ArrayList<Item>();
        items.add( item );
        mockery.checking( new Expectations() {{
            allowing( item ).id();
            will( returnValue( "itemid" ) );
            ignoring( item );
            ignoring( currentLocation );
            allowing( location2 ).items();
            will( returnValue( items ) );
            ignoring( location2 );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( currentLocation );
        model.addLocation( location2 );

        assertEquals( item, model.findItemByID( "itemid" ) );
    }

    @Test
    public void setting_the_current_score_allows_it_to_be_retrieved() {
        BasicModel model = new BasicModel();
        model.setCurrentScore( 23 );

        assertEquals( 23, model.currentScore() );
    }

    @Test
    public void setting_the_maximum_score_allows_it_to_be_retrieved() {
        BasicModel model = new BasicModel();
        model.setMaximumScore( 286 );

        assertEquals( 286, model.maximumScore() );
    }

    @Test
    public void current_area_name_is_mapped_from_current_location_area_id() {
        ModelLocation loc1 = mock( ModelLocation.class );
        when( loc1.areaID() ).thenReturn( "area-id" );
        BasicModel model = new BasicModel();
        model.addLocation( loc1 );

        model.addLocationArea( "area-id", "area name" );

        assertThat( model.currentLocationAreaName(), is( "area name" ) );
    }

    @Test
    public void area_ids_match_those_added() {
        ModelLocation loc1 = mock( ModelLocation.class );
        when( loc1.areaID() ).thenReturn( "area-id1" );
        ModelLocation loc2 = mock( ModelLocation.class );
        when( loc2.areaID() ).thenReturn( "area-id2" );
        BasicModel model = new BasicModel();
        model.addLocation( loc1 );
        model.addLocation( loc2 );

        model.addLocationArea( "area-id1", "area name" );
        model.addLocationArea( "area-id2", "area name" );

        assertThat( model.areaIDs(), hasItem( "area-id1" ) );
        assertThat( model.areaIDs(), hasItem( "area-id2" ) );
    }

    @Test
    public void subscribers_receive_current_location_change_events() {
        final ModelLocation loc1 = mockery.mock( ModelLocation.class, "loc1" );
        final ModelLocation loc2 = mockery.mock( ModelLocation.class, "loc2" );
        final Exit north = mockery.mock( Exit.class );
        final MovementEventSubscriber movementEventSubscriber =
            mockery.mock( MovementEventSubscriber.class );
        mockery.checking( new Expectations() {{
            allowing( loc1 ).exitable( north );
            will( returnValue( true ) );
            allowing( loc1 ).exitDestinationFor( north );
            will( returnValue( "loc2" ) );
            ignoring( loc1 );
            allowing( loc2 ).id();
            will( returnValue( "loc2" ) );
            ignoring( loc2 );
            ignoring( north );
            oneOf( movementEventSubscriber ).currentLocationChanged();
            ignoring( movementEventSubscriber );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( loc1 );
        model.addLocation( loc2 );
        model.subscribeForEvents( movementEventSubscriber );

        model.moveThroughExit( north );
    }

    @Test
    public void subscribers_do_not_receive_current_location_change_event_on_failed_exit() {
        final Exit north = mockery.mock( Exit.class );
        final MovementEventSubscriber movementEventSubscriber =
            mockery.mock( MovementEventSubscriber.class );
        mockery.checking( new Expectations() {{
            never( movementEventSubscriber ).currentLocationChanged();
            ignoring( north );
        }});
        BasicModel model = new BasicModel();
        model.subscribeForEvents( movementEventSubscriber );

        model.moveThroughExit( north );
    }

    @Test
    public void subscribers_get_change_location_event_on_set_current_location() {
        ModelLocation loc1 = mock( ModelLocation.class );
        when( loc1.id() ).thenReturn( "loc1id" );
        ModelLocation loc2 = mock( ModelLocation.class );
        when( loc2.id() ).thenReturn( "loc2id" );
        MovementEventSubscriber movementEventSubscriber = mock( MovementEventSubscriber.class );
        BasicModel model = new BasicModel();
        model.addLocation( loc1 );
        model.addLocation( loc2 );
        model.subscribeForEvents( movementEventSubscriber );

        model.setCurrentLocation( "loc2id" );

        verify( movementEventSubscriber ).currentLocationChanged();
    }

    @Test
    public void contextual_text_is_taken_from_the_current_location() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final String text = "contextual text lalalala";
        mockery.checking( new Expectations() {{
            oneOf( location ).contextualText();
            will( returnValue( text ) );
            ignoring( location );
        }});
        BasicModel model = new BasicModel();
        model.addLocation( location );

        assertEquals( text, model.contextualText() );
    }

    @Test
    public void move_item_to_inventory_removes_it_from_the_owning_location() {
        Item item = mock( Item.class );
        when( item.id() ).thenReturn( "itemid" );
        ArrayList<Item> items = new ArrayList<Item>();
        items.add( item );
        ModelLocation loc1 = mock( ModelLocation.class );
        when( loc1.items() ).thenReturn( items );
        BasicModel model = new BasicModel();
        model.addLocation( loc1 );

        model.moveItemToInventory( "itemid" );

        verify( loc1 ).removeItem( item );
    }

    @Test
    public void move_item_to_inventory_adds_it_to_the_inventory() {
        Item item = mock( Item.class );
        when( item.id() ).thenReturn( "itemid" );
        ArrayList<Item> items = new ArrayList<Item>();
        items.add( item );
        ModelLocation loc1 = mock( ModelLocation.class );
        when( loc1.items() ).thenReturn( items );
        BasicModel model = new BasicModel();
        model.addLocation( loc1 );

        model.moveItemToInventory( "itemid" );

        assertThat( model.inventoryItems(), hasItem( item ) );
    }

    @Test
    public void items_are_not_duplicated_in_the_inventory() {
        Item item = mock( Item.class );
        when( item.id() ).thenReturn( "itemid" );
        BasicModel model = new BasicModel();

        model.addToInventory( item );
        model.addToInventory( item );

        assertThat( Collections.frequency( model.inventoryItems(), item ), is( 1 ) );
    }

    @Test
    public void move_through_exit_calls_exit_use() {
        Exit exit = mock( Exit.class );
        ModelLocation loc = mock( ModelLocation.class );
        when( loc.exitable( exit ) ).thenReturn( true );
        when( loc.exitDestinationFor( exit ) ).thenReturn( "loc" );
        BasicModel model = new BasicModel();
        model.addLocation( loc );

        model.moveThroughExit( exit );

        verify( exit ).use();
    }

}

