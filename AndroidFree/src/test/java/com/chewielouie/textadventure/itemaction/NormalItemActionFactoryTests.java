package com.chewielouie.textadventure.itemaction;

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

    @Test
    public void creates_DestroyItemItemActions() {
        NormalItemActionFactory factory = new NormalItemActionFactory( null );

        ItemAction action = factory.create(
               "destroy item:itemid",
               null );

        assertTrue( action instanceof DestroyItemItemAction );
    }

    @Test
    public void creates_ChangeItemVisibilityItemActions() {
        NormalItemActionFactory factory = new NormalItemActionFactory( null );

        ItemAction action = factory.create(
               "change item visibility:itemid:visible",
               null );

        assertTrue( action instanceof ChangeItemVisibilityItemAction );
    }
}

