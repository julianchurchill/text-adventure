package com.chewielouie.textadventure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.Robolectric;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.StringBuffer;
import java.util.Map;
import android.content.Context;
import android.text.Spannable;
import android.widget.Button;
import android.widget.TextView;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.action.Action;

@RunWith(RobolectricTestRunner.class)
public class ActivityAcceptanceTests {

    private final String actionHistorySaveFileName = "action_history_save_file_1";
    private final String action_history =
        "action name:take specific item:item id:clocktowerskeletonkey:location id:townentrance:\n" +
        "action name:exit:exit id:townentrancenorth:\n" +
        "action name:exit:exit id:mainstreettownnorth:\n" +
        "action name:use with specific item:item id:clocktowerskeletonkey:extra item id:lockeddoor:\n";

    @Test
    public void on_resume_reads_and_replays_action_history() {
        RendersView renderer = mock( RendersView.class );
        BasicModel model = new BasicModel();
        BasicModelFactory modelFactory = mock( BasicModelFactory.class );
        when( modelFactory.createModel() ).thenReturn( model );
        TextAdventureActivity activity = new TextAdventureActivity( renderer,
            modelFactory );
        prepareActionHistorySaveFile( activity );

        activity.onResume();

        verify( renderer, atLeastOnce() ).render();
        ModelLocation townEntrance = model.findLocationByID( "townentrance" );
        Item skeletonKey = model.findItemByID( "clocktowerskeletonkey" );
        assertThat( townEntrance.items(), not( hasItem( skeletonKey ) ) );
        assertThat( model.inventoryItems(), hasItem( skeletonKey ) );
        ModelLocation clocktower = model.findLocationByID( "clocktower" );
        assertThat( model.currentLocation(), is( clocktower ) );
        Exit unlockedDoor = model.findExitByID( "clocktowerdoor" );
        assertThat( unlockedDoor.visible(), is( true ) );
    }

    private void prepareActionHistorySaveFile( Context context ) {
        try {
            FileOutputStream outputStream = context.openFileOutput(
                "action_history_save_file_1", Context.MODE_PRIVATE );
            outputStream.write( action_history.getBytes() );
            outputStream.close();
        } catch( FileNotFoundException e ) {
            e.printStackTrace();
        } catch( IOException e ) {
            e.printStackTrace();
        }
    }

    @Test
    public void on_pause_writes_action_history_to_a_file() {
        BasicModel model = new BasicModel();
        BasicModelFactory modelFactory = mock( BasicModelFactory.class );
        when( modelFactory.createModel() ).thenReturn( model );
        TextAdventureActivity activity = new TextAdventureActivity( modelFactory );
        activity.onCreate( null );
        activity.onResume();

        // do some actions...
        activity.onClick( getButton( "Take an item", activity ) );
        activity.onClick( getButton( "Take skeleton key", activity ) );
        activity.useExit( model.findExitByID( "townentrancenorth" ) );
        activity.useExit( model.findExitByID( "mainstreettownnorth" ) );
        activity.onClick( getButton( "Show inventory", activity ) );
        activity.onClick( getButton( "Skeleton key", activity ) );
        activity.onClick( getButton( "Use with", activity ) );
        activity.onClick( getButton( "locked door", activity ) );

        activity.onPause();

        String savedHistory = loadSerialisedActionHistory( activity );
        String expected_action_history =
            "action name:take an item:location id:townentrance:\n" +
            "action name:take specific item:item id:clocktowerskeletonkey:location id:townentrance:\n" +
            "action name:exit:exit id:townentrancenorth:\n" +
            "action name:exit:exit id:mainstreettownnorth:\n" +
            "action name:show inventory:\n" +
            "action name:inventory item:item id:clocktowerskeletonkey:location id:clocktower:\n" +
            "action name:use with:item id:clocktowerskeletonkey:location id:clocktower:\n" +
            "action name:use with specific item:item id:clocktowerskeletonkey:extra item id:lockeddoor:\n";
        assertThat( savedHistory, is( expected_action_history ) );
    }

    private Button getButton( String label, TextAdventureActivity activity ) {
        Button requestedButton = null;
        Map<Button,Action> actionButtonsMap = activity.actionButtons();
        for( Button button : actionButtonsMap.keySet() ) {
            if( button.getText().toString().equals( label ) ) {
                requestedButton = button;
                break;
            }
        }
        if( requestedButton == null ) {
            System.out.println( "requested button '" + label + "' not found!" );
            for( Button button : actionButtonsMap.keySet() )
               System.out.println( "available button with label '" + button.getText().toString() + "'" );
        }
        assertThat( requestedButton, is( notNullValue() ) );
        return requestedButton;
    }

    private String loadSerialisedActionHistory( Context context ) {
        StringBuffer serialisedHistory = new StringBuffer("");
        try {
            FileInputStream inputStream = context.openFileInput( actionHistorySaveFileName );
            int ch;
            while( (ch=inputStream.read()) != -1 )
                serialisedHistory.append( (char)ch );
        } catch( FileNotFoundException e ) {
            System.err.println("exception thrown: " + e.toString() );
        } catch( IOException e ) {
            System.err.println("exception thrown: " + e.toString() );
        }
        return serialisedHistory.toString();
    }
}
