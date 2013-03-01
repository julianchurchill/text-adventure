package com.chewielouie.textadventure.itemaction;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.item.Item;

@RunWith(JMock.class)
public class ChangeItemDescriptionItemActionTests {

    private Mockery mockery = new Mockery();

    @Test
    public void enact_changes_description_of_target_item() {
        final Item item = mockery.mock( Item.class );
        ChangeItemDescriptionItemAction action =
            new ChangeItemDescriptionItemAction( "new description", item );
        mockery.checking( new Expectations() {{
            oneOf( item ).setDescription( "new description" );
        }});

        action.enact();
    }
}

