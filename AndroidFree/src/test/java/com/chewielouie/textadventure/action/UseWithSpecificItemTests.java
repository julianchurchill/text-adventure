package com.chewielouie.textadventure.action;

import static org.junit.Assert.*;

import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.item.Item;

@RunWith(JMock.class)
public class UseWithSpecificItemTests {

    private Mockery mockery = new Mockery();

    UseWithSpecificItem createAction() {
        return new UseWithSpecificItem( null, null );
    }

    @Test
    public void name_is_correct() {
        assertEquals( "use with specific item", createAction().name() );
    }

    @Test
    public void label_is_target_item_name() {
        final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            allowing( item ).name();
            will( returnValue( "Item name" ) );
            ignoring( item );
        }});
        UseWithSpecificItem action = new UseWithSpecificItem( null, item );

        assertEquals( "Item name", action.label() );
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
        assertEquals( 0, createAction().followUpActions().size() );
    }

    @Test
    public void using_an_item_changes_the_user_text_to_the_use_with_message_of_the_target_item() {
        final Item original = mockery.mock( Item.class, "original" );
        final Item target = mockery.mock( Item.class, "target" );
        mockery.checking( new Expectations() {{
            ignoring( original );
            allowing( target ).canBeUsedWith( original );
            will( returnValue( true ) );
            allowing( target ).useWith( original );
            will( returnValue( "use with text" ) );
            ignoring( target );
        }});
        UseWithSpecificItem action = new UseWithSpecificItem( original, target );

        action.trigger();

        assertEquals( "use with text", action.userText() );
    }

    @Test
    public void using_an_item_causes_the_target_item_to_be_used() {
        final Item original = mockery.mock( Item.class, "original" );
        final Item target = mockery.mock( Item.class, "target" );
        mockery.checking( new Expectations() {{
            allowing( original ).id();
            will( returnValue( "originalid") );
            ignoring( original );
            oneOf( target ).useWith( original );
            ignoring( target );
        }});
        UseWithSpecificItem action = new UseWithSpecificItem( original, target );

        action.trigger();
    }

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


