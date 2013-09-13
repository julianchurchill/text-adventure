package com.chewielouie.textadventure.itemaction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.TextAdventureModel;

@RunWith(JMock.class)
public class TakeItemItemActionTests {

    private Mockery mockery = new Mockery();

    @Test
    public void enact_removes_item_from_location() {
        TextAdventureModel model = mock( TextAdventureModel.class );
        TakeItemItemAction action = new TakeItemItemAction( "itemid", model );

        action.enact();

        verify( model ).moveItemToInventory( "itemid" );
    }
}
