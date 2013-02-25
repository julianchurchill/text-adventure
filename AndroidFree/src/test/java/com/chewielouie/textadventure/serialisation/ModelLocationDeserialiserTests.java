package com.chewielouie.textadventure.serialisation;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.Item;
import com.chewielouie.textadventure.ItemFactory;
import com.chewielouie.textadventure.ModelLocation;

@RunWith(JMock.class)
public class ModelLocationDeserialiserTests {

    private Mockery mockery = new Mockery();

    @Test
    public void deserialise_extracts_id_after_stripping_trailing_newlines() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        ModelLocationDeserialiser d = new ModelLocationDeserialiser( location, null );
        mockery.checking( new Expectations() {{
            oneOf( location ).setId( "name" );
            ignoring( location );
        }});
        d.parse( "location id:name\n" );
    }

    @Test
    public void deserialise_finds_location_description() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        ModelLocationDeserialiser d = new ModelLocationDeserialiser( location, null );
        mockery.checking( new Expectations() {{
            oneOf( location ).setLocationDescription(
                "You are in a room.\n" +
                "It is a bit untidy." );
            ignoring( location );
        }});
        d.parse( "location id:name\n" +
                 "location description:You are in a room.\n" +
                                      "It is a bit untidy." );
    }

    @Test
    public void deserialise_extracts_location_description_up_to_exit_label() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        ModelLocationDeserialiser d = new ModelLocationDeserialiser( location, null );
        mockery.checking( new Expectations() {{
            oneOf( location ).setLocationDescription(
                "You are in a room.\n" +
                "It is a bit untidy.\n" );
            ignoring( location );
        }});
        d.parse( "location id:name\n" +
                 "location description:You are in a room.\n" +
                                      "It is a bit untidy.\n" +
                 "exit label:label\n" );
    }

    @Test
    public void deserialise_extracts_location_description_up_to_item() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        ModelLocationDeserialiser d = new ModelLocationDeserialiser( location, null );
        mockery.checking( new Expectations() {{
            oneOf( location ).setLocationDescription(
                "You are in a room.\n" +
                "It is a bit untidy.\n" );
            ignoring( location );
        }});
        d.parse( "location id:name\n" +
                 "location description:You are in a room.\n" +
                                      "It is a bit untidy.\n" +
                 "ITEM\n" );
    }

    //@Test
    //public void deserialise_extracts_exit() {
        //final ModelLocation location = mockery.mock( ModelLocation.class );
        //ModelLocationDeserialiser d = new ModelLocationDeserialiser( location, null );
        //final LocationExit exit = new LocationExit( "label", "destination",
               //Exit.DirectionHint.East );
        //mockery.checking( new Expectations() {{
            //oneOf( location ).addExit( exit );
            //ignoring( location );
        //}});
        //d.parse( "location id:name\n" +
                       //"exit label:label\n" +
                       //"exit destination:destination\n" +
                       //"exit direction hint:East" );
    //}

    //@Test
    //public void deserialise_exit_direction_hint_is_optional() {
        //ModelLocationDeserialiser d = createDeserialiser();
        //d.parse( "location id:name\n" +
                       //"exit label:label\n" +
                       //"exit destination:destination" );
        //assertEquals( "label", l.visibleExits().get(0).label() );
        //assertEquals( "destination", l.visibleExits().get(0).destination() );
        //assertEquals( Exit.DirectionHint.DontCare, l.visibleExits().get(0).directionHint() );
    //}

    //@Test
    //public void deserialise_exit_is_visible_by_default() {
        //ModelLocationDeserialiser d = createDeserialiser();
        //d.parse( "location id:name\n" +
                       //"exit label:label\n" +
                       //"exit destination:destination" );
        //assertTrue( l.visibleExits().get(0).visible() );
    //}

    //@Test
    //public void deserialise_extracts_exit_is_not_visible() {
        //ModelLocationDeserialiser d = createDeserialiser();
        //d.parse( "location id:name\n" +
                       //"exit label:label\n" +
                       //"exit destination:destination\n" +
                       //"exit is not visible:" );
        //assertFalse( l.exitsIncludingInvisibleOnes().get(0).visible() );
    //}

    //@Test
    //public void deserialise_extracts_exit_is_not_visible_for_relevant_exit_only() {
        //ModelLocationDeserialiser d = createDeserialiser();
        //d.parse( "location id:name\n" +
                       //"exit label:label1\n" +
                       //"exit destination:destination1\n" +
                       //"exit label:label2\n" +
                       //"exit destination:destination2\n" +
                       //"exit is not visible:" );
        //assertTrue( l.exitsIncludingInvisibleOnes().get(0).visible() );
        //assertFalse( l.exitsIncludingInvisibleOnes().get(1).visible() );
    //}

    //@Test
    //public void deserialise_extracts_exit_id() {
        //ModelLocationDeserialiser d = createDeserialiser();
        //d.parse( "location id:name\n" +
                       //"exit label:label\n" +
                       //"exit destination:destination\n" +
                       //"exit id:exit id" );
        //assertEquals( "exit id", l.visibleExits().get(0).id() );
    //}

    //@Test
    //public void deserialise_extracts_multiple_exits() {
        //ModelLocationDeserialiser d = createDeserialiser();
        //d.parse( "location id:name\n" +
                       //"exit label:label1\n" +
                       //"exit destination:destination\n" +
                       //"exit direction hint:East\n" +
                       //"exit label:label2\n" +
                       //"exit destination:destination\n" +
                       //"exit direction hint:North" );
        //assertEquals( "label2", l.visibleExits().get(1).label() );
        //assertEquals( "destination", l.visibleExits().get(1).destination() );
        //assertEquals( Exit.DirectionHint.North, l.visibleExits().get(1).directionHint() );
    //}

    @Test
    public void deserialise_extracts_item() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final Item item = mockery.mock( Item.class );
        final ItemFactory itemFactory = mockery.mock( ItemFactory.class );
        ModelLocationDeserialiser d =
            new ModelLocationDeserialiser( location, itemFactory );

        mockery.checking( new Expectations() {{
            allowing( itemFactory ).create();
            will( returnValue( item ) );
            ignoring( itemFactory );
            oneOf( item ).deserialise( "item name:item content\n" +
                                       "and more item content" );
            ignoring( item );
            ignoring( location );
        }});

        d.parse( "location id:name\n" +
                       "ITEM\nitem name:item content\n" +
                       "and more item content" );
    }

    @Test
    public void deserialise_extracts_multiple_items() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final Item item1 = mockery.mock( Item.class, "item1" );
        final Item item2 = mockery.mock( Item.class, "item2" );
        final ItemFactory itemFactory = mockery.mock( ItemFactory.class );
        ModelLocationDeserialiser d =
            new ModelLocationDeserialiser( location, itemFactory );

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
            ignoring( location );
        }});

        d.parse( "location id:name\n" +
                       "ITEM\nitem 1 content\n" +
                       "and more item content\n" +
                       "ITEM\nitem 2 content\n" +
                       "and more item content\n" );
    }

    @Test
    public void deserialise_adds_extracted_items_to_location() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final Item item1 = mockery.mock( Item.class, "item1" );
        final Item item2 = mockery.mock( Item.class, "item2" );
        final ItemFactory itemFactory = mockery.mock( ItemFactory.class );
        ModelLocationDeserialiser d =
            new ModelLocationDeserialiser( location, itemFactory );

        mockery.checking( new Expectations() {{
            atLeast( 1 ).of( itemFactory ).create();
                will( onConsecutiveCalls(
                      returnValue( item1 ),
                      returnValue( item2 ) ) );
            ignoring( itemFactory );
            ignoring( item1 );
            ignoring( item2 );
            oneOf( location ).addItem( item1 );
            oneOf( location ).addItem( item2 );
            ignoring( location );
        }});

        d.parse( "location id:name\n" +
                       "ITEM\nitem 1 content\n" +
                       "and more item content\n" +
                       "ITEM\nitem 2 content\n" +
                       "and more item content\n" );
    }
}


