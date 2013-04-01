package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    // This test no longer works because when TextView.setText() is called
    // with a Spannable you don't get the parsed content back with getText()
    // This might be a robolectric shadowing deficiency.
    //@Test
    //public void show_room_text_updates_text_view() {
        //TextAdventureActivity activity = new TextAdventureActivity();
        //activity.onCreate( null );

        //activity.showMainText( "cheese" );

        //final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        //assertEquals( "cheese", t.getText().toString() );
    //}

    @Test
    public void click_top_label_uses_the_correct_exit() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit1 = mockery.mock( Exit.class, "exit1" );
        final Exit exit2 = mockery.mock( Exit.class, "exit2" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit1 );
            ignoring( handler );
            allowing( exit1 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit1 );
            allowing( exit2 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit2 );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( exit1 );
        exits.add( exit2 );
        activity.showLocationExits( exits );

        TextView topLabel = (TextView)activity.findViewById( R.id.top_direction_label );
        activity.onClick( topLabel );

        mockery.assertIsSatisfied();
    }

    @Test
    public void click_bottom_label_uses_the_correct_exit() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit1 = mockery.mock( Exit.class, "exit1" );
        final Exit exit2 = mockery.mock( Exit.class, "exit2" );
        final Exit exit3 = mockery.mock( Exit.class, "exit3" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit2 );
            ignoring( handler );
            allowing( exit1 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit1 );
            allowing( exit2 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit2 );
            allowing( exit3 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit3 );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( exit1 );
        exits.add( exit2 );
        exits.add( exit3 );
        activity.showLocationExits( exits );

        TextView bottomLabel = (TextView)activity.findViewById( R.id.bottom_direction_label );
        activity.onClick( bottomLabel );

        mockery.assertIsSatisfied();
    }

    @Test
    public void click_right_label_uses_the_correct_exit() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit1 = mockery.mock( Exit.class, "exit1" );
        final Exit exit2 = mockery.mock( Exit.class, "exit2" );
        final Exit exit3 = mockery.mock( Exit.class, "exit3" );
        final Exit exit4 = mockery.mock( Exit.class, "exit4" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit3 );
            ignoring( handler );
            allowing( exit1 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit1 );
            allowing( exit2 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit2 );
            allowing( exit3 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit3 );
            allowing( exit4 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit4 );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( exit1 );
        exits.add( exit2 );
        exits.add( exit3 );
        exits.add( exit4 );
        activity.showLocationExits( exits );

        TextView rightLabel = (TextView)activity.findViewById( R.id.right_direction_label );
        activity.onClick( rightLabel );

        mockery.assertIsSatisfied();
    }

    @Test
    public void click_left_label_uses_the_correct_exit() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit1 = mockery.mock( Exit.class, "exit1" );
        final Exit exit2 = mockery.mock( Exit.class, "exit2" );
        final Exit exit3 = mockery.mock( Exit.class, "exit3" );
        final Exit exit4 = mockery.mock( Exit.class, "exit4" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit4 );
            ignoring( handler );
            allowing( exit1 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit1 );
            allowing( exit2 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit2 );
            allowing( exit3 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit3 );
            allowing( exit4 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit4 );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( exit1 );
        exits.add( exit2 );
        exits.add( exit3 );
        exits.add( exit4 );
        activity.showLocationExits( exits );

        TextView leftLabel = (TextView)activity.findViewById( R.id.left_direction_label );
        activity.onClick( leftLabel );

        mockery.assertIsSatisfied();
    }

    @Test
    public void top_direction_label_uses_first_exit_text() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        final Exit exit1 = mockery.mock( Exit.class, "exit1" );
        final Exit exit2 = mockery.mock( Exit.class, "exit2" );
        mockery.checking( new Expectations() {{
            allowing( exit1 ).label();
            will( returnValue( "first exit" ) );
            allowing( exit1 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit1 );
            allowing( exit2 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit2 );
        }});
        exits.add( exit1 );
        exits.add( exit2 );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.top_direction_label );
        assertEquals( "first exit", t.getText().toString() );
    }

    @Test
    public void bottom_direction_label_uses_second_exit_text() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        final Exit exit1 = mockery.mock( Exit.class, "exit1" );
        final Exit exit2 = mockery.mock( Exit.class, "exit2" );
        mockery.checking( new Expectations() {{
            allowing( exit1 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit1 );
            allowing( exit2 ).label();
            will( returnValue( "second exit" ) );
            allowing( exit2 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit2 );
        }});
        exits.add( exit1 );
        exits.add( exit2 );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.bottom_direction_label );
        assertEquals( "second exit", t.getText().toString() );
    }

    @Test
    public void right_direction_label_uses_third_exit_text() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        final Exit exit1 = mockery.mock( Exit.class, "exit1" );
        final Exit exit2 = mockery.mock( Exit.class, "exit2" );
        final Exit exit3 = mockery.mock( Exit.class, "exit3" );
        mockery.checking( new Expectations() {{
            allowing( exit1 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit1 );
            allowing( exit2 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit2 );
            allowing( exit3 ).label();
            will( returnValue( "third exit" ) );
            allowing( exit3 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit3 );
        }});
        exits.add( exit1 );
        exits.add( exit2 );
        exits.add( exit3 );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.right_direction_label );
        assertEquals( "third exit", t.getText().toString() );
    }

    @Test
    public void left_direction_label_uses_fourth_exit_text() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        final Exit exit1 = mockery.mock( Exit.class, "exit1" );
        final Exit exit2 = mockery.mock( Exit.class, "exit2" );
        final Exit exit3 = mockery.mock( Exit.class, "exit3" );
        final Exit exit4 = mockery.mock( Exit.class, "exit4" );
        mockery.checking( new Expectations() {{
            allowing( exit1 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit1 );
            allowing( exit2 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit2 );
            allowing( exit3 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit3 );
            allowing( exit4 ).label();
            will( returnValue( "fourth exit" ) );
            allowing( exit4 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            ignoring( exit4 );
        }});
        exits.add( exit1 );
        exits.add( exit2 );
        exits.add( exit3 );
        exits.add( exit4 );
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
        final Exit exit1 = mockery.mock( Exit.class, "exit1" );
        final Exit exit2 = mockery.mock( Exit.class, "exit2" );
        mockery.checking( new Expectations() {{
            allowing( exit2 ).label();
            will( returnValue( "second exit" ) );
            allowing( exit2 ).directionHint();
            will( returnValue( Exit.DirectionHint.North ) );
            ignoring( exit1 );
            ignoring( exit2 );
        }});
        exits.add( exit1 );
        exits.add( exit2 );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.top_direction_label );
        assertEquals( "second exit", t.getText().toString() );
    }

    @Test
    public void exit_with_south_direction_hint_is_preferred_on_bottom_label() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        final Exit exit1 = mockery.mock( Exit.class, "exit1" );
        mockery.checking( new Expectations() {{
            allowing( exit1 ).label();
            will( returnValue( "first exit" ) );
            allowing( exit1 ).directionHint();
            will( returnValue( Exit.DirectionHint.South ) );
            ignoring( exit1 );
        }});
        exits.add( exit1 );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.bottom_direction_label );
        assertEquals( "first exit", t.getText().toString() );
    }

    @Test
    public void exit_with_east_direction_hint_is_preferred_on_right_label() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        final Exit exit1 = mockery.mock( Exit.class, "exit1" );
        mockery.checking( new Expectations() {{
            allowing( exit1 ).label();
            will( returnValue( "first exit" ) );
            allowing( exit1 ).directionHint();
            will( returnValue( Exit.DirectionHint.East ) );
            ignoring( exit1 );
        }});
        exits.add( exit1 );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.right_direction_label );
        assertEquals( "first exit", t.getText().toString() );
    }

    @Test
    public void exit_with_west_direction_hint_is_preferred_on_left_label() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        final Exit exit1 = mockery.mock( Exit.class, "exit1" );
        mockery.checking( new Expectations() {{
            allowing( exit1 ).label();
            will( returnValue( "first exit" ) );
            allowing( exit1 ).directionHint();
            will( returnValue( Exit.DirectionHint.West ) );
            ignoring( exit1 );
        }});
        exits.add( exit1 );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.left_direction_label );
        assertEquals( "first exit", t.getText().toString() );
    }

    @Test
    public void exits_without_direction_hints_fill_the_holes() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        final Exit exit1 = mockery.mock( Exit.class, "exit1" );
        final Exit exit2 = mockery.mock( Exit.class, "exit2" );
        final Exit exit3 = mockery.mock( Exit.class, "exit3" );
        final Exit exit4 = mockery.mock( Exit.class, "exit4" );
        mockery.checking( new Expectations() {{
            allowing( exit1 ).label();
            will( returnValue( "first exit" ) );
            allowing( exit1 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            allowing( exit2 ).label();
            will( returnValue( "second exit" ) );
            allowing( exit2 ).directionHint();
            will( returnValue( Exit.DirectionHint.North ) );
            allowing( exit3 ).label();
            will( returnValue( "third exit" ) );
            allowing( exit3 ).directionHint();
            will( returnValue( Exit.DirectionHint.DontCare ) );
            allowing( exit4 ).label();
            will( returnValue( "fourth exit" ) );
            allowing( exit4 ).directionHint();
            will( returnValue( Exit.DirectionHint.East ) );
            ignoring( exit1 );
            ignoring( exit2 );
            ignoring( exit3 );
            ignoring( exit4 );
        }});
        exits.add( exit1 );
        exits.add( exit2 );
        exits.add( exit3 );
        exits.add( exit4 );
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
    public void set_actions_updates_action_view_with_buttons_for_each_action() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );
        final Action action1 = mockery.mock( Action.class, "action 1" );
        final Action action2 = mockery.mock( Action.class, "action 2" );
        mockery.checking( new Expectations() {{
            allowing( action1 ).label();
            will( returnValue( "action 1" ) );
            ignoring( action1 );
            allowing( action2 ).label();
            will( returnValue( "action 2" ) );
            ignoring( action2 );
        }});

        List<Action> actions = new ArrayList<Action>();
        actions.add( action1 );
        actions.add( action2 );
        activity.setActions( actions );

        ViewGroup actionView = (ViewGroup)activity.findViewById( R.id.available_actions );
        assertEquals( 2, actionView.getChildCount() );
        Button button1 = (Button)actionView.getChildAt(0);
        Button button2 = (Button)actionView.getChildAt(1);
        assertEquals( "action 1", button1.getText().toString() );
        assertEquals( "action 2", button2.getText().toString() );
    }

    @Test
    public void pressing_an_action_view_button_triggers_action_on_user_event_handler() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );
        final Action action = mockery.mock( Action.class );
        mockery.checking( new Expectations() {{
            allowing( action ).label();
            will( returnValue( "action" ) );
            ignoring( action );
            oneOf( handler ).enact( action );
            ignoring( handler );
        }});

        List<Action> actions = new ArrayList<Action>();
        actions.add( action );
        activity.setActions( actions );

        ViewGroup actionView = (ViewGroup)activity.findViewById( R.id.available_actions );
        Button button = (Button)actionView.getChildAt(0);
        button.performClick();

        mockery.assertIsSatisfied();
    }

    @Test
    public void action_view_buttons_update_to_show_follow_up_actions() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );
        final Action action = mockery.mock( Action.class, "action" );
        final Action newAction1 = mockery.mock( Action.class, "new action 1" );
        final Action newAction2 = mockery.mock( Action.class, "new action 2" );
        mockery.checking( new Expectations() {{
            allowing( action ).label();
            will( returnValue( "action" ) );
            ignoring( action );
            allowing( newAction1 ).label();
            will( returnValue( "new action 1" ) );
            ignoring( newAction1 );
            allowing( newAction2 ).label();
            will( returnValue( "new action 2" ) );
            ignoring( newAction2 );
            ignoring( handler );
        }});

        List<Action> actions = new ArrayList<Action>();
        actions.add( action );
        activity.setActions( actions );

        ViewGroup actionView = (ViewGroup)activity.findViewById( R.id.available_actions );
        Button button = (Button)actionView.getChildAt(0);
        button.performClick();
        List<Action> newActions = new ArrayList<Action>();
        newActions.add( newAction1 );
        newActions.add( newAction2 );
        activity.setActions( newActions );

        assertEquals( 2, actionView.getChildCount() );
        Button button1 = (Button)actionView.getChildAt(0);
        Button button2 = (Button)actionView.getChildAt(1);
        assertEquals( "new action 1", button1.getText().toString() );
        assertEquals( "new action 2", button2.getText().toString() );
    }

    //@Test
    //public void action_view_title_starts_as_Actions() {
    //@Test
    //public void action_view_title_changes_when_action_chosen() {
    //@Test
    //public void action_view_title_resets_to_Actions_when_action_chain_is_completed() {
}

