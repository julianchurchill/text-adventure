package com.chewielouie.textadventure.itemaction;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.TextAdventureModel;

@RunWith(JMock.class)
public class ChangeItemVisibilityItemActionTests {

    private Mockery mockery = new Mockery();

    @Test
    public void enact_finds_item_in_the_model() {
        final TextAdventureModel model =
            mockery.mock( TextAdventureModel.class );
        ChangeItemVisibilityItemAction action =
            new ChangeItemVisibilityItemAction( "itemid:visible", model );
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( model ).findItemByID( "itemid" );
            will( returnValue( item ) );
            ignoring( model );
            ignoring( item );
        }});

        action.enact();
    }

    @Test
    public void enact_makes_item_visible() {
        final TextAdventureModel model =
            mockery.mock( TextAdventureModel.class );
        ChangeItemVisibilityItemAction action =
            new ChangeItemVisibilityItemAction( "itemid:visible", model );
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            allowing( model ).findItemByID( "itemid" );
            will( returnValue( item ) );
            ignoring( model );
            oneOf( item ).setVisible( true );
            ignoring( item );
        }});

        action.enact();
    }

    @Test
    public void enact_makes_item_invisible() {
        final TextAdventureModel model =
            mockery.mock( TextAdventureModel.class );
        ChangeItemVisibilityItemAction action =
            new ChangeItemVisibilityItemAction( "itemid:invisible", model );
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            allowing( model ).findItemByID( "itemid" );
            will( returnValue( item ) );
            ignoring( model );
            oneOf( item ).setVisible( false );
            ignoring( item );
        }});

        action.enact();
    }

    @Test
    public void enact_makes_item_visible_by_default() {
        final TextAdventureModel model =
            mockery.mock( TextAdventureModel.class );
        ChangeItemVisibilityItemAction action =
            new ChangeItemVisibilityItemAction( "itemid:unknown", model );
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            allowing( model ).findItemByID( "itemid" );
            will( returnValue( item ) );
            ignoring( model );
            oneOf( item ).setVisible( true );
            ignoring( item );
        }});

        action.enact();
    }
}

