package com.chewielouie.textadventure.serialisation;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.itemaction.ItemAction;
import com.chewielouie.textadventure.ItemActionFactory;

@RunWith(JMock.class)
public class PlainTextItemDeserialiserTests {

    private Mockery mockery = new Mockery();

    @Test
    public void deserialise_extracts_item_name() {
        final Item item = mockery.mock( Item.class );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );
        mockery.checking( new Expectations() {{
            oneOf( item ).setName( "Name" );
            ignoring( item );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" );
    }

    @Test
    public void deserialise_extracts_item_description() {
        final Item item = mockery.mock( Item.class );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );
        mockery.checking( new Expectations() {{
            oneOf( item ).setDescription( "description" );
            ignoring( item );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" );
    }

    @Test
    public void deserialise_extracts_item_id() {
        final Item item = mockery.mock( Item.class );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );
        mockery.checking( new Expectations() {{
            oneOf( item ).setId( "an id" );
            ignoring( item );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item id:an id\n" );
    }

    @Test
    public void deserialise_extracts_item_countable_noun_prefix() {
        final Item item = mockery.mock( Item.class );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );
        mockery.checking( new Expectations() {{
            oneOf( item ).setCountableNounPrefix( "some" );
            ignoring( item );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item countable noun prefix:some\n" );
    }

    @Test
    public void deserialise_extracts_item_mid_sentence_cased_name() {
        final Item item = mockery.mock( Item.class );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );
        mockery.checking( new Expectations() {{
            oneOf( item ).setMidSentenceCasedName( "cased name" );
            ignoring( item );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item mid sentence cased name:cased name\n" );
    }

    @Test
    public void deserialise_extracts_item_is_untakeable() {
        final Item item = mockery.mock( Item.class );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );
        mockery.checking( new Expectations() {{
            oneOf( item ).setUntakeable();
            ignoring( item );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item is untakeable:\n" );
    }

    @Test
    public void deserialise_extracts_item_can_be_used_with() {
        final Item item = mockery.mock( Item.class, "item" );
        final Item target = mockery.mock( Item.class, "target" );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );
        mockery.checking( new Expectations() {{
            oneOf( item ).setCanBeUsedWith( "itemid" );
            ignoring( item );
            allowing( target ).id();
            will( returnValue( "itemid" ) );
            ignoring( target );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item can be used with:itemid\n" );
    }

    @Test
    public void deserialise_extracts_item_successful_use_messsage() {
        final Item item = mockery.mock( Item.class );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );
        mockery.checking( new Expectations() {{
            oneOf( item ).setUsedWithText( "message" );
            ignoring( item );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item successful use message:message\n" );
    }

    @Test
    public void deserialise_extracts_item_use_is_not_repeatable() {
        final Item item = mockery.mock( Item.class );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );
        mockery.checking( new Expectations() {{
            oneOf( item ).setUseIsNotRepeatable();
            ignoring( item );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item use is not repeatable:\n" );
    }

    @Test
    public void deserialise_extracts_multiple_item_use_actions_with_ItemAction_factory() {
        final Item item = mockery.mock( Item.class );
        final ItemActionFactory itemActionFactory =
            mockery.mock( ItemActionFactory.class );
        PlainTextItemDeserialiser d =
            new PlainTextItemDeserialiser( itemActionFactory );
        mockery.checking( new Expectations() {{
            ignoring( item );
            exactly( 2 ).of( itemActionFactory ).create( with( any( String.class ) ),
               with( any( Item.class ) ) );
            ignoring( itemActionFactory );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item use action:action:action arguments\n" +
                       "item use action:action:action arguments\n" );
    }

    @Test
    public void deserialise_extracts_item_use_action_into_ItemAction_object() {
        final Item item = mockery.mock( Item.class );
        final ItemActionFactory itemActionFactory =
            mockery.mock( ItemActionFactory.class );
        final ItemAction action = mockery.mock( ItemAction.class );
        PlainTextItemDeserialiser d =
            new PlainTextItemDeserialiser( itemActionFactory );
        mockery.checking( new Expectations() {{
            ignoring( item );
            oneOf( itemActionFactory ).create( "action:action arguments", item );
            will( returnValue( action ) );
            ignoring( itemActionFactory );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item use action:action:action arguments\n" );
    }

    @Test
    public void deserialise_adds_item_use_action_to_item() {
        final Item item = mockery.mock( Item.class );
        final ItemActionFactory itemActionFactory =
            mockery.mock( ItemActionFactory.class );
        final ItemAction action = mockery.mock( ItemAction.class );
        PlainTextItemDeserialiser d =
            new PlainTextItemDeserialiser( itemActionFactory );
        mockery.checking( new Expectations() {{
            oneOf( item ).addOnUseAction( action );
            ignoring( item );
            allowing( itemActionFactory ).create( with( any( String.class ) ),
               with( any( Item.class ) ) );
            will( returnValue( action ) );
            ignoring( itemActionFactory );
            ignoring( action );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item use action:action:action arguments\n" );
    }

    @Test
    public void deserialise_countable_noun_prefix_must_come_after_id() {
        final Item item = mockery.mock( Item.class );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );
        mockery.checking( new Expectations() {{
            allowing( item ).setName( with( any( String.class ) ) );
            allowing( item ).setDescription( with( any( String.class ) ) );
            allowing( item ).setId( with( any( String.class ) ) );
            allowing( item ).setCountableNounPrefix( with( any( String.class ) ) );
            allowing( item ).setCanBeUsedWith( with( any( String.class ) ) );
            allowing( item ).setUsedWithText( with( any( String.class ) ) );
            never( item ).setCountableNounPrefix( "some" );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item countable noun prefix:some\n" +
                       "item id:id\n" );
    }

    @Test
    public void deserialise_mid_sentence_cased_name_must_come_after_countable_noun_prefix() {
        final Item item = mockery.mock( Item.class );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );
        mockery.checking( new Expectations() {{
            allowing( item ).setName( with( any( String.class ) ) );
            allowing( item ).setDescription( with( any( String.class ) ) );
            allowing( item ).setCountableNounPrefix( with( any( String.class ) ) );
            allowing( item ).setId( with( any( String.class ) ) );
            allowing( item ).setCanBeUsedWith( with( any( String.class ) ) );
            allowing( item ).setUsedWithText( with( any( String.class ) ) );
            never( item ).setMidSentenceCasedName( "cased name" );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item mid sentence cased name:cased name\n" +
                       "item countable noun prefix:some\n" );
    }

    @Test
    public void deserialise_mid_sentence_cased_name_must_come_after_id() {
        final Item item = mockery.mock( Item.class );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );
        mockery.checking( new Expectations() {{
            allowing( item ).setName( with( any( String.class ) ) );
            allowing( item ).setDescription( with( any( String.class ) ) );
            allowing( item ).setCountableNounPrefix( with( any( String.class ) ) );
            allowing( item ).setId( with( any( String.class ) ) );
            allowing( item ).setCanBeUsedWith( with( any( String.class ) ) );
            allowing( item ).setUsedWithText( with( any( String.class ) ) );
            never( item ).setMidSentenceCasedName( "cased name" );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item mid sentence cased name:cased name\n" +
                       "item id:id\n" );
    }

    @Test
    public void deserialise_untakeable_must_come_after_countable_noun_prefix() {
        final Item item = mockery.mock( Item.class );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );
        mockery.checking( new Expectations() {{
            allowing( item ).setName( with( any( String.class ) ) );
            allowing( item ).setDescription( with( any( String.class ) ) );
            allowing( item ).setCountableNounPrefix( with( any( String.class ) ) );
            allowing( item ).setId( with( any( String.class ) ) );
            allowing( item ).setCanBeUsedWith( with( any( String.class ) ) );
            allowing( item ).setUsedWithText( with( any( String.class ) ) );
            never( item ).setUntakeable();
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item is untakeable:\n" +
                       "item countable noun prefix:some\n" );
    }

    @Test
    public void deserialise_untakeable_must_come_after_mid_sentence_cased_name() {
        final Item item = mockery.mock( Item.class );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );
        mockery.checking( new Expectations() {{
            allowing( item ).setName( with( any( String.class ) ) );
            allowing( item ).setDescription( with( any( String.class ) ) );
            allowing( item ).setCountableNounPrefix( with( any( String.class ) ) );
            allowing( item ).setMidSentenceCasedName( with( any( String.class ) ) );
            allowing( item ).setId( with( any( String.class ) ) );
            allowing( item ).setCanBeUsedWith( with( any( String.class ) ) );
            allowing( item ).setUsedWithText( with( any( String.class ) ) );
            never( item ).setUntakeable();
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item is untakeable:\n" +
                       "item mid sentence cased name:cased name\n" );
    }
}

