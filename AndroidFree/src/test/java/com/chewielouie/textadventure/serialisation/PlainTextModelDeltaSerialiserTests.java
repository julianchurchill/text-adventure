package com.chewielouie.textadventure.serialisation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.TextAdventureModel;

@RunWith(JMock.class)
public class PlainTextModelDeltaSerialiserTests {

    private Mockery mockery = new Mockery();

    @Test
    public void concatenates_inventory_item_ids_to_output() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final Item item1 = mockery.mock( Item.class, "item1" );
        final Item item2 = mockery.mock( Item.class, "item2" );
        final List<Item> inventoryItems = new ArrayList<Item>();
        inventoryItems.add( item1 );
        inventoryItems.add( item2 );
        mockery.checking( new Expectations() {{
            allowing( model ).inventoryItems();
            will( returnValue( inventoryItems ) );
            ignoring( model );
            allowing( item1 ).id();
            will( returnValue( "itemid1\n" ) );
            ignoring( item1 );
            allowing( item2 ).id();
            will( returnValue( "itemid2\n" ) );
            ignoring( item2 );
        }});
        PlainTextModelDeltaSerialiser s = new PlainTextModelDeltaSerialiser( null );

        assertThat( s.serialise( model ), is( "inventory item:itemid1\ninventory item:itemid2\n" ) );
    }

    @Test
    public void uses_location_serialiser_for_all_locations_in_model() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final ModelLocation location1 = mockery.mock( ModelLocation.class, "location1" );
        final ModelLocation location2 = mockery.mock( ModelLocation.class, "location2" );
        final Collection<ModelLocation> locations = new ArrayList<ModelLocation>();
        locations.add( location1 );
        locations.add( location2 );
        final LocationSerialiser locationSerialiser = mockery.mock( LocationSerialiser.class );
        mockery.checking( new Expectations() {{
            oneOf( model ).locations();
            will( returnValue( locations ) );
            ignoring( model );
            oneOf( locationSerialiser ).serialise( location1 );
            oneOf( locationSerialiser ).serialise( location2 );
            ignoring( locationSerialiser );
        }});
        PlainTextModelDeltaSerialiser s =
            new PlainTextModelDeltaSerialiser( locationSerialiser );

        s.serialise( model );
    }

    @Test
    public void concatenates_serialised_locations_to_output() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final ModelLocation location1 = mockery.mock( ModelLocation.class, "location1" );
        final ModelLocation location2 = mockery.mock( ModelLocation.class, "location2" );
        final Collection<ModelLocation> locations = new ArrayList<ModelLocation>();
        locations.add( location1 );
        locations.add( location2 );
        final LocationSerialiser locationSerialiser = mockery.mock( LocationSerialiser.class );
        mockery.checking( new Expectations() {{
            allowing( model ).locations();
            will( returnValue( locations ) );
            ignoring( model );
            allowing( locationSerialiser ).serialise( location1 );
            will( returnValue( "location1\n" ) );
            allowing( locationSerialiser ).serialise( location2 );
            will( returnValue( "location2\n" ) );
            ignoring( locationSerialiser );
        }});
        PlainTextModelDeltaSerialiser s =
            new PlainTextModelDeltaSerialiser( locationSerialiser );

        assertThat( s.serialise( model ), is( "LOCATION\nlocation1\nLOCATION\nlocation2\n" ) );
    }


    //@Test
    //public void adds_current_location_to_output() {

    //@Test
    //public void adds_current_score_to_output() {

    //@Test
    //public void adds_format_version_number_to_output() {

    //@Test
    //public void puts_format_version_number_at_start_of_output() {
}

