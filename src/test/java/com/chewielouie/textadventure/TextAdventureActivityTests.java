package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.Robolectric;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.action.Action;

@RunWith(RobolectricTestRunner.class)
public class TextAdventureActivityTests {

    private Mockery mockery = new Mockery();

    private MotionEvent createUpMotionEvent( float x, float y ) {
        long downTime = 0;
        long eventTime = 0;
        int action = MotionEvent.ACTION_UP;
        int metaState = 0;
        return MotionEvent.obtain( downTime, eventTime, action, x, y, metaState );
    }

    private MotionEvent createDownMotionEvent( float x, float y ) {
        long downTime = 0;
        long eventTime = 0;
        int action = MotionEvent.ACTION_DOWN;
        int metaState = 0;
        return MotionEvent.obtain( downTime, eventTime, action, x, y, metaState );
    }

    @Test
    public void renders_the_presenter_on_resume() {
        final RendersView r = mockery.mock( RendersView.class );
        mockery.checking( new Expectations() {{
            oneOf( r ).render();
        }});

        new TextAdventureActivity( r ) {
            // Override to make onResume() accessible for testing
            @Override
            public void onResume() {
                super.onResume();
            }
        }.onResume();

        mockery.assertIsSatisfied();
    }

    @Test
    public void show_room_text_updates_text_view() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        activity.showMainText( "cheese" );

        final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        assertEquals( "cheese", t.getText().toString() );
    }

    @Test
    public void down_touch_event_is_ignored() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        mockery.checking( new Expectations() {{
            never( handler ).moveThroughExit( with( any( Exit.class ) ) );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit" ) );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        activity.onTouch( t, createDownMotionEvent( 0, 0 ) );

        mockery.assertIsSatisfied();
    }

    @Test
    public void touch_event_in_top_right_quadrant_causes_first_exit_to_be_used() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit = new Exit( "first exit" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit );
            ignoring( handler );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( exit );
        exits.add( new Exit( "second exit" ) );
        activity.showLocationExits( exits );

        TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        t.getLayoutParams().width = 100;
        t.getLayoutParams().height = 100;
        float x = 75;
        float y = 0;
        activity.onTouch( t, createUpMotionEvent( x, y ) );

        mockery.assertIsSatisfied();
    }

    @Test
    public void touch_event_in_top_left_quadrant_causes_first_exit_to_be_used() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit = new Exit( "first exit" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit );
            ignoring( handler );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( exit );
        exits.add( new Exit( "second exit" ) );
        activity.showLocationExits( exits );

        TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        t.getLayoutParams().width = 100;
        t.getLayoutParams().height = 100;
        float x = 25;
        float y = 0;
        activity.onTouch( t, createUpMotionEvent( x, y ) );

        mockery.assertIsSatisfied();
    }

    @Test
    public void touch_event_in_bottom_right_quadrant_causes_second_exit_to_be_used() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit = new Exit( "second exit" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit );
            ignoring( handler );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( exit );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        t.getLayoutParams().width = 100;
        t.getLayoutParams().height = 100;
        float x = 75;
        float y = 99;
        activity.onTouch( t, createUpMotionEvent( x, y ) );

        mockery.assertIsSatisfied();
    }

    @Test
    public void touch_event_in_bottom_left_quadrant_causes_second_exit_to_be_used() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit = new Exit( "second exit" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit );
            ignoring( handler );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( exit );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        t.getLayoutParams().width = 100;
        t.getLayoutParams().height = 100;
        float x = 25;
        float y = 99;
        activity.onTouch( t, createUpMotionEvent( x, y ) );

        mockery.assertIsSatisfied();
    }

    @Test
    public void touch_event_in_right_bottom_quadrant_causes_third_exit_to_be_used() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit = new Exit( "third exit" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit );
            ignoring( handler );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit" ) );
        exits.add( exit );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        t.getLayoutParams().width = 100;
        t.getLayoutParams().height = 100;
        float x = 99;
        float y = 75;
        activity.onTouch( t, createUpMotionEvent( x, y ) );

        mockery.assertIsSatisfied();
    }

    @Test
    public void touch_event_in_right_top_quadrant_causes_third_exit_to_be_used() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit = new Exit( "third exit" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit );
            ignoring( handler );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit" ) );
        exits.add( exit );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        t.getLayoutParams().width = 100;
        t.getLayoutParams().height = 100;
        float x = 99;
        float y = 25;
        activity.onTouch( t, createUpMotionEvent( x, y ) );

        mockery.assertIsSatisfied();
    }

    @Test
    public void touch_event_in_right_top_quadrant_causes_third_exit_to_be_used_on_non_square_view() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit = new Exit( "third exit" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit );
            ignoring( handler );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit" ) );
        exits.add( exit );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        t.getLayoutParams().width = 200;
        t.getLayoutParams().height = 400;
        float x = 102;
        float y = 199;
        activity.onTouch( t, createUpMotionEvent( x, y ) );

        mockery.assertIsSatisfied();
    }

    @Test
    public void touch_event_in_left_bottom_quadrant_causes_fourth_exit_to_be_used() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit = new Exit( "fourth exit" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit );
            ignoring( handler );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit" ) );
        exits.add( new Exit( "third exit" ) );
        exits.add( exit );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        t.getLayoutParams().width = 100;
        t.getLayoutParams().height = 100;
        float x = 1;
        float y = 75;
        assertTrue( 1 < (t.getLayoutParams().height-75) );
        activity.onTouch( t, createUpMotionEvent( x, y ) );

        mockery.assertIsSatisfied();
    }

    @Test
    public void touch_event_in_left_top_quadrant_causes_fourth_exit_to_be_used() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit = new Exit( "fourth exit" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit );
            ignoring( handler );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit" ) );
        exits.add( new Exit( "third exit" ) );
        exits.add( exit );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        t.getLayoutParams().width = 100;
        t.getLayoutParams().height = 100;
        float x = 1;
        float y = 25;
        activity.onTouch( t, createUpMotionEvent( x, y ) );

        mockery.assertIsSatisfied();
    }

    @Test
    public void touch_event_in_top_quadrant_with_no_exits_is_ignored() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        mockery.checking( new Expectations() {{
            never( handler ).moveThroughExit( with( any( Exit.class ) ) );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        float x = 0;
        float y = 0;
        activity.onTouch( t, createUpMotionEvent( x, y ) );

        mockery.assertIsSatisfied();
    }

    @Test
    public void click_top_label_uses_the_correct_exit() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit = new Exit( "first exit" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit );
            ignoring( handler );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( exit );
        exits.add( new Exit( "second exit" ) );
        activity.showLocationExits( exits );

        TextView topLabel = (TextView)activity.findViewById( R.id.top_direction_label );
        activity.onClick( topLabel );

        mockery.assertIsSatisfied();
    }

    @Test
    public void click_bottom_label_uses_the_correct_exit() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit = new Exit( "second exit" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit );
            ignoring( handler );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( exit );
        exits.add( new Exit( "third exit" ) );
        activity.showLocationExits( exits );

        TextView bottomLabel = (TextView)activity.findViewById( R.id.bottom_direction_label );
        activity.onClick( bottomLabel );

        mockery.assertIsSatisfied();
    }
    //@Test
    //public void click_right_label_uses_the_correct_exit() {
    //@Test
    //public void click_left_label_uses_the_correct_exit() {

    // And expand the following tests to account for exits with Directionhints
    // these cases are currently covered by tests above to do with onTouch

    @Test
    public void top_direction_label_uses_first_exit_text() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit" ) );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.top_direction_label );
        assertEquals( "first exit", t.getText().toString() );
    }

    @Test
    public void bottom_direction_label_uses_second_exit_text() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit" ) );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.bottom_direction_label );
        assertEquals( "second exit", t.getText().toString() );
    }

    @Test
    public void right_direction_label_uses_third_exit_text() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit" ) );
        exits.add( new Exit( "third exit" ) );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.right_direction_label );
        assertEquals( "third exit", t.getText().toString() );
    }

    @Test
    public void left_direction_label_uses_fourth_exit_text() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit" ) );
        exits.add( new Exit( "third exit" ) );
        exits.add( new Exit( "fourth exit" ) );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.left_direction_label );
        assertEquals( "fourth exit", t.getText().toString() );
    }

    @Test
    public void top_direction_label_is_blank_if_no_exits() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.top_direction_label );
        assertEquals( "", t.getText().toString() );
    }

    @Test
    public void bottom_direction_label_is_blank_if_less_than_two_exits() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.bottom_direction_label );
        assertEquals( "", t.getText().toString() );
    }

    @Test
    public void right_direction_label_is_blank_if_less_than_three_exits() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.right_direction_label );
        assertEquals( "", t.getText().toString() );
    }

    @Test
    public void left_direction_label_is_blank_if_less_than_four_exits() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.left_direction_label );
        assertEquals( "", t.getText().toString() );
    }

    @Test
    public void exit_with_north_direction_hint_is_preferred_on_top_label() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit", "dest", Exit.DirectionHint.North ) );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.top_direction_label );
        assertEquals( "second exit", t.getText().toString() );
    }

    @Test
    public void exit_with_south_direction_hint_is_preferred_on_bottom_label() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit", "dest", Exit.DirectionHint.South ) );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.bottom_direction_label );
        assertEquals( "first exit", t.getText().toString() );
    }

    @Test
    public void exit_with_east_direction_hint_is_preferred_on_right_label() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit", "dest", Exit.DirectionHint.East ) );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.right_direction_label );
        assertEquals( "first exit", t.getText().toString() );
    }

    @Test
    public void exit_with_west_direction_hint_is_preferred_on_left_label() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit", "dest", Exit.DirectionHint.West ) );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.left_direction_label );
        assertEquals( "first exit", t.getText().toString() );
    }

    @Test
    public void exits_without_direction_hints_fill_the_holes() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit", "dest", Exit.DirectionHint.North ) );
        exits.add( new Exit( "third exit" ) );
        exits.add( new Exit( "fourth exit", "dest", Exit.DirectionHint.East  ) );
        activity.showLocationExits( exits );

        TextView t = (TextView)activity.findViewById( R.id.top_direction_label );
        assertEquals( "second exit", t.getText().toString() );
        t = (TextView)activity.findViewById( R.id.bottom_direction_label );
        assertEquals( "first exit", t.getText().toString() );
        t = (TextView)activity.findViewById( R.id.right_direction_label );
        assertEquals( "fourth exit", t.getText().toString() );
        t = (TextView)activity.findViewById( R.id.left_direction_label );
        assertEquals( "third exit", t.getText().toString() );
    }

    @Test
    public void on_touch_must_never_consume_events_so_that_long_press_will_still_work() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "only exit" ) );
        activity.showLocationExits( exits );

        TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        t.getLayoutParams().width = 100;
        t.getLayoutParams().height = 100;
        float x = 75;
        float y = 0;
        assertFalse( activity.onTouch( t, createUpMotionEvent( 75, 0 ) ) );
    }

    @Test
    public void long_touch_does_not_trigger_an_exit_action() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        mockery.checking( new Expectations() {{
            never( handler ).moveThroughExit( with( any( Exit.class ) ) );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "only exit" ) );
        activity.showLocationExits( exits );

        TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        t.getLayoutParams().width = 100;
        t.getLayoutParams().height = 100;
        float x = 75;
        float y = 0;
        activity.onLongClick( t );
        activity.onTouch( t, createUpMotionEvent( x, y ) );

        mockery.assertIsSatisfied();
    }

    //@Test
    //public void long_touch_in_the_centre_shows_actions_menu() {
        //TextAdventureActivity activity = new TextAdventureActivity();
        //activity.onCreate( null );

        //final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        //t.getLayoutParams().width = 100;
        //t.getLayoutParams().height = 100;
        //float x = 50;
        //float y = 50;
        //activity.onTouch( t, createUpMotionEvent( x, y ) );

        //confirm actions menu is shown - don't know how to do this!
    //}

    @Test
    public void actions_menu_contains_last_set_of_actions_sent_with_set_actions() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );
        final ContextMenu menu = mockery.mock( ContextMenu.class );
        final TextView view = (TextView)activity.findViewById( R.id.main_text_output );
        final ContextMenuInfo menuInfo = mockery.mock( ContextMenuInfo.class );
        final Action action = mockery.mock( Action.class );
        mockery.checking( new Expectations() {{
            allowing( action ).label();
            will( returnValue( "action 1" ) );
            ignoring( action );
            oneOf( menu ).add( "action 1" );
            ignoring( menu );
            ignoring( menuInfo );
        }});

        List<Action> actions = new ArrayList<Action>();
        actions.add( action );
        activity.setActions( actions );

        activity.onCreateContextMenu( menu, view, menuInfo );

        mockery.assertIsSatisfied();
    }

    @Test
    public void selection_of_an_action_item_is_reported_to_user_action_handler() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        final MenuItem menuItem = mockery.mock( MenuItem.class );
        final Action action = mockery.mock( Action.class );
        mockery.checking( new Expectations() {{
            allowing( action ).label();
            will( returnValue( "action 1" ) );
            ignoring( action );
            allowing( menuItem ).getTitle();
            will( returnValue( "action 1" ) );
            ignoring( menuItem );
            oneOf( handler ).enact( action );
            ignoring( handler );
        }});

        List<Action> actions = new ArrayList<Action>();
        actions.add( action );
        activity.setActions( actions );

        assertTrue( activity.onContextItemSelected( menuItem ) );

        mockery.assertIsSatisfied();
    }

    //@Test
    //public void on_immediate_user_action_choice_show_a_context_menu_with_the_choices() {
        //TextAdventureActivity activity = new TextAdventureActivity();
        //activity.onCreate( null );
        //final Action action = mockery.mock( Action.class );
        //mockery.checking( new Expectations() {{
            //oneOf( action ).label();
            //will( returnValue( "action 1" ) );
            //ignoring( action );
        //}});

        //List<Action> actions = new ArrayList<Action>();
        //actions.add( action );
        //activity.giveUserImmediateActionChoice( actions );

        // The call to openContextMenu in giveUserImmediateActionChoice() doesn't trigger
        // onCreateContextMenu as expected and so this test fails...

        //mockery.assertIsSatisfied();
    //}

    //@Test
    //public void after_immediate_user_action_choice_context_menu_shows_original_actions() {
        //TextAdventureActivity activity = new TextAdventureActivity();
        //activity.onCreate( null );
        //final ContextMenu menu = mockery.mock( ContextMenu.class );
        //final TextView view = (TextView)activity.findViewById( R.id.main_text_output );
        //final ContextMenuInfo menuInfo = mockery.mock( ContextMenuInfo.class );
        //final Action action = mockery.mock( Action.class, "original action" );
        //final Action immediateAction = mockery.mock( Action.class, "immediate action" );
        //mockery.checking( new Expectations() {{
            //allowing( action ).label();
            //will( returnValue( "action 1" ) );
            //ignoring( action );
            //oneOf( menu ).add( "action 1" );
            //ignoring( menu );
            //ignoring( menuInfo );
            //ignoring( immediateAction );
        //}});

        //List<Action> immediateActions = new ArrayList<Action>();
        //immediateActions.add( immediateAction );
        //activity.giveUserImmediateActionChoice( immediateActions );

        // The call to openContextMenu in giveUserImmediateActionChoice() doesn't trigger
        // onCreateContextMenu as expected so this test is invalid...

        //List<Action> actions = new ArrayList<Action>();
        //actions.add( action );
        //activity.setActions( actions );

        //activity.onCreateContextMenu( menu, view, menuInfo );

        //mockery.assertIsSatisfied();
    //}
}

