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

