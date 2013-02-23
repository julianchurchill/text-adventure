package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class NormalItemFactoryTests {

    private Mockery mockery = new Mockery();

    NormalItemFactory createFactory() {
        return new NormalItemFactory( null );
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

    @Test
    public void passes_ItemActionFactory_on_to_NormalItem_objects() {
        final ItemActionFactory itemActionFactory =
            mockery.mock( ItemActionFactory.class );
        NormalItem item =
            (NormalItem)new NormalItemFactory( itemActionFactory ).create();

        assertEquals( itemActionFactory, item.itemActionFactory() );
    }
}

