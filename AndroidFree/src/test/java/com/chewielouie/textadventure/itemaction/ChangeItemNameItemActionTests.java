package com.chewielouie.textadventure.itemaction;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.item.Item;

@RunWith(JMock.class)
public class ChangeItemNameItemActionTests {

    private Mockery mockery = new Mockery();

    @Test
    public void enact_changes_name_of_target_item() {
        final Item item = mockery.mock( Item.class );
        ChangeItemNameItemAction action =
            new ChangeItemNameItemAction( "new name", item );
        mockery.checking( new Expectations() {{
            oneOf( item ).setName( "new name" );
        }});

        action.enact();
    }
}

