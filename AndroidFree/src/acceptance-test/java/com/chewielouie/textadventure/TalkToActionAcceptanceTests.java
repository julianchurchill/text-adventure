package com.chewielouie.textadventure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.TalkToAction;
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

public class TalkToActionAcceptanceTests {

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
                new PlainTextExitDeserialiser( itemActionFactory ) ),
            itemDeserialiser,
            content );
        return model;
    }

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
                        "item talk initial phrase:hello:short phrase:Hello!\n" +
                        "item talk response to:hello:Hello yourself!\n" +
                        "item talk initial phrase:lookingwell:short phrase:You're looking well.\n" +
                        "item talk response to:lookingwell:Why thank you, it's all down to my new slimming regime.\n";

        BasicModel model = createModelWithContent( content );
        TextAdventureView view = mock( TextAdventureView.class );
        TextAdventurePresenter presenter = new TextAdventurePresenter(
            view, model, (UserInventory)model, null );

        presenter.render();
        ArgumentCaptor<List> actionsCaptor = ArgumentCaptor.forClass( List.class );
        verify( view ).setActions( actionsCaptor.capture() );
        TalkToAction helloTalkToAction = null;
        List<Action> actions = actionsCaptor.getValue();
        for( Action action : actions )
            if( action instanceof TalkToAction )
                helloTalkToAction = (TalkToAction)action;
        assertThat( helloTalkToAction, is( notNullValue() ) );

        UserActionHandler userActionHandler = presenter;
        userActionHandler.enact( helloTalkToAction );
        verify( view ).setActions( helloTalkToAction.followUpActions() );

        Action sayAction = helloTalkToAction.followUpActions().get(0);
        userActionHandler.enact( sayAction );

        ArgumentCaptor<String> mainText = ArgumentCaptor.forClass( String.class );
        verify( view, atLeastOnce() ).showMainText( mainText.capture() );
        assertThat( mainText.getValue(), containsString( "Hello yourself!" ) );
    }

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
                        "item talk initial phrase:hello:short phrase:Hello!\n" +
                        "item talk response to:hello:Hello yourself!\n" +
                        "item talk follow up phrase to:hello:checkin:I just came to check in...:I just came to check in. Glad to see you're well.\n";

        BasicModel model = createModelWithContent( content );
        TextAdventureView view = mock( TextAdventureView.class );
        TextAdventurePresenter presenter = new TextAdventurePresenter(
            view, model, (UserInventory)model, null );

        presenter.render();
        ArgumentCaptor<List> actionsCaptor = ArgumentCaptor.forClass( List.class );
        verify( view ).setActions( actionsCaptor.capture() );
        TalkToAction helloTalkToAction = null;
        List<Action> actions = actionsCaptor.getValue();
        for( Action action : actions )
            if( action instanceof TalkToAction )
                helloTalkToAction = (TalkToAction)action;
        assertThat( helloTalkToAction, is( notNullValue() ) );

        UserActionHandler userActionHandler = presenter;
        userActionHandler.enact( helloTalkToAction );
        verify( view ).setActions( helloTalkToAction.followUpActions() );

        Action sayAction = helloTalkToAction.followUpActions().get(0);
        userActionHandler.enact( sayAction );

        verify( view, atLeastOnce() ).setActions( actionsCaptor.capture() );
        actions = actionsCaptor.getValue();
        assertThat( actions.size(), is( 1 ) );
        assertThat( actions.get( 0 ).label(), is( "Say \"I just came to check in...\"" ) );
        userActionHandler.enact( actions.get( 0 ) );

        ArgumentCaptor<String> mainText = ArgumentCaptor.forClass( String.class );
        verify( view, atLeastOnce() ).showMainText( mainText.capture() );
        assertThat( mainText.getValue(), containsString( "I just came to check in. Glad to see you're well." ) );
    }

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
                        "item talk initial phrase:hello:short phrase:Hello!\n" +
                        "item talk response to:hello:Hello yourself!\n" +
                        "item talk action in response to:hello:make exit visible:mainstreeteast\n";

        BasicModel model = createModelWithContent( content );
        TextAdventureView view = mock( TextAdventureView.class );
        TextAdventurePresenter presenter = new TextAdventurePresenter(
            view, model, (UserInventory)model, null );

        presenter.render();
        ArgumentCaptor<List> actionsCaptor = ArgumentCaptor.forClass( List.class );
        verify( view ).setActions( actionsCaptor.capture() );
        TalkToAction helloTalkToAction = null;
        List<Action> actions = actionsCaptor.getValue();
        for( Action action : actions )
            if( action instanceof TalkToAction )
                helloTalkToAction = (TalkToAction)action;
        assertThat( helloTalkToAction, is( notNullValue() ) );

        UserActionHandler userActionHandler = presenter;
        userActionHandler.enact( helloTalkToAction );
        verify( view ).setActions( helloTalkToAction.followUpActions() );

        Exit exit = model.findExitByID( "mainstreeteast" );
        assertThat( exit, is( notNullValue() ) );
        assertThat( exit.visible(), is( false ) );
        Action sayAction = helloTalkToAction.followUpActions().get(0);
        userActionHandler.enact( sayAction );

        assertThat( exit.visible(), is( true ) );
    }
}
