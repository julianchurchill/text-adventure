package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import android.view.MotionEvent;
import android.widget.TextView;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.Robolectric;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

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

    @Test
    public void show_room_text_updates_text_view() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        activity.showLocationDescription( "cheese" );

        final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        assertEquals( "cheese", t.getText().toString() );
    }

    @Test
    public void touch_event_in_top_quadrant_causes_first_exit_to_be_used() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( "first exit" );
            ignoring( handler );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        List<String> exits = new ArrayList<String>();
        exits.add( "first exit" );
        exits.add( "second exit" );
        activity.showLocationExits( exits );

        long downTime = 0;
        long eventTime = 0;
        int action = 0;
        float x = 0;
        float y = 0;
        int metaState = 0;
        activity.dispatchTouchEvent( MotionEvent.obtain( downTime, eventTime,
                   action, x, y, metaState ) );

        mockery.assertIsSatisfied();
    }

    //public void touch_event_in_bottom_quadrant_causes_second_exit_to_be_used() {
    //public void touch_event_in_right_quadrant_causes_third_exit_to_be_used() {
    //public void touch_event_in_left_quadrant_causes_fourth_exit_to_be_used() {

    //public void touch_event_in_top_quadrant_with_no_exits_is_ignored() {
    //public void touch_event_in_second_quadrant_with_only_one_exit_is_ignored() {
    //public void touch_event_in_third_quadrant_with_only_two_exits_is_ignored() {
    //public void touch_event_in_fourth_quadrant_with_only_three_exits_is_ignored() {
}

