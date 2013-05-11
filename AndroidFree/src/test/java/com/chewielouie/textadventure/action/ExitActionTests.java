package com.chewielouie.textadventure.action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.TextAdventureModel;

@RunWith(JMock.class)
public class ExitActionTests {

    private Mockery mockery = new Mockery();

    @Test
    public void name_is_correct() {
        assertThat( "exit", is( new ExitAction( null, null ).name() ) );
    }

    @Test
    public void user_text_is_empty() {
        ExitAction action = new ExitAction( null, null );
        action.trigger();
        assertThat( action.userText(), is( "" ) );
    }

    @Test
    public void user_text_available_is_false() {
        ExitAction action = new ExitAction( null, null );

        assertThat( action.userTextAvailable(), is( false ) );
    }

    @Test
    public void label_is_composed_from_exit_label() {
        final Exit exit = mockery.mock( Exit.class );
        ExitAction action = new ExitAction( exit, null );
        mockery.checking( new Expectations() {{
            allowing( exit ).label(); will( returnValue( "exit label" ) );
            ignoring( exit );
        }});

        assertThat( action.label(), is( "exit label" ) );
    }

    @Test
    public void user_does_not_have_to_choose_a_follow_up_action() {
        ExitAction action = new ExitAction( null, null );

        assertThat( action.userMustChooseFollowUpAction(), is( false ) );
    }

    @Test
    public void there_are_no_follow_up_actions() {
        ExitAction action = new ExitAction( null, null );

        assertThat( action.followUpActions().size(), is( 0 ) );
    }

    @Test
    public void on_trigger_model_is_called_with_exit() {
        final Exit exit = mockery.mock( Exit.class );
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        ExitAction action = new ExitAction( exit, model );
        mockery.checking( new Expectations() {{
            ignoring( exit );
            oneOf( model ).moveThroughExit( exit );
            ignoring( model );
        }});

        action.trigger();
    }
}
