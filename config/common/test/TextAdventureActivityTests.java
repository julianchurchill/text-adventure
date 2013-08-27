package REPLACE_ME;

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
import com.chewielouie.textadventure.UserActionHandler;

@RunWith(RobolectricTestRunner.class)
public class TextAdventureActivityTests {

    private Mockery mockery = new Mockery();

    // This test is commented out because render() is now called at the
    // end of a thread started by onResume()
    // @Test
    // public void renders_the_presenter_on_resume() {
    //     final RendersView r = mockery.mock( RendersView.class );
    //     mockery.checking( new Expectations() {{
    //         oneOf( r ).render();
    //     }});

    //     TextAdventureDummyActivity t = new TextAdventureDummyActivity( r );
    //     t.onCreate( null );
    //     t.onResume();

    //     mockery.assertIsSatisfied();
    // }

    // This test no longer works because when TextView.setText() is called
    // with a Spannable you don't get the parsed content back with getText()
    // This might be a robolectric shadowing deficiency.
    //@Test
    //public void show_room_text_updates_text_view() {
        //TextAdventureDummyActivity activity = new TextAdventureDummyActivity();
        //activity.onCreate( null );

        //activity.showMainText( "cheese" );

        //final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        //assertEquals( "cheese", t.getText().toString() );
    //}

    @Test
    public void set_actions_updates_action_view_with_buttons_for_each_action() {
        TextAdventureDummyActivity activity = new TextAdventureDummyActivity();
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

        ViewGroup actionView = (ViewGroup)activity.findViewById( activity.R_id_available_actions() );
        assertEquals( 2, actionView.getChildCount() );
        Button button1 = (Button)actionView.getChildAt(0);
        Button button2 = (Button)actionView.getChildAt(1);
        assertEquals( "action 1", button1.getText().toString() );
        assertEquals( "action 2", button2.getText().toString() );
    }

    @Test
    public void pressing_an_action_view_button_triggers_action_on_user_event_handler() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        TextAdventureDummyActivity activity = new TextAdventureDummyActivity( handler );
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

        ViewGroup actionView = (ViewGroup)activity.findViewById( activity.R_id_available_actions() );
        Button button = (Button)actionView.getChildAt(0);
        button.performClick();

        mockery.assertIsSatisfied();
    }

    @Test
    public void action_view_buttons_update_to_show_follow_up_actions() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        TextAdventureDummyActivity activity = new TextAdventureDummyActivity( handler );
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

        ViewGroup actionView = (ViewGroup)activity.findViewById( activity.R_id_available_actions() );
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

    @Test
    public void current_score_updates_the_acquired_score_text() {
        TextAdventureDummyActivity activity = new TextAdventureDummyActivity();
        activity.onCreate( null );

        activity.currentScore( 5 );
        activity.maximumScore( 10 );

        final TextView t = (TextView)activity.findViewById( activity.R_id_score_text_view() );
        assertEquals( "50% completed", t.getText().toString() );
    }

    @Test
    public void pressing_back_finishes_the_activity_when_not_in_an_action_chain() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        TextAdventureDummyActivity activity = new TextAdventureDummyActivity( handler );
        activity.onCreate( null );
        mockery.checking( new Expectations() {{
            allowing( handler ).inAnActionChain();
            will( returnValue( false ) );
            ignoring( handler );
        }});

        activity.onBackPressed();
        assertTrue( activity.isFinishing() );
    }

    @Test
    public void pressing_back_whilst_in_an_action_chain_does_not_finish_the_activity() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        TextAdventureDummyActivity activity = new TextAdventureDummyActivity( handler );
        activity.onCreate( null );
        final Action action = mockery.mock( Action.class, "action" );
        final Action newAction = mockery.mock( Action.class, "new action" );
        mockery.checking( new Expectations() {{
            allowing( handler ).inAnActionChain();
            will( returnValue( true ) );
            ignoring( handler );
            ignoring( action );
            ignoring( newAction );
        }});

        enterActionChain( activity, action, newAction );
        activity.onBackPressed();

        assertFalse( activity.isFinishing() );
    }

    private void enterActionChain( TextAdventureDummyActivity activity, Action action, Action newAction ) {
        List<Action> actions = new ArrayList<Action>();
        actions.add( action );
        activity.setActions( actions );

        ViewGroup actionView = (ViewGroup)activity.findViewById( activity.R_id_available_actions() );
        Button button = (Button)actionView.getChildAt(0);
        button.performClick();
        List<Action> newActions = new ArrayList<Action>();
        newActions.add( newAction );
        activity.setActions( newActions );
    }

    @Test
    public void pressing_back_whilst_in_an_action_chain_cancels_the_chain_on_the_user_action_handler() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        TextAdventureDummyActivity activity = new TextAdventureDummyActivity( handler );
        activity.onCreate( null );
        final Action action = mockery.mock( Action.class, "action" );
        final Action newAction = mockery.mock( Action.class, "new action" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).cancelActionChain();
            allowing( handler ).inAnActionChain();
            will( returnValue( true ) );
            ignoring( handler );
            ignoring( action );
            ignoring( newAction );
        }});

        enterActionChain( activity, action, newAction );
        activity.onBackPressed();

        mockery.assertIsSatisfied();
    }
}

