package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import org.junit.Test;

public class NormalItemFactoryTests {

    @Test
    public void produces_NormalItem_objects() {
        Item item = new NormalItemFactory().create();
        assertTrue( item instanceof NormalItem );
    }

    @Test
    public void produces_NormalItem_objects_with_default_cased_name() {
        Item item = new NormalItemFactory().create();
        item.setName( "Name" );
        assertEquals( "name", item.midSentenceCasedName() );
    }
}

