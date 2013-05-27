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
        TextAdventureActivity realAndroidContext = new TextAdventureActivity();
        writeFile( "test_JSON_model", tinyJSONModelContent, realAndroidContext );
        BasicModelConverter modelConverter = mock( BasicModelConverter.class );
        JSONToActionListConverter converter =
            new JSONToActionListConverter( realAndroidContext, "test_JSON_model",
                                           modelConverter );

        converter.actions();

        verify( modelConverter ).inferActionsFrom(
                                        Mockito.any( TextAdventureModel.class ) );
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

    private final String tinyJSONModelContent = "{\"@id\":1,\"@type\":\"com.chewielouie.textadventure.BasicModel\"}";
}

