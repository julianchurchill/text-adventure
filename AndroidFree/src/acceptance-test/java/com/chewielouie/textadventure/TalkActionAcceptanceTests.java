package com.chewielouie.textadventure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.TalkAction;
import com.chewielouie.textadventure.action.UserActionFactory;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.ItemFactory;
import com.chewielouie.textadventure.item.NormalItemFactory;
import com.chewielouie.textadventure.itemaction.ItemActionFactory;
import com.chewielouie.textadventure.itemaction.NormalItemActionFactory;
import com.chewielouie.textadventure.serialisation.ItemDeserialiser;
import com.chewielouie.textadventure.serialisation.PlainTextExitDeserialiser;
import com.chewielouie.textadventure.serialisation.PlainTextItemDeserialiser;
import com.chewielouie.textadventure.serialisation.PlainTextModelLocationDeserialiser;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class TalkActionAcceptanceTests {

    private BasicModel createModelWithContent( String content ) {
        BasicModel model = new BasicModel();
        UserInventory inventory = model;
        ItemActionFactory itemActionFactory = new NormalItemActionFactory( model );
        ItemFactory itemFactory = new NormalItemFactory();
        ItemDeserialiser itemDeserialiser = new PlainTextItemDeserialiser( itemActionFactory );
        new PlainTextModelPopulator( model,
                                     new LocationFactory( inventory, new UserActionFactory() ),
                                     inventory,
                                     itemFactory,
                                     new PlainTextModelLocationDeserialiser(
                                         itemFactory, new LocationExitFactory(),
                                         itemDeserialiser,
                                         new PlainTextExitDeserialiser() ),
                                     itemDeserialiser,
                                     content );
        return model;
    }

    @Ignore( "Awaiting feature completion" )
    @Test
    public void selecting_a_talk_phrase_causes_the_linked_response_to_be_included_in_the_main_text() {
        String content = "LOCATION\n" +
                        "location id:generalstore\n" +
                        "location description:description\n" +
                        "ITEM\n" +
                        "item name:item\n" +
                        "item description:description\n" +
                        "item id:itemid\n" +
                        "item countable noun prefix:a\n" +
                        "item talk initial phrase:hello:Hello!\n" +
                        "item talk response to:hello:Hello yourself!\n" +
                        "item talk initial phrase:lookingwell:You're looking well.\n" +
                        "item talk response to:lookingwell:Why thank you, it's all down to my new slimming regime.\n";

        BasicModel model = createModelWithContent( content );
        TextAdventureView view = mock( TextAdventureView.class );
        TextAdventurePresenter presenter = new TextAdventurePresenter(
            view, model, (UserInventory)model, null );

        presenter.render();
        ArgumentCaptor<List> actionsCaptor = ArgumentCaptor.forClass( List.class );
        verify( view ).setActions( actionsCaptor.capture() );
        TalkAction helloTalkAction = null;
        List<Action> actions = actionsCaptor.getValue();
        for( Action action : actions )
            if( action instanceof TalkAction )
                helloTalkAction = (TalkAction)action;
        assertThat( helloTalkAction, is( notNullValue() ) );

        UserActionHandler userActionHandler = presenter;
        userActionHandler.enact( helloTalkAction );

        ArgumentCaptor<String> mainText = ArgumentCaptor.forClass( String.class );
        verify( view ).showMainText( mainText.capture() );
        assertThat( mainText.getValue(), containsString( "Hello yourself!" ) );
    }

    @Ignore( "Awaiting feature completion" )
    @Test
    public void selecting_a_talk_phrase_causes_follow_up_phrases_to_be_delivered_as_talk_actions_to_the_view() {
        String content = "LOCATION\n" +
                        "location id:generalstore\n" +
                        "location description:description\n" +
                        "ITEM\n" +
                        "item name:item\n" +
                        "item description:description\n" +
                        "item id:itemid\n" +
                        "item countable noun prefix:a\n" +
                        "item talk initial phrase:hello:Hello!\n" +
                        "item talk response to:hello:Hello yourself!\n" +
                        "item talk follow up phrase to:hello:checkin:I just came to check in.\n";

        BasicModel model = createModelWithContent( content );
        TextAdventureView view = mock( TextAdventureView.class );
        TextAdventurePresenter presenter = new TextAdventurePresenter(
            view, model, (UserInventory)model, null );

        presenter.render();
        ArgumentCaptor<List> actionsCaptor = ArgumentCaptor.forClass( List.class );
        verify( view ).setActions( actionsCaptor.capture() );
        TalkAction helloTalkAction = null;
        List<Action> actions = actionsCaptor.getValue();
        for( Action action : actions )
            if( action instanceof TalkAction )
                helloTalkAction = (TalkAction)action;
        assertThat( helloTalkAction, is( notNullValue() ) );

        UserActionHandler userActionHandler = presenter;
        userActionHandler.enact( helloTalkAction );

        verify( view ).setActions( actionsCaptor.capture() );
        actions = actionsCaptor.getValue();
        assertThat( actions.size(), is( 1 ) );
        assertThat( actions.get( 0 ), is( instanceOf( TalkAction.class ) ) );
        assertThat( ((TalkAction)actions.get( 0 )).phraseText(), is( "I just came to check in." ) );
    }

    @Ignore( "Awaiting feature completion" )
    @Test
    public void selecting_a_talk_phrase_causes_follow_up_actions_to_be_enacted() {
        String content = "LOCATION\n" +
                        "location id:generalstore\n" +
                        "location description:description\n" +
                        "EXIT\n" +
                        "exit label:exit label\n" +
                        "exit destination:mainstreet\n" +
                        "exit direction hint:North\n" +
                        "exit is not visible:\n" +
                        "exit id:mainstreeteast\n" +
                        "ITEM\n" +
                        "item name:item\n" +
                        "item description:description\n" +
                        "item id:itemid\n" +
                        "item countable noun prefix:a\n" +
                        "item talk initial phrase:hello:Hello!\n" +
                        "item talk response to:hello:Hello yourself!\n" +
                        "item talk action in response to:lookingforwork:make exit visible:mainstreeteast\n";

        BasicModel model = createModelWithContent( content );
        TextAdventureView view = mock( TextAdventureView.class );
        TextAdventurePresenter presenter = new TextAdventurePresenter(
            view, model, (UserInventory)model, null );

        presenter.render();
        ArgumentCaptor<List> actionsCaptor = ArgumentCaptor.forClass( List.class );
        verify( view ).setActions( actionsCaptor.capture() );
        TalkAction helloTalkAction = null;
        List<Action> actions = actionsCaptor.getValue();
        for( Action action : actions )
            if( action instanceof TalkAction )
                helloTalkAction = (TalkAction)action;
        assertThat( helloTalkAction, is( notNullValue() ) );

        Exit exit = model.findExitByID( "mainstreeteast" );
        assertThat( exit, is( notNullValue() ) );
        assertThat( exit.visible(), is( false ) );
        UserActionHandler userActionHandler = presenter;
        userActionHandler.enact( helloTalkAction );

        assertThat( exit.visible(), is( true ) );
    }
}
