package com.chewielouie.textadventure.serialisation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.TalkPhraseSink;
import com.chewielouie.textadventure.itemaction.ItemAction;
import com.chewielouie.textadventure.itemaction.ItemActionFactory;

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
    public void deserialise_extracts_item_visibility_and_sets_visible() {
        final Item item = mockery.mock( Item.class );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );
        mockery.checking( new Expectations() {{
            oneOf( item ).setVisible( true );
            ignoring( item );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item visibility:visible\n" );
    }

    @Test
    public void deserialise_extracts_item_visibility_and_sets_invisible() {
        final Item item = mockery.mock( Item.class );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );
        mockery.checking( new Expectations() {{
            oneOf( item ).setVisible( false );
            ignoring( item );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item visibility:invisible\n" );
    }

    @Test
    public void deserialise_extracts_item_visibility_and_sets_visible_by_default() {
        final Item item = mockery.mock( Item.class );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );
        mockery.checking( new Expectations() {{
            oneOf( item ).setVisible( true );
            ignoring( item );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item visibility:unknown\n" );
    }

    @Test
    public void deserialise_extracts_item_examine_text() {
        final Item item = mockery.mock( Item.class );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );
        mockery.checking( new Expectations() {{
            oneOf( item ).setExamineText( "message" );
            ignoring( item );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item examine message:message\n" );
    }

    @Test
    public void deserialise_extracts_item_examine_action_is_not_repeatable() {
        final Item item = mockery.mock( Item.class );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );
        mockery.checking( new Expectations() {{
            oneOf( item ).setExamineActionIsNotRepeatable();
            ignoring( item );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item examine action is not repeatable:\n" );
    }

    @Test
    public void deserialise_extracts_multiple_item_examine_actions_with_ItemAction_factory() {
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
                       "item on examine action:actionname:action arguments\n" +
                       "item on examine action:actionname:action arguments\n" );
    }

    @Test
    public void deserialise_extracts_item_examine_action_into_ItemAction_object() {
        final Item item = mockery.mock( Item.class );
        final ItemActionFactory itemActionFactory =
            mockery.mock( ItemActionFactory.class );
        final ItemAction action = mockery.mock( ItemAction.class );
        PlainTextItemDeserialiser d =
            new PlainTextItemDeserialiser( itemActionFactory );
        mockery.checking( new Expectations() {{
            ignoring( item );
            oneOf( itemActionFactory ).create( "actionname:action arguments", item );
            will( returnValue( action ) );
            ignoring( itemActionFactory );
        }});

        d.deserialise( item,
                       "item name:Name\n" +
                       "item description:description\n" +
                       "item on examine action:actionname:action arguments\n" );
    }

    @Test
    public void deserialise_adds_item_examine_action_to_item() {
        final Item item = mockery.mock( Item.class );
        final ItemActionFactory itemActionFactory =
            mockery.mock( ItemActionFactory.class );
        final ItemAction action = mockery.mock( ItemAction.class );
        PlainTextItemDeserialiser d =
            new PlainTextItemDeserialiser( itemActionFactory );
        mockery.checking( new Expectations() {{
            oneOf( item ).addOnExamineAction( action );
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
                       "item on examine action:actionname:action arguments\n" );
    }

    @Test
    public void deserialise_extracts_multiple_initial_talk_phrases() {
        TalkPhraseSink talkPhraseSink = mock( TalkPhraseSink.class );
        Item item = mock( Item.class );
        when( item.getTalkPhraseSink() ).thenReturn( talkPhraseSink );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );

        d.deserialise( item,
                       "item talk initial phrase:hello1:short content1:long content1\n" +
                       "item talk initial phrase:hello2:short content2:long content2\n" );
        verify( talkPhraseSink ).addInitialPhrase( "hello1", "short content1", "long content1" );
        verify( talkPhraseSink ).addInitialPhrase( "hello2", "short content2", "long content2" );
    }

    @Test
    public void deserialise_extracts_multiple_responses() {
        TalkPhraseSink talkPhraseSink = mock( TalkPhraseSink.class );
        Item item = mock( Item.class );
        when( item.getTalkPhraseSink() ).thenReturn( talkPhraseSink );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );

        d.deserialise( item,
                       "item talk initial phrase:id1:short content:Hello!\n" +
                       "item talk response to:id1:1st response\n" +
                       "item talk initial phrase:id2:short content:Hello2!\n" +
                       "item talk response to:id2:2nd response\n" );
        verify( talkPhraseSink ).addResponse( "id1", "1st response" );
        verify( talkPhraseSink ).addResponse( "id2", "2nd response" );
    }

    @Test
    public void deserialise_extracts_multiple_talk_follow_up_phrases() {
        TalkPhraseSink talkPhraseSink = mock( TalkPhraseSink.class );
        Item item = mock( Item.class );
        when( item.getTalkPhraseSink() ).thenReturn( talkPhraseSink );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( null );

        d.deserialise( item,
                       "item talk initial phrase:hello:short content:Hello!\n" +
                       "item talk follow up phrase to:hello:followup1:short content1:The next step in the conversation.\n" +
                       "item talk follow up phrase to:hello:followup2:short content2:Another next step in the conversation.\n" );
        verify( talkPhraseSink ).addFollowUpPhrase( "hello", "followup1", "short content1", "The next step in the conversation." );
        verify( talkPhraseSink ).addFollowUpPhrase( "hello", "followup2", "short content2", "Another next step in the conversation." );
    }

    @Test
    public void deserialise_extracts_multiple_talk_actions_in_response() {
        TalkPhraseSink talkPhraseSink = mock( TalkPhraseSink.class );
        Item item = mock( Item.class );
        when( item.getTalkPhraseSink() ).thenReturn( talkPhraseSink );
        ItemAction action1 = mock( ItemAction.class );
        ItemAction action2 = mock( ItemAction.class );
        ItemActionFactory itemActionFactory = mock( ItemActionFactory.class );
        when( itemActionFactory.create( "action name 1:action arg", item ) )
          .thenReturn( action1 );
        when( itemActionFactory.create( "action name 2:action arg", item ) )
          .thenReturn( action2 );
        PlainTextItemDeserialiser d = new PlainTextItemDeserialiser( itemActionFactory );

        d.deserialise( item,
                       "item talk initial phrase:hello:short content:Hello!\n" +
                       "item talk action in response to:hello1:action name 1:action arg\n" +
                       "item talk action in response to:hello2:action name 2:action arg\n" );
        verify( talkPhraseSink ).addActionInResponseTo( "hello1", action1 );
        verify( talkPhraseSink ).addActionInResponseTo( "hello2", action2 );
        verify( itemActionFactory ).create( "action name 1:action arg", item );
        verify( itemActionFactory ).create( "action name 2:action arg", item );
    }
}
