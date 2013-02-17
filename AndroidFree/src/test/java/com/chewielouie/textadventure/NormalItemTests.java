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
        NormalItem item = new NormalItem( "NAME", "description", "a", "Name" );
        assertTrue( item.takeable() );
    }

    @Test
    public void setting_untakeable_makes_item_not_takeable() {
        NormalItem item = new NormalItem( "NAME", "description", "a", "Name" );
        item.setUntakeable();
        assertFalse( item.takeable() );
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
    public void deserialise_countable_noun_prefix_must_come_after_id() {
        NormalItem item = new NormalItem( "", "" );
        item.deserialise( "item name:Name\n" +
                          "item description:description\n" +
                          "item countable noun prefix:some\n" +
                          "item id:id\n" );
        assertFalse( item.countableNounPrefix().equals( "some" ) );
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
    public void deserialise_extracts_item_is_untakeable() {
        NormalItem item = new NormalItem( "", "" );
        item.deserialise( "item name:Name\n" +
                          "item description:description\n" +
                          "item is untakeable:\n" );
        assertFalse( item.takeable() );
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
}

