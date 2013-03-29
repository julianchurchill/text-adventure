package com.chewielouie.textadventure.itemaction;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.TextAdventureModel;

@RunWith(JMock.class)
public class DestroyItemItemActionTests {

    private Mockery mockery = new Mockery();

    @Test
    public void enact_destroys_item_in_the_model() {
        final TextAdventureModel model =
            mockery.mock( TextAdventureModel.class );
        DestroyItemItemAction action =
            new DestroyItemItemAction( "itemid", model );
        mockery.checking( new Expectations() {{
            oneOf( model ).destroyItem( "itemid" );
        }});

        action.enact();
    }
}

