package com.chewielouie.textadventure.serialisation;

import static org.hamcrest.MatcherAssert.assertThat;
// import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
// import com.chewielouie.textadventure.Exit;
// import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionHistory;
import com.chewielouie.textadventure.action.ActionParameters;
// import com.chewielouie.textadventure.action.ActionRecord;

public class ActionHistoryDeserialiserTests {

    @Test
    public void clears_history_if_nothing_to_deserialise() {
        ActionHistory history = mock( ActionHistory.class );
        new ActionHistoryDeserialiser( history ).deserialise();

        verify( history ).clear();
        verify( history, never() ).addActionWithParameters( any( Action.class ),
                                                            any( ActionParameters.class ) );
    }
}
