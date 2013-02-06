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

    @Test
    public void click_right_label_uses_the_correct_exit() {
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
        exits.add( new Exit( "fourth exit" ) );
        activity.showLocationExits( exits );

        TextView rightLabel = (TextView)activity.findViewById( R.id.right_direction_label );
        activity.onClick( rightLabel );

        mockery.assertIsSatisfied();
    }

    @Test
    public void click_left_label_uses_the_correct_exit() {
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

        TextView leftLabel = (TextView)activity.findViewById( R.id.left_direction_label );
        activity.onClick( leftLabel );

        mockery.assertIsSatisfied();
    }

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

