package com.chewielouie.textadventure;

import static org.junit.Assert.*;

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
}

