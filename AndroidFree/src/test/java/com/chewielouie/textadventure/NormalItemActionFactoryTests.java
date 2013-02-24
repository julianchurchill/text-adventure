package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import org.junit.Test;

public class NormalItemActionFactoryTests {

    @Test
    public void creates_ChangeItemDescriptionItemActions() {
        NormalItemActionFactory factory = new NormalItemActionFactory( null );

        ItemAction action = factory.create(
               "change item description:new description",
               null );

        assertTrue( action instanceof ChangeItemDescriptionItemAction );
    }

    @Test
    public void creates_ChangeItemNameItemActions() {
        NormalItemActionFactory factory = new NormalItemActionFactory( null );

        ItemAction action = factory.create(
               "change item name:new name",
               null );

        assertTrue( action instanceof ChangeItemNameItemAction );
    }

    @Test
    public void creates_MakeExitVisibleItemActions() {
        NormalItemActionFactory factory = new NormalItemActionFactory( null );

        ItemAction action = factory.create(
               "make exit visible:exit name",
               null );

        assertTrue( action instanceof MakeExitVisibleItemAction );
    }
}

