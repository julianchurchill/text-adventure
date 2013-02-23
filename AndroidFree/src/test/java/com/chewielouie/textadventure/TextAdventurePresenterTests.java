package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ShowInventory;
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
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null );

        mockery.checking( new Expectations() {{
            allowing( model ).currentLocationDescription();
            will( returnValue( "some room text" ) );
            ignoring( model );
            oneOf( view ).showMainText( "some room text" );
            ignoring( view );
        }});

        p.render();
    }

    @Test
    public void render_tells_view_what_room_exits_are_available() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null );

        final List<Exit> exits = new ArrayList<Exit>();
        final Exit exit1 = mockery.mock( Exit.class, "exit1" );
        final Exit exit2 = mockery.mock( Exit.class, "exit2" );
        exits.add( exit1 );
        exits.add( exit2 );
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
    public void render_tells_of_view_default_actions() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null );

        final List<Action> actions = new ArrayList<Action>( p.defaultActions() );
        mockery.checking( new Expectations() {{
            ignoring( model );
            oneOf( view ).setActions( actions );
            ignoring( view );
        }});

        p.render();
    }

    @Test
    public void default_actions_include_show_inventory() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null );

        boolean actionsIncludesShowInventory = false;
        List<Action> actions = p.defaultActions();
        for( Action a : actions )
            if( a instanceof ShowInventory )
                actionsIncludesShowInventory = true;
        assertTrue( actionsIncludesShowInventory );
    }

    @Test
    public void render_includes_location_actions_in_view_set_actions() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null );

        Action action = mockery.mock( Action.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final List<Action> locationActions = new ArrayList<Action>();
        locationActions.add( action );
        final List<Action> actions = new ArrayList<Action>( p.defaultActions() );
        actions.add( action );
        mockery.checking( new Expectations() {{
            allowing( model ).currentLocation();
            will( returnValue( location ) );
            ignoring( model );
            allowing( location ).actions();
            will( returnValue( locationActions ) );
            ignoring( location );
            oneOf( view ).setActions( actions );
            ignoring( view );
        }});

        p.render();
    }

    @Test
    public void move_through_exit_calls_model() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null );
        final Exit north = mockery.mock( Exit.class, "exit" );
        mockery.checking( new Expectations() {{
            oneOf( model ).moveThroughExit( north );
            ignoring( model );
            ignoring( view );
            ignoring( north );
        }});

        p.moveThroughExit( north );
    }

    @Test
    public void move_through_exit_updates_view_with_location_description() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final Exit north = mockery.mock( Exit.class, "exit" );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null );
        mockery.checking( new Expectations() {{
            allowing( model ).currentLocationDescription();
            will( returnValue( "some room text" ) );
            ignoring( model );
            oneOf( view ).showMainText( "some room text" );
            ignoring( view );
        }});

        p.moveThroughExit( north );
    }

    @Test
    public void move_through_exit_updates_view_with_location_exits() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null );
        final Exit north = mockery.mock( Exit.class, "north" );
        final Exit south = mockery.mock( Exit.class, "south" );
        final List<Exit> exits = new ArrayList<Exit>();
        exits.add( north );
        exits.add( south );
        mockery.checking( new Expectations() {{
            allowing( model ).currentLocationExits();
            will( returnValue( exits ) );
            ignoring( model );
            oneOf( view ).showLocationExits( exits );
            ignoring( view );
            ignoring( north );
            ignoring( south );
        }});

        p.moveThroughExit( north );
    }

    @Test
    public void upon_enact_action_presenter_triggers_the_action() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null );
        final Action action = mockery.mock( Action.class );
        mockery.checking( new Expectations() {{
            oneOf( action ).trigger();
            ignoring( action );
            ignoring( model );
            ignoring( view );
        }});

        p.enact( action );
    }

    @Test
    public void upon_enact_action_that_requires_further_action_pass_new_actions_to_view() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null );
        final Action action = mockery.mock( Action.class, "original action" );
        final List<Action> actions = new ArrayList<Action>();
        actions.add( mockery.mock( Action.class, "follow up action" ) );
        mockery.checking( new Expectations() {{
            oneOf( action ).trigger();
            oneOf( action ).userMustChooseFollowUpAction();
            will( returnValue( true ) );
            oneOf( action ).followUpActions();
            will( returnValue( actions ) );
            ignoring( action );
            ignoring( model );
            oneOf( view ).setActions( actions );
            ignoring( view );
        }});

        p.enact( action );
    }

    @Test
    public void upon_enact_action_that_requires_no_further_action_tell_view_of_location_actions() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null );
        final Action action = mockery.mock( Action.class, "original action" );
        final List<Action> locationActions = new ArrayList<Action>( p.defaultActions() );
        mockery.checking( new Expectations() {{
            allowing( action ).trigger();
            allowing( action ).userMustChooseFollowUpAction();
            will( returnValue( false ) );
            ignoring( action );
            ignoring( model );
            oneOf( view ).setActions( locationActions );
            ignoring( view );
        }});

        p.enact( action );
    }

    @Test
    public void if_text_is_available_for_display_after_enacting_an_action_tell_the_view() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null );
        final Action action = mockery.mock( Action.class );
        mockery.checking( new Expectations() {{
            allowing( action ).trigger();
            allowing( action ).userTextAvailable();
            will( returnValue( true ) );
            allowing( action ).userText();
            will( returnValue( "some user text" ) );
            ignoring( action );
            allowing( model ).currentLocationDescription();
            will( returnValue( "location description" ) );
            ignoring( model );
            oneOf( view ).showMainText( "location description\n\nsome user text\n\n" );
            ignoring( view );
        }});

        p.enact( action );
    }

    @Test
    public void text_output_from_multiple_actions_is_appended_to_main_text() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null );
        final Action action = mockery.mock( Action.class );
        mockery.checking( new Expectations() {{
            allowing( action ).userTextAvailable();
            will( returnValue( true ) );
            allowing( action ).userText();
            will( returnValue( "some user text" ) );
            ignoring( action );
            allowing( model ).currentLocationDescription();
            will( returnValue( "location description" ) );
            ignoring( model );
            oneOf( view ).showMainText( "location description\n\nsome user text\n\nsome user text\n\n" );
            ignoring( view );
        }});

        p.enact( action );
        p.enact( action );
    }

    @Test
    public void changing_locations_clears_the_action_text_history() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null );
        final Action action = mockery.mock( Action.class );
        final Exit north = mockery.mock( Exit.class, "north" );
        mockery.checking( new Expectations() {{
            allowing( action ).userTextAvailable();
            will( returnValue( true ) );
            allowing( action ).userText();
            will( returnValue( "some user text" ) );
            ignoring( action );
            allowing( model ).currentLocationDescription();
            will( returnValue( "location description" ) );
            allowing( model ).moveThroughExit( north );
            ignoring( model );
            exactly(2).of( view ).showMainText( "location description\n\nsome user text\n\n" );
            ignoring( view );
            ignoring( north );
        }});

        p.enact( action );
        p.moveThroughExit( north );
        p.enact( action );
    }

    @Test
    public void actions_are_updated_on_view_after_enacting_any_action() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null );

        final Action action = mockery.mock( Action.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final List<Action> locationActions = new ArrayList<Action>();
        locationActions.add( action );
        final List<Action> actions = new ArrayList<Action>( p.defaultActions() );
        actions.add( action );
        mockery.checking( new Expectations() {{
            allowing( model ).currentLocation();
            will( returnValue( location ) );
            ignoring( model );
            allowing( location ).actions();
            will( returnValue( locationActions ) );
            ignoring( location );
            ignoring( action );
            oneOf( view ).setActions( actions );
            ignoring( view );
        }});

        p.enact( action );
    }
}

