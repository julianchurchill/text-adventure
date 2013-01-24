package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class TextAdventurePresenterTests {

    private Mockery mockery = new Mockery();

    @Test
    public void render_tells_view_to_show_room_text_from_model() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model );

        mockery.checking( new Expectations() {{
            allowing( model ).currentRoomText();
            will( returnValue( "some room text" ) );
            oneOf( view ).showRoomText( "some room text" );
        }});

        p.render();
    }
}

