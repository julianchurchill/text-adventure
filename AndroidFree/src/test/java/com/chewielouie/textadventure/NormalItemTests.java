package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class NormalItemTests {

    private Mockery mockery = new Mockery();

    @Test
    public void countable_noun_prefix_defaults_to_a() {
        NormalItem item = new NormalItem( "name", "description" );
        assertEquals( "a", item.countableNounPrefix() );
    }

    @Test
    public void countable_noun_prefix_is_as_specified() {
        NormalItem item = new NormalItem( "name", "description", "some" );
        assertEquals( "some", item.countableNounPrefix() );
    }

    @Test
    public void mid_sentence_cased_name_defaults_to_name_lower_cased() {
        NormalItem item = new NormalItem( "NAME", "description" );
        assertEquals( "name", item.midSentenceCasedName() );
    }

    @Test
    public void mid_sentence_cased_name_can_be_supplied_on_construction() {
        NormalItem item = new NormalItem( "NAME", "description", "a", "Name" );
        assertEquals( "Name", item.midSentenceCasedName() );
    }

    @Test
    public void item_is_takeable_by_default() {
        NormalItem item = new NormalItem( "", "", "", "" );
        assertTrue( item.takeable() );
    }

    @Test
    public void setting_untakeable_makes_item_not_takeable() {
        NormalItem item = new NormalItem( "", "", "", "" );
        item.setUntakeable();
        assertFalse( item.takeable() );
    }

    @Test
    public void a_target_item_needs_an_id_to_test_for_usability() {
        NormalItem item = new NormalItem( "", "", "", "" );
        final Item itemWithoutID = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            allowing( itemWithoutID ).id();
            will( returnValue( "" ) );
            ignoring( itemWithoutID );
        }});

        assertFalse( item.canBeUsedWith( itemWithoutID ) );
    }

    @Test
    public void a_target_item_needs_an_id_to_test_for_usability_even_after_deserialisation() {
        final Item itemWithoutID = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            allowing( itemWithoutID ).id();
            will( returnValue( "" ) );
            ignoring( itemWithoutID );
        }});
        NormalItem item = new NormalItem( "", "" );
        item.deserialise( "item name:Name\n" +
                          "item description:description\n" );

        assertFalse( item.canBeUsedWith( itemWithoutID ) );
    }

    @Test
    public void used_with_text_is_blank_by_default() {
        NormalItem item = new NormalItem( "", "", "", "" );
        assertEquals( "", item.usedWithText() );
    }

    @Test
    public void item_use_is_repeatable_by_default() {
        NormalItem item = new NormalItem( "", "", "", "" );
        assertTrue( item.useIsRepeatable() );
    }

    @Test
    public void item_use_is_not_repeatable_can_be_set() {
        NormalItem item = new NormalItem( "", "", "", "" );
        item.setUseIsNotRepeatable();
        assertFalse( item.useIsRepeatable() );
    }

    @Test
    public void used_with_text_is_succesful_use_message_for_non_repeatable_use_item_before_being_used() {
        NormalItem item = new NormalItem( "", "", "", "" );
        item.deserialise( "item name:Name\n" +
                          "item successful use message:message\n" );
        item.setUseIsNotRepeatable();
        assertEquals( "message", item.usedWithText() );
    }

    @Test
    public void used_with_text_is_deserialised_message_for_non_repeatable_use_item_after_being_used_once() {
        NormalItem item = new NormalItem( "", "", "", "" );
        item.deserialise( "item name:Name\n" +
                          "item successful use message:message\n" );
        item.setUseIsNotRepeatable();

        item.use();

        assertEquals( "message", item.usedWithText() );
    }

    @Test
    public void used_with_text_is_failure_message_for_non_repeatable_use_item_after_being_used_twice() {
        NormalItem item = new NormalItem( "", "", "", "" );
        item.deserialise( "item name:Name\n" +
                          "item successful use message:message\n" );
        item.setUseIsNotRepeatable();

        item.use();
        item.use();

        assertEquals( "You have already done that.", item.usedWithText() );
    }

    @Test
    public void used_with_text_is_successful_use_message_for_repeatable_use_item_after_being_used_twice() {
        NormalItem item = new NormalItem( "", "", "", "" );
        item.deserialise( "item name:Name\n" +
                          "item successful use message:message\n" );

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
        NormalItem item = new NormalItem( "", "", "", "" );
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
        NormalItem item = new NormalItem( "", "", "", "" );
        item.setUseIsNotRepeatable();
        item.addOnUseAction( action1 );
        item.addOnUseAction( action2 );

        item.use();
        item.use();
    }

    @Test
    public void two_objects_with_the_same_value_should_be_equal() {
        NormalItem object1 = new NormalItem( "name", "description" );
        NormalItem object2 = new NormalItem( "name", "description" );

        assertEquals( object1, object2 );
    }

    @Test
    public void two_objects_with_different_values_should_not_be_equal() {
        NormalItem item1 = new NormalItem( "name a", "description a" );
        NormalItem item2 = new NormalItem( "name b", "description b" );

        assertNotEquals( item1, item2 );
    }

    @Test
    public void an_object_is_not_equal_to_a_non_object() {
        NormalItem object = new NormalItem( "name", "description" );
        Object notANormalItem = new Object();

        assertNotEquals( object, notANormalItem );
    }

    @Test
    public void two_objects_with_the_same_value_should_have_the_same_hashcode() {
        NormalItem object1 = new NormalItem( "name", "description" );
        NormalItem object2 = new NormalItem( "name", "description" );

        assertEquals( object1.hashCode(), object2.hashCode() );
    }

    @Test
    public void two_objects_with_different_values_should_have_different_hashcodes() {
        NormalItem item1 = new NormalItem( "name a", "description a" );
        NormalItem item2 = new NormalItem( "name b", "description b" );

        assertNotEquals( item1.hashCode(), item2.hashCode() );
    }

    @Test
    public void deserialise_extracts_item_name() {
        NormalItem item = new NormalItem( "", "" );
        item.deserialise( "item name:Name\n" +
                          "item description:description\n" );
        assertEquals( "Name", item.name() );
    }

    @Test
    public void deserialise_extracts_item_description() {
        NormalItem item = new NormalItem( "", "" );
        item.deserialise( "item name:Name\n" +
                          "item description:description\n" );
        assertEquals( "description", item.description() );
    }

    @Test
    public void deserialise_extracts_item_id() {
        NormalItem item = new NormalItem( "", "" );
        item.deserialise( "item name:Name\n" +
                          "item description:description\n" +
                          "item id:an id\n" );
        assertEquals( "an id", item.id() );
    }

    @Test
    public void deserialise_extracts_item_countable_noun_prefix() {
        NormalItem item = new NormalItem( "", "" );
        item.deserialise( "item name:Name\n" +
                          "item description:description\n" +
                          "item countable noun prefix:some\n" );
        assertEquals( "some", item.countableNounPrefix() );
    }

    @Test
    public void deserialise_extracts_item_mid_sentence_cased_name() {
        NormalItem item = new NormalItem( "", "" );
        item.deserialise( "item name:Name\n" +
                          "item description:description\n" +
                          "item mid sentence cased name:cased name\n" );
        assertEquals( "cased name", item.midSentenceCasedName() );
    }

    @Test
    public void deserialise_extracts_item_is_untakeable() {
        NormalItem item = new NormalItem( "", "" );
        item.deserialise( "item name:Name\n" +
                          "item description:description\n" +
                          "item is untakeable:\n" );
        assertFalse( item.takeable() );
    }

    @Test
    public void deserialise_extracts_item_can_be_used_with() {
        final Item target = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            allowing( target ).id();
            will( returnValue( "itemid" ) );
            ignoring( target );
        }});
        NormalItem item = new NormalItem( "", "" );
        assertFalse( item.canBeUsedWith( target ) );

        item.deserialise( "item name:Name\n" +
                          "item description:description\n" +
                          "item can be used with:itemid\n" );
        assertTrue( item.canBeUsedWith( target ) );
    }

    @Test
    public void deserialise_extracts_item_successful_use_messsage() {
        NormalItem item = new NormalItem( "", "" );

        item.deserialise( "item name:Name\n" +
                          "item description:description\n" +
                          "item successful use message:message\n" );
        assertEquals( "message", item.usedWithText() );
    }

    @Test
    public void deserialise_extracts_item_use_is_not_repeatable() {
        NormalItem item = new NormalItem( "", "" );

        item.deserialise( "item name:Name\n" +
                          "item description:description\n" +
                          "item use is not repeatable:\n" );
        assertFalse( item.useIsRepeatable() );
    }

    @Test
    public void deserialise_extracts_multiple_item_use_actions_with_ItemAction_factory() {
        final ItemActionFactory itemActionFactory = mockery.mock( ItemActionFactory.class );
        mockery.checking( new Expectations() {{
            exactly( 2 ).of( itemActionFactory ).create( with( any( String.class ) ) );
            ignoring( itemActionFactory );
        }});
        NormalItem item = new NormalItem( "", "", itemActionFactory );

        item.deserialise( "item name:Name\n" +
                          "item description:description\n" +
                          "item use action:action:action arguments\n" +
                          "item use action:action:action arguments\n" );
    }

    @Test
    public void deserialise_extracts_item_use_action_into_ItemAction_object() {
        final ItemActionFactory itemActionFactory = mockery.mock( ItemActionFactory.class );
        final ItemAction action = mockery.mock( ItemAction.class );
        mockery.checking( new Expectations() {{
            oneOf( itemActionFactory ).create( "action:action arguments" );
            will( returnValue( action ) );
            ignoring( itemActionFactory );
        }});
        NormalItem item = new NormalItem( "", "", itemActionFactory );

        item.deserialise( "item name:Name\n" +
                          "item description:description\n" +
                          "item use action:action:action arguments\n" );
    }

    @Test
    public void deserialise_extracts_item_use_action_and_enacts_it_during_use() {
        final ItemActionFactory itemActionFactory = mockery.mock( ItemActionFactory.class );
        final ItemAction action = mockery.mock( ItemAction.class );
        mockery.checking( new Expectations() {{
            allowing( itemActionFactory ).create( with( any( String.class ) ) );
            will( returnValue( action ) );
            ignoring( itemActionFactory );
            oneOf( action ).enact();
            ignoring( action );
        }});
        NormalItem item = new NormalItem( "", "", itemActionFactory );

        item.deserialise( "item name:Name\n" +
                          "item description:description\n" +
                          "item use action:action:action arguments\n" );
        item.use();
    }

    @Test
    public void deserialise_countable_noun_prefix_must_come_after_id() {
        NormalItem item = new NormalItem( "", "" );
        item.deserialise( "item name:Name\n" +
                          "item description:description\n" +
                          "item countable noun prefix:some\n" +
                          "item id:id\n" );
        assertFalse( item.countableNounPrefix().equals( "some" ) );
    }

    @Test
    public void deserialise_mid_sentence_cased_name_must_come_after_countable_noun_prefix() {
        NormalItem item = new NormalItem( "", "" );
        item.deserialise( "item name:Name\n" +
                          "item description:description\n" +
                          "item mid sentence cased name:cased name\n" +
                          "item countable noun prefix:some\n" );
        assertFalse( item.midSentenceCasedName().equals( "cased name" ) );
    }

    @Test
    public void deserialise_mid_sentence_cased_name_must_come_after_id() {
        NormalItem item = new NormalItem( "", "" );
        item.deserialise( "item name:Name\n" +
                          "item description:description\n" +
                          "item mid sentence cased name:cased name\n" +
                          "item id:id\n" );
        assertFalse( item.midSentenceCasedName().equals( "cased name" ) );
    }

    @Test
    public void deserialise_untakeable_must_come_after_countable_noun_prefix() {
        NormalItem item = new NormalItem( "", "" );
        item.deserialise( "item name:Name\n" +
                          "item description:description\n" +
                          "item is untakeable:\n" +
                          "item countable noun prefix:some\n" );
        assertTrue( item.takeable() );
    }

    @Test
    public void deserialise_untakeable_must_come_after_mid_sentence_cased_name() {
        NormalItem item = new NormalItem( "", "" );
        item.deserialise( "item name:Name\n" +
                          "item description:description\n" +
                          "item is untakeable:\n" +
                          "item mid sentence cased name:cased name\n" );
        assertTrue( item.takeable() );
    }

    @Test
    public void deserialise_item_countable_noun_prefix_is_optional() {
        NormalItem item = new NormalItem( "", "" );
        item.deserialise( "item name:Name\n" +
                          "item description:description\n" );
        assertEquals( "", item.countableNounPrefix() );
    }

    @Test
    public void deserialise_item_mid_sentence_cased_name_is_optional() {
        NormalItem item = new NormalItem( "", "" );
        item.deserialise( "item name:Name\n" +
                       "item description:description\n" );
        assertEquals( "name", item.midSentenceCasedName() );
    }

    @Test
    public void deserialise_item_untakeable_is_optional() {
        NormalItem item = new NormalItem( "", "" );
        item.deserialise( "item name:Name\n" +
                          "item description:description\n" );
        assertTrue( item.takeable() );
    }

    @Test
    public void deserialise_item_id_is_optional() {
        NormalItem item = new NormalItem( "", "" );
        item.deserialise( "item name:Name\n" +
                          "item description:description\n" );
        assertEquals( "", item.id() );
    }
}

