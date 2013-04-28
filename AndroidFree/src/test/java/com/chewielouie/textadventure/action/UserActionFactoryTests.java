package com.chewielouie.textadventure.action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class UserActionFactoryTests {

    private Mockery mockery = new Mockery();

    @Test
    public void create_show_inventory_action_makes_right_action() {
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        UserActionFactory factory = new UserActionFactory();

        Action action = factory.createShowInventoryAction( inventory, model );

        assertThat( action, is( instanceOf( ShowInventory.class ) ) );
    }

    @Test
    public void create_inventory_item_action_makes_right_action() {
        final Item item = mockery.mock( Item.class );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            ignoring( item );
            ignoring( inventory );
            ignoring( location );
        }});
        UserActionFactory factory = new UserActionFactory();

        Action action = factory.createInventoryItemAction( item, inventory,
                                                           location );

        assertThat( action, is( instanceOf( InventoryItem.class ) ) );
    }

    @Test
    public void create_examine_action_makes_right_action() {
        final Item item = mockery.mock( Item.class );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            ignoring( item );
            ignoring( inventory );
            ignoring( location );
        }});
        UserActionFactory factory = new UserActionFactory();

        Action action = factory.createExamineAction( item );

        assertThat( action, is( instanceOf( Examine.class ) ) );
    }

    @Test
    public void create_use_with_action_makes_right_action() {
        final Item item = mockery.mock( Item.class );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            ignoring( item );
            ignoring( inventory );
            ignoring( location );
        }});
        UserActionFactory factory = new UserActionFactory();

        Action action = factory.createUseWithAction( item, inventory,
                                                           location );

        assertThat( action, is( instanceOf( UseWith.class ) ) );
    }

    @Test
    public void create_examine_an_item_action_makes_right_action() {
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            ignoring( item );
        }});
        UserActionFactory factory = new UserActionFactory();

        List<Item> items = new ArrayList<Item>();
        Action action = factory.createExamineAnItemAction( items );

        assertThat( action, is( instanceOf( ExamineAnItem.class ) ) );
    }

    @Test
    public void create_take_an_item_action_makes_right_action() {
        final Item item = mockery.mock( Item.class );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            ignoring( item );
            ignoring( inventory );
            ignoring( location );
        }});
        UserActionFactory factory = new UserActionFactory();

        List<Item> items = new ArrayList<Item>();
        Action action = factory.createTakeAnItemAction( items, inventory,
                                                        location );

        assertThat( action, is( instanceOf( TakeAnItem.class ) ) );
    }
}
