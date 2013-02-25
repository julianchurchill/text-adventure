package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import org.junit.Test;

public class LocationExitFactoryTests {

    @Test
    public void produces_LocationExit_objects() {
        Exit exit = new LocationExitFactory().create();
        assertTrue( exit instanceof LocationExit );
    }
}

