package com.chewielouie.textadventure_common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import com.chewielouie.textadventure.BasicModel;
import com.chewielouie.textadventure.TextAdventureModel;
import org.mockito.Mockito;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.Robolectric;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;

@RunWith(RobolectricTestRunner.class)
public class JSONToActionListConverterTests {
    @Test
    public void convert_non_existent_JSON_file_returns_null() throws FileNotFoundException {
        Context context = mock( Context.class );
        when( context.openFileInput( "any old file" ) ).thenThrow(
                FileNotFoundException.class );
        JSONToActionListConverter converter =
            new JSONToActionListConverter( context, "any old file", null );

        assertThat( converter.actions(), is( nullValue() ) );
    }

    @Test
    public void passes_loaded_JSON_model_to_BasicModelConverter() throws FileNotFoundException {
        DummyActivity realAndroidContext = new DummyActivity();
        writeFile( "test_JSON_model", tinyJSONModelContent, realAndroidContext );
        BasicModelConverter modelConverter = mock( BasicModelConverter.class );
        JSONToActionListConverter converter =
            new JSONToActionListConverter( realAndroidContext, "test_JSON_model",
                                           modelConverter );

        converter.actions();

        verify( modelConverter ).inferActionsFrom(
                                        Mockito.any( TextAdventureModel.class ) );
    }

    private final String tinyJSONModelContent = "{\"@id\":1,\"@type\":\"com.chewielouie.textadventure.BasicModel\"}";

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

    private final String tinyJSONModelContent_withUnlockedDoor = "{\"@id\":1,\"@type\":\"com.chewielouie.textadventure.BasicModel\",\"locations\":{\"@id\":28,\"@type\":\"java.util.HashMap\",\"@keys\":[\"smallminechamber\"],\"@items\":[{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"location description\",\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"name\":\"unlocked door\"}]}}]}}";

    @Test
    public void adds_locked_door_item_id_to_model_when_item_named_unlocked_door() throws FileNotFoundException {
        DummyActivity realAndroidContext = new DummyActivity();
        writeFile( "test_JSON_model", tinyJSONModelContent_withUnlockedDoor,
                   realAndroidContext );
        BasicModelConverter modelConverter = mock( BasicModelConverter.class );
        JSONToActionListConverter converter =
            new JSONToActionListConverter( realAndroidContext, "test_JSON_model",
                                           modelConverter );

        converter.actions();

        BasicModel model = converter.model();
        assertThat( model.findItemByID( "lockeddoor" ), is( notNullValue() ) );
    }

    private final String tinyJSONModelContent_withLockedDoor = "{\"@id\":1,\"@type\":\"com.chewielouie.textadventure.BasicModel\",\"locations\":{\"@id\":28,\"@type\":\"java.util.HashMap\",\"@keys\":[\"smallminechamber\"],\"@items\":[{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"location description\",\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"name\":\"locked door\"}]}}]}}";

    @Test
    public void adds_locked_door_item_id_to_model_when_item_named_locked_door() throws FileNotFoundException {
        DummyActivity realAndroidContext = new DummyActivity();
        writeFile( "test_JSON_model", tinyJSONModelContent_withLockedDoor,
                   realAndroidContext );
        BasicModelConverter modelConverter = mock( BasicModelConverter.class );
        JSONToActionListConverter converter =
            new JSONToActionListConverter( realAndroidContext, "test_JSON_model",
                                           modelConverter );

        converter.actions();

        BasicModel model = converter.model();
        assertThat( model.findItemByID( "lockeddoor" ), is( notNullValue() ) );
    }

    private final String tinyJSONModelContent_withPocketLint = "{\"@id\":1,\"@type\":\"com.chewielouie.textadventure.BasicModel\",\"inventory\":{\"@ref\":1},\"inventoryItems\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"name\":\"Pocket lint\"}]}}";

    @Test
    public void adds_pocket_lint_item_id_to_model() throws FileNotFoundException {
        DummyActivity realAndroidContext = new DummyActivity();
        writeFile( "test_JSON_model", tinyJSONModelContent_withPocketLint,
                   realAndroidContext );
        BasicModelConverter modelConverter = mock( BasicModelConverter.class );
        JSONToActionListConverter converter =
            new JSONToActionListConverter( realAndroidContext, "test_JSON_model",
                                           modelConverter );

        converter.actions();

        BasicModel model = converter.model();
        assertThat( model.findItemByID( "pocketlint" ), is( notNullValue() ) );
    }

    private final String tinyJSONModelContent_withBananaPeelInInventory = "{\"@id\":1,\"@type\":\"com.chewielouie.textadventure.BasicModel\",\"inventory\":{\"@ref\":1},\"inventoryItems\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"name\":\"Banana peel\"}]}}";

    @Test
    public void adds_banana_peel_item_id_to_model_when_in_inventory() throws FileNotFoundException {
        DummyActivity realAndroidContext = new DummyActivity();
        writeFile( "test_JSON_model", tinyJSONModelContent_withBananaPeelInInventory,
                   realAndroidContext );
        BasicModelConverter modelConverter = mock( BasicModelConverter.class );
        JSONToActionListConverter converter =
            new JSONToActionListConverter( realAndroidContext, "test_JSON_model",
                                           modelConverter );

        converter.actions();

        BasicModel model = converter.model();
        assertThat( model.findItemByID( "bananapeel" ), is( notNullValue() ) );
    }

    private final String tinyJSONModelContent_withBananaPeelInLocation = "{\"@id\":1,\"@type\":\"com.chewielouie.textadventure.BasicModel\",\"locations\":{\"@id\":28,\"@type\":\"java.util.HashMap\",\"@keys\":[\"smallminechamber\"],\"@items\":[{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"location description\",\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"name\":\"Banana peel\"}]}}]}}";

    @Test
    public void adds_banana_peel_item_id_to_model_when_in_location() throws FileNotFoundException {
        DummyActivity realAndroidContext = new DummyActivity();
        writeFile( "test_JSON_model", tinyJSONModelContent_withBananaPeelInLocation,
                   realAndroidContext );
        BasicModelConverter modelConverter = mock( BasicModelConverter.class );
        JSONToActionListConverter converter =
            new JSONToActionListConverter( realAndroidContext, "test_JSON_model",
                                           modelConverter );

        converter.actions();

        BasicModel model = converter.model();
        assertThat( model.findItemByID( "bananapeel" ), is( notNullValue() ) );
    }

    private final String tinyJSONModelContent_withDustOfTheAncientsInInventory = "{\"@id\":1,\"@type\":\"com.chewielouie.textadventure.BasicModel\",\"inventory\":{\"@ref\":1},\"inventoryItems\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"name\":\"Dust of the Ancients\"}]}}";

    @Test
    public void adds_dust_of_the_ancients_item_id_to_model_when_in_inventory() throws FileNotFoundException {
        DummyActivity realAndroidContext = new DummyActivity();
        writeFile( "test_JSON_model", tinyJSONModelContent_withDustOfTheAncientsInInventory,
                   realAndroidContext );
        BasicModelConverter modelConverter = mock( BasicModelConverter.class );
        JSONToActionListConverter converter =
            new JSONToActionListConverter( realAndroidContext, "test_JSON_model",
                                           modelConverter );

        converter.actions();

        BasicModel model = converter.model();
        assertThat( model.findItemByID( "dustoftheancients" ), is( notNullValue() ) );
    }

    private final String tinyJSONModelContent_withDustOfTheAncientsInLocation = "{\"@id\":1,\"@type\":\"com.chewielouie.textadventure.BasicModel\",\"locations\":{\"@id\":28,\"@type\":\"java.util.HashMap\",\"@keys\":[\"smallminechamber\"],\"@items\":[{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"location description\",\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"name\":\"Dust of the Ancients\"}]}}]}}";

    @Test
    public void adds_dust_of_the_ancients_item_id_to_model_when_in_location() throws FileNotFoundException {
        DummyActivity realAndroidContext = new DummyActivity();
        writeFile( "test_JSON_model", tinyJSONModelContent_withDustOfTheAncientsInLocation,
                   realAndroidContext );
        BasicModelConverter modelConverter = mock( BasicModelConverter.class );
        JSONToActionListConverter converter =
            new JSONToActionListConverter( realAndroidContext, "test_JSON_model",
                                           modelConverter );

        converter.actions();

        BasicModel model = converter.model();
        assertThat( model.findItemByID( "dustoftheancients" ), is( notNullValue() ) );
    }

    private final String tinyJSONModelContent_withBagsOfJunk = "{\"@id\":1,\"@type\":\"com.chewielouie.textadventure.BasicModel\",\"locations\":{\"@id\":28,\"@type\":\"java.util.HashMap\",\"@keys\":[\"smallminechamber\"],\"@items\":[{\"@type\":\"com.chewielouie.textadventure.Location\",\"description\":\"location description\",\"items\":{\"@type\":\"java.util.ArrayList\",\"@items\":[{\"@type\":\"com.chewielouie.textadventure.item.NormalItem\",\"name\":\"Bags of junk\"}]}}]}}";

    @Test
    public void adds_bagss_of_junk_item_id_to_model() throws FileNotFoundException {
        DummyActivity realAndroidContext = new DummyActivity();
        writeFile( "test_JSON_model", tinyJSONModelContent_withBagsOfJunk,
                   realAndroidContext );
        BasicModelConverter modelConverter = mock( BasicModelConverter.class );
        JSONToActionListConverter converter =
            new JSONToActionListConverter( realAndroidContext, "test_JSON_model",
                                           modelConverter );

        converter.actions();

        BasicModel model = converter.model();
        assertThat( model.findItemByID( "bagsofjunk" ), is( notNullValue() ) );
    }
}
