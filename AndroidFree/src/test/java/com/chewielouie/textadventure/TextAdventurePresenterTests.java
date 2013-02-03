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
    public void render_tells_view_default_action_of_show_inventory_is_available() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model );

        final List<Action> actions = new ArrayList<Action>();
        actions.add( new ShowInventory( null ) );
        mockery.checking( new Expectations() {{
            ignoring( model );
            oneOf( view ).setActions( actions );
            ignoring( view );
        }});

        p.render();
    }

    @Test
    public void move_through_exit_calls_model() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model );
        final Exit north = new Exit( "north" );
        mockery.checking( new Expectations() {{
            oneOf( model ).moveThroughExit( north );
            ignoring( model );
            ignoring( view );
        }});

        p.moveThroughExit( north );
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

        p.moveThroughExit( new Exit( "north" ) );
    }

    @Test
    public void move_through_exit_updates_view_with_location_exits() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model );
        final Exit north = new Exit( "north" );
        final List<Exit> exits = new ArrayList<Exit>();
        exits.add( north );
        exits.add( new Exit( "south" ) );
        mockery.checking( new Expectations() {{
            allowing( model ).currentLocationExits();
            will( returnValue( exits ) );
            ignoring( model );
            oneOf( view ).showLocationExits( exits );
            ignoring( view );
        }});

        p.moveThroughExit( north );
    }

    @Test
    public void upon_enact_action_presenter_triggers_the_action() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model );
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
        TextAdventurePresenter p = new TextAdventurePresenter( view, model );
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
            oneOf( view ).giveUserImmediateActionChoice( actions );
            ignoring( view );
        }});

        p.enact( action );
    }

    @Test
    public void upon_enact_action_that_requires_no_further_action_do_not_pass_any_new_actions_to_view() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, model );
        final Action action = mockery.mock( Action.class, "original action" );
        mockery.checking( new Expectations() {{
            allowing( action ).trigger();
            allowing( action ).userMustChooseFollowUpAction();
            will( returnValue( false ) );
            ignoring( action );
            ignoring( model );
            never( view ).giveUserImmediateActionChoice( with( any( List.class ) ) );
            ignoring( view );
        }});

        p.enact( action );
    }
}

