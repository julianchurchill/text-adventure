package com.chewielouie.textadventure.item;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class NormalItemFactoryTests {

    private Mockery mockery = new Mockery();

    NormalItemFactory createFactory() {
        return new NormalItemFactory();
    }

    @Test
    public void produces_NormalItem_objects() {
        Item item = createFactory().create();
        assertTrue( item instanceof NormalItem );
    }

    @Test
    public void produces_NormalItem_objects_with_default_cased_name() {
        Item item = createFactory().create();
        item.setName( "Name" );
        assertEquals( "name", item.midSentenceCasedName() );
    }
}

