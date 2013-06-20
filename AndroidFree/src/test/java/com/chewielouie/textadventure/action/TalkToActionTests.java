package com.chewielouie.textadventure.action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.TalkPhraseSource;
import org.junit.Test;

public class TalkToActionTests {

    @Test
    public void label_includes_item_name() {
        Item item = mock( Item.class );
        when( item.midSentenceCasedName() ).thenReturn( "Shopkeeper" );
        TalkToAction action = new TalkToAction( item, null );

        assertThat( action.label(), is( "Talk to Shopkeeper" ) );
    }

    @Test
    public void follow_up_actions_are_SayActions_created_with_action_factory() {
        TalkPhraseSource talkable = mock( TalkPhraseSource.class );
        when( talkable.initialPhraseIds() ).thenReturn( Arrays.asList( "id1" ) );
        Item item = mock( Item.class );
        when( item.getTalkPhraseSource() ).thenReturn( talkable );
        ActionFactory actionFactory = mock( ActionFactory.class );
        TalkToAction action = new TalkToAction( item, actionFactory );

        action.followUpActions();

        verify( actionFactory ).createSayAction( "id1", item );
    }

    @Test
    public void action_factory_is_saved() {
        ActionFactory actionFactory = mock( ActionFactory.class );
        TalkToAction action = new TalkToAction( null, actionFactory );

        assertThat( action.actionFactory(), is( actionFactory ) );
    }

    @Test
    public void user_must_choose_follow_up_action() {
        TalkToAction action = new TalkToAction( null, null );

        assertThat( action.userMustChooseFollowUpAction(), is( true ) );
    }

    @Test
    public void user_text_is_not_available() {
        TalkToAction action = new TalkToAction( null, null );

        assertThat( action.userTextAvailable(), is( false ) );
    }
}
