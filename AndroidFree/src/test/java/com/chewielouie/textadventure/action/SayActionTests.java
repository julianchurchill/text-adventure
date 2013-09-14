package com.chewielouie.textadventure.action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.TalkPhraseSource;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Test;

public class SayActionTests {

    @Test
    public void label_includes_short_phrase_from_phrase_source() {
        TalkPhraseSource phraseSource = mock( TalkPhraseSource.class );
        when( phraseSource.shortPhraseById( "id" ) ).thenReturn( "my short phrase..." );
        Item item = mock( Item.class );
        when( item.getTalkPhraseSource() ).thenReturn( phraseSource );
        SayAction action = new SayAction( "id", item, null );

        assertThat( action.label(), is( "Say \"my short phrase...\"" ) );
    }

    @Test
    public void user_text_includes_phrase_and_response_from_phrase_source() {
        TalkPhraseSource phraseSource = mock( TalkPhraseSource.class );
        when( phraseSource.phraseById( "id" ) ).thenReturn( "my long phrase" );
        when( phraseSource.responseToPhraseById( "id" ) ).thenReturn( "my response" );
        Item item = mock( Item.class );
        when( item.getTalkPhraseSource() ).thenReturn( phraseSource );
        SayAction action = new SayAction( "id", item, null );

        action.trigger();

        assertThat( action.userText(), is( "my long phrase\n\nmy response" ) );
    }

    @Test
    public void user_text_includes_only_phrase_if_response_not_available() {
        TalkPhraseSource phraseSource = mock( TalkPhraseSource.class );
        when( phraseSource.phraseById( "id" ) ).thenReturn( "my long phrase" );
        when( phraseSource.responseToPhraseById( "id" ) ).thenReturn( "" );
        Item item = mock( Item.class );
        when( item.getTalkPhraseSource() ).thenReturn( phraseSource );
        SayAction action = new SayAction( "id", item, null );

        action.trigger();

        assertThat( action.userText(), is( "my long phrase" ) );
    }

    @Test
    public void user_text_is_available() {
        SayAction action = new SayAction( "", null, null );
        assertThat( action.userTextAvailable(), is( true ) );
    }

    @Test
    public void follow_up_actions_include_a_say_action_for_each_follow_on_phrase() {
        TalkPhraseSource phraseSource = mock( TalkPhraseSource.class );
        when( phraseSource.followOnPhrasesIdsForPhraseById( "id" ) )
            .thenReturn( Arrays.asList( "follow on id 1", "follow on id 2" ) );
        Item item = mock( Item.class );
        when( item.getTalkPhraseSource() ).thenReturn( phraseSource );
        SayAction followOnSayAction1 = mock( SayAction.class );
        SayAction followOnSayAction2 = mock( SayAction.class );
        ActionFactory factory = mock( ActionFactory.class );
        when( factory.createSayAction( "follow on id 1", item ) )
            .thenReturn( followOnSayAction1 );
        when( factory.createSayAction( "follow on id 2", item ) )
            .thenReturn( followOnSayAction2 );
        SayAction action = new SayAction( "id", item, factory );

        assertThat( action.followUpActions(), hasItem( followOnSayAction1 ) );
        assertThat( action.followUpActions(), hasItem( followOnSayAction2 ) );
    }

    @Test
    public void user_must_choose_follow_up_action_if_responses_are_available() {
        TalkPhraseSource phraseSource = mock( TalkPhraseSource.class );
        when( phraseSource.followOnPhrasesIdsForPhraseById( "id" ) )
            .thenReturn( Arrays.asList( "follow on id 1" ) );
        Item item = mock( Item.class );
        when( item.getTalkPhraseSource() ).thenReturn( phraseSource );
        SayAction action = new SayAction( "id", item, null );

        assertThat( action.userMustChooseFollowUpAction(), is( true ) );
    }

    @Test
    public void user_must_not_choose_follow_up_action_if_responses_are_not_available() {
        TalkPhraseSource phraseSource = mock( TalkPhraseSource.class );
        when( phraseSource.followOnPhrasesIdsForPhraseById( "id" ) )
            .thenReturn( new ArrayList<String>() );
        Item item = mock( Item.class );
        when( item.getTalkPhraseSource() ).thenReturn( phraseSource );
        SayAction action = new SayAction( "id", item, null );

        assertThat( action.userMustChooseFollowUpAction(), is( false ) );
    }

    @Test
    public void action_factory_can_be_retrieved() {
        ActionFactory factory = mock( ActionFactory.class );
        SayAction action = new SayAction( "", null, factory );
        assertThat( action.actionFactory(), is( factory ) );
    }

    @Test
    public void item_actions_are_enacted_when_say_action_triggered() {
        TalkPhraseSource phraseSource = mock( TalkPhraseSource.class );
        Item item = mock( Item.class );
        when( item.getTalkPhraseSource() ).thenReturn( phraseSource );
        SayAction action = new SayAction( "id", item, null );

        action.trigger();

        verify( phraseSource ).executeActionsForPhraseById( "id" );
    }
}
