package com.chewielouie.textadventure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.FileNotFoundException;
import android.content.Context;

public class JSONToActionListConverterTests {
    @Test
    public void convert_non_existent_JSON_file_returns_null() throws FileNotFoundException {
        Context context = mock( Context.class );
        when( context.openFileInput( "any old file" ) ).thenThrow(
                FileNotFoundException.class );
        JSONToActionListConverter converter =
            new JSONToActionListConverter( context, "any old file" );

        assertThat( converter.actions(), is( nullValue() ) );
    }
}

