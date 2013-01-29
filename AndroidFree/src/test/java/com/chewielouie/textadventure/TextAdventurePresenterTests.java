package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class TextAdventurePresenterTests {

    private Mockery mockery = new Mockery();

    @Test
    public void render_tells_view_to_show_location_description_from_model() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model );

        mockery.checking( new Expectations() {{
            allowing( model ).currentLocationDescription();
            will( returnValue( "some room text" ) );
            ignoring( model );
            oneOf( view ).showLocationDescription( "some room text" );
            ignoring( view );
        }});

        p.render();
    }

    @Test
    public void render_tells_view_to_what_room_exits_are_available() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model );

        final List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "north" ) );
        exits.add( new Exit( "south" ) );
        mockery.checking( new Expectations() {{
            allowing( model ).currentLocationExits();
            will( returnValue( exits ) );
            ignoring( model );
            oneOf( view ).showLocationExits( exits );
            ignoring( view );
        }});

        p.render();
    }

    @Test
    public void move_through_exit_calls_model() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model );
        mockery.checking( new Expectations() {{
            oneOf( model ).moveThroughExit( "north" );
            ignoring( model );
            ignoring( view );
        }});

        p.moveThroughExit( "north" );
    }

    @Test
    public void move_through_exit_updates_view_with_location_description() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model );
        mockery.checking( new Expectations() {{
            allowing( model ).currentLocationDescription();
            will( returnValue( "some room text" ) );
            ignoring( model );
            oneOf( view ).showLocationDescription( "some room text" );
            ignoring( view );
        }});

        p.moveThroughExit( "north" );
    }

    @Test
    public void move_through_exit_updates_view_with_location_exits() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model );
        final List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "north" ) );
        exits.add( new Exit( "south" ) );
        mockery.checking( new Expectations() {{
            allowing( model ).currentLocationExits();
            will( returnValue( exits ) );
            ignoring( model );
            oneOf( view ).showLocationExits( exits );
            ignoring( view );
        }});

        p.moveThroughExit( "north" );
    }
}

