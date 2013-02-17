package com.chewielouie.textadventure.action;

import static org.junit.Assert.*;

import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.Item;

@RunWith(JMock.class)
public class UseWithSpecificItemTests {

    private Mockery mockery = new Mockery();

    UseWithSpecificItem createAction() {
        return new UseWithSpecificItem( null );
    }

    @Test
    public void label_is_item_name() {
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            allowing( item ).name();
            will( returnValue( "Item name" ) );
            ignoring( item );
        }});
        UseWithSpecificItem action = new UseWithSpecificItem( item );

        assertEquals( "Item name", action.label() );
    }

    @Test
    public void user_text_contains_failure_message_by_default() {
        final Item item = mockery.mock( Item.class );
        UseWithSpecificItem action = new UseWithSpecificItem( item );
        assertEquals( "Nothing happens.", action.userText() );
    }

    @Test
    public void user_text_available_is_true() {
        assertTrue( createAction().userTextAvailable() );
    }

    @Test
    public void user_must_choose_follow_up_action_is_always_false() {
        assertFalse( createAction().userMustChooseFollowUpAction() );
    }

    @Test
    public void follow_up_actions_is_empty() {
        Item item = mockery.mock( Item.class );
        UseWithSpecificItem action = new UseWithSpecificItem( item );

        List<Action> actions = action.followUpActions();
        assertEquals( 0, actions.size() );
    }

    @Test
    public void using_an_item_that_is_usable_with_the_target_item_changes_the_user_text_to_the_success_message_of_target_item() {
        final Item original = mockery.mock( Item.class, "original" );
        final Item target = mockery.mock( Item.class, "target" );
        mockery.checking( new Expectations() {{
            allowing( original ).id();
            will( returnValue( "originalid") );
            ignoring( original );
            allowing( target ).canBeUsedWith( original );
            will( returnValue( true ) );
            allowing( target ).usedWithSuccessText( with( any( Item.class ) ) );
            will( returnValue( "success text" ) );
            ignoring( target );
        }});
        UseWithSpecificItem action = new UseWithSpecificItem( original, target );

        action.trigger();

        assertEquals( "success text", action.userText() );
    }

    //@Test
    //public void using_an_item_causes_the_target_item_to_be_queried_for_usability() {
        //final Item original = mockery.mock( Item.class );
        //final Item target = mockery.mock( Item.class );
        //mockery.checking( new Expectations() {{
            //allowing( original ).id();
            //will( returnValue( "originalid") );
            //ignoring( original );
            //oneOf( target ).canBeUsedWith( "originalid" );
            //will( returnValue( true ) );
            //ignoring( target );
        //}});
        //UseWithSpecificItem action = new UseWithSpecificItem( original, target );

        //action.trigger();
    //}

    //@Test
    //public void using_an_item_causes_the_original_item_to_be_queried_for_usability() {
        //final Item original = mockery.mock( Item.class );
        //final Item target = mockery.mock( Item.class );
        //mockery.checking( new Expectations() {{
            //oneOf( original ).canBeUsedWith( "targetid" );
            //will( returnValue( true ) );
            //ignoring( original );
            //allowing( target ).id();
            //will( returnValue( "targetid") );
            //ignoring( target );
        //}});
        //UseWithSpecificItem action = new UseWithSpecificItem( original, target );

        //action.trigger();
    //}

    //@Test
    //public void using_an_item_that_is_usable_with_the_target_causes_the_original_item_id_to_be_used_to_retrieve_the_success_text() {
        //final Item original = mockery.mock( Item.class );
        //final Item target = mockery.mock( Item.class );
        //mockery.checking( new Expectations() {{
            //allowing( original ).id();
            //will( returnValue( "originalid") );
            //ignoring( original );
            //allowing( target ).canBeUsedWith( "originalid" );
            //will( returnValue( true ) );
            //oneOf( target ).usedWithSuccessText( "originalid" );
            //will( returnValue( "success text" ) );
            //ignoring( target );
        //}});
        //UseWithSpecificItem action = new UseWithSpecificItem( original, target );

        //action.trigger();

        //assertEquals( "success text", action.userText() );
    //}

    //@Test
    //public void using_an_item_that_is_not_usable_with_the_target_item_leaves_the_user_text_as_the_default_failure_message() {

    @Test
    public void two_objects_with_the_same_value_should_be_equal() {
        UseWithSpecificItem object1 = createAction();
        UseWithSpecificItem object2 = createAction();

        assertEquals( createAction(), createAction() );
    }

    @Test
    public void a_show_inventory_object_is_not_equal_to_a_non_object() {
        UseWithSpecificItem object = createAction();
        Object notAUseWithSpecificItem = new Object();

        assertNotEquals( object, notAUseWithSpecificItem );
    }

    @Test
    public void two_objects_with_the_same_value_should_have_the_same_hashcode() {
        UseWithSpecificItem object1 = createAction();
        UseWithSpecificItem object2 = createAction();

        assertEquals( object1.hashCode(), object2.hashCode() );
    }
}


