package com.chewielouie.textadventure.action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
// import com.chewielouie.textadventure.item.Item;
// import com.chewielouie.textadventure.ActionHistory;
// import com.chewielouie.textadventure.Exit;
// import com.chewielouie.textadventure.ModelLocation;
// import com.chewielouie.textadventure.TextAdventureModel;
// import com.chewielouie.textadventure.UserInventory;
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
    public void actionFactory_delegates_to_wrapped_action() {
        ActionFactory actionFactory = mock( ActionFactory.class );
        Action wrappedAction = mock( Action.class );
        when( wrappedAction.actionFactory() ).thenReturn( actionFactory );

        assertThat(
            new RecordableAction( wrappedAction, null ).actionFactory(),
            is( actionFactory ) );
        verify( wrappedAction ).actionFactory();
    }
}
