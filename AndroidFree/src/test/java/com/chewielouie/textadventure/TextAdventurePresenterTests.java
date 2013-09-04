package com.chewielouie.textadventure;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionFactory;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class TextAdventurePresenterTests {

    private Mockery mockery = new Mockery();

    @Test
    public void render_tells_view_to_show_contextual_text_from_model() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        mockery.checking( new Expectations() {{
            allowing( model ).contextualText();
            will( returnValue( "some contextual text" ) );
            ignoring( model );
            oneOf( view ).showMainText( "some contextual text" );
            ignoring( view );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.render();
    }

    @Test
    public void render_tells_view_to_show_location_description_from_model() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        mockery.checking( new Expectations() {{
            allowing( model ).currentLocationDescription();
            will( returnValue( "some room text" ) );
            ignoring( model );
            oneOf( view ).showMainText( "some room text" );
            ignoring( view );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.render();
    }

    @Test
    public void render_appends_location_description_to_contextual_text() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        mockery.checking( new Expectations() {{
            allowing( model ).contextualText();
            will( returnValue( "some contextual text" ) );
            allowing( model ).currentLocationDescription();
            will( returnValue( "some room text" ) );
            ignoring( model );
            oneOf( view ).showMainText( "some contextual text\nsome room text" );
            ignoring( view );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.render();
    }

    @Test
    public void render_tells_view_of_available_items_text() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        mockery.checking( new Expectations() {{
            allowing( model ).availableItemsText();
            will( returnValue( "some items text" ) );
            ignoring( model );
            oneOf( view ).showAvailableItemsText( "some items text" );
            ignoring( view );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.render();
    }

    @Test
    public void render_tells_view_what_room_exits_are_available() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
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
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.render();
    }

    @Test
    public void render_tells_view_of_default_actions() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        mockery.checking( new Expectations() {{
            ignoring( model );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        final List<Action> actions = new ArrayList<Action>( p.defaultActions() );
        mockery.checking( new Expectations() {{
            oneOf( view ).setActions( actions );
            ignoring( view );
        }});
        p.render();
    }

    @Test
    public void default_actions_include_show_inventory() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final ActionFactory actionFactory = mockery.mock( ActionFactory.class );
        final Action action = mockery.mock( Action.class );
        mockery.checking( new Expectations() {{
            oneOf( actionFactory ).createShowInventoryAction( inventory, model );
            will( returnValue( action ) );
            ignoring( actionFactory );
            ignoring( model );
            ignoring( inventory );
        }});
        TextAdventurePresenter p =
            new TextAdventurePresenter( null, model, inventory, actionFactory );

        assertThat( p.defaultActions().get( 0 ), is( action ) );
    }

    @Test
    public void render_includes_location_actions_in_view_set_actions() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        Action action = mockery.mock( Action.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final List<Action> locationActions = new ArrayList<Action>();
        locationActions.add( action );
        mockery.checking( new Expectations() {{
            allowing( model ).currentLocation();
            will( returnValue( location ) );
            ignoring( model );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        final List<Action> actions = new ArrayList<Action>( p.defaultActions() );
        actions.add( action );
        mockery.checking( new Expectations() {{
            allowing( location ).actions();
            will( returnValue( locationActions ) );
            ignoring( location );
            oneOf( view ).setActions( actions );
            ignoring( view );
        }});

        p.render();
    }

    @Test
    public void upon_enact_action_presenter_triggers_the_action() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final Action action = mockery.mock( Action.class );
        mockery.checking( new Expectations() {{
            oneOf( action ).trigger();
            ignoring( action );
            ignoring( model );
            ignoring( view );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.enact( action );
    }

    @Test
    public void upon_enact_presenter_refreshes_view_with_exits() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final Action action = mockery.mock( Action.class );
        final Exit exit = mockery.mock( Exit.class );
        final List<Exit> exits = new ArrayList<Exit>();
        exits.add( exit );
        mockery.checking( new Expectations() {{
            ignoring( action );
            allowing( model ).currentLocationExits();
            will( returnValue( exits ) );
            ignoring( model );
            oneOf( view ).showLocationExits( exits );
            ignoring( view );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.enact( action );
    }

    @Test
    public void upon_enact_action_that_requires_further_action_pass_new_actions_to_view() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
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
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.enact( action );
    }

    @Test
    public void upon_enact_action_that_requires_no_further_action_tell_view_of_location_actions() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final Action action = mockery.mock( Action.class, "original action" );
        mockery.checking( new Expectations() {{
            ignoring( model );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        final List<Action> locationActions = new ArrayList<Action>( p.defaultActions() );
        mockery.checking( new Expectations() {{
            allowing( action ).trigger();
            allowing( action ).userMustChooseFollowUpAction();
            will( returnValue( false ) );
            ignoring( action );
            oneOf( view ).setActions( locationActions );
            ignoring( view );
        }});

        p.enact( action );
    }

    @Test
    public void if_text_is_available_for_display_after_enacting_an_action_tell_the_view() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
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
            oneOf( view ).showMainText( "location description\nsome user text\n" );
            ignoring( view );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.enact( action );
    }

    @Test
    public void text_output_from_multiple_actions_is_appended_to_main_text() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
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
            oneOf( view ).showMainText( "location description\nsome user text\n\nsome user text\n" );
            ignoring( view );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.enact( action );
        p.enact( action );
    }

    @Test
    public void if_text_is_available_for_display_after_enacting_an_action_remind_the_view_upon_render() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
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
            exactly( 2 ).of( view ).showMainText( "location description\nsome user text\n" );
            ignoring( view );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.enact( action );
        p.render();
    }

    @Test
    public void actions_are_updated_on_view_after_enacting_any_action() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final Action action = mockery.mock( Action.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final List<Action> locationActions = new ArrayList<Action>();
        locationActions.add( action );
        final List<Action> actions = new ArrayList<Action>();
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
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.clearDefaultActions();

        p.enact( action );
    }

    @Test
    public void score_is_retrieved_from_model_and_passed_to_view_on_enact() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final Action action = mockery.mock( Action.class );
        mockery.checking( new Expectations() {{
            oneOf( view ).currentScore( 42 );
            oneOf( view ).maximumScore( 137 );
            ignoring( view );
            oneOf( model ).currentScore();
            will( returnValue( 42 ) );
            oneOf( model ).maximumScore();
            will( returnValue( 137 ) );
            ignoring( model );
            ignoring( action );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.enact( action );
    }

    @Test
    public void ruby_count_is_retrieved_from_model_and_passed_to_view_on_render() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        mockery.checking( new Expectations() {{
            oneOf( view ).currentScore( 42 );
            oneOf( view ).maximumScore( 137 );
            ignoring( view );
            oneOf( model ).currentScore();
            will( returnValue( 42 ) );
            oneOf( model ).maximumScore();
            will( returnValue( 137 ) );
            ignoring( model );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.render();
    }

    @Test
    public void in_an_action_chain_if_follow_up_action_must_be_chosen() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final Action action = mockery.mock( Action.class, "original action" );
        final List<Action> actions = new ArrayList<Action>();
        actions.add( mockery.mock( Action.class, "follow up action" ) );
        mockery.checking( new Expectations() {{
            ignoring( view );
            ignoring( model );
            allowing( action ).userMustChooseFollowUpAction();
            will( returnValue( true ) );
            allowing( action ).followUpActions();
            will( returnValue( actions ) );
            ignoring( action );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.enact( action );
        assertTrue( p.inAnActionChain() );
    }

    @Test
    public void not_in_an_action_chain_if_no_follow_up_action_must_be_chosen() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final Action action = mockery.mock( Action.class, "original action" );
        mockery.checking( new Expectations() {{
            ignoring( view );
            ignoring( model );
            allowing( action ).userMustChooseFollowUpAction();
            will( returnValue( false ) );
            ignoring( action );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.enact( action );
        assertFalse( p.inAnActionChain() );
    }

    @Test
    public void not_in_an_action_chain_if_no_follow_up_actions_at_the_end_of_a_chain() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final Action action = mockery.mock( Action.class, "original action" );
        final List<Action> actions = new ArrayList<Action>();
        final Action followUpAction = mockery.mock( Action.class, "follow up action" );
        actions.add( followUpAction );
        mockery.checking( new Expectations() {{
            ignoring( view );
            ignoring( model );
            allowing( action ).userMustChooseFollowUpAction();
            will( returnValue( true ) );
            allowing( action ).followUpActions();
            will( returnValue( actions ) );
            ignoring( action );
            allowing( followUpAction ).userMustChooseFollowUpAction();
            will( returnValue( false ) );
            ignoring( followUpAction );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.enact( action );
        p.enact( followUpAction );

        assertFalse( p.inAnActionChain() );
    }

    @Test
    public void cancel_action_chain_causes_view_to_be_notified_of_default_and_location_actions() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        Action action = mockery.mock( Action.class );
        final ModelLocation location = mockery.mock( ModelLocation.class );
        final List<Action> locationActions = new ArrayList<Action>();
        locationActions.add( action );
        mockery.checking( new Expectations() {{
            allowing( model ).currentLocation();
            will( returnValue( location ) );
            ignoring( model );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        final List<Action> actions = new ArrayList<Action>( p.defaultActions() );
        actions.add( action );
        mockery.checking( new Expectations() {{
            allowing( location ).actions();
            will( returnValue( locationActions ) );
            ignoring( location );
            oneOf( view ).setActions( actions );
            ignoring( view );
        }});

        p.cancelActionChain();
    }

    @Test
    public void no_longer_in_an_action_chain_after_cancel_action_chain_whilst_in_an_action_chain() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final Action action = mockery.mock( Action.class, "original action" );
        final List<Action> actions = new ArrayList<Action>();
        actions.add( mockery.mock( Action.class, "follow up action" ) );
        mockery.checking( new Expectations() {{
            ignoring( view );
            ignoring( model );
            allowing( action ).userMustChooseFollowUpAction();
            will( returnValue( true ) );
            allowing( action ).followUpActions();
            will( returnValue( actions ) );
            ignoring( action );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.enact( action );

        p.cancelActionChain();
        assertFalse( p.inAnActionChain() );
    }

    // Don't know how to write this in JMock - Expectations need to be set before
    // constructing TextAdventurePresenter but the presenter is needed for the Expectations!
    // @Test
    // public void subscribes_for_events_from_model() {
    //     final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
    //     final TextAdventurePresenter p = new TextAdventurePresenter( null, model, null, null );

    //     mockery.checking( new Expectations() {{
    //         oneOf( model ).subscribeForEvents( p );
    //         ignoring( model );
    //     }});
    // }

    @Test
    public void on_current_location_changed_event_action_text_is_cleared() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
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
            oneOf( view ).showMainText( "location description\nsome user text\n" );
            oneOf( view ).showMainText( "location description" );
            ignoring( view );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.enact( action );
        p.currentLocationChanged();
    }

    @Test
    public void on_current_location_changed_updates_view_with_available_items_text() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        mockery.checking( new Expectations() {{
            allowing( model ).availableItemsText();
            will( returnValue( "some items text" ) );
            ignoring( model );
            oneOf( view ).showAvailableItemsText( "some items text" );
            ignoring( view );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.currentLocationChanged();
    }

    @Test
    public void on_current_location_changed_updates_view_with_location_description() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        mockery.checking( new Expectations() {{
            allowing( model ).currentLocationDescription();
            will( returnValue( "some room text" ) );
            ignoring( model );
            oneOf( view ).showMainText( "some room text" );
            ignoring( view );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.currentLocationChanged();
    }

    @Test
    public void on_current_location_changed_updates_view_with_location_exits() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
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
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.currentLocationChanged();
    }

    @Test
    public void on_current_location_changed_updates_view_with_location_area_name() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        mockery.checking( new Expectations() {{
            allowing( model ).currentLocationAreaName();
            will( returnValue( "area name" ) );
            ignoring( model );
            oneOf( view ).showAreaName( "area name" );
            ignoring( view );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, model, null, null );
        p.currentLocationChanged();
    }


    @Test
    public void with_view_updates_disabled_view_does_not_get_updates_on_location_change() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, null, null, null );

        p.disableViewUpdates();
        p.currentLocationChanged();
    }

    @Test
    public void with_view_updates_disabled_view_does_not_get_updates_on_render() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        TextAdventurePresenter p = new TextAdventurePresenter( view, null, null, null );

        p.disableViewUpdates();
        p.render();
    }

    @Test
    public void with_view_updates_disabled_view_does_not_get_updates_on_enact() {
        final TextAdventureView view = mockery.mock( TextAdventureView.class );
        final Action action = mockery.mock( Action.class );
        mockery.checking( new Expectations() {{
            ignoring( action );
        }});
        TextAdventurePresenter p = new TextAdventurePresenter( view, null, null, null );

        p.disableViewUpdates();
        p.enact( action );
    }
}

