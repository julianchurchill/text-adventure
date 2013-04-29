package com.chewielouie.textadventure.itemaction;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.ItemDecorator;

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
    public void ChangeItemDescriptionItemAction_has_item_decorated() {    
        final ItemDecorator decorator = mockery.mock( ItemDecorator.class );
        final Item item = mockery.mock( Item.class, "item" );
        final Item decoratedItem = mockery.mock( Item.class, "decorated item" );
        mockery.checking( new Expectations() {{
            allowing( decorator ).decorate( item ); will( returnValue( decoratedItem ) );
            ignoring( decorator );
            ignoring( item );
            ignoring( decoratedItem );
        }});
        NormalItemActionFactory factory = new NormalItemActionFactory( null );
        factory.setItemDecorator( decorator );

        ChangeItemDescriptionItemAction action =
            (ChangeItemDescriptionItemAction )factory.create(
               "change item description:new description", item );
        assertThat( action.item(), is( decoratedItem ) );
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
    public void ChangeItemNameItemAction_has_item_decorated() {    
        final ItemDecorator decorator = mockery.mock( ItemDecorator.class );
        final Item item = mockery.mock( Item.class, "item" );
        final Item decoratedItem = mockery.mock( Item.class, "decorated item" );
        mockery.checking( new Expectations() {{
            allowing( decorator ).decorate( item ); will( returnValue( decoratedItem ) );
            ignoring( decorator );
            ignoring( item );
            ignoring( decoratedItem );
        }});
        NormalItemActionFactory factory = new NormalItemActionFactory( null );
        factory.setItemDecorator( decorator );

        ChangeItemNameItemAction action =
            (ChangeItemNameItemAction )factory.create(
               "change item name:new name", item );
        assertThat( action.item(), is( decoratedItem ) );
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
}

