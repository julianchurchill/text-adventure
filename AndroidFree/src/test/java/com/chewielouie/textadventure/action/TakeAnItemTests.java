package com.chewielouie.textadventure.action;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.UserInventory;

@RunWith(JMock.class)
public class TakeAnItemTests {

    private Mockery mockery = new Mockery();

    TakeAnItem createAction() {
        return new TakeAnItem( new ArrayList<Item>(), null, null, null );
    }

    @Test
    public void name_is_correct() {
        assertEquals( "take an item", createAction().name() );
    }

    @Test
    public void label_is_take_an_item() {
        assertEquals( "Take an item", createAction().label() );
    }

    @Test
    public void user_must_choose_follow_up_action_is_always_true() {
        assertTrue( createAction().userMustChooseFollowUpAction() );
    }

    @Test
    public void follow_up_actions_use_action_factory_to_get_TakeSpecificItem_action_for_each_item() {
        final ActionFactory actionFactory = mockery.mock( ActionFactory.class );
        final Action takeAction1 = mockery.mock( Action.class, "action 1" );
        final Action takeAction2 = mockery.mock( Action.class, "action 2" );
        final List<Item> items = new ArrayList<Item>();
        final Item item1 = mockery.mock( Item.class, "item 1" );
        final Item item2 = mockery.mock( Item.class, "item 2" );
        items.add( item1 );
        items.add( item2 );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        mockery.checking( new Expectations() {{
            oneOf( actionFactory ).createTakeSpecificItemAction( item1, inventory, location );
            will( returnValue( takeAction1 ) );
            oneOf( actionFactory ).createTakeSpecificItemAction( item2, inventory, location );
            will( returnValue( takeAction2 ) );
            ignoring( actionFactory );
        }});
        TakeAnItem action = new TakeAnItem( items, inventory, location, actionFactory );

        List<Action> actions = action.followUpActions();
        assertTrue( actions.size() > 1 );
        assertThat( actions.get( 0 ), is( takeAction1 ) );
        assertThat( actions.get( 1 ), is( takeAction2 ) );
    }

    @Test
    public void two_objects_with_the_same_value_should_be_equal() {
        TakeAnItem object1 = createAction();
        TakeAnItem object2 = createAction();

        assertEquals( createAction(), createAction() );
    }

    @Test
    public void a_show_inventory_object_is_not_equal_to_a_non_object() {
        TakeAnItem object = createAction();
        Object notATakeAnItem = new Object();

        assertNotEquals( object, notATakeAnItem );
    }

    @Test
    public void two_objects_with_the_same_value_should_have_the_same_hashcode() {
        TakeAnItem object1 = createAction();
        TakeAnItem object2 = createAction();

        assertEquals( object1.hashCode(), object2.hashCode() );
    }
}

