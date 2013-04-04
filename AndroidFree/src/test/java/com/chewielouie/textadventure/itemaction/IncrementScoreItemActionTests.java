package com.chewielouie.textadventure.itemaction;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.TextAdventureModel;

@RunWith(JMock.class)
public class IncrementScoreItemActionTests {

    private Mockery mockery = new Mockery();

    @Test
    public void enact_increments_score_in_model_by_one() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        IncrementScoreItemAction action = new IncrementScoreItemAction( model );
        mockery.checking( new Expectations() {{
            allowing( model ).currentScore();
            will( returnValue( 46 ) );
            oneOf( model ).setCurrentScore( 47 );
            ignoring( model );
        }});

        action.enact();
    }
}


