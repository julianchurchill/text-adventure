package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.serialisation.ModelLocationDeserialiser;

@RunWith(JMock.class)
public class PlainTextModelPopulatorTests {

    private Mockery mockery = new Mockery();

    @Test
    public void an_inventory_item_is_deserialised_from_inventory_tag_onwards() {
        final Item item = mockery.mock( Item.class );
        final ItemFactory itemFactory = mockery.mock( ItemFactory.class );
        final ModelLocationDeserialiser locationDeserialiser =
            mockery.mock( ModelLocationDeserialiser.class );

        mockery.checking( new Expectations() {{
            allowing( itemFactory ).create();
            will( returnValue( item ) );
            ignoring( itemFactory );
            oneOf( item ).deserialise( "inventory content" );
            ignoring( item );
            ignoring( locationDeserialiser );
        }});

        new PlainTextModelPopulator( null, null, null, itemFactory,
                                     locationDeserialiser,
                                     "INVENTORY ITEM\ninventory content" );
    }

    @Test
    public void multiple_inventory_items_are_deserialised_from_inventory_tag_onwards() {
        final Item item = mockery.mock( Item.class );
        final ItemFactory itemFactory = mockery.mock( ItemFactory.class );
        final ModelLocationDeserialiser locationDeserialiser =
            mockery.mock( ModelLocationDeserialiser.class );

        mockery.checking( new Expectations() {{
            allowing( itemFactory ).create();
            will( returnValue( item ) );
            ignoring( itemFactory );
            oneOf( item ).deserialise( "inventory item 1\n" );
            oneOf( item ).deserialise( "inventory item 2\n" );
            ignoring( item );
            ignoring( locationDeserialiser );
        }});

        new PlainTextModelPopulator( null, null, null, itemFactory,
                                     locationDeserialiser,
                                     "INVENTORY ITEM\ninventory item 1\n" +
                                     "INVENTORY ITEM\ninventory item 2\n" );
    }

    @Test
    public void inventory_item_is_added_to_user_inventory() {
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final Item item = mockery.mock( Item.class );
        final ItemFactory itemFactory = mockery.mock( ItemFactory.class );
        final ModelLocationDeserialiser locationDeserialiser =
            mockery.mock( ModelLocationDeserialiser.class );

        mockery.checking( new Expectations() {{
            allowing( itemFactory ).create();
            will( returnValue( item ) );
            ignoring( itemFactory );
            ignoring( item );
            oneOf( inventory ).addToInventory( item );
            ignoring( inventory );
            ignoring( locationDeserialiser );
        }});

        new PlainTextModelPopulator( null, null,
                                     inventory, itemFactory,
                                     locationDeserialiser,
                                     "INVENTORY ITEM\ninventory content" );
    }

    @Test
    public void multiple_inventory_items_are_added_to_user_inventory() {
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final Item item1 = mockery.mock( Item.class, "item 1" );
        final Item item2 = mockery.mock( Item.class, "item 2" );
        final ItemFactory itemFactory = mockery.mock( ItemFactory.class );
        final ModelLocationDeserialiser locationDeserialiser =
            mockery.mock( ModelLocationDeserialiser.class );

        mockery.checking( new Expectations() {{
            atLeast( 1 ).of( itemFactory ).create();
                will( onConsecutiveCalls(
                      returnValue( item1 ),
                      returnValue( item2 ) ) );
            ignoring( itemFactory );
            ignoring( item1 );
            ignoring( item2 );
            oneOf( inventory ).addToInventory( item1 );
            oneOf( inventory ).addToInventory( item2 );
            ignoring( inventory );
            ignoring( locationDeserialiser );
        }});

        new PlainTextModelPopulator( null, null,
                                     inventory, itemFactory,
                                     locationDeserialiser,
                                     "INVENTORY ITEM\ninventory item 1\n" +
                                     "INVENTORY ITEM\ninventory item 2\n" );
    }

    @Test
    public void inventory_item_content_does_not_include_location_content() {
        final Item item = mockery.mock( Item.class );
        final ItemFactory itemFactory = mockery.mock( ItemFactory.class );
        final ModelLocationDeserialiser locationDeserialiser =
            mockery.mock( ModelLocationDeserialiser.class );

        mockery.checking( new Expectations() {{
            allowing( itemFactory ).create();
            will( returnValue( item ) );
            ignoring( itemFactory );
            oneOf( item ).deserialise( "inventory content\n" );
            ignoring( item );
            ignoring( locationDeserialiser );
        }});

        new PlainTextModelPopulator( null, null,
                                     null, itemFactory,
                                     locationDeserialiser,
                                     "INVENTORY ITEM\ninventory content\n" +
                                     "LOCATION\nlocation content\n" );
    }

    @Test
    public void location_is_created_using_factory() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final ModelLocationFactory locationFactory =
            mockery.mock( ModelLocationFactory.class );
        final ModelLocationDeserialiser locationDeserialiser =
            mockery.mock( ModelLocationDeserialiser.class );

        mockery.checking( new Expectations() {{
            oneOf( locationFactory ).create();
            will( returnValue( location ) );
            ignoring( locationFactory );
            ignoring( locationDeserialiser );
            ignoring( location );
        }});

        new PlainTextModelPopulator( null, locationFactory, null, null,
                                     locationDeserialiser,
                                     "LOCATION\nlocation_name:name" );
    }

    @Test
    public void location_is_deserialised_from_location_tag_onwards() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final ModelLocationFactory locationFactory =
            mockery.mock( ModelLocationFactory.class );
        final ModelLocationDeserialiser locationDeserialiser =
            mockery.mock( ModelLocationDeserialiser.class );

        mockery.checking( new Expectations() {{
            allowing( locationFactory ).create();
            will( returnValue( location ) );
            ignoring( locationFactory );
            oneOf( locationDeserialiser ).deserialise( location,
                                        "location_name:name" );
            ignoring( locationDeserialiser );
            ignoring( location );
        }});

        new PlainTextModelPopulator( null, locationFactory, null, null,
                                     locationDeserialiser,
                                     "LOCATION\nlocation_name:name" );
    }

    @Test
    public void location_content_is_correct_when_preceeded_by_inventory_items() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final ModelLocationFactory locationFactory =
            mockery.mock( ModelLocationFactory.class );
        final ModelLocationDeserialiser locationDeserialiser =
            mockery.mock( ModelLocationDeserialiser.class );

        mockery.checking( new Expectations() {{
            allowing( locationFactory ).create();
            will( returnValue( location ) );
            ignoring( locationFactory );
            oneOf( locationDeserialiser ).deserialise( location,
                                        "location content\n" );
            ignoring( locationDeserialiser );
            ignoring( location );
        }});

        new PlainTextModelPopulator( null, locationFactory,
                                     null, null,
                                     locationDeserialiser,
                                     "INVENTORY ITEM\ninventory content\n" +
                                     "LOCATION\nlocation content\n" );
    }

    @Test
    public void multiple_locations_are_deserialised_from_location_tag_onwards() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final ModelLocationFactory locationFactory =
            mockery.mock( ModelLocationFactory.class );
        final ModelLocationDeserialiser locationDeserialiser =
            mockery.mock( ModelLocationDeserialiser.class );

        mockery.checking( new Expectations() {{
            allowing( locationFactory ).create();
            will( returnValue( location ) );
            ignoring( locationFactory );
            oneOf( locationDeserialiser ).deserialise( location,
                                        "location_name:name\n" );
            oneOf( locationDeserialiser ).deserialise( location,
                                        "location_name:name2\n" );
            ignoring( locationDeserialiser );
            ignoring( location );
        }});

        new PlainTextModelPopulator( null, locationFactory, null, null,
                                     locationDeserialiser,
                                     "LOCATION\nlocation_name:name\n" +
                                     "LOCATION\nlocation_name:name2\n" );
    }

    @Test
    public void location_is_added_to_model() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final ModelLocationFactory locationFactory =
            mockery.mock( ModelLocationFactory.class );
        final ModelLocationDeserialiser locationDeserialiser =
            mockery.mock( ModelLocationDeserialiser.class );

        mockery.checking( new Expectations() {{
            allowing( locationFactory ).create();
            will( returnValue( location ) );
            ignoring( locationFactory );
            ignoring( locationDeserialiser );
            ignoring( location );
            oneOf( model ).addLocation( location );
            ignoring( model );
        }});

        new PlainTextModelPopulator( model, locationFactory, null, null,
                                     locationDeserialiser,
                                     "LOCATION\nlocation_name:name" );
    }

    @Test
    public void multiple_locations_are_added_to_model() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final ModelLocation location1 = mockery.mock( ModelLocation.class, "l1" );
        final ModelLocation location2 = mockery.mock( ModelLocation.class, "l2" );
        final ModelLocationFactory locationFactory =
            mockery.mock( ModelLocationFactory.class );
        final ModelLocationDeserialiser locationDeserialiser =
            mockery.mock( ModelLocationDeserialiser.class );

        mockery.checking( new Expectations() {{
          atLeast( 1 ).of( locationFactory ).create();
              will( onConsecutiveCalls(
                      returnValue( location1 ),
                      returnValue( location2 ) ) );
          ignoring( locationFactory );
          ignoring( locationDeserialiser );
          ignoring( location1 );
          ignoring( location2 );
          oneOf( model ).addLocation( location1 );
          oneOf( model ).addLocation( location2 );
          ignoring( model );
        }});

        new PlainTextModelPopulator( model, locationFactory, null, null,
                                     locationDeserialiser,
                                     "LOCATION\nlocation_name:name1\n" +
                                     "LOCATION\nlocation_name:name2" );
    }
}

