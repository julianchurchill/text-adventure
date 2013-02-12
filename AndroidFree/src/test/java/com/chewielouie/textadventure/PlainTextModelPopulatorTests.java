package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class PlainTextModelPopulatorTests {

    private Mockery mockery = new Mockery();

    @Test
    public void an_inventory_item_is_deserialised_from_inventory_tag_onwards() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final ModelLocationFactory locationFactory =
            mockery.mock( ModelLocationFactory.class );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final Item item = mockery.mock( Item.class );
        final ItemFactory itemFactory = mockery.mock( ItemFactory.class );

        mockery.checking( new Expectations() {{
            allowing( itemFactory ).create();
            will( returnValue( item ) );
            ignoring( itemFactory );
            oneOf( item ).deserialise( "inventory content" );
            ignoring( item );
            ignoring( inventory );
            ignoring( locationFactory );
            ignoring( model );
        }});

        new PlainTextModelPopulator( model, locationFactory, itemFactory,
                                     "INVENTORY ITEM\ninventory content" );
    }

    //@Test
    //public void multiple_inventory_items_are_deserialised_from_inventory_tag_onwards() {

    //@Test
    //public void inventory_item_is_added_to_user_inventory() {

    //@Test
    //public void multiple_inventory_items_are_added_to_user_inventory() {

    //@Test
    //public void inventory_section_may_be_followed_by_locations_section() {

    @Test
    public void location_is_deserialised_from_location_tag_onwards() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final ModelLocationFactory locationFactory =
            mockery.mock( ModelLocationFactory.class );

        mockery.checking( new Expectations() {{
            allowing( locationFactory ).create();
            will( returnValue( location ) );
            ignoring( locationFactory );
            oneOf( location ).deserialise( "location_name:name" );
            ignoring( location );
            ignoring( model );
        }});

        new PlainTextModelPopulator( model, locationFactory, null,
                                     "LOCATION\nlocation_name:name" );
    }

    @Test
    public void multiple_locations_are_deserialised_from_location_tag_onwards() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final ModelLocationFactory locationFactory =
            mockery.mock( ModelLocationFactory.class );

        mockery.checking( new Expectations() {{
            allowing( locationFactory ).create();
            will( returnValue( location ) );
            ignoring( locationFactory );
            oneOf( location ).deserialise( "location_name:name\n" );
            oneOf( location ).deserialise( "location_name:name2\n" );
            ignoring( location );
            ignoring( model );
        }});

        new PlainTextModelPopulator( model, locationFactory, null,
                                     "LOCATION\nlocation_name:name\n" +
                                     "LOCATION\nlocation_name:name2\n" );
    }

    @Test
    public void location_is_added_to_model() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final ModelLocationFactory locationFactory =
            mockery.mock( ModelLocationFactory.class );

        mockery.checking( new Expectations() {{
            allowing( locationFactory ).create();
            will( returnValue( location ) );
            ignoring( locationFactory );
            ignoring( location );
            oneOf( model ).addLocation( location );
            ignoring( model );
        }});

        new PlainTextModelPopulator( model, locationFactory, null,
                                     "LOCATION\nlocation_name:name" );
    }

    @Test
    public void multiple_locations_are_added_to_model() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final ModelLocation location1 = mockery.mock( ModelLocation.class, "l1" );
        final ModelLocation location2 = mockery.mock( ModelLocation.class, "l2" );
        final ModelLocationFactory locationFactory =
            mockery.mock( ModelLocationFactory.class );

        mockery.checking( new Expectations() {{
          atLeast( 1 ).of( locationFactory ).create();
              will( onConsecutiveCalls(
                      returnValue( location1 ),
                      returnValue( location2 ) ) );
          ignoring( locationFactory );
          ignoring( location1 );
          ignoring( location2 );
          oneOf( model ).addLocation( location1 );
          oneOf( model ).addLocation( location2 );
          ignoring( model );
        }});

        new PlainTextModelPopulator( model, locationFactory, null,
                                     "LOCATION\nlocation_name:name1\n" +
                                     "LOCATION\nlocation_name:name2" );
    }
}

