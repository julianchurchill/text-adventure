package com.chewielouie.textadventure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.Robolectric;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import com.chewielouie.textadventure.item.Item;

@RunWith(RobolectricTestRunner.class)
public class ActivityAcceptanceTests {

    private final String action_history =
        "action name:take specific item:item id:clocktowerskeletonkey:location id:townentrance:\n" +
        "action name:exit:exit id:townentrancenorth:\n" +
        "action name:exit:exit id:mainstreettownnorth:\n" +
        "action name:use with specific item:item id:clocktowerskeletonkey:extra item id:lockeddoor:\n";

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

    // @Test
    // public void on_pause_writes_action_history_to_a_file() {
}
