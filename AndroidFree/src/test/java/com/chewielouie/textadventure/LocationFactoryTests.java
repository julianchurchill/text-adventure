package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class LocationFactoryTests {

    private Mockery mockery = new Mockery();

    @Test
    public void produces_Location_objects() {
        ModelLocation location = new LocationFactory( null, null ).create();
        assertTrue( location instanceof Location );
    }

    @Test
    public void passes_UserInventory_to_created_object() {
        final UserInventory inventory = mockery.mock( UserInventory.class );
        Location l = (Location)new LocationFactory( inventory, null ).create();

        assertEquals( inventory, l.inventory() );
    }
}

