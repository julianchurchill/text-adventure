package REPLACE_ME;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.junit.Ignore;
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
import com.chewielouie.textadventure.BasicModel;
import com.chewielouie.textadventure.BasicModelFactory;
import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.RendersView;

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
        TextAdventureDummyActivity activity = new TextAdventureDummyActivity( renderer,
            modelFactory );
        prepareActionHistorySaveFile( activity );
        activity.setModelContent( modelContent );

        activity.onCreate( null );
        activity.onResume();

        ModelLocation townEntrance = model.findLocationByID( "townentrance" );
        Item skeletonKey = model.findItemByID( "clocktowerskeletonkey" );
        assertThat( townEntrance.items(), not( hasItem( skeletonKey ) ) );
        assertThat( model.inventoryItems(), hasItem( skeletonKey ) );
        Exit unlockedDoor = model.findExitByID( "clocktowerdoor" );
        assertThat( unlockedDoor.visible(), is( true ) );
        ModelLocation clocktower = model.findLocationByID( "clocktower" );
        assertThat( model.currentLocation(), is( clocktower ) );
    }

    private void prepareActionHistorySaveFile( Context context ) {
        writeFile( "action_history_save_file_1", action_history, context );
    }

    private void writeFile( String filename, String content, Context context ) {
        try {
            FileOutputStream outputStream = context.openFileOutput(
                filename, Context.MODE_PRIVATE );
            outputStream.write( content.getBytes() );
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
        TextAdventureDummyActivity activity = new TextAdventureDummyActivity( modelFactory );
        activity.setModelContent( modelContent );
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

    private Button getButton( String label, TextAdventureDummyActivity activity ) {
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
        return loadFile( context, actionHistorySaveFileName );
    }

    private String loadFile( Context context, String filename ) {
        StringBuffer content = new StringBuffer("");
        try {
            FileInputStream inputStream = context.openFileInput( filename );
            int ch;
            while( (ch=inputStream.read()) != -1 )
                content.append( (char)ch );
        } catch( FileNotFoundException e ) {
            System.err.println("exception thrown: " + e.toString() );
        } catch( IOException e ) {
            System.err.println("exception thrown: " + e.toString() );
        }
        return content.toString();
    }

    @Test
    public void on_resume_reads_a_json_file_and_saves_as_an_action_history_file__unlock_door() {
        TextAdventureDummyActivity activity = new TextAdventureDummyActivity();
        activity.deleteFile( actionHistorySaveFileName );
        prepareJSONSaveFile( activity, json_model_unlock_door );
        activity.setModelContent( modelContent );
        activity.onCreate( null );

        activity.onResume();
        activity.onPause();

        String savedHistory = loadSerialisedActionHistory( activity );
        String expected_action_history =
            "action name:take specific item:item id:clocktowerskeletonkey:location id:townentrance:\n" +
            "action name:use with specific item:item id:clocktowerskeletonkey:extra item id:lockeddoor:\n";
        assertThat( savedHistory, is( expected_action_history ) );
    }

    @Test
    public void on_resume_reads_a_json_file_and_replays_actions__unlock_door() {
        RendersView renderer = mock( RendersView.class );
        BasicModel model = new BasicModel();
        BasicModelFactory modelFactory = mock( BasicModelFactory.class );
        when( modelFactory.createModel() ).thenReturn( model );
        TextAdventureDummyActivity activity = new TextAdventureDummyActivity( renderer,
            modelFactory );
        activity.setModelContent( modelContent );
        activity.deleteFile( actionHistorySaveFileName );
        prepareJSONSaveFile( activity, json_model_unlock_door );
        activity.onCreate( null );

        activity.onResume();

        ModelLocation townEntrance = model.findLocationByID( "townentrance" );
        Item skeletonKey = model.findItemByID( "clocktowerskeletonkey" );
        assertThat( townEntrance.items(), not( hasItem( skeletonKey ) ) );
        assertThat( model.inventoryItems(), hasItem( skeletonKey ) );
        Exit unlockedDoor = model.findExitByID( "clocktowerdoor" );
        assertThat( unlockedDoor.visible(), is( true ) );
        assertThat( model.currentLocation(), is( townEntrance ) );
    }

    private void prepareJSONSaveFile( Context context, String content ) {
        writeFile( "save_file_1", content, context );
     }

     // Generated by V1.0 - actions { take skeleton key, north, north, show inventory, use skeleton key with locked door,
     //                               examine an item, examine locked door }
     private final String json_model_unlock_door = "{\"@id\":1,\"@type\":\"com.chewielouie.textadventure.BasicModel\",\"currentLocation\":{\"@id\":32,\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You stand before a mighty clock tower. The clock goes TICK!\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"mainstreettown\",\"directionHint\":{\"@id\":76,\"name\":\"South\",\"ordinal\":2},\"id\":\"\",\"label\":\"South\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"groundfloorclocktower\",\"directionHint\":{\"@id\":69,\"name\":\"North\",\"ordinal\":1},\"id\":\"clocktowerdoor\",\"label\":\"Clock tower door\",\"visible\":true}]},\"id\":{\"@id\":31,\"value\":\"clocktower\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@id\":12,\"value\":\"\"},\"countableNounPrefix\":\"a\",\"description\":\"It is quite heavy, ornate and made of iron.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"clockminutehand\",\"midSentenceCasedName\":\"clock minute hand\",\"name\":\"Clock minute hand\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@id\":35,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"clocktowerskeletonkey\",\"countableNounPrefix\":\"a\",\"description\":{\"@id\":52,\"value\":\"It is unlocked. It appears to allow access to the clock tower itself.\"},\"usedWithText\":\"You hear a click and the door springs open.\",\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemDescriptionItemAction\",\"description\":{\"@ref\":52},\"item\":{\"@ref\":35}},\"content\":\"change item description:It is unlocked. It appears to allow access to the clock tower itself.\",\"item\":{\"@ref\":35},\"logger\":{\"@id\":42,\"@type\":\"com.chewielouie.textadventure.StdoutLogger\"}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemNameItemAction\",\"item\":{\"@ref\":35},\"name\":{\"@id\":37,\"value\":\"unlocked door\"}},\"content\":\"change item name:unlocked door\",\"item\":{\"@ref\":35},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.MakeExitVisibleItemAction\",\"exitID\":\"clocktowerdoor\",\"model\":{\"@ref\":1}},\"content\":\"make exit visible:clocktowerdoor\",\"item\":{\"@ref\":35},\"logger\":{\"@ref\":42}}]},\"id\":\"\",\"midSentenceCasedName\":null,\"name\":{\"@ref\":37},\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":true,\"takeable\":false,\"useIsRepeatable\":false,\"used\":true,\"examineActionIsRepeatable\":true,\"visible\":true}]},\"x\":300,\"y\":240},\"locations\":{\"@id\":28,\"@type\":\"java.util.HashMap\",\"@keys\":[\"smallminechamber\",\"darktunnel2\",\"darktunnel1\",\"evensmallerannex\",\"dimlylitannex\",\"townentrance\",\"townoutbuildings\",\"outsideamineshaft\",\"smallshed\",\"candlelitchamber\",\"mainstreettown\",\"secondfloorclocktower\",\"mineshaftchamber1\",\"firstfloorclocktower\",\"groundfloorclocktower\",\"minesmithy\",\"clocktower\"],\"@items\":[{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You enter the chamber and although it is lit by a candle burning gently in an alcove you strain your eyes to peer into the shadows. Out shuffles a man, bedraggled and somber, head down muttering to himself. You notice he has a manacle around his ankle, chained to a nearby rock. Approaching him you say softly 'Hello? Who are you? What's happened here?'. The man replies 'I was taken, I don't know who it was. They hit me from behind and I woke up here, like this.' gesturing to the chain at his feet. 'Help me, please.' splutters the man as he gasps for breath. He is clearly suffering from his circumstances and you feel inclined to find a way to help him out of his predicament.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"darktunnel2\",\"directionHint\":{\"@ref\":69},\"id\":{\"@ref\":12},\"label\":\"North\",\"visible\":true}]},\"id\":{\"@id\":567,\"value\":\"smallminechamber\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"multitool\",\"countableNounPrefix\":\"an\",\"description\":\"He is dirty and looks exhausted. Chained to a nearby rock you see no easy way to free him.\",\"usedWithText\":\"The man cowers away from you and you feel foolish for threatening the poor man with your impressive multitool.\",\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"imprisonedman\",\"midSentenceCasedName\":null,\"name\":\"Imprisoned man\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@id\":588,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"pickaxe\",\"countableNounPrefix\":\"a\",\"description\":\"It looks to be softer than the surrounding walls, particularly where the chain link has been pinned to it. You wonder if there might be a way to lever or pick the pin out from the rock.\",\"usedWithText\":\"You swing the pick axe high above your head and bring it hard down upon the rock, pin and chain. The links shatter into pieces and scatter across the floor, freeing the man who jumps to his feet with joy. 'Oh thank you good sir - I can return to my shop at last, if there's anything left to return to.' he says.\",\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemDescriptionItemAction\",\"description\":\"It looks to be softer than the surrounding walls. There is a hole where a chain pin used to be embedded in the rock.\",\"item\":{\"@ref\":588}},\"content\":\"change item description:It looks to be softer than the surrounding walls. There is a hole where a chain pin used to be embedded in the rock.\",\"item\":{\"@ref\":588},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeLocationDescriptionItemAction\",\"locationID\":\"smallminechamber\",\"model\":{\"@ref\":1},\"newDescription\":\"You enter the chamber and although it is lit by a candle burning gently in an alcove you strain your eyes to peer into the shadows. This room looks lived in, of a sort. You notice shattered chain links on the floor.\"},\"content\":\"change location description:smallminechamber:You enter the chamber and although it is lit by a candle burning gently in an alcove you strain your eyes to peer into the shadows. This room looks lived in, of a sort. You notice shattered chain links on the floor.\",\"item\":{\"@ref\":588},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"imprisonedman\",\"model\":{\"@ref\":1},\"visibility\":false},\"content\":\"change item visibility:imprisonedman:invisible\",\"item\":{\"@ref\":588},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"shopkeeper\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:shopkeeper:visible\",\"item\":{\"@ref\":588},\"logger\":{\"@ref\":42}}]},\"id\":\"rock\",\"midSentenceCasedName\":null,\"name\":\"Rock\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":false,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@id\":571,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"minemap\",\"countableNounPrefix\":\"a\",\"description\":\"He is dirty and looks exhausted but has the light of hope in his eyes now that he is free. He also looks a little lost.\",\"usedWithText\":\"You hand the map to the shop keeper who smiles and nods at you with gratitude. 'Thank you, very kind. For your help have a ruby and come by my shop in town anytime - I'm sure I can arrange a favourable discount on anything that takes your eye.'. With that the man walks out leaving you alone. The air stirs around you and you feel slightly wealthier.\",\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"shopkeeper\",\"model\":{\"@ref\":1},\"visibility\":false},\"content\":\"change item visibility:shopkeeper:invisible\",\"item\":{\"@ref\":571},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.IncrementScoreItemAction\",\"model\":{\"@ref\":1}},\"content\":\"increment score:\",\"item\":{\"@ref\":571},\"logger\":{\"@ref\":42}}]},\"id\":\"shopkeeper\",\"midSentenceCasedName\":null,\"name\":\"Shop keeper\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":false,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":false}]},\"x\":420,\"y\":720},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"The footprints continue further south where you can see a dim light burning. The air down here is dank and you feel a little claustrophobic. Off to the east is a candle lit chamber.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"darktunnel1\",\"directionHint\":{\"@ref\":69},\"id\":{\"@ref\":12},\"label\":\"North\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"smallminechamber\",\"directionHint\":{\"@ref\":76},\"id\":{\"@ref\":12},\"label\":\"South\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"candlelitchamber\",\"directionHint\":{\"@id\":379,\"name\":\"East\",\"ordinal\":3},\"id\":{\"@ref\":12},\"label\":\"Candle lit chamber\",\"visible\":true}]},\"id\":{\"@id\":551,\"value\":\"darktunnel2\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":420,\"y\":660},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are in a dark tunnel with a trail of scuffed footprints leading deeper into the mine. You think you can make out two sets of prints in the dirt. The mine entrance lies to the north and the tunnel continues south.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"mineshaftchamber1\",\"directionHint\":{\"@ref\":69},\"id\":{\"@ref\":12},\"label\":\"North\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"darktunnel2\",\"directionHint\":{\"@ref\":76},\"id\":{\"@ref\":12},\"label\":\"South\",\"visible\":true}]},\"id\":{\"@id\":538,\"value\":\"darktunnel1\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":420,\"y\":600},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"This is an even smaller annex attached to a larger better lit one. There is straw on the floor and it looks like someone has been sleeping here.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"dimlylitannex\",\"directionHint\":{\"@ref\":69},\"id\":{\"@ref\":12},\"label\":\"Dimly lit annex\",\"visible\":true}]},\"id\":{\"@id\":507,\"value\":\"evensmallerannex\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@id\":518,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"a\",\"description\":\"It still has the indentation of a sleeping body although the body does not appear to still be here.\",\"usedWithText\":{\"@ref\":12},\"examineText\":\"You delve deeply into the pile of straw and unearth a variety of delightful things, mostly ticks and beetles, as well as some unspeakable left overs. At the very bottom of the pile you find a wooden pole. You wish the owner was around so you could beat them with it for having left their bedroom in such a disgraceful state.\",\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"pileofstraw\",\"midSentenceCasedName\":null,\"name\":\"Pile of straw\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"woodenpole\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:woodenpole:visible\",\"item\":{\"@ref\":518},\"logger\":{\"@ref\":42}}]},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":false,\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"a\",\"description\":\"It looks like it was attached to something at one end. Perhaps it was a tool of some sort.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"woodenpole\",\"midSentenceCasedName\":null,\"name\":\"Wooden pole\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":false}]},\"x\":480,\"y\":660},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"This annex is carved out of the rock face with several indentations holding a variety of items including bones, a burnt out candle, some old torn paper and a metal ring.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"mineshaftchamber1\",\"directionHint\":{\"@ref\":379},\"id\":{\"@ref\":12},\"label\":\"East\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"evensmallerannex\",\"directionHint\":{\"@ref\":76},\"id\":{\"@ref\":12},\"label\":\"Even smaller annex\",\"visible\":true}]},\"id\":{\"@id\":454,\"value\":\"dimlylitannex\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@id\":465,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"woodenpole\",\"countableNounPrefix\":\"a\",\"description\":\"This is a sizeable chunk of metal, pointy at two ends with a notch in the center. It looks like it could be fitted onto something but it is wedged in a crack in the wall.\",\"usedWithText\":\"The wooden pole slots perfectly into the axe head allowing you to apply your weight to the end of the pole causing the axe to pop out of the crack in the wall like a lollipop from a babies mouth. It is quite a hefty mining implement and is well weighted. You feel you could move a substantial amount of rock with this if it were but a touch sharper.\",\"examineText\":\"Upon further consideration you see it is shaped like an axe head. No wait, it is an axe head.\",\"onUseActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"bluntpickaxe\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:bluntpickaxe:visible\",\"item\":{\"@ref\":465},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.DestroyItemItemAction\",\"itemID\":\"axehead\",\"model\":{\"@ref\":1}},\"content\":\"destroy item:axehead\",\"item\":{\"@ref\":465},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.DestroyItemItemAction\",\"itemID\":\"woodenpole\",\"model\":{\"@ref\":1}},\"content\":\"destroy item:woodenpole\",\"item\":{\"@ref\":465},\"logger\":{\"@ref\":42}}]},\"id\":\"axehead\",\"midSentenceCasedName\":null,\"name\":\"Chunk of metal\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemDescriptionItemAction\",\"description\":\"This is quite clearly an axe head. You are however missing a means to wield it and it is wedged in a crack in the wall.\",\"item\":{\"@ref\":465}},\"content\":\"change item description:This is quite clearly an axe head. You are however missing a means to wield it and it is wedged in a crack in the wall.\",\"item\":{\"@ref\":465},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemNameItemAction\",\"item\":{\"@ref\":465},\"name\":\"Axe head\"},\"content\":\"change item name:Axe head\",\"item\":{\"@ref\":465},\"logger\":{\"@ref\":42}}]},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":false,\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"a\",\"description\":\"It looks rather blunt. You can probably bludgeon some small rocks into shards with it given a few hours and some angry words but not much else.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"bluntpickaxe\",\"midSentenceCasedName\":null,\"name\":\"Blunt pick axe\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":false}]},\"x\":480,\"y\":600},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are standing outside a small town. The main street goes north, to the west there are some small out buildings and to the east you can see what looks like an abandoned mine shaft.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"mainstreettown\",\"directionHint\":{\"@ref\":69},\"id\":\"\",\"label\":\"North\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"outsideamineshaft\",\"directionHint\":{\"@ref\":379},\"id\":\"\",\"label\":\"East\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"townoutbuildings\",\"directionHint\":{\"@id\":151,\"name\":\"West\",\"ordinal\":4},\"id\":\"\",\"label\":\"West\",\"visible\":true}]},\"id\":{\"@id\":427,\"value\":\"townentrance\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"some\",\"description\":\"It is yellow and a little moldy.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"\",\"midSentenceCasedName\":\"banana peel\",\"name\":\"Banana peel\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true}]},\"x\":300,\"y\":480},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"There are a number of small shack like structures here, fallen down and decrepit they haven't been used for years.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"townentrance\",\"directionHint\":{\"@ref\":379},\"id\":\"\",\"label\":\"East\",\"visible\":true}]},\"id\":{\"@id\":388,\"value\":\"townoutbuildings\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@id\":400,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"spade\",\"countableNounPrefix\":\"a\",\"description\":\"It has recently been disturbed.\",\"usedWithText\":\"You dig deep into the dirt, sweat dripping from your brow in the dry heat. Clank! The spade strikes something metallic. You brush the earth away to reveal an old clock face, without a minute or an hour hand. It looks sad.\",\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.DestroyItemItemAction\",\"itemID\":\"moundofearth\",\"model\":{\"@ref\":1}},\"content\":\"destroy item:moundofearth\",\"item\":{\"@ref\":400},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"clockface\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:clockface:visible\",\"item\":{\"@ref\":400},\"logger\":{\"@ref\":42}}]},\"id\":\"moundofearth\",\"midSentenceCasedName\":null,\"name\":\"mound of earth\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"a\",\"description\":\"It is a disembodied clock face, without hands.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"clockface\",\"midSentenceCasedName\":\"clock face\",\"name\":\"Clock face\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":false}]},\"x\":180,\"y\":480},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You stand before an old blocked up mine shaft, there are some dusty footprints in the dirt around the entrance however there is no obvious way in. A sign states \\\"McCreedys Mine - Stay out or suffer the consequences!\\\". To the east is an old ramshackle shed.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"townentrance\",\"directionHint\":{\"@ref\":151},\"id\":{\"@ref\":12},\"label\":\"West\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"smallshed\",\"directionHint\":{\"@ref\":379},\"id\":{\"@ref\":12},\"label\":\"Ramshackle Shed\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"mineshaftchamber1\",\"directionHint\":{\"@ref\":76},\"id\":\"mineshaftentrance\",\"label\":\"Mineshaft entrance\",\"visible\":false}]},\"id\":{\"@id\":368,\"value\":\"outsideamineshaft\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":420,\"y\":480},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are standing inside a small shed. It is dusty and dirty and a variety of mining tools rest against the walls.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"outsideamineshaft\",\"directionHint\":{\"@ref\":151},\"id\":{\"@ref\":12},\"label\":\"West\",\"visible\":true}]},\"id\":{\"@id\":325,\"value\":\"smallshed\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"a\",\"description\":\"It is a wooden handled spade. It looks like it has seen quite a lot of use and might not be good for much.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"spade\",\"midSentenceCasedName\":null,\"name\":\"Spade\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@id\":337,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"some\",\"description\":\"They are full of rubbish, there might be something of interest here but it is hard to tell.\",\"usedWithText\":{\"@ref\":12},\"examineText\":\"You look closer and see something glinting in the darkness. You scramble around in the bag trying to reach it, grasping with your finger tips pulling it out and dropping it at your feet.\",\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":{\"@ref\":12},\"midSentenceCasedName\":\"bags of junk\",\"name\":\"Bags of junk\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemDescriptionItemAction\",\"description\":\"They are full of rubbish. Nothing else.\",\"item\":{\"@ref\":337}},\"content\":\"change item description:They are full of rubbish. Nothing else.\",\"item\":{\"@ref\":337},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"clockhourhand\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:clockhourhand:visible\",\"item\":{\"@ref\":337},\"logger\":{\"@ref\":42}}]},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":false,\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"a\",\"description\":\"It is the hour hand from a clock.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"clockhourhand\",\"midSentenceCasedName\":\"clock hour hand\",\"name\":\"Clock hour hand\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":false}]},\"x\":480,\"y\":480},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You stand in a circular chamber with candles lining the walls in metal brakets. There is a table in the centre of the room and an exit leading deeper into the mine to what looks like a smithy. To the west a tunnel meanders back through the darkness.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"darktunnel2\",\"directionHint\":{\"@ref\":151},\"id\":{\"@ref\":12},\"label\":\"Dark tunnel\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"minesmithy\",\"directionHint\":{\"@ref\":76},\"id\":{\"@ref\":12},\"label\":\"Smithy\",\"visible\":true}]},\"id\":{\"@id\":287,\"value\":\"candlelitchamber\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@id\":298,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"a\",\"description\":\"It is covered with papers.\",\"usedWithText\":{\"@ref\":12},\"examineText\":\"You're not sure how anyone found anything in this mess but a particular sheet of paper catches your eye. As you reach to pick it up it falls to the ground.\",\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"table\",\"midSentenceCasedName\":null,\"name\":\"Table\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"minemap\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:minemap:visible\",\"item\":{\"@ref\":298},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemDescriptionItemAction\",\"description\":\"It is covered with papers. Boring, uninteresting papers.\",\"item\":{\"@ref\":298}},\"content\":\"change item description:It is covered with papers. Boring, uninteresting papers.\",\"item\":{\"@ref\":298},\"logger\":{\"@ref\":42}}]},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":false,\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"a\",\"description\":\"This is a map of the mine tunnels - useful in case you get lost.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"minemap\",\"midSentenceCasedName\":null,\"name\":\"Map\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":false}]},\"x\":480,\"y\":660},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are on the main street of the town. There is a clock tower to the north.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"clocktower\",\"directionHint\":{\"@ref\":69},\"id\":\"\",\"label\":\"North\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"townentrance\",\"directionHint\":{\"@ref\":76},\"id\":\"\",\"label\":\"South\",\"visible\":true}]},\"id\":{\"@id\":263,\"value\":\"mainstreettown\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"some\",\"description\":\"This dust looks pretty powerful, for dust.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"\",\"midSentenceCasedName\":\"Dust of the Ancients\",\"name\":\"Dust of the Ancients\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true}]},\"x\":300,\"y\":360},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are at the top of the clock tower standing in front of the clock mechanism.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"firstfloorclocktower\",\"directionHint\":{\"@ref\":76},\"id\":{\"@ref\":12},\"label\":\"Down\",\"visible\":true}]},\"id\":{\"@id\":163,\"value\":\"secondfloorclocktower\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@id\":233,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"clockface\",\"countableNounPrefix\":\"a\",\"description\":\"It is missing a clock face and both hands. Looking at it you decide the face would be best placed upon the mechanism before the hands.\",\"usedWithText\":\"You place the clock face onto the front of the mechanism and it clicks gently into place.\",\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"clockmechanismwithface\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:clockmechanismwithface:visible\",\"item\":{\"@ref\":233},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.DestroyItemItemAction\",\"itemID\":\"clockface\",\"model\":{\"@ref\":1}},\"content\":\"destroy item:clockface\",\"item\":{\"@ref\":233},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.DestroyItemItemAction\",\"itemID\":\"clockmechanism\",\"model\":{\"@ref\":1}},\"content\":\"destroy item:clockmechanism\",\"item\":{\"@ref\":233},\"logger\":{\"@ref\":42}}]},\"id\":\"clockmechanism\",\"midSentenceCasedName\":\"clock mechanism\",\"name\":\"Clock mechanism\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@id\":210,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"clockhourhand\",\"countableNounPrefix\":\"a\",\"description\":\"It has an engraved clock face but is missing its hands. It looks like it needs to have the hour hand fitted first.\",\"usedWithText\":\"You push the hour hand onto the spindle in the centre of the clock face.\",\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"clockmechanismwithfaceandhourhand\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:clockmechanismwithfaceandhourhand:visible\",\"item\":{\"@ref\":210},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.DestroyItemItemAction\",\"itemID\":\"clockhourhand\",\"model\":{\"@ref\":1}},\"content\":\"destroy item:clockhourhand\",\"item\":{\"@ref\":210},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.DestroyItemItemAction\",\"itemID\":\"clockmechanismwithface\",\"model\":{\"@ref\":1}},\"content\":\"destroy item:clockmechanismwithface\",\"item\":{\"@ref\":210},\"logger\":{\"@ref\":42}}]},\"id\":\"clockmechanismwithface\",\"midSentenceCasedName\":\"clock mechanism\",\"name\":\"Clock mechanism\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":false},{\"@id\":175,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"clockminutehand\",\"countableNounPrefix\":\"a\",\"description\":\"It has an engraved clock face but is missing its minute hand. The hour hand has been expertly placed upon the central spindle.\",\"usedWithText\":\"You push the minute hand on top of the hour hand already attached to the spindle in the centre of the clock face. It clips in place with a satisfying click. The mechanism starts to whir and the hands begin to gently jerk with the movement of the mechanism inside. The air stirs around you and you feel slightly wealthier. In the distance you hear what sounds like a rock slide and you take a mental note to investigate this further.\",\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.MakeExitVisibleItemAction\",\"exitID\":\"mineshaftentrance\",\"model\":{\"@ref\":1}},\"content\":\"make exit visible:mineshaftentrance\",\"item\":{\"@ref\":175},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeLocationDescriptionItemAction\",\"locationID\":\"outsideamineshaft\",\"model\":{\"@ref\":1},\"newDescription\":\"You stand before an old mine shaft. There are some dusty footprints in the dirt around the entrance. A sign states \\\"McCreedys Mine - Stay out or suffer the consequences!\\\". To the east is an old ramshackle shed.\"},\"content\":\"change location description:outsideamineshaft:You stand before an old mine shaft. There are some dusty footprints in the dirt around the entrance. A sign states \\\"McCreedys Mine - Stay out or suffer the consequences!\\\". To the east is an old ramshackle shed.\",\"item\":{\"@ref\":175},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.IncrementScoreItemAction\",\"model\":{\"@ref\":1}},\"content\":\"increment score:\",\"item\":{\"@ref\":175},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"clockmechanismwithfaceandhourhandandminutehand\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:clockmechanismwithfaceandhourhandandminutehand:visible\",\"item\":{\"@ref\":175},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.DestroyItemItemAction\",\"itemID\":\"clockminutehand\",\"model\":{\"@ref\":1}},\"content\":\"destroy item:clockminutehand\",\"item\":{\"@ref\":175},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.DestroyItemItemAction\",\"itemID\":\"clockmechanismwithfaceandhourhand\",\"model\":{\"@ref\":1}},\"content\":\"destroy item:clockmechanismwithfaceandhourhand\",\"item\":{\"@ref\":175},\"logger\":{\"@ref\":42}}]},\"id\":\"clockmechanismwithfaceandhourhand\",\"midSentenceCasedName\":\"clock mechanism\",\"name\":\"Clock mechanism\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":false},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"a\",\"description\":\"It has an engraved clock face and both hands. It ticks gently.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"clockmechanismwithfaceandhourhandandminutehand\",\"midSentenceCasedName\":\"clock mechanism\",\"name\":\"Clock mechanism\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":false}]},\"x\":300,\"y\":0},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You stand just inside a dusty mineshaft. It looks like it has been abandoned and not used for many years. Until recently the entrance was blocked by a large rock fall. There are footprints leading down a small dark tunnel and there is a dimly lit annex to the right.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"outsideamineshaft\",\"directionHint\":{\"@ref\":69},\"id\":{\"@ref\":12},\"label\":\"Outside\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"darktunnel1\",\"directionHint\":{\"@ref\":76},\"id\":{\"@ref\":12},\"label\":\"Dark tunnel\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"dimlylitannex\",\"directionHint\":{\"@ref\":151},\"id\":{\"@ref\":12},\"label\":\"Dimly lit annex\",\"visible\":true}]},\"id\":{\"@id\":144,\"value\":\"mineshaftchamber1\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":420,\"y\":540},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are standing on the first floor of the clock tower. The wooden floorboards creak and groan as you tread across them.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"secondfloorclocktower\",\"directionHint\":{\"@ref\":69},\"id\":{\"@ref\":12},\"label\":\"Up\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"groundfloorclocktower\",\"directionHint\":{\"@ref\":76},\"id\":{\"@ref\":12},\"label\":\"Down\",\"visible\":true}]},\"id\":{\"@id\":132,\"value\":\"firstfloorclocktower\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":300,\"y\":60},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are inside the mighty clock tower. Sound resonates around you as the clock ticks.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"firstfloorclocktower\",\"directionHint\":{\"@ref\":69},\"id\":{\"@ref\":12},\"label\":\"Up\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"clocktower\",\"directionHint\":{\"@ref\":76},\"id\":{\"@ref\":12},\"label\":\"Clock tower door\",\"visible\":true}]},\"id\":{\"@id\":118,\"value\":\"groundfloorclocktower\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":300,\"y\":120},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"This is the mine smithy, used for emergency tool repair to save the workers from trekking back into town. There are a selection of tools lying around, none of which catch your eye.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"candlelitchamber\",\"directionHint\":{\"@ref\":69},\"id\":{\"@ref\":12},\"label\":\"Candle lit chamber\",\"visible\":true}]},\"id\":{\"@id\":82,\"value\":\"minesmithy\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@id\":93,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"bluntpickaxe\",\"countableNounPrefix\":\"a\",\"description\":\"This is used to sharpen blunted mining tools.\",\"usedWithText\":\"You sit down at the wheel and place the blunted axe head against the sharpening stone, pumping the pedal to turn the wheel with such vigour that sparks fly across the room. If only you applied yourself with such energy to all your endeavours you might see sparks more often.\",\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.DestroyItemItemAction\",\"itemID\":\"bluntpickaxe\",\"model\":{\"@ref\":1}},\"content\":\"destroy item:bluntpickaxe\",\"item\":{\"@ref\":93},\"logger\":{\"@ref\":42}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"pickaxe\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:pickaxe:visible\",\"item\":{\"@ref\":93},\"logger\":{\"@ref\":42}}]},\"id\":\"sharpeningwheel\",\"midSentenceCasedName\":null,\"name\":\"Sharpening wheel\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"a\",\"description\":\"This is a typical miners pick axe and has been recently sharpened.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"pickaxe\",\"midSentenceCasedName\":null,\"name\":\"Pick axe\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":false}]},\"x\":480,\"y\":720},{\"@ref\":32}]},\"inventoryItems\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"some\",\"description\":\"It's fluffy and shaped like an inverted belly button.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":{\"@ref\":12},\"midSentenceCasedName\":null,\"name\":\"Pocket lint\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":{\"@ref\":12},\"description\":\"It's got a lot of tools on it. You feel like a man of the wilderness when you wield this formidable contraption.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"multitool\",\"midSentenceCasedName\":null,\"name\":\"Multitool\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"a\",\"description\":\"It is rather dirty and has clearly been carved from some poor unfortunates metacarpal.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"clocktowerskeletonkey\",\"midSentenceCasedName\":\"skeleton key\",\"name\":\"Skeleton key\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true}]},\"currentScore\":0,\"maximumScore\":2}";

    @Test
    public void on_resume_reads_a_json_file_and_saves_as_an_action_history_file__get_wooden_pole() {
        TextAdventureDummyActivity activity = new TextAdventureDummyActivity();
        activity.deleteFile( actionHistorySaveFileName );
        prepareJSONSaveFile( activity, json_model_get_wooden_pole );
        activity.setModelContent( modelContent );
        activity.onCreate( null );

        activity.onResume();
        activity.onPause();

        String savedHistory = loadSerialisedActionHistory( activity );
        String expected_action_history =
            "action name:take specific item:item id:clocktowerskeletonkey:location id:townentrance:\n" +
            "action name:take specific item:item id:spade:location id:smallshed:\n" +
            "action name:use with specific item:item id:clocktowerskeletonkey:extra item id:lockeddoor:\n" +
            "action name:use with specific item:item id:spade:extra item id:moundofearth:\n" +
            "action name:take specific item:item id:clockface:location id:townoutbuildings:\n" +
            "action name:use with specific item:item id:clockface:extra item id:clockmechanism:\n" +
            "action name:examine:item id:bagsofjunk:\n" +
            "action name:take specific item:item id:clockhourhand:location id:smallshed:\n" +
            "action name:use with specific item:item id:clockhourhand:extra item id:clockmechanismwithface:\n" +
            "action name:take specific item:item id:clockminutehand:location id:clocktower:\n" +
            "action name:use with specific item:item id:clockminutehand:extra item id:clockmechanismwithfaceandhourhand:\n" +
            "action name:examine:item id:axehead:\n" +
            "action name:examine:item id:pileofstraw:\n" +
            "action name:take specific item:item id:woodenpole:location id:evensmallerannex:\n";
        assertThat( savedHistory, is( expected_action_history ) );
    }

    @Test
    public void on_resume_reads_a_json_file_and_replays_actions__get_wooden_pole() {
        RendersView renderer = mock( RendersView.class );
        BasicModel model = new BasicModel();
        BasicModelFactory modelFactory = mock( BasicModelFactory.class );
        when( modelFactory.createModel() ).thenReturn( model );
        TextAdventureDummyActivity activity = new TextAdventureDummyActivity( renderer,
            modelFactory );
        activity.deleteFile( actionHistorySaveFileName );
        prepareJSONSaveFile( activity, json_model_get_wooden_pole );
        activity.setModelContent( modelContent );
        activity.onCreate( null );

        activity.onResume();

        ModelLocation townEntrance = model.findLocationByID( "townentrance" );
        Item skeletonKey = model.findItemByID( "clocktowerskeletonkey" );
        assertThat( townEntrance.items(), not( hasItem( skeletonKey ) ) );
        assertThat( model.inventoryItems(), hasItem( skeletonKey ) );
        Exit unlockedDoor = model.findExitByID( "clocktowerdoor" );
        assertThat( unlockedDoor.visible(), is( true ) );

        Item completeClock = model.findItemByID( "clockmechanismwithfaceandhourhandandminutehand" );
        assertThat( completeClock.visible(), is( true ) );
        assertThat( model.currentScore(), is( 1 ) );
        Item woodenPole = model.findItemByID( "woodenpole" );
        assertThat( model.inventoryItems(), hasItem( woodenPole ) );
        assertThat( model.currentLocation(), is( townEntrance ) );
    }

     // Generated by V1.0 - actions { take skeleton key, use skeleton key with locked door, use spade with mound, take clockface, use clockface,
     //                               examine bags of junk, take clock hour hand, use clock hour hand, take minute hand, use minute hand,
     //                               examine axehead, examine straw, take wooden pole }
     private final String json_model_get_wooden_pole = "{\"@id\":1,\"@type\":\"com.chewielouie.textadventure.BasicModel\",\"currentLocation\":{\"@id\":388,\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"This is an even smaller annex attached to a larger better lit one. There is straw on the floor and it looks like someone has been sleeping here.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"dimlylitannex\",\"directionHint\":{\"@id\":75,\"name\":\"North\",\"ordinal\":1},\"id\":{\"@id\":11,\"value\":\"\"},\"label\":\"Dimly lit annex\",\"visible\":true}]},\"id\":{\"@id\":387,\"value\":\"evensmallerannex\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@id\":391,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":11},\"countableNounPrefix\":\"a\",\"description\":\"It still has the indentation of a sleeping body although the body does not appear to still be here.\",\"usedWithText\":{\"@ref\":11},\"examineText\":\"You delve deeply into the pile of straw and unearth a variety of delightful things, mostly ticks and beetles, as well as some unspeakable left overs. At the very bottom of the pile you find a wooden pole. You wish the owner was around so you could beat them with it for having left their bedroom in such a disgraceful state.\",\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"pileofstraw\",\"midSentenceCasedName\":null,\"name\":\"Pile of straw\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"woodenpole\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:woodenpole:visible\",\"item\":{\"@ref\":391},\"logger\":{\"@id\":56,\"@type\":\"com.chewielouie.textadventure.StdoutLogger\"}}]},\"examined\":true,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":false,\"visible\":true}]},\"x\":480,\"y\":660},\"locations\":{\"@id\":42,\"@type\":\"java.util.HashMap\",\"@keys\":[\"smallminechamber\",\"darktunnel2\",\"darktunnel1\",\"evensmallerannex\",\"dimlylitannex\",\"townentrance\",\"townoutbuildings\",\"outsideamineshaft\",\"smallshed\",\"candlelitchamber\",\"mainstreettown\",\"secondfloorclocktower\",\"mineshaftchamber1\",\"firstfloorclocktower\",\"groundfloorclocktower\",\"minesmithy\",\"clocktower\"],\"@items\":[{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You enter the chamber and although it is lit by a candle burning gently in an alcove you strain your eyes to peer into the shadows. Out shuffles a man, bedraggled and somber, head down muttering to himself. You notice he has a manacle around his ankle, chained to a nearby rock. Approaching him you say softly 'Hello? Who are you? What's happened here?'. The man replies 'I was taken, I don't know who it was. They hit me from behind and I woke up here, like this.' gesturing to the chain at his feet. 'Help me, please.' splutters the man as he gasps for breath. He is clearly suffering from his circumstances and you feel inclined to find a way to help him out of his predicament.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"darktunnel2\",\"directionHint\":{\"@ref\":75},\"id\":{\"@ref\":11},\"label\":\"North\",\"visible\":true}]},\"id\":{\"@id\":440,\"value\":\"smallminechamber\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"multitool\",\"countableNounPrefix\":\"an\",\"description\":\"He is dirty and looks exhausted. Chained to a nearby rock you see no easy way to free him.\",\"usedWithText\":\"The man cowers away from you and you feel foolish for threatening the poor man with your impressive multitool.\",\"examineText\":{\"@ref\":11},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"imprisonedman\",\"midSentenceCasedName\":null,\"name\":\"Imprisoned man\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@id\":461,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"pickaxe\",\"countableNounPrefix\":\"a\",\"description\":\"It looks to be softer than the surrounding walls, particularly where the chain link has been pinned to it. You wonder if there might be a way to lever or pick the pin out from the rock.\",\"usedWithText\":\"You swing the pick axe high above your head and bring it hard down upon the rock, pin and chain. The links shatter into pieces and scatter across the floor, freeing the man who jumps to his feet with joy. 'Oh thank you good sir - I can return to my shop at last, if there's anything left to return to.' he says.\",\"examineText\":{\"@ref\":11},\"onUseActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemDescriptionItemAction\",\"description\":\"It looks to be softer than the surrounding walls. There is a hole where a chain pin used to be embedded in the rock.\",\"item\":{\"@ref\":461}},\"content\":\"change item description:It looks to be softer than the surrounding walls. There is a hole where a chain pin used to be embedded in the rock.\",\"item\":{\"@ref\":461},\"logger\":{\"@ref\":56}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeLocationDescriptionItemAction\",\"locationID\":\"smallminechamber\",\"model\":{\"@ref\":1},\"newDescription\":\"You enter the chamber and although it is lit by a candle burning gently in an alcove you strain your eyes to peer into the shadows. This room looks lived in, of a sort. You notice shattered chain links on the floor.\"},\"content\":\"change location description:smallminechamber:You enter the chamber and although it is lit by a candle burning gently in an alcove you strain your eyes to peer into the shadows. This room looks lived in, of a sort. You notice shattered chain links on the floor.\",\"item\":{\"@ref\":461},\"logger\":{\"@ref\":56}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"imprisonedman\",\"model\":{\"@ref\":1},\"visibility\":false},\"content\":\"change item visibility:imprisonedman:invisible\",\"item\":{\"@ref\":461},\"logger\":{\"@ref\":56}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"shopkeeper\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:shopkeeper:visible\",\"item\":{\"@ref\":461},\"logger\":{\"@ref\":56}}]},\"id\":\"rock\",\"midSentenceCasedName\":null,\"name\":\"Rock\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":false,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@id\":444,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"minemap\",\"countableNounPrefix\":\"a\",\"description\":\"He is dirty and looks exhausted but has the light of hope in his eyes now that he is free. He also looks a little lost.\",\"usedWithText\":\"You hand the map to the shop keeper who smiles and nods at you with gratitude. 'Thank you, very kind. For your help have a ruby and come by my shop in town anytime - I'm sure I can arrange a favourable discount on anything that takes your eye.'. With that the man walks out leaving you alone. The air stirs around you and you feel slightly wealthier.\",\"examineText\":{\"@ref\":11},\"onUseActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"shopkeeper\",\"model\":{\"@ref\":1},\"visibility\":false},\"content\":\"change item visibility:shopkeeper:invisible\",\"item\":{\"@ref\":444},\"logger\":{\"@ref\":56}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.IncrementScoreItemAction\",\"model\":{\"@ref\":1}},\"content\":\"increment score:\",\"item\":{\"@ref\":444},\"logger\":{\"@ref\":56}}]},\"id\":\"shopkeeper\",\"midSentenceCasedName\":null,\"name\":\"Shop keeper\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":false,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":false}]},\"x\":420,\"y\":720},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"The footprints continue further south where you can see a dim light burning. The air down here is dank and you feel a little claustrophobic. Off to the east is a candle lit chamber.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"darktunnel1\",\"directionHint\":{\"@ref\":75},\"id\":{\"@ref\":11},\"label\":\"North\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"smallminechamber\",\"directionHint\":{\"@id\":82,\"name\":\"South\",\"ordinal\":2},\"id\":{\"@ref\":11},\"label\":\"South\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"candlelitchamber\",\"directionHint\":{\"@id\":288,\"name\":\"East\",\"ordinal\":3},\"id\":{\"@ref\":11},\"label\":\"Candle lit chamber\",\"visible\":true}]},\"id\":{\"@id\":424,\"value\":\"darktunnel2\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":420,\"y\":660},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are in a dark tunnel with a trail of scuffed footprints leading deeper into the mine. You think you can make out two sets of prints in the dirt. The mine entrance lies to the north and the tunnel continues south.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"mineshaftchamber1\",\"directionHint\":{\"@ref\":75},\"id\":{\"@ref\":11},\"label\":\"North\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"darktunnel2\",\"directionHint\":{\"@ref\":82},\"id\":{\"@ref\":11},\"label\":\"South\",\"visible\":true}]},\"id\":{\"@id\":411,\"value\":\"darktunnel1\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":420,\"y\":600},{\"@ref\":388},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"This annex is carved out of the rock face with several indentations holding a variety of items including bones, a burnt out candle, some old torn paper and a metal ring.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"mineshaftchamber1\",\"directionHint\":{\"@ref\":288},\"id\":{\"@ref\":11},\"label\":\"East\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"evensmallerannex\",\"directionHint\":{\"@ref\":82},\"id\":{\"@ref\":11},\"label\":\"Even smaller annex\",\"visible\":true}]},\"id\":{\"@id\":337,\"value\":\"dimlylitannex\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@id\":348,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"woodenpole\",\"countableNounPrefix\":\"a\",\"description\":{\"@id\":358,\"value\":\"This is quite clearly an axe head. You are however missing a means to wield it and it is wedged in a crack in the wall.\"},\"usedWithText\":\"The wooden pole slots perfectly into the axe head allowing you to apply your weight to the end of the pole causing the axe to pop out of the crack in the wall like a lollipop from a babies mouth. It is quite a hefty mining implement and is well weighted. You feel you could move a substantial amount of rock with this if it were but a touch sharper.\",\"examineText\":{\"@ref\":11},\"onUseActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"bluntpickaxe\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:bluntpickaxe:visible\",\"item\":{\"@ref\":348},\"logger\":{\"@ref\":56}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.DestroyItemItemAction\",\"itemID\":\"axehead\",\"model\":{\"@ref\":1}},\"content\":\"destroy item:axehead\",\"item\":{\"@ref\":348},\"logger\":{\"@ref\":56}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.DestroyItemItemAction\",\"itemID\":\"woodenpole\",\"model\":{\"@ref\":1}},\"content\":\"destroy item:woodenpole\",\"item\":{\"@ref\":348},\"logger\":{\"@ref\":56}}]},\"id\":\"axehead\",\"midSentenceCasedName\":null,\"name\":{\"@id\":354,\"value\":\"Axe head\"},\"onExamineActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemDescriptionItemAction\",\"description\":{\"@ref\":358},\"item\":{\"@ref\":348}},\"content\":\"change item description:This is quite clearly an axe head. You are however missing a means to wield it and it is wedged in a crack in the wall.\",\"item\":{\"@ref\":348},\"logger\":{\"@ref\":56}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemNameItemAction\",\"item\":{\"@ref\":348},\"name\":{\"@ref\":354}},\"content\":\"change item name:Axe head\",\"item\":{\"@ref\":348},\"logger\":{\"@ref\":56}}]},\"examined\":true,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":false,\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":11},\"countableNounPrefix\":\"a\",\"description\":\"It looks rather blunt. You can probably bludgeon some small rocks into shards with it given a few hours and some angry words but not much else.\",\"usedWithText\":{\"@ref\":11},\"examineText\":{\"@ref\":11},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"bluntpickaxe\",\"midSentenceCasedName\":null,\"name\":\"Blunt pick axe\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":false}]},\"x\":480,\"y\":600},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are standing outside a small town. The main street goes north, to the west there are some small out buildings and to the east you can see what looks like an abandoned mine shaft.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"mainstreettown\",\"directionHint\":{\"@ref\":75},\"id\":\"\",\"label\":\"North\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"outsideamineshaft\",\"directionHint\":{\"@ref\":288},\"id\":\"\",\"label\":\"East\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"townoutbuildings\",\"directionHint\":{\"@id\":157,\"name\":\"West\",\"ordinal\":4},\"id\":\"\",\"label\":\"West\",\"visible\":true}]},\"id\":{\"@id\":310,\"value\":\"townentrance\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":11},\"countableNounPrefix\":\"some\",\"description\":\"It is yellow and a little moldy.\",\"usedWithText\":{\"@ref\":11},\"examineText\":{\"@ref\":11},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"\",\"midSentenceCasedName\":\"banana peel\",\"name\":\"Banana peel\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true}]},\"x\":300,\"y\":480},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"There are a number of small shack like structures here, fallen down and decrepit they haven't been used for years.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"townentrance\",\"directionHint\":{\"@ref\":288},\"id\":\"\",\"label\":\"East\",\"visible\":true}]},\"id\":{\"@id\":297,\"value\":\"townoutbuildings\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":180,\"y\":480},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You stand before an old mine shaft. There are some dusty footprints in the dirt around the entrance. A sign states \\\"McCreedys Mine - Stay out or suffer the consequences!\\\". To the east is an old ramshackle shed.\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"townentrance\",\"directionHint\":{\"@ref\":157},\"id\":{\"@ref\":11},\"label\":\"West\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"smallshed\",\"directionHint\":{\"@ref\":288},\"id\":{\"@ref\":11},\"label\":\"Ramshackle Shed\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"mineshaftchamber1\",\"directionHint\":{\"@ref\":82},\"id\":\"mineshaftentrance\",\"label\":\"Mineshaft entrance\",\"visible\":true}]},\"id\":{\"@id\":277,\"value\":\"outsideamineshaft\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":420,\"y\":480},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are standing inside a small shed. It is dusty and dirty and a variety of mining tools rest against the walls.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"outsideamineshaft\",\"directionHint\":{\"@ref\":157},\"id\":{\"@ref\":11},\"label\":\"West\",\"visible\":true}]},\"id\":{\"@id\":250,\"value\":\"smallshed\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@id\":254,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":11},\"countableNounPrefix\":\"some\",\"description\":{\"@id\":264,\"value\":\"They are full of rubbish. Nothing else.\"},\"usedWithText\":{\"@ref\":11},\"examineText\":\"You look closer and see something glinting in the darkness. You scramble around in the bag trying to reach it, grasping with your finger tips pulling it out and dropping it at your feet.\",\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":{\"@ref\":11},\"midSentenceCasedName\":\"bags of junk\",\"name\":\"Bags of junk\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemDescriptionItemAction\",\"description\":{\"@ref\":264},\"item\":{\"@ref\":254}},\"content\":\"change item description:They are full of rubbish. Nothing else.\",\"item\":{\"@ref\":254},\"logger\":{\"@ref\":56}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"clockhourhand\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:clockhourhand:visible\",\"item\":{\"@ref\":254},\"logger\":{\"@ref\":56}}]},\"examined\":true,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":false,\"visible\":true}]},\"x\":480,\"y\":480},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You stand in a circular chamber with candles lining the walls in metal brakets. There is a table in the centre of the room and an exit leading deeper into the mine to what looks like a smithy. To the west a tunnel meanders back through the darkness.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"darktunnel2\",\"directionHint\":{\"@ref\":157},\"id\":{\"@ref\":11},\"label\":\"Dark tunnel\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"minesmithy\",\"directionHint\":{\"@ref\":82},\"id\":{\"@ref\":11},\"label\":\"Smithy\",\"visible\":true}]},\"id\":{\"@id\":212,\"value\":\"candlelitchamber\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@id\":223,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":11},\"countableNounPrefix\":\"a\",\"description\":\"It is covered with papers.\",\"usedWithText\":{\"@ref\":11},\"examineText\":\"You're not sure how anyone found anything in this mess but a particular sheet of paper catches your eye. As you reach to pick it up it falls to the ground.\",\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"table\",\"midSentenceCasedName\":null,\"name\":\"Table\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"minemap\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:minemap:visible\",\"item\":{\"@ref\":223},\"logger\":{\"@ref\":56}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemDescriptionItemAction\",\"description\":\"It is covered with papers. Boring, uninteresting papers.\",\"item\":{\"@ref\":223}},\"content\":\"change item description:It is covered with papers. Boring, uninteresting papers.\",\"item\":{\"@ref\":223},\"logger\":{\"@ref\":56}}]},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":false,\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":11},\"countableNounPrefix\":\"a\",\"description\":\"This is a map of the mine tunnels - useful in case you get lost.\",\"usedWithText\":{\"@ref\":11},\"examineText\":{\"@ref\":11},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"minemap\",\"midSentenceCasedName\":null,\"name\":\"Map\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":false}]},\"x\":480,\"y\":660},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are on the main street of the town. There is a clock tower to the north.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"clocktower\",\"directionHint\":{\"@ref\":75},\"id\":\"\",\"label\":\"North\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"townentrance\",\"directionHint\":{\"@ref\":82},\"id\":\"\",\"label\":\"South\",\"visible\":true}]},\"id\":{\"@id\":188,\"value\":\"mainstreettown\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":11},\"countableNounPrefix\":\"some\",\"description\":\"This dust looks pretty powerful, for dust.\",\"usedWithText\":{\"@ref\":11},\"examineText\":{\"@ref\":11},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"\",\"midSentenceCasedName\":\"Dust of the Ancients\",\"name\":\"Dust of the Ancients\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true}]},\"x\":300,\"y\":360},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are at the top of the clock tower standing in front of the clock mechanism.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"firstfloorclocktower\",\"directionHint\":{\"@ref\":82},\"id\":{\"@ref\":11},\"label\":\"Down\",\"visible\":true}]},\"id\":{\"@id\":169,\"value\":\"secondfloorclocktower\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":11},\"countableNounPrefix\":\"a\",\"description\":\"It has an engraved clock face and both hands. It ticks gently.\",\"usedWithText\":{\"@ref\":11},\"examineText\":{\"@ref\":11},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"clockmechanismwithfaceandhourhandandminutehand\",\"midSentenceCasedName\":\"clock mechanism\",\"name\":\"Clock mechanism\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":true,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true}]},\"x\":300,\"y\":0},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You stand just inside a dusty mineshaft. It looks like it has been abandoned and not used for many years. Until recently the entrance was blocked by a large rock fall. There are footprints leading down a small dark tunnel and there is a dimly lit annex to the right.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"outsideamineshaft\",\"directionHint\":{\"@ref\":75},\"id\":{\"@ref\":11},\"label\":\"Outside\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"darktunnel1\",\"directionHint\":{\"@ref\":82},\"id\":{\"@ref\":11},\"label\":\"Dark tunnel\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"dimlylitannex\",\"directionHint\":{\"@ref\":157},\"id\":{\"@ref\":11},\"label\":\"Dimly lit annex\",\"visible\":true}]},\"id\":{\"@id\":150,\"value\":\"mineshaftchamber1\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":420,\"y\":540},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are standing on the first floor of the clock tower. The wooden floorboards creak and groan as you tread across them.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"secondfloorclocktower\",\"directionHint\":{\"@ref\":75},\"id\":{\"@ref\":11},\"label\":\"Up\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"groundfloorclocktower\",\"directionHint\":{\"@ref\":82},\"id\":{\"@ref\":11},\"label\":\"Down\",\"visible\":true}]},\"id\":{\"@id\":138,\"value\":\"firstfloorclocktower\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":300,\"y\":60},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are inside the mighty clock tower. Sound resonates around you as the clock ticks.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"firstfloorclocktower\",\"directionHint\":{\"@ref\":75},\"id\":{\"@ref\":11},\"label\":\"Up\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"clocktower\",\"directionHint\":{\"@ref\":82},\"id\":{\"@ref\":11},\"label\":\"Clock tower door\",\"visible\":true}]},\"id\":{\"@id\":124,\"value\":\"groundfloorclocktower\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":300,\"y\":120},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"This is the mine smithy, used for emergency tool repair to save the workers from trekking back into town. There are a selection of tools lying around, none of which catch your eye.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"candlelitchamber\",\"directionHint\":{\"@ref\":75},\"id\":{\"@ref\":11},\"label\":\"Candle lit chamber\",\"visible\":true}]},\"id\":{\"@id\":88,\"value\":\"minesmithy\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@id\":99,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"bluntpickaxe\",\"countableNounPrefix\":\"a\",\"description\":\"This is used to sharpen blunted mining tools.\",\"usedWithText\":\"You sit down at the wheel and place the blunted axe head against the sharpening stone, pumping the pedal to turn the wheel with such vigour that sparks fly across the room. If only you applied yourself with such energy to all your endeavours you might see sparks more often.\",\"examineText\":{\"@ref\":11},\"onUseActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.DestroyItemItemAction\",\"itemID\":\"bluntpickaxe\",\"model\":{\"@ref\":1}},\"content\":\"destroy item:bluntpickaxe\",\"item\":{\"@ref\":99},\"logger\":{\"@ref\":56}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"pickaxe\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:pickaxe:visible\",\"item\":{\"@ref\":99},\"logger\":{\"@ref\":56}}]},\"id\":\"sharpeningwheel\",\"midSentenceCasedName\":null,\"name\":\"Sharpening wheel\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":11},\"countableNounPrefix\":\"a\",\"description\":\"This is a typical miners pick axe and has been recently sharpened.\",\"usedWithText\":{\"@ref\":11},\"examineText\":{\"@ref\":11},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"pickaxe\",\"midSentenceCasedName\":null,\"name\":\"Pick axe\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":false}]},\"x\":480,\"y\":720},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You stand before a mighty clock tower. The clock goes TICK!\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"mainstreettown\",\"directionHint\":{\"@ref\":82},\"id\":\"\",\"label\":\"South\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"groundfloorclocktower\",\"directionHint\":{\"@ref\":75},\"id\":\"clocktowerdoor\",\"label\":\"Clock tower door\",\"visible\":true}]},\"id\":{\"@id\":45,\"value\":\"clocktower\"},\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@id\":49,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"clocktowerskeletonkey\",\"countableNounPrefix\":\"a\",\"description\":{\"@id\":66,\"value\":\"It is unlocked. It appears to allow access to the clock tower itself.\"},\"usedWithText\":\"You hear a click and the door springs open.\",\"examineText\":{\"@ref\":11},\"onUseActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemDescriptionItemAction\",\"description\":{\"@ref\":66},\"item\":{\"@ref\":49}},\"content\":\"change item description:It is unlocked. It appears to allow access to the clock tower itself.\",\"item\":{\"@ref\":49},\"logger\":{\"@ref\":56}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemNameItemAction\",\"item\":{\"@ref\":49},\"name\":{\"@id\":51,\"value\":\"unlocked door\"}},\"content\":\"change item name:unlocked door\",\"item\":{\"@ref\":49},\"logger\":{\"@ref\":56}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.MakeExitVisibleItemAction\",\"exitID\":\"clocktowerdoor\",\"model\":{\"@ref\":1}},\"content\":\"make exit visible:clocktowerdoor\",\"item\":{\"@ref\":49},\"logger\":{\"@ref\":56}}]},\"id\":\"\",\"midSentenceCasedName\":null,\"name\":{\"@ref\":51},\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":false,\"used\":true,\"examineActionIsRepeatable\":true,\"visible\":true}]},\"x\":300,\"y\":240}]},\"inventoryItems\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":11},\"countableNounPrefix\":\"some\",\"description\":\"It's fluffy and shaped like an inverted belly button.\",\"usedWithText\":{\"@ref\":11},\"examineText\":{\"@ref\":11},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":{\"@ref\":11},\"midSentenceCasedName\":null,\"name\":\"Pocket lint\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":11},\"countableNounPrefix\":{\"@ref\":11},\"description\":\"It's got a lot of tools on it. You feel like a man of the wilderness when you wield this formidable contraption.\",\"usedWithText\":{\"@ref\":11},\"examineText\":{\"@ref\":11},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"multitool\",\"midSentenceCasedName\":null,\"name\":\"Multitool\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":11},\"countableNounPrefix\":\"a\",\"description\":\"It is a wooden handled spade. It looks like it has seen quite a lot of use and might not be good for much.\",\"usedWithText\":{\"@ref\":11},\"examineText\":{\"@ref\":11},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"spade\",\"midSentenceCasedName\":null,\"name\":\"Spade\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":11},\"countableNounPrefix\":\"a\",\"description\":\"It is rather dirty and has clearly been carved from some poor unfortunates metacarpal.\",\"usedWithText\":{\"@ref\":11},\"examineText\":{\"@ref\":11},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"clocktowerskeletonkey\",\"midSentenceCasedName\":\"skeleton key\",\"name\":\"Skeleton key\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":11},\"countableNounPrefix\":\"a\",\"description\":\"It looks like it was attached to something at one end. Perhaps it was a tool of some sort.\",\"usedWithText\":{\"@ref\":11},\"examineText\":{\"@ref\":11},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"woodenpole\",\"midSentenceCasedName\":null,\"name\":\"Wooden pole\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true}]},\"currentScore\":1,\"maximumScore\":2}";

    @Test
    public void on_resume_reads_a_json_file_and_saves_as_an_action_history_file__free_shopkeeper() {
        TextAdventureDummyActivity activity = new TextAdventureDummyActivity();
        activity.deleteFile( actionHistorySaveFileName );
        prepareJSONSaveFile( activity, json_model_upto_free_shopkeeper );
        activity.setModelContent( modelContent );
        activity.onCreate( null );

        activity.onResume();
        activity.onPause();

        String savedHistory = loadSerialisedActionHistory( activity );
        String expected_action_history =
            "action name:take specific item:item id:clocktowerskeletonkey:location id:townentrance:\n" +
            "action name:take specific item:item id:spade:location id:smallshed:\n" +
            "action name:use with specific item:item id:clocktowerskeletonkey:extra item id:lockeddoor:\n" +
            "action name:use with specific item:item id:spade:extra item id:moundofearth:\n" +
            "action name:take specific item:item id:clockface:location id:townoutbuildings:\n" +
            "action name:use with specific item:item id:clockface:extra item id:clockmechanism:\n" +
            "action name:examine:item id:bagsofjunk:\n" +
            "action name:take specific item:item id:clockhourhand:location id:smallshed:\n" +
            "action name:use with specific item:item id:clockhourhand:extra item id:clockmechanismwithface:\n" +
            "action name:take specific item:item id:clockminutehand:location id:clocktower:\n" +
            "action name:use with specific item:item id:clockminutehand:extra item id:clockmechanismwithfaceandhourhand:\n" +
            "action name:examine:item id:axehead:\n" +
            "action name:examine:item id:pileofstraw:\n" +
            "action name:take specific item:item id:woodenpole:location id:evensmallerannex:\n" +
            "action name:use with specific item:item id:woodenpole:extra item id:axehead:\n" +
            "action name:take specific item:item id:bluntpickaxe:location id:dimlylitannex:\n" +
            "action name:use with specific item:item id:bluntpickaxe:extra item id:sharpeningwheel:\n" +
            "action name:take specific item:item id:pickaxe:location id:minesmithy:\n" +
            "action name:examine:item id:table:\n" +
            "action name:take specific item:item id:minemap:location id:candlelitchamber:\n" +
            "action name:use with specific item:item id:pickaxe:extra item id:rock:\n" +
            "action name:use with specific item:item id:minemap:extra item id:shopkeeper:\n";
        assertThat( savedHistory, is( expected_action_history ) );
    }

    @Test
    public void on_resume_reads_a_json_file_and_replays_actions__free_shopkeeper() {
        RendersView renderer = mock( RendersView.class );
        BasicModel model = new BasicModel();
        BasicModelFactory modelFactory = mock( BasicModelFactory.class );
        when( modelFactory.createModel() ).thenReturn( model );
        TextAdventureDummyActivity activity = new TextAdventureDummyActivity( renderer,
            modelFactory );
        activity.deleteFile( actionHistorySaveFileName );
        prepareJSONSaveFile( activity, json_model_upto_free_shopkeeper );
        activity.setModelContent( modelContent );
        activity.onCreate( null );

        activity.onResume();

        ModelLocation townEntrance = model.findLocationByID( "townentrance" );
        Item skeletonKey = model.findItemByID( "clocktowerskeletonkey" );
        assertThat( townEntrance.items(), not( hasItem( skeletonKey ) ) );
        assertThat( model.inventoryItems(), hasItem( skeletonKey ) );
        Exit unlockedDoor = model.findExitByID( "clocktowerdoor" );
        assertThat( unlockedDoor.visible(), is( true ) );

        Item completeClock = model.findItemByID( "clockmechanismwithfaceandhourhandandminutehand" );
        assertThat( completeClock.visible(), is( true ) );
        assertThat( model.currentScore(), is( 2 ) );
        Item pickAxe = model.findItemByID( "pickaxe" );
        assertThat( model.inventoryItems(), hasItem( pickAxe ) );
        assertThat( model.currentLocation(), is( townEntrance ) );
    }

    // Generated by V1.0 - actions { take skeleton key, use skeleton key with locked door, use spade with mound, take clockface, use clockface,
    //                               examine bags of junk, take clock hour hand, use clock hour hand, take minute hand, use minute hand,
    //                               examine axehead, examine straw, take wooden pole, use wooden pole with axe head, take blunt pick axe,
    //                               examine table, take map, use blunt pick axe with sharpening wheel, take pick axe, use pick axe with rock,
    //                               give map to shop keeper }
    private final String json_model_upto_free_shopkeeper = "{\"@id\":1,\"@type\":\"com.chewielouie.textadventure.BasicModel\",\"currentLocation\":{\"@id\":592,\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":{\"@id\":645,\"value\":\"You enter the chamber and although it is lit by a candle burning gently in an alcove you strain your eyes to peer into the shadows. This room looks lived in, of a sort. You notice shattered chain links on the floor.\"},\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"darktunnel2\",\"directionHint\":{\"@id\":117,\"name\":\"North\",\"ordinal\":1},\"id\":{\"@id\":12,\"value\":\"\"},\"label\":\"North\",\"visible\":true}]},\"id\":\"smallminechamber\",\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"multitool\",\"countableNounPrefix\":\"an\",\"description\":\"He is dirty and looks exhausted. Chained to a nearby rock you see no easy way to free him.\",\"usedWithText\":\"The man cowers away from you and you feel foolish for threatening the poor man with your impressive multitool.\",\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"imprisonedman\",\"midSentenceCasedName\":null,\"name\":\"Imprisoned man\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":false},{\"@id\":621,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"pickaxe\",\"countableNounPrefix\":\"a\",\"description\":{\"@id\":653,\"value\":\"It looks to be softer than the surrounding walls. There is a hole where a chain pin used to be embedded in the rock.\"},\"usedWithText\":\"You swing the pick axe high above your head and bring it hard down upon the rock, pin and chain. The links shatter into pieces and scatter across the floor, freeing the man who jumps to his feet with joy. 'Oh thank you good sir - I can return to my shop at last, if there's anything left to return to.' he says.\",\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemDescriptionItemAction\",\"description\":{\"@ref\":653},\"item\":{\"@ref\":621}},\"content\":\"change item description:It looks to be softer than the surrounding walls. There is a hole where a chain pin used to be embedded in the rock.\",\"item\":{\"@ref\":621},\"logger\":{\"@id\":86,\"@type\":\"com.chewielouie.textadventure.StdoutLogger\"}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeLocationDescriptionItemAction\",\"locationID\":\"smallminechamber\",\"model\":{\"@ref\":1},\"newDescription\":{\"@ref\":645}},\"content\":\"change location description:smallminechamber:You enter the chamber and although it is lit by a candle burning gently in an alcove you strain your eyes to peer into the shadows. This room looks lived in, of a sort. You notice shattered chain links on the floor.\",\"item\":{\"@ref\":621},\"logger\":{\"@ref\":86}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"imprisonedman\",\"model\":{\"@ref\":1},\"visibility\":false},\"content\":\"change item visibility:imprisonedman:invisible\",\"item\":{\"@ref\":621},\"logger\":{\"@ref\":86}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"shopkeeper\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:shopkeeper:visible\",\"item\":{\"@ref\":621},\"logger\":{\"@ref\":86}}]},\"id\":\"rock\",\"midSentenceCasedName\":null,\"name\":\"Rock\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":true,\"takeable\":false,\"useIsRepeatable\":false,\"used\":true,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@id\":595,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"minemap\",\"countableNounPrefix\":\"a\",\"description\":\"He is dirty and looks exhausted but has the light of hope in his eyes now that he is free. He also looks a little lost.\",\"usedWithText\":\"You hand the map to the shop keeper who smiles and nods at you with gratitude. 'Thank you, very kind. For your help have a ruby and come by my shop in town anytime - I'm sure I can arrange a favourable discount on anything that takes your eye.'. With that the man walks out leaving you alone. The air stirs around you and you feel slightly wealthier.\",\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"shopkeeper\",\"model\":{\"@ref\":1},\"visibility\":false},\"content\":\"change item visibility:shopkeeper:invisible\",\"item\":{\"@ref\":595},\"logger\":{\"@ref\":86}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.IncrementScoreItemAction\",\"model\":{\"@ref\":1}},\"content\":\"increment score:\",\"item\":{\"@ref\":595},\"logger\":{\"@ref\":86}}]},\"id\":\"shopkeeper\",\"midSentenceCasedName\":null,\"name\":\"Shop keeper\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":true,\"takeable\":false,\"useIsRepeatable\":false,\"used\":true,\"examineActionIsRepeatable\":true,\"visible\":false}]},\"x\":420,\"y\":720},\"locations\":{\"@id\":71,\"@type\":\"java.util.HashMap\",\"@keys\":[\"smallminechamber\",\"darktunnel2\",\"darktunnel1\",\"evensmallerannex\",\"townentrance\",\"dimlylitannex\",\"townoutbuildings\",\"outsideamineshaft\",\"smallshed\",\"candlelitchamber\",\"mainstreettown\",\"secondfloorclocktower\",\"firstfloorclocktower\",\"mineshaftchamber1\",\"groundfloorclocktower\",\"minesmithy\",\"clocktower\"],\"@items\":[{\"@ref\":592},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"The footprints continue further south where you can see a dim light burning. The air down here is dank and you feel a little claustrophobic. Off to the east is a candle lit chamber.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"darktunnel1\",\"directionHint\":{\"@ref\":117},\"id\":{\"@ref\":12},\"label\":\"North\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"smallminechamber\",\"directionHint\":{\"@id\":125,\"name\":\"South\",\"ordinal\":2},\"id\":{\"@ref\":12},\"label\":\"South\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"candlelitchamber\",\"directionHint\":{\"@id\":417,\"name\":\"East\",\"ordinal\":3},\"id\":{\"@ref\":12},\"label\":\"Candle lit chamber\",\"visible\":true}]},\"id\":\"darktunnel2\",\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":420,\"y\":660},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are in a dark tunnel with a trail of scuffed footprints leading deeper into the mine. You think you can make out two sets of prints in the dirt. The mine entrance lies to the north and the tunnel continues south.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"mineshaftchamber1\",\"directionHint\":{\"@ref\":117},\"id\":{\"@ref\":12},\"label\":\"North\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"darktunnel2\",\"directionHint\":{\"@ref\":125},\"id\":{\"@ref\":12},\"label\":\"South\",\"visible\":true}]},\"id\":\"darktunnel1\",\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":420,\"y\":600},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"This is an even smaller annex attached to a larger better lit one. There is straw on the floor and it looks like someone has been sleeping here.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"dimlylitannex\",\"directionHint\":{\"@ref\":117},\"id\":{\"@ref\":12},\"label\":\"Dimly lit annex\",\"visible\":true}]},\"id\":\"evensmallerannex\",\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@id\":511,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"a\",\"description\":\"It still has the indentation of a sleeping body although the body does not appear to still be here.\",\"usedWithText\":{\"@ref\":12},\"examineText\":\"You delve deeply into the pile of straw and unearth a variety of delightful things, mostly ticks and beetles, as well as some unspeakable left overs. At the very bottom of the pile you find a wooden pole. You wish the owner was around so you could beat them with it for having left their bedroom in such a disgraceful state.\",\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"pileofstraw\",\"midSentenceCasedName\":null,\"name\":\"Pile of straw\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"woodenpole\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:woodenpole:visible\",\"item\":{\"@ref\":511},\"logger\":{\"@ref\":86}}]},\"examined\":true,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":false,\"visible\":true}]},\"x\":480,\"y\":660},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are standing outside a small town. The main street goes north, to the west there are some small out buildings and to the east you can see what looks like an abandoned mine shaft.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"mainstreettown\",\"directionHint\":{\"@ref\":117},\"id\":{\"@ref\":12},\"label\":\"North\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"outsideamineshaft\",\"directionHint\":{\"@ref\":417},\"id\":{\"@ref\":12},\"label\":\"East\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"townoutbuildings\",\"directionHint\":{\"@id\":211,\"name\":\"West\",\"ordinal\":4},\"id\":{\"@ref\":12},\"label\":\"West\",\"visible\":true}]},\"id\":\"townentrance\",\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"some\",\"description\":\"It is yellow and a little moldy.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":{\"@ref\":12},\"midSentenceCasedName\":\"banana peel\",\"name\":\"Banana peel\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true}]},\"x\":300,\"y\":480},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"This annex is carved out of the rock face with several indentations holding a variety of items including bones, a burnt out candle, some old torn paper and a metal ring.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"mineshaftchamber1\",\"directionHint\":{\"@ref\":417},\"id\":{\"@ref\":12},\"label\":\"East\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"evensmallerannex\",\"directionHint\":{\"@ref\":125},\"id\":{\"@ref\":12},\"label\":\"Even smaller annex\",\"visible\":true}]},\"id\":\"dimlylitannex\",\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":480,\"y\":600},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"There are a number of small shack like structures here, fallen down and decrepit they haven't been used for years.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"townentrance\",\"directionHint\":{\"@ref\":417},\"id\":{\"@ref\":12},\"label\":\"East\",\"visible\":true}]},\"id\":\"townoutbuildings\",\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":180,\"y\":480},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You stand before an old mine shaft. There are some dusty footprints in the dirt around the entrance. A sign states \\\"McCreedys Mine - Stay out or suffer the consequences!\\\". To the east is an old ramshackle shed.\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"townentrance\",\"directionHint\":{\"@ref\":211},\"id\":{\"@ref\":12},\"label\":\"West\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"smallshed\",\"directionHint\":{\"@ref\":417},\"id\":{\"@ref\":12},\"label\":\"Ramshackle Shed\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"mineshaftchamber1\",\"directionHint\":{\"@ref\":125},\"id\":\"mineshaftentrance\",\"label\":\"Mineshaft entrance\",\"visible\":true}]},\"id\":\"outsideamineshaft\",\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":420,\"y\":480},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are standing inside a small shed. It is dusty and dirty and a variety of mining tools rest against the walls.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"outsideamineshaft\",\"directionHint\":{\"@ref\":211},\"id\":{\"@ref\":12},\"label\":\"West\",\"visible\":true}]},\"id\":\"smallshed\",\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@id\":363,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"some\",\"description\":{\"@id\":376,\"value\":\"They are full of rubbish. Nothing else.\"},\"usedWithText\":{\"@ref\":12},\"examineText\":\"You look closer and see something glinting in the darkness. You scramble around in the bag trying to reach it, grasping with your finger tips pulling it out and dropping it at your feet.\",\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":{\"@ref\":12},\"midSentenceCasedName\":\"bags of junk\",\"name\":\"Bags of junk\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemDescriptionItemAction\",\"description\":{\"@ref\":376},\"item\":{\"@ref\":363}},\"content\":\"change item description:They are full of rubbish. Nothing else.\",\"item\":{\"@ref\":363},\"logger\":{\"@ref\":86}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"clockhourhand\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:clockhourhand:visible\",\"item\":{\"@ref\":363},\"logger\":{\"@ref\":86}}]},\"examined\":true,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":false,\"visible\":true}]},\"x\":480,\"y\":480},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You stand in a circular chamber with candles lining the walls in metal brakets. There is a table in the centre of the room and an exit leading deeper into the mine to what looks like a smithy. To the west a tunnel meanders back through the darkness.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"darktunnel2\",\"directionHint\":{\"@ref\":211},\"id\":{\"@ref\":12},\"label\":\"Dark tunnel\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"minesmithy\",\"directionHint\":{\"@ref\":125},\"id\":{\"@ref\":12},\"label\":\"Smithy\",\"visible\":true}]},\"id\":\"candlelitchamber\",\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@id\":317,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"a\",\"description\":{\"@id\":324,\"value\":\"It is covered with papers. Boring, uninteresting papers.\"},\"usedWithText\":{\"@ref\":12},\"examineText\":\"You're not sure how anyone found anything in this mess but a particular sheet of paper catches your eye. As you reach to pick it up it falls to the ground.\",\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"table\",\"midSentenceCasedName\":null,\"name\":\"Table\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"minemap\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:minemap:visible\",\"item\":{\"@ref\":317},\"logger\":{\"@ref\":86}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemDescriptionItemAction\",\"description\":{\"@ref\":324},\"item\":{\"@ref\":317}},\"content\":\"change item description:It is covered with papers. Boring, uninteresting papers.\",\"item\":{\"@ref\":317},\"logger\":{\"@ref\":86}}]},\"examined\":true,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":false,\"visible\":true}]},\"x\":480,\"y\":660},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are on the main street of the town. There is a clock tower to the north.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"clocktower\",\"directionHint\":{\"@ref\":117},\"id\":{\"@ref\":12},\"label\":\"North\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"townentrance\",\"directionHint\":{\"@ref\":125},\"id\":{\"@ref\":12},\"label\":\"South\",\"visible\":true}]},\"id\":\"mainstreettown\",\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"some\",\"description\":\"This dust looks pretty powerful, for dust.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":{\"@ref\":12},\"midSentenceCasedName\":\"Dust of the Ancients\",\"name\":\"Dust of the Ancients\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true}]},\"x\":300,\"y\":360},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are at the top of the clock tower standing in front of the clock mechanism.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"firstfloorclocktower\",\"directionHint\":{\"@ref\":125},\"id\":{\"@ref\":12},\"label\":\"Down\",\"visible\":true}]},\"id\":\"secondfloorclocktower\",\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"a\",\"description\":\"It has an engraved clock face and both hands. It ticks gently.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"clockmechanismwithfaceandhourhandandminutehand\",\"midSentenceCasedName\":\"clock mechanism\",\"name\":\"Clock mechanism\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":true,\"takeable\":false,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true}]},\"x\":300,\"y\":0},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are standing on the first floor of the clock tower. The wooden floorboards creak and groan as you tread across them.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"secondfloorclocktower\",\"directionHint\":{\"@ref\":117},\"id\":{\"@ref\":12},\"label\":\"Up\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"groundfloorclocktower\",\"directionHint\":{\"@ref\":125},\"id\":{\"@ref\":12},\"label\":\"Down\",\"visible\":true}]},\"id\":\"firstfloorclocktower\",\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":300,\"y\":60},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You stand just inside a dusty mineshaft. It looks like it has been abandoned and not used for many years. Until recently the entrance was blocked by a large rock fall. There are footprints leading down a small dark tunnel and there is a dimly lit annex to the right.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"outsideamineshaft\",\"directionHint\":{\"@ref\":117},\"id\":{\"@ref\":12},\"label\":\"Outside\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"darktunnel1\",\"directionHint\":{\"@ref\":125},\"id\":{\"@ref\":12},\"label\":\"Dark tunnel\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"dimlylitannex\",\"directionHint\":{\"@ref\":211},\"id\":{\"@ref\":12},\"label\":\"Dimly lit annex\",\"visible\":true}]},\"id\":\"mineshaftchamber1\",\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":420,\"y\":540},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You are inside the mighty clock tower. Sound resonates around you as the clock ticks.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"firstfloorclocktower\",\"directionHint\":{\"@ref\":117},\"id\":{\"@ref\":12},\"label\":\"Up\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"clocktower\",\"directionHint\":{\"@ref\":125},\"id\":{\"@ref\":12},\"label\":\"Clock tower door\",\"visible\":true}]},\"id\":\"groundfloorclocktower\",\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\"},\"x\":300,\"y\":120},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"This is the mine smithy, used for emergency tool repair to save the workers from trekking back into town. There are a selection of tools lying around, none of which catch your eye.\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"candlelitchamber\",\"directionHint\":{\"@ref\":117},\"id\":{\"@ref\":12},\"label\":\"Candle lit chamber\",\"visible\":true}]},\"id\":\"minesmithy\",\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@id\":138,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"bluntpickaxe\",\"countableNounPrefix\":\"a\",\"description\":\"This is used to sharpen blunted mining tools.\",\"usedWithText\":\"You sit down at the wheel and place the blunted axe head against the sharpening stone, pumping the pedal to turn the wheel with such vigour that sparks fly across the room. If only you applied yourself with such energy to all your endeavours you might see sparks more often.\",\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.DestroyItemItemAction\",\"itemID\":\"bluntpickaxe\",\"model\":{\"@ref\":1}},\"content\":\"destroy item:bluntpickaxe\",\"item\":{\"@ref\":138},\"logger\":{\"@ref\":86}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemVisibilityItemAction\",\"itemID\":\"pickaxe\",\"model\":{\"@ref\":1},\"visibility\":true},\"content\":\"change item visibility:pickaxe:visible\",\"item\":{\"@ref\":138},\"logger\":{\"@ref\":86}}]},\"id\":\"sharpeningwheel\",\"midSentenceCasedName\":null,\"name\":\"Sharpening wheel\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":true,\"used\":true,\"examineActionIsRepeatable\":true,\"visible\":true}]},\"x\":480,\"y\":720},{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"You stand before a mighty clock tower. The clock goes TICK!\n\",\"exits\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"mainstreettown\",\"directionHint\":{\"@ref\":125},\"id\":{\"@ref\":12},\"label\":\"South\",\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.LocationExit\",\"destination\":\"groundfloorclocktower\",\"directionHint\":{\"@ref\":117},\"id\":\"clocktowerdoor\",\"label\":\"Clock tower door\",\"visible\":true}]},\"id\":\"clocktower\",\"inventory\":{\"@ref\":1},\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@id\":79,\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":\"clocktowerskeletonkey\",\"countableNounPrefix\":\"a\",\"description\":{\"@id\":100,\"value\":\"It is unlocked. It appears to allow access to the clock tower itself.\"},\"usedWithText\":\"You hear a click and the door springs open.\",\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemDescriptionItemAction\",\"description\":{\"@ref\":100},\"item\":{\"@ref\":79}},\"content\":\"change item description:It is unlocked. It appears to allow access to the clock tower itself.\",\"item\":{\"@ref\":79},\"logger\":{\"@ref\":86}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.ChangeItemNameItemAction\",\"item\":{\"@ref\":79},\"name\":{\"@id\":81,\"value\":\"unlocked door\"}},\"content\":\"change item name:unlocked door\",\"item\":{\"@ref\":79},\"logger\":{\"@ref\":86}},{\"@type\":\"com.chewielouie.textadventure.itemaction.LoggableItemAction\",\"action\":{\"@type\":\"com.chewielouie.textadventure.itemaction.MakeExitVisibleItemAction\",\"exitID\":\"clocktowerdoor\",\"model\":{\"@ref\":1}},\"content\":\"make exit visible:clocktowerdoor\",\"item\":{\"@ref\":79},\"logger\":{\"@ref\":86}}]},\"id\":{\"@ref\":12},\"midSentenceCasedName\":null,\"name\":{\"@ref\":81},\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":false,\"useIsRepeatable\":false,\"used\":true,\"examineActionIsRepeatable\":true,\"visible\":true}]},\"x\":300,\"y\":240}]},\"inventoryItems\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"some\",\"description\":\"It's fluffy and shaped like an inverted belly button.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":{\"@ref\":12},\"midSentenceCasedName\":null,\"name\":\"Pocket lint\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":{\"@ref\":12},\"description\":\"It's got a lot of tools on it. You feel like a man of the wilderness when you wield this formidable contraption.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"multitool\",\"midSentenceCasedName\":null,\"name\":\"Multitool\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"a\",\"description\":\"It is a wooden handled spade. It looks like it has seen quite a lot of use and might not be good for much.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"spade\",\"midSentenceCasedName\":null,\"name\":\"Spade\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"a\",\"description\":\"It is rather dirty and has clearly been carved from some poor unfortunates metacarpal.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"clocktowerskeletonkey\",\"midSentenceCasedName\":\"skeleton key\",\"name\":\"Skeleton key\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"a\",\"description\":\"This is a map of the mine tunnels - useful in case you get lost.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"minemap\",\"midSentenceCasedName\":null,\"name\":\"Map\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true},{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"canBeUsedWithTargetID\":{\"@ref\":12},\"countableNounPrefix\":\"a\",\"description\":\"This is a typical miners pick axe and has been recently sharpened.\",\"usedWithText\":{\"@ref\":12},\"examineText\":{\"@ref\":12},\"onUseActions\":{\"@type\":\"java.util.ArrayList\"},\"id\":\"pickaxe\",\"midSentenceCasedName\":null,\"name\":\"Pick axe\",\"onExamineActions\":{\"@type\":\"java.util.ArrayList\"},\"examined\":false,\"takeable\":true,\"useIsRepeatable\":true,\"used\":false,\"examineActionIsRepeatable\":true,\"visible\":true}]},\"currentScore\":2,\"maximumScore\":2}";

    private String modelContent = "INVENTORY ITEM\n" +
"item name:Pocket lint\n" +
"item description:It's fluffy and shaped like an inverted belly button.\n" +
"item countable noun prefix:some\n" +
"item id:pocketlint\n" +
"INVENTORY ITEM\n" +
"item name:Multitool\n" +
"item description:It's got a lot of tools on it. You feel like a man of the wilderness when you wield this formidable contraption.\n" +
"item id:multitool\n" +
"LOCATION AREA\n" +
"location area id:town_area\n" +
"location area name:Town\n" +
"LOCATION AREA\n" +
"location area id:mine_area\n" +
"location area name:Mine\n" +
"LOCATION AREA\n" +
"location area id:church_area\n" +
"location area name:Church\n" +
"LOCATION AREA\n" +
"location area id:friary_area\n" +
"location area name:Friary\n" +
"LOCATION\n" +
"x:300\n" +
"y:480\n" +
"location id:townentrance\n" +
"location area id:town_area\n" +
"location description:You are standing outside a small town. The main street goes north, to the west there are some small out buildings and to the east you can see what looks like an abandoned mine shaft.\n" +
"EXIT\n" +
"exit label:North\n" +
"exit destination:mainstreettown\n" +
"exit direction hint:North\n" +
"exit id:townentrancenorth\n" +
"EXIT\n" +
"exit label:East\n" +
"exit destination:outsideamineshaft\n" +
"exit direction hint:East\n" +
"exit id:townentranceeast\n" +
"EXIT\n" +
"exit label:West\n" +
"exit destination:townoutbuildings\n" +
"exit direction hint:West\n" +
"exit id:townentrancewest\n" +
"ITEM\n" +
"item name:Skeleton key\n" +
"item description:It is rather dirty and has clearly been carved from some poor unfortunates metacarpal.\n" +
"item id:clocktowerskeletonkey\n" +
"item countable noun prefix:a\n" +
"item mid sentence cased name:skeleton key\n" +
"ITEM\n" +
"item name:Banana peel\n" +
"item description:It is yellow and a little moldy.\n" +
"item id:bananapeel\n" +
"item countable noun prefix:some\n" +
"item mid sentence cased name:banana peel\n" +
"LOCATION\n" +
"x:300\n" +
"y:0\n" +
"location id:secondfloorclocktower\n" +
"location area id:town_area\n" +
"location description:You are at the top of the clock tower standing in front of the clock mechanism.\n" +
"EXIT\n" +
"exit label:Down\n" +
"exit destination:firstfloorclocktower\n" +
"exit direction hint:South\n" +
"exit id:secondfloorclocktowerdown\n" +
"ITEM\n" +
"item name:Clock mechanism\n" +
"item description:It is missing a clock face and both hands. Looking at it you decide the face would be best placed upon the mechanism before the hands.\n" +
"item countable noun prefix:a\n" +
"item mid sentence cased name:clock mechanism\n" +
"item id:clockmechanism\n" +
"item is untakeable:\n" +
"item can be used with:clockface\n" +
"item successful use message:You place the clock face onto the front of the mechanism and it clicks gently into place.\n" +
"item use action:change item visibility:clockmechanismwithface:visible\n" +
"item use action:destroy item:clockface\n" +
"item use action:destroy item:clockmechanism\n" +
"ITEM\n" +
"item name:Clock mechanism\n" +
"item description:It has an engraved clock face but is missing its hands. It looks like it needs to have the hour hand fitted first.\n" +
"item countable noun prefix:a\n" +
"item mid sentence cased name:clock mechanism\n" +
"item id:clockmechanismwithface\n" +
"item is untakeable:\n" +
"item visibility:invisible\n" +
"item can be used with:clockhourhand\n" +
"item successful use message:You push the hour hand onto the spindle in the centre of the clock face.\n" +
"item use action:change item visibility:clockmechanismwithfaceandhourhand:visible\n" +
"item use action:destroy item:clockhourhand\n" +
"item use action:destroy item:clockmechanismwithface\n" +
"ITEM\n" +
"item name:Clock mechanism\n" +
"item description:It has an engraved clock face but is missing its minute hand. The hour hand has been expertly placed upon the central spindle.\n" +
"item countable noun prefix:a\n" +
"item mid sentence cased name:clock mechanism\n" +
"item id:clockmechanismwithfaceandhourhand\n" +
"item is untakeable:\n" +
"item visibility:invisible\n" +
"item can be used with:clockminutehand\n" +
"item successful use message:You push the minute hand on top of the hour hand already attached to the spindle in the centre of the clock face. It clips in place with a satisfying click. The mechanism starts to whir and the hands begin to gently jerk with the movement of the mechanism inside. In the distance you hear what sounds like a rock slide and you take a mental note to investigate this further.\n" +
"item use action:make exit visible:mineshaftentrance\n" +
"item use action:change location description:outsideamineshaft:You stand before an old mine shaft. There are some dusty footprints in the dirt around the entrance. A sign states \"McCreedys Mine - Stay out or suffer the consequences!\". To the east is an old ramshackle shed.\n" +
"item use action:increment score:\n" +
"item use action:change item visibility:clockmechanismwithfaceandhourhandandminutehand:visible\n" +
"item use action:destroy item:clockminutehand\n" +
"item use action:destroy item:clockmechanismwithfaceandhourhand\n" +
"ITEM\n" +
"item name:Clock mechanism\n" +
"item description:It has an engraved clock face and both hands. It ticks gently.\n" +
"item countable noun prefix:a\n" +
"item mid sentence cased name:clock mechanism\n" +
"item id:clockmechanismwithfaceandhourhandandminutehand\n" +
"item is untakeable:\n" +
"item visibility:invisible\n" +
"LOCATION\n" +
"x:300\n" +
"y:60\n" +
"location id:firstfloorclocktower\n" +
"location area id:town_area\n" +
"location description:You are standing on the first floor of the clock tower. The wooden floorboards creak and groan as you tread across them.\n" +
"EXIT\n" +
"exit label:Up\n" +
"exit destination:secondfloorclocktower\n" +
"exit direction hint:North\n" +
"exit id:firstfloorclocktowerup\n" +
"EXIT\n" +
"exit label:Down\n" +
"exit destination:groundfloorclocktower\n" +
"exit direction hint:South\n" +
"exit id:firstfloorclocktowerdown\n" +
"LOCATION\n" +
"x:300\n" +
"y:120\n" +
"location id:groundfloorclocktower\n" +
"location area id:town_area\n" +
"location description:You are inside the mighty clock tower. Sound resonates around you as the clock ticks.\n" +
"EXIT\n" +
"exit label:Up\n" +
"exit destination:firstfloorclocktower\n" +
"exit direction hint:North\n" +
"exit id:groundfloorclocktowerup\n" +
"EXIT\n" +
"exit label:Clock tower door\n" +
"exit destination:clocktower\n" +
"exit direction hint:South\n" +
"exit id:groundfloorclocktowerdoor\n" +
"LOCATION\n" +
"x:480\n" +
"y:480\n" +
"location id:smallshed\n" +
"location area id:town_area\n" +
"location description:You are standing inside a small shed. It is dusty and dirty and a variety of mining tools rest against the walls.\n" +
"EXIT\n" +
"exit label:West\n" +
"exit destination:outsideamineshaft\n" +
"exit direction hint:West\n" +
"exit id:smallshedwest\n" +
"ITEM\n" +
"item name:Spade\n" +
"item description:It is a wooden handled spade. It looks like it has seen quite a lot of use and might not be good for much.\n" +
"item countable noun prefix:a\n" +
"item id:spade\n" +
"ITEM\n" +
"item name:Bags of junk\n" +
"item description:They are full of rubbish, there might be something of interest here but it is hard to tell.\n" +
"item countable noun prefix:some\n" +
"item id:bagsofjunk\n" +
"item mid sentence cased name:bags of junk\n" +
"item is untakeable:\n" +
"item examine action is not repeatable:\n" +
"item examine message:You look closer and see something glinting in the darkness. You scramble around in the bag trying to reach it, grasping with your finger tips pulling it out and dropping it at your feet.\n" +
"item on examine action:change item description:They are full of rubbish. Nothing else.\n" +
"item on examine action:change item visibility:clockhourhand:visible\n" +
"ITEM\n" +
"item name:Clock hour hand\n" +
"item description:It is the hour hand from a clock.\n" +
"item countable noun prefix:a\n" +
"item id:clockhourhand\n" +
"item visibility:invisible\n" +
"item mid sentence cased name:clock hour hand\n" +
"\n" +
"LOCATION\n" +
"x:300\n" +
"y:240\n" +
"location id:clocktower\n" +
"location area id:town_area\n" +
"location description:You stand before a mighty clock tower. The clock goes TICK!\n" +
"EXIT\n" +
"exit label:South\n" +
"exit destination:mainstreettown\n" +
"exit direction hint:South\n" +
"exit id:clocktowersouth\n" +
"EXIT\n" +
"exit label:Clock tower door\n" +
"exit destination:groundfloorclocktower\n" +
"exit direction hint:North\n" +
"exit is not visible:\n" +
"exit id:clocktowerdoor\n" +
"EXIT\n" +
"exit label:West\n" +
"exit destination:cobbledroad\n" +
"exit direction hint:West\n" +
"exit is not visible:\n" +
"exit id:clocktower_to_cobbledroad\n" +
"EXIT\n" +
"exit label:East\n" +
"exit destination:nobles_quarter_west_street\n" +
"exit direction hint:East\n" +
"exit is not visible:\n" +
"exit id:clocktower_to_nobles_quarter_west_street\n" +
"ITEM\n" +
"item name:Clock minute hand\n" +
"item description:It is quite heavy, ornate and made of iron.\n" +
"item id:clockminutehand\n" +
"item countable noun prefix:a\n" +
"item mid sentence cased name:clock minute hand\n" +
"ITEM\n" +
"item name:locked door\n" +
"item description:It is locked and looks impenetrable. It appears to allow access to the clock tower itself.\n" +
"item id:lockeddoor\n" +
"item countable noun prefix:a\n" +
"item is untakeable:\n" +
"item can be used with:clocktowerskeletonkey\n" +
"item successful use message:You hear a click and the door springs open.\n" +
"item use is not repeatable:\n" +
"item use action:change item description:It is unlocked. It appears to allow access to the clock tower itself.\n" +
"item use action:change item name:unlocked door\n" +
"item use action:make exit visible:clocktowerdoor\n" +
"LOCATION\n" +
"x:300\n" +
"y:360\n" +
"location id:mainstreettown\n" +
"location area id:town_area\n" +
"location description:You are on the main street of the town. There is a clock tower to the north.\n" +
"EXIT\n" +
"exit label:North\n" +
"exit destination:clocktower\n" +
"exit direction hint:North\n" +
"exit id:mainstreettownnorth\n" +
"EXIT\n" +
"exit label:South\n" +
"exit destination:townentrance\n" +
"exit direction hint:South\n" +
"exit id:mainstreettownsouth\n" +
"EXIT\n" +
"exit label:Merchants Lane\n" +
"exit destination:merchantslane\n" +
"exit direction hint:West\n" +
"exit id:merchantslaneentrance\n" +
"exit is not visible:\n" +
"EXIT\n" +
"exit label:East\n" +
"exit destination:eastofmainstreet\n" +
"exit direction hint:East\n" +
"exit id:mainstreeteast\n" +
"exit is not visible:\n" +
"ITEM\n" +
"item name:Dust of the Ancients\n" +
"item description:This dust looks pretty powerful, for dust.\n" +
"item id:dustoftheancients\n" +
"item countable noun prefix:some\n" +
"item mid sentence cased name:Dust of the Ancients\n" +
"LOCATION\n" +
"x:180\n" +
"y:480\n" +
"location id:townoutbuildings\n" +
"location area id:town_area\n" +
"location description:There are a number of small shack like structures here, fallen down and decrepit they haven't been used for years.\n" +
"EXIT\n" +
"exit label:East\n" +
"exit destination:townentrance\n" +
"exit direction hint:East\n" +
"exit id:townoutbuildingseast\n" +
"ITEM\n" +
"item name:mound of earth\n" +
"item description:It has recently been disturbed.\n" +
"item id:moundofearth\n" +
"item countable noun prefix:a\n" +
"item is untakeable:\n" +
"item can be used with:spade\n" +
"item successful use message:You dig deep into the dirt, sweat dripping from your brow in the dry heat. Clank! The spade strikes something metallic. You brush the earth away to reveal an old clock face, without a minute or an hour hand. It looks sad.\n" +
"item use action:destroy item:moundofearth\n" +
"item use action:change item visibility:clockface:visible\n" +
"ITEM\n" +
"item name:Clock face\n" +
"item description:It is a disembodied clock face, without hands.\n" +
"item id:clockface\n" +
"item countable noun prefix:a\n" +
"item visibility:invisible\n" +
"item mid sentence cased name:clock face\n" +
"LOCATION\n" +
"x:420\n" +
"y:480\n" +
"location id:outsideamineshaft\n" +
"location area id:town_area\n" +
"location description:You stand before an old blocked up mine shaft, there are some dusty footprints in the dirt around the entrance however there is no obvious way in. A sign states \"McCreedys Mine - Stay out or suffer the consequences!\". To the east is an old ramshackle shed.\n" +
"EXIT\n" +
"exit label:West\n" +
"exit destination:townentrance\n" +
"exit direction hint:West\n" +
"exit id:outsideamineshaftwest\n" +
"EXIT\n" +
"exit label:Ramshackle Shed\n" +
"exit destination:smallshed\n" +
"exit direction hint:East\n" +
"exit id:outsideamineshafteast\n" +
"EXIT\n" +
"exit label:Mineshaft entrance\n" +
"exit destination:mineshaftchamber1\n" +
"exit direction hint:South\n" +
"exit id:mineshaftentrance\n" +
"exit is not visible:\n" +
"LOCATION\n" +
"x:420\n" +
"y:540\n" +
"location id:mineshaftchamber1\n" +
"location area id:mine_area\n" +
"location description:You stand just inside a dusty mineshaft. It looks like it has been abandoned and not used for many years. Until recently the entrance was blocked by a large rock fall. There are footprints leading down a small dark tunnel and there is a dimly lit annex to the right.\n" +
"EXIT\n" +
"exit label:Outside\n" +
"exit destination:outsideamineshaft\n" +
"exit direction hint:North\n" +
"exit id:mineshaftchamber1outside\n" +
"EXIT\n" +
"exit label:Dark tunnel\n" +
"exit destination:darktunnel1\n" +
"exit direction hint:South\n" +
"exit id:mineshaftchamber1darktunnel\n" +
"EXIT\n" +
"exit label:Dimly lit annex\n" +
"exit destination:dimlylitannex\n" +
"exit direction hint:West\n" +
"exit id:mineshaftchamber1annex\n" +
"LOCATION\n" +
"x:420\n" +
"y:600\n" +
"location id:darktunnel1\n" +
"location area id:mine_area\n" +
"location description:You are in a dark tunnel with a trail of scuffed footprints leading deeper into the mine. You think you can make out two sets of prints in the dirt. The mine entrance lies to the north and the tunnel continues south.\n" +
"EXIT\n" +
"exit label:North\n" +
"exit destination:mineshaftchamber1\n" +
"exit direction hint:North\n" +
"exit id:darktunnel1north\n" +
"EXIT\n" +
"exit label:South\n" +
"exit destination:darktunnel2\n" +
"exit direction hint:South\n" +
"exit id:darktunnel1south\n" +
"LOCATION\n" +
"x:420\n" +
"y:660\n" +
"location id:darktunnel2\n" +
"location area id:mine_area\n" +
"location description:The footprints continue further south where you can see a dim light burning. The air down here is dank and you feel a little claustrophobic. Off to the east is a candle lit chamber.\n" +
"EXIT\n" +
"exit label:North\n" +
"exit destination:darktunnel1\n" +
"exit direction hint:North\n" +
"exit id:darktunnel2north\n" +
"EXIT\n" +
"exit label:South\n" +
"exit destination:smallminechamber\n" +
"exit direction hint:South\n" +
"exit id:darktunnel2south\n" +
"EXIT\n" +
"exit label:Candle lit chamber\n" +
"exit destination:candlelitchamber\n" +
"exit direction hint:East\n" +
"exit id:darktunnel2candlelitchamber\n" +
"LOCATION\n" +
"x:420\n" +
"y:720\n" +
"location id:smallminechamber\n" +
"location area id:mine_area\n" +
"location description:You enter the chamber and although it is lit by a candle burning gently in an alcove you strain your eyes to peer into the shadows. Out shuffles a man, bedraggled and somber, head down muttering to himself. You notice he has a manacle around his ankle, chained to a nearby rock. Approaching him you say softly 'Hello? Who are you? What's happened here?'. The man replies 'I was taken, I don't know who it was. They hit me from behind and I woke up here, like this.' gesturing to the chain at his feet. 'Help me, please.' splutters the man as he gasps for breath. He is clearly suffering from his circumstances and you feel inclined to find a way to help him out of his predicament.\n" +
"EXIT\n" +
"exit label:North\n" +
"exit destination:darktunnel2\n" +
"exit direction hint:North\n" +
"exit id:smallminechambernorth\n" +
"ITEM\n" +
"item name:Imprisoned man\n" +
"item description:He is dirty and looks exhausted. Chained to a nearby rock you see no easy way to free him.\n" +
"item id:imprisonedman\n" +
"item countable noun prefix:an\n" +
"item is untakeable:\n" +
"item can be used with:multitool\n" +
"item successful use message:The man cowers away from you and you feel foolish for threatening the poor man with your impressive multitool.\n" +
"ITEM\n" +
"item name:Rock\n" +
"item description:It looks to be softer than the surrounding walls, particularly where the chain link has been pinned to it. You wonder if there might be a way to lever or pick the pin out from the rock.\n" +
"item id:rock\n" +
"item countable noun prefix:a\n" +
"item is untakeable:\n" +
"item use is not repeatable:\n" +
"item can be used with:pickaxe\n" +
"item successful use message:You swing the pick axe high above your head and bring it hard down upon the rock, pin and chain. The links shatter into pieces and scatter across the floor, freeing the man who jumps to his feet with joy. 'Oh thank you good sir - I can return to my shop at last, if there's anything left to return to.' he says.\n" +
"item use action:change item description:It looks to be softer than the surrounding walls. There is a hole where a chain pin used to be embedded in the rock.\n" +
"item use action:change location description:smallminechamber:You enter the chamber and although it is lit by a candle burning gently in an alcove you strain your eyes to peer into the shadows. This room looks lived in, of a sort. You notice shattered chain links on the floor.\n" +
"item use action:change item visibility:imprisonedman:invisible\n" +
"item use action:change item visibility:shopkeeper:visible\n" +
"ITEM\n" +
"item name:Shop keeper\n" +
"item description:He is dirty and looks exhausted but has the light of hope in his eyes now that he is free. He also looks a little lost.\n" +
"item id:shopkeeper\n" +
"item countable noun prefix:a\n" +
"item is untakeable:\n" +
"item visibility:invisible\n" +
"item use is not repeatable:\n" +
"item can be used with:minemap\n" +
"item successful use message:You hand the map to the shop keeper who smiles and nods at you with gratitude. 'Thank you, you have been very kind to me. Come by my shop in town anytime - I'm sure I can arrange a favourable discount on anything that takes your eye.'. With that the man walks out leaving you alone.\n" +
"item use action:change item visibility:shopkeeper:invisible\n" +
"item use action:destroy item:minemap\n" +
"item use action:increment score:\n" +
"item use action:make exit visible:merchantslaneentrance\n" +
"ITEM\n" +
"item name:Glowing seam\n" +
"item description:A seam runs through the wall of this cavern that glows with an eerie light. It looks soft enough to mine if you have the right tools.\n" +
"item id:arc_stone_seam_1\n" +
"item countable noun prefix:a\n" +
"item visibility:invisible\n" +
"item is untakeable:\n" +
"item can be used with:enchanted_pickaxe\n" +
"item successful use message:You hoist the enchanted pick axe above your head. The seam appears to react to the pick axe as soon as you bring it near. You strike once and a deep rumble resounds about you however none of the rock is chipped away. Perhaps you need to stop tickling it and hit it harder?\n" +
"item use action:change item visibility:arc_stone_seam_1:invisible\n" +
"item use action:change item visibility:arc_stone_seam_2:visible\n" +
"ITEM\n" +
"item name:Glowing seam\n" +
"item description:A seam runs through the wall of this cavern that glows with an eerie light. It looks soft enough to mine if you have the right tools.\n" +
"item id:arc_stone_seam_2\n" +
"item countable noun prefix:a\n" +
"item visibility:invisible\n" +
"item is untakeable:\n" +
"item can be used with:enchanted_pickaxe\n" +
"item successful use message:The seam is definitely reactive, you raise the pick axe and bring it firmly down on the rock. A small fleck of rock chips away to reveal a larger stone embedded in the seam however it will take some more work to remove it.\n" +
"item use action:change item visibility:arc_stone_seam_2:invisible\n" +
"item use action:change item visibility:arc_stone_seam_3:visible\n" +
"ITEM\n" +
"item name:Glowing seam\n" +
"item description:A seam runs through the wall of this cavern that glows with an eerie light. It looks soft enough to mine if you have the right tools.\n" +
"item id:arc_stone_seam_3\n" +
"item countable noun prefix:a\n" +
"item visibility:invisible\n" +
"item is untakeable:\n" +
"item can be used with:enchanted_pickaxe\n" +
"item successful use message:You resolve to strike hard this time and raise the axe in both hands bringing it confidently down upon the seam. The rock cracks and the embedded stone is freed from the surrounding rock popping like a cracked kernel of corn. The axe shatters with the force of the blow and the remaining pieces disintegrate from the loss of the magical forces that had previously enchanted them.\n" +
"item use action:change item visibility:arc_stone_seam_3:invisible\n" +
"item use action:destroy item:enchanted_pickaxe\n" +
"item use action:change item visibility:arc_stone:visible\n" +
"item use action:increment score:\n" +
"ITEM\n" +
"item name:Arc stone\n" +
"item description:This stone is roughly the size of your fist and glows a sickly green. You sense there is great power within but you do not think you have the power to unlock it.\n" +
"item id:arc_stone\n" +
"item countable noun prefix:an\n" +
"item visibility:invisible\n" +
"\n" +
"LOCATION\n" +
"x:480\n" +
"y:600\n" +
"location id:dimlylitannex\n" +
"location area id:mine_area\n" +
"location description:This annex is carved out of the rock face with several indentations holding a variety of items including bones, a burnt out candle, some old torn paper and a metal ring.\n" +
"EXIT\n" +
"exit label:East\n" +
"exit destination:mineshaftchamber1\n" +
"exit direction hint:East\n" +
"exit id:dimlylitannexeast\n" +
"EXIT\n" +
"exit label:Even smaller annex\n" +
"exit destination:evensmallerannex\n" +
"exit direction hint:South\n" +
"exit id:dimlylitannexevensmallerannex\n" +
"ITEM\n" +
"item name:Chunk of metal\n" +
"item description:This is a sizeable chunk of metal, pointy at two ends with a notch in the center. It looks like it could be fitted onto something but it is wedged in a crack in the wall.\n" +
"item id:axehead\n" +
"item countable noun prefix:a\n" +
"item is untakeable:\n" +
"item examine action is not repeatable:\n" +
"item examine message:Upon further consideration you see it is shaped like an axe head. No wait, it is an axe head.\n" +
"item on examine action:change item description:This is quite clearly an axe head. You are however missing a means to wield it and it is wedged in a crack in the wall.\n" +
"item on examine action:change item name:Axe head\n" +
"item can be used with:woodenpole\n" +
"item successful use message:The wooden pole slots perfectly into the axe head allowing you to apply your weight to the end of the pole causing the axe to pop out of the crack in the wall like a lollipop from a babies mouth. It is quite a hefty mining implement and is well weighted. You feel you could move a substantial amount of rock with this if it were but a touch sharper.\n" +
"item use action:change item visibility:bluntpickaxe:visible\n" +
"item use action:destroy item:axehead\n" +
"item use action:destroy item:woodenpole\n" +
"ITEM\n" +
"item name:Blunt pick axe\n" +
"item description:It looks rather blunt. You can probably bludgeon some small rocks into shards with it given a few hours and some angry words but not much else.\n" +
"item id:bluntpickaxe\n" +
"item countable noun prefix:a\n" +
"item visibility:invisible\n" +
"LOCATION\n" +
"x:480\n" +
"y:660\n" +
"location id:evensmallerannex\n" +
"location area id:mine_area\n" +
"location description:This is an even smaller annex attached to a larger better lit one. There is straw on the floor and it looks like someone has been sleeping here.\n" +
"EXIT\n" +
"exit label:Dimly lit annex\n" +
"exit destination:dimlylitannex\n" +
"exit direction hint:North\n" +
"exit id:evensmallerannexdimlylitannex\n" +
"ITEM\n" +
"item name:Pile of straw\n" +
"item description:It still has the indentation of a sleeping body although the body does not appear to still be here.\n" +
"item id:pileofstraw\n" +
"item countable noun prefix:a\n" +
"item is untakeable:\n" +
"item examine action is not repeatable:\n" +
"item examine message:You delve deeply into the pile of straw and unearth a variety of delightful things, mostly ticks and beetles, as well as some unspeakable left overs. At the very bottom of the pile you find a wooden pole. You wish the owner was around so you could beat them with it for having left their bedroom in such a disgraceful state.\n" +
"item on examine action:change item visibility:woodenpole:visible\n" +
"ITEM\n" +
"item name:Wooden pole\n" +
"item description:It looks like it was attached to something at one end. Perhaps it was a tool of some sort.\n" +
"item id:woodenpole\n" +
"item countable noun prefix:a\n" +
"item visibility:invisible\n" +
"LOCATION\n" +
"x:480\n" +
"y:660\n" +
"location id:candlelitchamber\n" +
"location area id:mine_area\n" +
"location description:You stand in a circular chamber with candles lining the walls in metal brakets. There is a table in the centre of the room and an exit leading deeper into the mine to what looks like a smithy. To the west a tunnel meanders back through the darkness.\n" +
"EXIT\n" +
"exit label:Dark tunnel\n" +
"exit destination:darktunnel2\n" +
"exit direction hint:West\n" +
"exit id:candlelitchamberdarktunnel\n" +
"EXIT\n" +
"exit label:Smithy\n" +
"exit destination:minesmithy\n" +
"exit direction hint:South\n" +
"exit id:candlelitchambersmithy\n" +
"ITEM\n" +
"item name:Table\n" +
"item description:It is covered with papers.\n" +
"item id:table\n" +
"item countable noun prefix:a\n" +
"item is untakeable:\n" +
"item examine action is not repeatable:\n" +
"item examine message:You're not sure how anyone found anything in this mess but a particular sheet of paper catches your eye. As you reach to pick it up it falls to the ground.\n" +
"item on examine action:change item visibility:minemap:visible\n" +
"item on examine action:change item visibility:tornpaper_mine:visible\n" +
"item on examine action:change item description:It is covered with papers. Boring, uninteresting papers.\n" +
"ITEM\n" +
"item name:Map\n" +
"item description:This is a map of the mine tunnels - useful in case you get lost.\n" +
"item id:minemap\n" +
"item countable noun prefix:a\n" +
"item visibility:invisible\n" +
"ITEM\n" +
"item name:Torn paper\n" +
"item description:The paper was hidden amongst all the others on the desk and is ripped in several places. There are stains all over it and you don't feel like putting something this filthy in your pocket. There is smudged writing on it but you think you can make out a word - \"bendlebox\". You wonder what it means.\n" +
"item id:tornpaper_mine\n" +
"item countable noun prefix:some\n" +
"item is untakeable:\n" +
"item visibility:invisible\n" +
"\n" +
"LOCATION\n" +
"x:480\n" +
"y:720\n" +
"location id:minesmithy\n" +
"location area id:mine_area\n" +
"location description:This is the mine smithy, used for emergency tool repair to save the workers from trekking back into town. There are a selection of tools lying around, none of which catch your eye.\n" +
"EXIT\n" +
"exit label:Candle lit chamber\n" +
"exit destination:candlelitchamber\n" +
"exit direction hint:North\n" +
"exit id:minesmithycandlelitchamber\n" +
"ITEM\n" +
"item name:Sharpening wheel\n" +
"item description:This is used to sharpen blunted mining tools.\n" +
"item id:sharpeningwheel\n" +
"item countable noun prefix:a\n" +
"item is untakeable:\n" +
"item can be used with:bluntpickaxe\n" +
"item successful use message:You sit down at the wheel and place the blunted axe head against the sharpening stone, pumping the pedal to turn the wheel with such vigour that sparks fly across the room. If only you applied yourself with such energy to all your endeavours you might see sparks more often.\n" +
"item use action:destroy item:bluntpickaxe\n" +
"item use action:change item visibility:pickaxe:visible\n" +
"ITEM\n" +
"item name:Pick axe\n" +
"item description:This is a typical miners pick axe and has been recently sharpened.\n" +
"item id:pickaxe\n" +
"item countable noun prefix:a\n" +
"item visibility:invisible\n" +
"";
}
