package com.chewielouie.textadventure.serialisation;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.ExitFactory;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.ItemFactory;
import com.chewielouie.textadventure.ModelLocation;

@RunWith(JMock.class)
public class PlainTextModelLocationDeserialiserTests {

    private Mockery mockery = new Mockery();

    @Test
    public void deserialise_extracts_id_after_stripping_trailing_newlines() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        PlainTextModelLocationDeserialiser d = new PlainTextModelLocationDeserialiser( null, null );
        mockery.checking( new Expectations() {{
            oneOf( location ).setId( "name" );
            ignoring( location );
        }});
        d.deserialise( location, "location id:name\n" );
    }

    @Test
    public void deserialise_finds_location_area_id() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        PlainTextModelLocationDeserialiser d = new PlainTextModelLocationDeserialiser( null, null );
        mockery.checking( new Expectations() {{
            oneOf( location ).setAreaID( "area-id" );
            ignoring( location );
        }});
        d.deserialise( location, "location id:name\n" +
                                 "location area id:area-id\n" );
    }

    @Test
    public void deserialise_finds_location_description() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        PlainTextModelLocationDeserialiser d = new PlainTextModelLocationDeserialiser( null, null );
        mockery.checking( new Expectations() {{
            oneOf( location ).setLocationDescription(
                "You are in a room.\n" +
                "It is a bit untidy." );
            ignoring( location );
        }});
        d.deserialise( location, "location id:name\n" +
                 "location description:You are in a room.\n" +
                                      "It is a bit untidy." );
    }

    @Test
    public void deserialise_extracts_location_description_up_to_exit() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        PlainTextModelLocationDeserialiser d = new PlainTextModelLocationDeserialiser( null, null );
        mockery.checking( new Expectations() {{
            oneOf( location ).setLocationDescription(
                "You are in a room.\n" +
                "It is a bit untidy.\n" );
            ignoring( location );
        }});
        d.deserialise( location, "location id:name\n" +
                 "location description:You are in a room.\n" +
                                      "It is a bit untidy.\n" +
                 "EXIT\n" );
    }

    @Test
    public void deserialise_extracts_location_description_up_to_item() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        PlainTextModelLocationDeserialiser d = new PlainTextModelLocationDeserialiser( null, null );
        mockery.checking( new Expectations() {{
            oneOf( location ).setLocationDescription(
                "You are in a room.\n" +
                "It is a bit untidy.\n" );
            ignoring( location );
        }});
        d.deserialise( location, "location id:name\n" +
                 "location description:You are in a room.\n" +
                                      "It is a bit untidy.\n" +
                 "ITEM\n" );
    }

    @Test
    public void deserialise_uses_factory_for_new_exit_objects() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final Exit exit = mockery.mock( Exit.class );
        final ExitFactory exitFactory = mockery.mock( ExitFactory.class );
        final ExitDeserialiser exitDeserialiser =
            mockery.mock( ExitDeserialiser.class );
        PlainTextModelLocationDeserialiser d =
            new PlainTextModelLocationDeserialiser( null, exitFactory, null,
                   exitDeserialiser );

        mockery.checking( new Expectations() {{
            oneOf( exitFactory ).create();
            will( returnValue( exit ) );
            ignoring( exitFactory );
            ignoring( exitDeserialiser );
            ignoring( exit );
            ignoring( location );
        }});

        d.deserialise( location, "location id:name\n" +
                       "EXIT\nexit 1\n" +
                       "some more content" );
    }

    @Test
    public void deserialise_extracts_exit_content_using_exit_deserialiser() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final Exit exit = mockery.mock( Exit.class );
        final ExitFactory exitFactory = mockery.mock( ExitFactory.class );
        final ExitDeserialiser exitDeserialiser =
            mockery.mock( ExitDeserialiser.class );
        PlainTextModelLocationDeserialiser d =
            new PlainTextModelLocationDeserialiser( null, exitFactory, null,
                   exitDeserialiser );

        mockery.checking( new Expectations() {{
            allowing( exitFactory ).create();
            will( returnValue( exit ) );
            ignoring( exitFactory );
            oneOf( exitDeserialiser ).deserialise( exit,
                                        "exit 1\n" +
                                        "some more content" );
            ignoring( exitDeserialiser );
            ignoring( exit );
            ignoring( location );
        }});

        d.deserialise( location, "location id:name\n" +
                       "EXIT\nexit 1\n" +
                       "some more content" );
    }

    @Test
    public void deserialise_extracts_exit_content_upto_ITEM() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final Exit exit = mockery.mock( Exit.class );
        final ExitFactory exitFactory = mockery.mock( ExitFactory.class );
        final ExitDeserialiser exitDeserialiser =
            mockery.mock( ExitDeserialiser.class );
        PlainTextModelLocationDeserialiser d =
            new PlainTextModelLocationDeserialiser( null, exitFactory, null,
                   exitDeserialiser );

        mockery.checking( new Expectations() {{
            allowing( exitFactory ).create();
            will( returnValue( exit ) );
            ignoring( exitFactory );
            oneOf( exitDeserialiser ).deserialise( exit,
                                        "exit 1\n" +
                                        "some more content\n" );
            ignoring( exitDeserialiser );
            ignoring( exit );
            ignoring( location );
        }});

        d.deserialise( location, "location id:name\n" +
                       "EXIT\nexit 1\n" +
                       "some more content\n" +
                       "ITEM\n" );
    }

    @Test
    public void deserialise_extracts_multiple_exits() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final Exit exit1 = mockery.mock( Exit.class, "exit1" );
        final Exit exit2 = mockery.mock( Exit.class, "exit2" );
        final ExitFactory exitFactory = mockery.mock( ExitFactory.class );
        final ExitDeserialiser exitDeserialiser =
            mockery.mock( ExitDeserialiser.class );
        PlainTextModelLocationDeserialiser d =
            new PlainTextModelLocationDeserialiser( null, exitFactory, null,
                   exitDeserialiser );

        mockery.checking( new Expectations() {{
            atLeast( 1 ).of( exitFactory ).create();
                will( onConsecutiveCalls(
                      returnValue( exit1 ),
                      returnValue( exit2 ) ) );
            ignoring( exitFactory );
            oneOf( exitDeserialiser ).deserialise( exit1,
                                       "exit 1 content\n" +
                                       "some more content" );
            oneOf( exitDeserialiser ).deserialise( exit2,
                                       "exit 2 content\n" +
                                       "some more content" );
            ignoring( exitDeserialiser );
            ignoring( exit1 );
            ignoring( exit2 );
            ignoring( location );
        }});

        d.deserialise( location, "location id:name\n" +
                       "EXIT\nexit 1 content\n" +
                       "some more content" +
                       "EXIT\nexit 2 content\n" +
                       "some more content" );
    }

    @Test
    public void deserialise_adds_extracted_exits_to_location() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final Exit exit1 = mockery.mock( Exit.class, "exit1" );
        final Exit exit2 = mockery.mock( Exit.class, "exit2" );
        final ExitFactory exitFactory = mockery.mock( ExitFactory.class );
        final ExitDeserialiser exitDeserialiser =
            mockery.mock( ExitDeserialiser.class );
        PlainTextModelLocationDeserialiser d =
            new PlainTextModelLocationDeserialiser( null, exitFactory, null,
                   exitDeserialiser );

        mockery.checking( new Expectations() {{
            atLeast( 1 ).of( exitFactory ).create();
                will( onConsecutiveCalls(
                      returnValue( exit1 ),
                      returnValue( exit2 ) ) );
            ignoring( exitFactory );
            ignoring( exitDeserialiser );
            ignoring( exit1 );
            ignoring( exit2 );
            oneOf( location ).addExit( exit1 );
            oneOf( location ).addExit( exit2 );
            ignoring( location );
        }});

        d.deserialise( location, "location id:name\n" +
                       "EXIT\nexit 1 content\n" +
                       "and more exit content\n" +
                       "EXIT\nexit 2 content\n" +
                       "and more exit content\n" );
    }

    @Test
    public void deserialise_uses_factory_for_new_item_objects() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final Item item = mockery.mock( Item.class );
        final ItemFactory itemFactory = mockery.mock( ItemFactory.class );
        PlainTextModelLocationDeserialiser d =
            new PlainTextModelLocationDeserialiser( itemFactory, null );

        mockery.checking( new Expectations() {{
            oneOf( itemFactory ).create();
            will( returnValue( item ) );
            ignoring( itemFactory );
            ignoring( item );
            ignoring( location );
        }});

        d.deserialise( location, "location id:name\n" +
                       "ITEM\nitem name:item content\n" +
                       "and more item content" );
    }

    @Test
    public void deserialise_extracts_item_content_using_item_deserialiser() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final Item item = mockery.mock( Item.class );
        final ItemFactory itemFactory = mockery.mock( ItemFactory.class );
        final ItemDeserialiser itemDeserialiser =
            mockery.mock( ItemDeserialiser.class );
        PlainTextModelLocationDeserialiser d =
            new PlainTextModelLocationDeserialiser( itemFactory, null,
                   itemDeserialiser, null );

        mockery.checking( new Expectations() {{
            allowing( itemFactory ).create();
            will( returnValue( item ) );
            ignoring( itemFactory );
            oneOf( itemDeserialiser ).deserialise( item,
                                       "item name:item content\n" +
                                       "and more item content" );
            ignoring( itemDeserialiser );
            ignoring( item );
            ignoring( location );
        }});

        d.deserialise( location, "location id:name\n" +
                       "ITEM\nitem name:item content\n" +
                       "and more item content" );
    }

    @Test
    public void deserialise_extracts_multiple_items() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final Item item1 = mockery.mock( Item.class, "item1" );
        final Item item2 = mockery.mock( Item.class, "item2" );
        final ItemFactory itemFactory = mockery.mock( ItemFactory.class );
        final ItemDeserialiser itemDeserialiser =
            mockery.mock( ItemDeserialiser.class );
        PlainTextModelLocationDeserialiser d =
            new PlainTextModelLocationDeserialiser( itemFactory, null,
                   itemDeserialiser, null );

        mockery.checking( new Expectations() {{
            atLeast( 1 ).of( itemFactory ).create();
                will( onConsecutiveCalls(
                      returnValue( item1 ),
                      returnValue( item2 ) ) );
            ignoring( itemFactory );
            oneOf( itemDeserialiser ).deserialise( item1,
                                       "item 1 content\n" +
                                       "and more item content\n" );
            oneOf( itemDeserialiser ).deserialise( item2,
                                       "item 2 content\n" +
                                       "and more item content\n" );
            ignoring( itemDeserialiser );
            ignoring( item1 );
            ignoring( item2 );
            ignoring( location );
        }});

        d.deserialise( location, "location id:name\n" +
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
        PlainTextModelLocationDeserialiser d =
            new PlainTextModelLocationDeserialiser( itemFactory, null );

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

        d.deserialise( location, "location id:name\n" +
                       "ITEM\nitem 1 content\n" +
                       "and more item content\n" +
                       "ITEM\nitem 2 content\n" +
                       "and more item content\n" );
    }

    @Test
    public void location_may_have_optional_coordinates() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        PlainTextModelLocationDeserialiser d = new PlainTextModelLocationDeserialiser( null, null );
        mockery.checking( new Expectations() {{
            oneOf( location ).setX( 5 );
            oneOf( location ).setY( 10 );
            ignoring( location );
        }});
        d.deserialise( location, "x:5\n" +
                                 "y:10\n" +
                                 "location id:name\n" +
                                 "location description:You are in a room.\n" +
                                         "It is a bit untidy." );
    }

    @Test
    public void deserialised_tags_should_be_taken_from_the_start_of_the_line() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        PlainTextModelLocationDeserialiser d = new PlainTextModelLocationDeserialiser( null, null );
        mockery.checking( new Expectations() {{
            never( location ).setX( with( any( Integer.class ) ) );
            ignoring( location );
        }});
        d.deserialise( location, "location id:name\n" +
                                 "location description:You are in a room.\n" +
                                         "It is a bit untidy.\n" +
                                 "a_tag_that_ends_with_another_tag____x:102020\n" );
    }

    @Test
    public void deserialise_finds_text_to_show_on_first_entry() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        PlainTextModelLocationDeserialiser d = new PlainTextModelLocationDeserialiser( null, null );
        mockery.checking( new Expectations() {{
            oneOf( location ).setTextForFirstEntry(
                "You've never been here before.\n" +
                "It is a bit untidy." );
            ignoring( location );
        }});
        d.deserialise( location, "location id:name\n" +
                 "text to show on first entry:You've never been here before.\n" +
                 "It is a bit untidy." );
    }

    @Test
    public void deserialise_of_first_entry_text_does_not_include_exits_or_items() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        PlainTextModelLocationDeserialiser d = new PlainTextModelLocationDeserialiser( null, null );
        mockery.checking( new Expectations() {{
            oneOf( location ).setTextForFirstEntry(
                "You've never been here before.\n" +
                "It is a bit untidy.\n" );
            ignoring( location );
        }});
        d.deserialise( location, "location id:name\n" +
                 "text to show on first entry:You've never been here before.\n" +
                 "It is a bit untidy.\n" +
                 "EXIT\n" +
                 "ITEM\n" );
    }

    @Test
    public void deserialise_of_description_does_not_include_first_entry_text() {
        final ModelLocation location = mockery.mock( ModelLocation.class );
        PlainTextModelLocationDeserialiser d = new PlainTextModelLocationDeserialiser( null, null );
        mockery.checking( new Expectations() {{
            oneOf( location ).setLocationDescription(
                "You are in a room.\n" );
            ignoring( location );
        }});
        d.deserialise( location, "location id:name\n" +
                 "location description:You are in a room.\n" +
                 "text to show on first entry:You've never been here before.\n" +
                 "It is a bit untidy." );
    }
}


