package com.chewielouie.textadventure.action;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.Item;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.UserInventory;

@RunWith(JMock.class)
public class ExamineAnItemTests {

    private Mockery mockery = new Mockery();

    ExamineAnItem createAction() {
        return new ExamineAnItem( new ArrayList<Item>() );
    }

    @Test
    public void label_is_examine_an_item() {
        assertEquals( "Examine an item", createAction().label() );
    }

    @Test
    public void user_must_choose_follow_up_action_is_always_true() {
        assertTrue( createAction().userMustChooseFollowUpAction() );
    }

    @Test
    public void follow_up_actions_contains_Examine_actions_for_each_location_item() {
        final List<Item> items = new ArrayList<Item>();
        Item item1 = mockery.mock( Item.class, "item 1" );
        Item item2 = mockery.mock( Item.class, "item 2" );
        items.add( item1 );
        items.add( item2 );
        ExamineAnItem action = new ExamineAnItem( items );

        List<Action> actions = action.followUpActions();
        assertEquals( 2, actions.size() );
        assertTrue( actions.get(0) instanceof Examine );
        assertEquals( item1, ((Examine)actions.get(0)).item() );
        assertTrue( actions.get(1) instanceof Examine );
        assertEquals( item2, ((Examine)actions.get(1)).item() );
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


