package com.chewielouie.textadventure.action;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.Item;
import com.chewielouie.textadventure.NormalItem;
import com.chewielouie.textadventure.UserInventory;

@RunWith(JMock.class)
public class TakeSpecificItemTests {

    private Mockery mockery = new Mockery();

    TakeSpecificItem createAction( Item item ) {
        return new TakeSpecificItem( item, null );
    }

    @Test
    public void label_includes_item_name() {
        final Item item = mockery.mock( Item.class );
        TakeSpecificItem action = new TakeSpecificItem( item, null );
        mockery.checking( new Expectations() {{
            allowing( item ).name();
            will( returnValue( "item name" ) );
            ignoring( item );
        }});

        assertEquals( "Take item name", action.label() );
    }

    @Test
    public void user_must_choose_follow_up_action_is_always_false() {
        TakeSpecificItem object1 = createAction( new NormalItem( "", "" ) );
        assertFalse( object1.userMustChooseFollowUpAction() );
    }

    @Test
    public void trigger_adds_item_to_model_inventory() {
        final Item item = mockery.mock( Item.class );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        TakeSpecificItem action = new TakeSpecificItem( item, inventory );
        mockery.checking( new Expectations() {{
            ignoring( item );
            oneOf( inventory ).addToInventory( item );
            ignoring( inventory );
        }});

        action.trigger();
    }

    @Test
    public void two_objects_with_the_same_value_should_be_equal() {
        TakeSpecificItem object1 = createAction( new NormalItem( "", "" ) );
        TakeSpecificItem object2 = createAction( new NormalItem( "", "" ) );

        assertEquals( object1, object2 );
    }

    @Test
    public void a_show_inventory_object_is_not_equal_to_a_non_object() {
        TakeSpecificItem object = createAction( new NormalItem( "", "" ) );
        Object notATakeSpecificItem = new Object();

        assertNotEquals( object, notATakeSpecificItem );
    }

    @Test
    public void two_objects_with_the_same_value_should_have_the_same_hashcode() {
        TakeSpecificItem object1 = createAction( new NormalItem( "", "" ) );
        TakeSpecificItem object2 = createAction( new NormalItem( "", "" ) );

        assertEquals( object1.hashCode(), object2.hashCode() );
    }
}


