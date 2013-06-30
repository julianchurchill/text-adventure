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
public class ExamineAnItemTests {

    private Mockery mockery = new Mockery();

    ExamineAnItem createAction() {
        return new ExamineAnItem( new ArrayList<Item>(), null );
    }

    @Test
    public void name_is_correct() {
        assertEquals( "examine an item", createAction().name() );
    }

    @Test
    public void label_is_examine_an_item() {
        assertEquals( "Examine", createAction().label() );
    }

    @Test
    public void user_must_choose_follow_up_action_is_always_true() {
        assertTrue( createAction().userMustChooseFollowUpAction() );
    }

    @Test
    public void follow_up_actions_use_action_factory_to_get_Examine_action() {
        final ActionFactory actionFactory = mockery.mock( ActionFactory.class );
        final Action examineAction1 = mockery.mock( Action.class, "action 1" );
        final Action examineAction2 = mockery.mock( Action.class, "action 2" );
        final List<Item> items = new ArrayList<Item>();
        final Item item1 = mockery.mock( Item.class, "item 1" );
        final Item item2 = mockery.mock( Item.class, "item 2" );
        items.add( item1 );
        items.add( item2 );
        mockery.checking( new Expectations() {{
            oneOf( actionFactory ).createExamineAction( item1 );
            will( returnValue( examineAction1 ) );
            oneOf( actionFactory ).createExamineAction( item2 );
            will( returnValue( examineAction2 ) );
            ignoring( actionFactory );
        }});
        ExamineAnItem action = new ExamineAnItem( items, actionFactory );

        List<Action> actions = action.followUpActions();
        assertTrue( actions.size() > 1 );
        assertThat( actions.get( 0 ), is( examineAction1 ) );
        assertThat( actions.get( 1 ), is( examineAction2 ) );
    }

    @Test
    public void two_objects_with_the_same_value_should_be_equal() {
        ExamineAnItem object1 = createAction();
        ExamineAnItem object2 = createAction();

        assertEquals( createAction(), createAction() );
    }

    @Test
    public void a_show_inventory_object_is_not_equal_to_a_non_object() {
        ExamineAnItem object = createAction();
        Object notAExamineAnItem = new Object();

        assertNotEquals( object, notAExamineAnItem );
    }

    @Test
    public void two_objects_with_the_same_value_should_have_the_same_hashcode() {
        ExamineAnItem object1 = createAction();
        ExamineAnItem object2 = createAction();

        assertEquals( object1.hashCode(), object2.hashCode() );
    }
}


