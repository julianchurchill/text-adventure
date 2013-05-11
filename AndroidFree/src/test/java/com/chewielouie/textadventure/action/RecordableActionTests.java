package com.chewielouie.textadventure.action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

public class RecordableActionTests {

    @Test
    public void label_delegates_to_wrapped_action() {
        Action wrappedAction = mock( Action.class );
        when( wrappedAction.label() ).thenReturn( "label" );

        assertThat( new RecordableAction( wrappedAction, null ).label(), is( "label" ) );
        verify( wrappedAction ).label();
    }

    @Test
    public void trigger_delegates_to_wrapped_action() {
        Action wrappedAction = mock( Action.class );

        new RecordableAction( wrappedAction, null ).trigger();

        verify( wrappedAction ).trigger();
    }

    @Test
    public void userMustChooseFollowUpAction_delegates_to_wrapped_action() {
        Action wrappedAction = mock( Action.class );
        when( wrappedAction.userMustChooseFollowUpAction() ).thenReturn( true );

        assertThat(
            new RecordableAction( wrappedAction, null ).userMustChooseFollowUpAction(),
            is( true ) );
        verify( wrappedAction ).userMustChooseFollowUpAction();
    }

    @Test
    public void followUpActions_delegates_to_wrapped_action() {
        List actions = mock( List.class );
        Action wrappedAction = mock( Action.class );
        when( wrappedAction.followUpActions() ).thenReturn( actions );

        assertThat(
            new RecordableAction( wrappedAction, null ).followUpActions(),
            is( actions ) );
        verify( wrappedAction ).followUpActions();
    }

    @Test
    public void userTextAvailable_delegates_to_wrapped_action() {
        Action wrappedAction = mock( Action.class );
        when( wrappedAction.userTextAvailable() ).thenReturn( true );

        assertThat(
            new RecordableAction( wrappedAction, null ).userTextAvailable(),
            is( true ) );
        verify( wrappedAction ).userTextAvailable();
    }

    @Test
    public void userText_delegates_to_wrapped_action() {
        Action wrappedAction = mock( Action.class );
        when( wrappedAction.userText() ).thenReturn( "user text" );

        assertThat(
            new RecordableAction( wrappedAction, null ).userText(),
            is( "user text" ) );
        verify( wrappedAction ).userText();
    }

    @Test
    public void name_delegates_to_wrapped_action() {
        Action wrappedAction = mock( Action.class );
        when( wrappedAction.name() ).thenReturn( "name" );

        assertThat( new RecordableAction( wrappedAction, null ).name(), is( "name" ) );
        verify( wrappedAction ).name();
    }

    @Test
    public void actionFactory_delegates_to_wrapped_action() {
        ActionFactory actionFactory = mock( ActionFactory.class );
        Action wrappedAction = mock( Action.class );
        when( wrappedAction.actionFactory() ).thenReturn( actionFactory );

        assertThat(
            new RecordableAction( wrappedAction, null ).actionFactory(),
            is( actionFactory ) );
        verify( wrappedAction ).actionFactory();
    }

    @Test
    public void on_trigger_records_action_with_parameters_in_history() {
        ActionHistory actionHistory = mock( ActionHistory.class );
        Action wrappedAction = mock( Action.class );

        RecordableAction a = new RecordableAction( wrappedAction, actionHistory );
        ActionParameters params = mock( ActionParameters.class );
        a.setActionParameters( params );
        a.trigger();

        verify( actionHistory ).addActionWithParameters( wrappedAction, params );
    }
}
