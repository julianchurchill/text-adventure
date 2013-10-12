package com.chewielouie.textadventure.item;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.itemaction.ItemAction;

@RunWith(JMock.class)
public class NormalItemTests {

    private Mockery mockery = new Mockery();

    @Test
    public void countable_noun_prefix_defaults_to_a() {
        NormalItem item = new NormalItem();
        assertEquals( "a", item.countableNounPrefix() );
    }

    @Test
    public void countable_noun_prefix_is_as_specified() {
        NormalItem item = new NormalItem();
        item.setCountableNounPrefix( "some" );
        assertEquals( "some", item.countableNounPrefix() );
    }

    @Test
    public void mid_sentence_cased_name_defaults_to_name_lower_cased() {
        NormalItem item = new NormalItem();
        item.setName( "NAME" );
        assertEquals( "name", item.midSentenceCasedName() );
    }

    @Test
    public void mid_sentence_cased_name_can_be_set() {
        NormalItem item = new NormalItem();
        item.setMidSentenceCasedName( "Name" );
        assertEquals( "Name", item.midSentenceCasedName() );
    }

    @Test
    public void item_is_not_a_proper_noun_by_default() {
        NormalItem item = new NormalItem();
        assertThat( item.properNoun(), is( false ) );
    }

    @Test
    public void setting_proper_noun_makes_item_a_proper_noun() {
        NormalItem item = new NormalItem();
        item.setProperNoun();
        assertThat( item.properNoun(), is( true ) );
    }

    @Test
    public void item_is_not_plural_by_default() {
        NormalItem item = new NormalItem();
        assertFalse( item.plural() );
    }

    @Test
    public void setting_plural_makes_item_plural() {
        NormalItem item = new NormalItem();
        item.setPlural();
        assertTrue( item.plural() );
    }

    @Test
    public void item_is_takeable_by_default() {
        NormalItem item = new NormalItem();
        assertTrue( item.takeable() );
    }

    @Test
    public void setting_untakeable_makes_item_not_takeable() {
        NormalItem item = new NormalItem();
        item.setUntakeable();
        assertFalse( item.takeable() );
    }

    @Test
    public void a_target_item_needs_an_id_to_test_for_usability() {
        NormalItem item = new NormalItem();
        final Item itemWithoutID = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            allowing( itemWithoutID ).id();
            will( returnValue( "" ) );
            ignoring( itemWithoutID );
        }});

        assertFalse( item.canBeUsedWith( itemWithoutID ) );
    }

    Item makeMockItemWithID( String id ) {
        Item item = mock( Item.class );
        when( item.id() ).thenReturn( id );
        return item;
    }

    @Test
    public void useWith_can_call_actions_repeatedly_for_the_correct_item() {
        Item otherItem = makeMockItemWithID( "otherItemID" );
        ItemAction action = mock( ItemAction.class );
        NormalItem item = new NormalItem();
        item.addOnUseActionFor( "otherItemID", action );

        item.useWith( otherItem );
        item.useWith( otherItem );

        verify( action, times(2) ).enact();
    }

    @Test
    public void useWith_does_not_call_actions_for_other_items() {
        Item otherItem1 = makeMockItemWithID( "otherItemID1" );
        Item otherItem2 = makeMockItemWithID( "otherItemID2" );
        ItemAction action = mock( ItemAction.class );
        NormalItem item = new NormalItem();
        item.addOnUseActionFor( "otherItemID1", action );

        item.useWith( otherItem2 );

        verify( action, never() ).enact();
    }

    @Test
    public void useWith_calls_actions_only_once_if_item_use_is_not_repeateable() {
        Item otherItem = makeMockItemWithID( "otherItemID" );
        ItemAction action = mock( ItemAction.class );
        NormalItem item = new NormalItem();
        item.addOnUseActionFor( "otherItemID", action );
        item.setUseIsNotRepeatableFor( "otherItemID" );

        item.useWith( otherItem );
        item.useWith( otherItem );

        verify( action, times(1) ).enact();
    }

    @Test
    public void useWith_returns_used_with_text_for_correct_item() {
        Item otherItem = makeMockItemWithID( "otherItemID" );
        ItemAction action = mock( ItemAction.class );
        NormalItem item = new NormalItem();
        item.addOnUseActionFor( "otherItemID", action );
        item.setUsedWithTextFor( "otherItemID", "been used" );

        assertThat( item.useWith( otherItem ), is( "been used" ) );
    }

    @Test
    public void useWith_returns_used_with_text_for_correct_item_even_if_no_actions_to_do() {
        Item otherItem = makeMockItemWithID( "otherItemID" );
        NormalItem item = new NormalItem();
        item.setUsedWithTextFor( "otherItemID", "been used" );

        assertThat( item.useWith( otherItem ), is( "been used" ) );
    }

    @Test
    public void useWith_returns_used_with_text_on_repeated_use_of_non_repeatable_combination() {
        Item otherItem = makeMockItemWithID( "otherItemID" );
        ItemAction action = mock( ItemAction.class );
        NormalItem item = new NormalItem();
        item.addOnUseActionFor( "otherItemID", action );
        item.setUseIsNotRepeatableFor( "otherItemID" );

        item.useWith( otherItem );

        assertThat( item.useWith( otherItem ), is( "You have already done that." ) );
    }

    @Test
    public void useWith_returns_used_with_text_for_items_that_cannot_be_used_together() {
        Item otherItem = makeMockItemWithID( "otherItemID" );
        NormalItem item = new NormalItem();

        assertThat( item.useWith( otherItem ), is( "Nothing happens." ) );
    }

    @Test
    public void used_with_text_is_blank_by_default() {
        NormalItem item = new NormalItem();
        assertEquals( "", item.usedWithText() );
    }

    @Test
    public void item_use_is_repeatable_by_default() {
        NormalItem item = new NormalItem();
        assertTrue( item.useIsRepeatable() );
    }

    @Test
    public void item_use_is_not_repeatable_can_be_set() {
        NormalItem item = new NormalItem();
        item.setUseIsNotRepeatable();
        assertFalse( item.useIsRepeatable() );
    }

    @Test
    public void used_with_text_is_correct_for_non_repeatable_use_item_before_being_used() {
        NormalItem item = new NormalItem();
        item.setUsedWithText( "message" );

        item.setUseIsNotRepeatable();

        assertEquals( "message", item.usedWithText() );
    }

    @Test
    public void used_with_text_is_correct_for_non_repeatable_use_item_after_being_used_once() {
        NormalItem item = new NormalItem();
        item.setUsedWithText( "message" );
        item.setUseIsNotRepeatable();

        item.use();

        assertEquals( "message", item.usedWithText() );
    }

    @Test
    public void used_with_text_is_failure_message_for_non_repeatable_use_item_after_being_used_twice() {
        NormalItem item = new NormalItem();
        item.setUsedWithText( "message" );
        item.setUseIsNotRepeatable();

        item.use();
        item.use();

        assertEquals( "You have already done that.", item.usedWithText() );
    }

    @Test
    public void used_with_text_is_successful_use_message_for_repeatable_use_item_after_being_used_twice() {
        NormalItem item = new NormalItem();
        item.setUsedWithText( "message" );

        item.use();
        item.use();

        assertEquals( "message", item.usedWithText() );
    }

    @Test
    public void item_use_actions_are_all_enacted_upon_use() {
        final ItemAction action1 = mockery.mock( ItemAction.class, "act1" );
        final ItemAction action2 = mockery.mock( ItemAction.class, "act2" );
        mockery.checking( new Expectations() {{
            oneOf( action1 ).enact();
            ignoring( action1 );
            oneOf( action2 ).enact();
            ignoring( action2 );
        }});
        NormalItem item = new NormalItem();
        item.addOnUseAction( action1 );
        item.addOnUseAction( action2 );

        item.use();
    }

    @Test
    public void item_use_actions_are_not_enacted_on_repeated_use_for_an_unrepeatable_use_item() {
        final ItemAction action1 = mockery.mock( ItemAction.class, "act1" );
        final ItemAction action2 = mockery.mock( ItemAction.class, "act2" );
        mockery.checking( new Expectations() {{
            oneOf( action1 ).enact();
            ignoring( action1 );
            oneOf( action2 ).enact();
            ignoring( action2 );
        }});
        NormalItem item = new NormalItem();
        item.setUseIsNotRepeatable();
        item.addOnUseAction( action1 );
        item.addOnUseAction( action2 );

        item.use();
        item.use();
    }

    @Test
    public void item_is_visible_by_default() {
        NormalItem item = new NormalItem();
        assertTrue( item.visible() );
    }

    @Test
    public void setting_item_to_visible_makes_it_visible() {
        NormalItem item = new NormalItem();
        item.setVisible( true );
        assertTrue( item.visible() );
    }

    @Test
    public void setting_item_to_invisible_makes_it_not_visible() {
        NormalItem item = new NormalItem();
        item.setVisible( false );
        assertFalse( item.visible() );
    }

    @Test
    public void examine_enacts_all_on_examine_actions() {
        final ItemAction action1 = mockery.mock( ItemAction.class, "act1" );
        final ItemAction action2 = mockery.mock( ItemAction.class, "act2" );
        mockery.checking( new Expectations() {{
            oneOf( action1 ).enact();
            ignoring( action1 );
            oneOf( action2 ).enact();
            ignoring( action2 );
        }});
        NormalItem item = new NormalItem();
        item.addOnExamineAction( action1 );
        item.addOnExamineAction( action2 );

        item.examine();
    }

    @Test
    public void set_examine_text_gets_returned() {
        NormalItem item = new NormalItem();
        item.setExamineText( "some examine text" );
        assertEquals( "some examine text", item.examineText() );
    }

    @Test
    public void examine_text_is_blank_by_default() {
        NormalItem item = new NormalItem();
        assertEquals( "", item.examineText() );
    }

    @Test
    public void set_examine_action_is_not_repeatable_gets_returned() {
        NormalItem item = new NormalItem();
        item.setExamineActionIsNotRepeatable();
        assertTrue( item.examineActionIsNotRepeatable() );
    }

    @Test
    public void examine_action_is_repeatable_by_default() {
        NormalItem item = new NormalItem();
        assertFalse( item.examineActionIsNotRepeatable() );
    }

    @Test
    public void examine_text_is_blank_upon_repeated_examines_when_examine_action_is_not_repeatable() {
        NormalItem item = new NormalItem();
        item.setExamineActionIsNotRepeatable();

        item.examine();
        item.examine();

        assertEquals( "", item.examineText() );
    }

    @Test
    public void examine_text_is_constant_upon_repeated_examines_when_examine_action_is_repeatable() {
        NormalItem item = new NormalItem();
        item.setExamineText( "some examine text" );

        item.examine();
        item.examine();

        assertEquals( "some examine text", item.examineText() );
    }

    @Test
    public void on_examine_actions_are_enacted_only_once_for_not_repeatable_examine() {
        final ItemAction action = mockery.mock( ItemAction.class );
        mockery.checking( new Expectations() {{
            oneOf( action ).enact();
        }});
        NormalItem item = new NormalItem();
        item.setExamineActionIsNotRepeatable();
        item.addOnExamineAction( action );

        item.examine();
        item.examine();
    }

    @Test
    public void on_examine_actions_are_enacted_repeatedly_for_repeatable_examine() {
        final ItemAction action = mockery.mock( ItemAction.class );
        mockery.checking( new Expectations() {{
            exactly( 2 ).of( action ).enact();
        }});
        NormalItem item = new NormalItem();
        item.addOnExamineAction( action );

        item.examine();
        item.examine();
    }

    @Test
    public void item_on_construction_cannot_be_talked_to() {
        assertThat( new NormalItem().canTalkTo(), is( false ) );
    }

    @Test
    public void item_can_be_talked_to_after_initial_phrase_added() {
        NormalItem item = new NormalItem();
        item.getTalkPhraseSink().addInitialPhrase( "id", "", "content" );
        assertThat( item.canTalkTo(), is( true ) );
    }

    @Test
    public void added_initial_phrase_ids_can_be_retrieved() {
        NormalItem item = new NormalItem();
        item.getTalkPhraseSink().addInitialPhrase( "id1", "", "content" );
        item.getTalkPhraseSink().addInitialPhrase( "id2", "", "content" );

        TalkPhraseSource source = item.getTalkPhraseSource();
        assertThat( source.initialPhraseIds().size(), is( 2 ) );
        assertThat( source.initialPhraseIds(), hasItem( "id1" ) );
        assertThat( source.initialPhraseIds(), hasItem( "id2" ) );
    }

    @Test
    public void added_initial_phrases_can_be_retrieved() {
        NormalItem item = new NormalItem();
        item.getTalkPhraseSink().addInitialPhrase( "id1", "", "content 1" );
        item.getTalkPhraseSink().addInitialPhrase( "id2", "", "content 2" );

        TalkPhraseSource source = item.getTalkPhraseSource();
        assertThat( source.phraseById( "id1" ), is( "content 1" ) );
        assertThat( source.phraseById( "id2" ), is( "content 2" ) );
    }

    @Test
    public void short_phrases_for_initial_phrases_can_be_retrieved() {
        NormalItem item = new NormalItem();
        item.getTalkPhraseSink().addInitialPhrase( "id", "short phrase", "main content" );
        assertThat( item.getTalkPhraseSource().shortPhraseById( "id" ), is( "short phrase" ) );
    }

    @Test
    public void added_response_can_be_retrieved() {
        NormalItem item = new NormalItem();
        item.getTalkPhraseSink().addResponse( "id1", "content 1" );
        item.getTalkPhraseSink().addResponse( "id2", "content 2" );

        TalkPhraseSource source = item.getTalkPhraseSource();
        assertThat( source.responseToPhraseById( "id1" ), is( "content 1" ) );
        assertThat( source.responseToPhraseById( "id2" ), is( "content 2" ) );
    }

    @Test
    public void added_follow_up_phrase_ids_can_be_retrieved() {
        NormalItem item = new NormalItem();
        item.getTalkPhraseSink().addFollowUpPhrase( "parent id 1", "id1", "", "content 1" );
        item.getTalkPhraseSink().addFollowUpPhrase( "parent id 1", "id2", "", "content 2" );
        item.getTalkPhraseSink().addFollowUpPhrase( "parent id 2", "id3", "", "content 3" );

        TalkPhraseSource source = item.getTalkPhraseSource();
        List<String> followOnPhrases = source.followOnPhrasesIdsForPhraseById( "parent id 1" );
        assertThat( followOnPhrases.size(), is( 2 ) );
        assertThat( followOnPhrases, hasItem( "id1" ) );
        assertThat( followOnPhrases, hasItem( "id2" ) );
        followOnPhrases = source.followOnPhrasesIdsForPhraseById( "parent id 2" );
        assertThat( followOnPhrases.size(), is( 1 ) );
        assertThat( followOnPhrases, hasItem( "id3" ) );
    }

    @Test
    public void added_follow_up_phrases_ids_only_appear_once() {
        NormalItem item = new NormalItem();
        item.getTalkPhraseSink().addFollowUpPhrase( "parent id 1", "id1", "", "content 1" );
        item.getTalkPhraseSink().addFollowUpPhrase( "parent id 1", "id1", "", "content 1" );

        TalkPhraseSource source = item.getTalkPhraseSource();
        List<String> followOnPhrases = source.followOnPhrasesIdsForPhraseById( "parent id 1" );
        assertThat( followOnPhrases.size(), is( 1 ) );
        assertThat( followOnPhrases, hasItem( "id1" ) );
    }

    @Test
    public void added_follow_up_phrases_can_be_retrieved() {
        NormalItem item = new NormalItem();
        item.getTalkPhraseSink().addFollowUpPhrase( "parent id 1", "id1", "", "content 1" );
        item.getTalkPhraseSink().addFollowUpPhrase( "parent id 1", "id2", "", "content 2" );
        item.getTalkPhraseSink().addFollowUpPhrase( "parent id 2", "id3", "", "content 3" );

        TalkPhraseSource source = item.getTalkPhraseSource();
        assertThat( source.phraseById( "id1" ), is( "content 1" ) );
        assertThat( source.phraseById( "id2" ), is( "content 2" ) );
        assertThat( source.phraseById( "id3" ), is( "content 3" ) );
    }

    @Test
    public void added_follow_up_phrases_without_content_refer_to_already_added_phrase_content() {
        NormalItem item = new NormalItem();
        item.getTalkPhraseSink().addFollowUpPhrase( "parent id 1", "id1", "", "content 1" );
        item.getTalkPhraseSink().addFollowUpPhrase( "parent id 2", "id1" );

        TalkPhraseSource source = item.getTalkPhraseSource();
        List<String> followOnPhrases = source.followOnPhrasesIdsForPhraseById( "parent id 2" );
        assertThat( followOnPhrases.size(), is( 1 ) );
        assertThat( followOnPhrases, hasItem( "id1" ) );
    }

    @Test
    public void follow_on_phrases_for_non_existent_id_is_empty() {
        NormalItem item = new NormalItem();
        TalkPhraseSource source = item.getTalkPhraseSource();
        List<String> followOnPhrases = source.followOnPhrasesIdsForPhraseById( "id" );
        assertThat( followOnPhrases.size(), is( 0 ) );
    }

    @Test
    public void short_phrases_for_follow_up_phrases_can_be_retrieved() {
        NormalItem item = new NormalItem();
        item.getTalkPhraseSink().addFollowUpPhrase( "parent id", "id", "short phrase", "main content" );
        assertThat( item.getTalkPhraseSource().shortPhraseById( "id" ), is( "short phrase" ) );
    }

    @Test
    public void added_actions_can_be_executed() {
        NormalItem item = new NormalItem();
        ItemAction action1 = mock( ItemAction.class );
        ItemAction action2 = mock( ItemAction.class );
        ItemAction action3 = mock( ItemAction.class );
        item.getTalkPhraseSink().addActionInResponseTo( "id1", action1 );
        item.getTalkPhraseSink().addActionInResponseTo( "id1", action2 );
        item.getTalkPhraseSink().addActionInResponseTo( "id2", action3 );

        item.getTalkPhraseSource().executeActionsForPhraseById( "id1" );

        verify( action1 ).enact();
        verify( action2 ).enact();
        verify( action3, never() ).enact();
    }

    @Test
    public void executing_actions_for_a_phrase_id_that_is_not_known_does_not_execute_any_actions() {
        NormalItem item = new NormalItem();
        ItemAction action = mock( ItemAction.class );
        item.getTalkPhraseSink().addActionInResponseTo( "id1", action );

        item.getTalkPhraseSource().executeActionsForPhraseById( "unknown id" );

        verify( action, never() ).enact();
    }

    @Test
    public void two_objects_with_the_same_value_should_be_equal() {
        NormalItem object1 = new NormalItem();
        object1.setName( "name" );
        NormalItem object2 = new NormalItem();
        object2.setName( "name" );

        assertEquals( object1, object2 );
    }

    @Test
    public void two_objects_with_different_values_should_not_be_equal() {
        NormalItem item1 = new NormalItem();
        item1.setName( "name a" );
        NormalItem item2 = new NormalItem();
        item2.setName( "name b" );

        assertNotEquals( item1, item2 );
    }

    @Test
    public void an_object_is_not_equal_to_a_non_object() {
        NormalItem object = new NormalItem();
        Object notANormalItem = new Object();

        assertNotEquals( object, notANormalItem );
    }

    @Test
    public void two_objects_with_the_same_value_should_have_the_same_hashcode() {
        NormalItem object1 = new NormalItem();
        object1.setName( "name" );
        NormalItem object2 = new NormalItem();
        object2.setName( "name" );

        assertEquals( object1.hashCode(), object2.hashCode() );
    }

    @Test
    public void two_objects_with_different_values_should_have_different_hashcodes() {
        NormalItem item1 = new NormalItem();
        item1.setName( "name a" );
        NormalItem item2 = new NormalItem();
        item2.setName( "name b" );

        assertNotEquals( item1.hashCode(), item2.hashCode() );
    }
}

