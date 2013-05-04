package com.chewielouie.textadventure.action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.item.Item;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class UserActionFactoryTests {

    private Mockery mockery = new Mockery();

    @Test
    public void create_show_inventory_action_makes_right_action() {
        UserActionFactory factory = new UserActionFactory();

        Action action = factory.createShowInventoryAction( null, null );

        assertThat( action, is( instanceOf( ShowInventory.class ) ) );
    }

    @Test
    public void create_inventory_item_action_makes_right_action() {
        UserActionFactory factory = new UserActionFactory();

        Action action = factory.createInventoryItemAction( null, null,
                                                           null );

        assertThat( action, is( instanceOf( InventoryItem.class ) ) );
    }

    @Test
    public void create_examine_action_makes_right_action() {
        UserActionFactory factory = new UserActionFactory();

        Action action = factory.createExamineAction( null );

        assertThat( action, is( instanceOf( Examine.class ) ) );
    }

    @Test
    public void create_use_with_action_makes_right_action() {
        UserActionFactory factory = new UserActionFactory();

        Action action = factory.createUseWithAction( null, null,
                                                     null );

        assertThat( action, is( instanceOf( UseWith.class ) ) );
    }

    @Test
    public void create_examine_an_item_action_makes_right_action() {
        UserActionFactory factory = new UserActionFactory();

        List<Item> items = new ArrayList<Item>();
        Action action = factory.createExamineAnItemAction( items );

        assertThat( action, is( instanceOf( ExamineAnItem.class ) ) );
    }

    @Test
    public void create_take_an_item_action_makes_right_action() {
        UserActionFactory factory = new UserActionFactory();

        List<Item> items = new ArrayList<Item>();
        Action action = factory.createTakeAnItemAction( items, null,
                                                        null );

        assertThat( action, is( instanceOf( TakeAnItem.class ) ) );
    }

    @Test
    public void create_take_specific_item_action_makes_right_action() {
        UserActionFactory factory = new UserActionFactory();

        Action action = factory.createTakeSpecificItemAction( null,
                                            null, null );

        assertThat( action, is( instanceOf( TakeSpecificItem.class ) ) );
    }

    @Test
    public void create_use_with_specific_item_action_makes_right_action() {
        UserActionFactory factory = new UserActionFactory();

        Action action = factory.createUseWithSpecificItemAction( null, null );

        assertThat( action, is( instanceOf( UseWithSpecificItem.class ) ) );
    }

    @Test
    public void create_exit_action_makes_right_action() {
        UserActionFactory factory = new UserActionFactory();

        Action action = factory.createExitAction( null );

        assertThat( action, is( instanceOf( ExitAction.class ) ) );
    }
}
