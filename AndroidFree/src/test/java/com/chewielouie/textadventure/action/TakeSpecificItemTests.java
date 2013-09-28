package com.chewielouie.textadventure.action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
public class TakeSpecificItemTests {

    private Mockery mockery = new Mockery();

    TakeSpecificItem createAction() {
        return new TakeSpecificItem( null, null, null );
    }

    TakeSpecificItem createAction( Item item ) {
        return new TakeSpecificItem( item, null, null );
    }

    @Test
    public void name_is_correct() {
        assertEquals( "take specific item", createAction( null ).name() );
    }

    @Test
    public void label_includes_item_name() {
        final Item item = mockery.mock( Item.class );
        TakeSpecificItem action = createAction( item );
        mockery.checking( new Expectations() {{
            allowing( item ).midSentenceCasedName();
            will( returnValue( "item name" ) );
            ignoring( item );
        }});

        assertEquals( "Take item name", action.label() );
    }

    @Test
    public void user_text_is_available() {
        TakeSpecificItem action = new TakeSpecificItem( null, null, null );
        assertTrue( action.userTextAvailable() );
    }

    @Test
    public void user_text_contains_item_name() {
        final Item item = mockery.mock( Item.class );
        TakeSpecificItem action = createAction( item );
        mockery.checking( new Expectations() {{
            allowing( item ).midSentenceCasedName();
            will( returnValue( "item name" ) );
            ignoring( item );
        }});

        assertEquals( "You take the item name.", action.userText() );
    }

    @Test
    public void user_must_choose_follow_up_action_is_always_false() {
        assertFalse( createAction().userMustChooseFollowUpAction() );
    }

    @Test
    public void trigger_adds_item_to_model_inventory() {
        final Item item = mockery.mock( Item.class );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        TakeSpecificItem action = new TakeSpecificItem( item, inventory, location );
        mockery.checking( new Expectations() {{
            ignoring( item );
            oneOf( inventory ).addToInventory( item );
            ignoring( inventory );
            ignoring( location );
        }});

        action.trigger();
    }

    @Test
    public void trigger_removes_item_from_location() {
        final Item item = mockery.mock( Item.class );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        TakeSpecificItem action = new TakeSpecificItem( item, inventory, location );
        mockery.checking( new Expectations() {{
            ignoring( item );
            ignoring( inventory );
            oneOf( location ).removeItem( item );
            ignoring( location );
        }});

        action.trigger();
    }

    @Test
    public void two_objects_with_the_same_item_should_be_equal() {
        Item item = mock( Item.class );
        TakeSpecificItem object1 = createAction( item );
        TakeSpecificItem object2 = createAction( item );

        assertEquals( object1, object2 );
    }

    @Test
    public void a_take_specific_item_object_is_not_equal_to_a_non_object() {
        TakeSpecificItem object = createAction();
        Object notATakeSpecificItem = new Object();

        assertNotEquals( object, notATakeSpecificItem );
    }

    @Test
    public void two_objects_with_the_same_item_should_have_the_same_hashcode() {
        Item item = mock( Item.class );
        TakeSpecificItem object1 = createAction( item );
        TakeSpecificItem object2 = createAction( item );

        assertEquals( object1.hashCode(), object2.hashCode() );
    }
}


