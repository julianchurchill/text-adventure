package com.chewielouie.textadventure.itemaction;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.item.Item;

public class NormalItemActionFactoryTests {

    private Mockery mockery = new Mockery();

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
    public void creates_ChangeExitVisibilityItemActions() {
        NormalItemActionFactory factory = new NormalItemActionFactory( null );

        ItemAction action = factory.create(
               "change exit visibility:exitid:visible",
               null );

        assertTrue( action instanceof ChangeExitVisibilityItemAction );
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

    @Test
    public void creates_IncrementScoreItemActions() {
        NormalItemActionFactory factory = new NormalItemActionFactory( null );

        ItemAction action = factory.create(
               "increment score:",
               null );

        assertTrue( action instanceof IncrementScoreItemAction );
    }

    @Test
    public void creates_ChangeLocationDescriptionItemActions() {
        NormalItemActionFactory factory = new NormalItemActionFactory( null );

        ItemAction action = factory.create(
               "change location description:locid:new description",
               null );

        assertTrue( action instanceof ChangeLocationDescriptionItemAction );
    }

    @Test
    public void creates_TakeItemItemActions() {
        NormalItemActionFactory factory = new NormalItemActionFactory( null );

        ItemAction action = factory.create(
               "take item:itemid",
               null );

        assertTrue( action instanceof TakeItemItemAction );
    }
}
