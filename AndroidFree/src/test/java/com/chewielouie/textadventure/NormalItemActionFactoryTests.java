package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import org.junit.Test;

public class NormalItemActionFactoryTests {

    @Test
    public void creates_ChangeItemDescriptionItemActions() {
        NormalItemActionFactory factory = new NormalItemActionFactory();

        ItemAction action = factory.create(
               "change item description:new description",
               null );

        assertTrue( action instanceof ChangeItemDescriptionItemAction );
    }

    @Test
    public void creates_ChangeItemNameItemActions() {
        NormalItemActionFactory factory = new NormalItemActionFactory();

        ItemAction action = factory.create(
               "change item name:new name",
               null );

        assertTrue( action instanceof ChangeItemNameItemAction );
    }
}

