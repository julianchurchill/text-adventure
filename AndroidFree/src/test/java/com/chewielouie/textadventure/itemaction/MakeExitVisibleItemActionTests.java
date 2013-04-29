package com.chewielouie.textadventure.itemaction;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.TextAdventureModel;

@RunWith(JMock.class)
public class MakeExitVisibleItemActionTests {

    private Mockery mockery = new Mockery();

    @Test
    public void enact_makes_named_exit_visible() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        final Exit exit = mockery.mock( Exit.class );
        MakeExitVisibleItemAction action =
            new MakeExitVisibleItemAction( "exit id", model );
        mockery.checking( new Expectations() {{
            oneOf( model ).findExitByID( "exit id" );
            will( returnValue( exit ) );
            ignoring( model );
            oneOf( exit ).setVisible();
            ignoring( exit );
        }});

        action.enact();
    }

    @Test
    public void enact_fails_gracefully_when_it_cant_find_the_exit() {
        final TextAdventureModel model = mockery.mock( TextAdventureModel.class );
        MakeExitVisibleItemAction action =
            new MakeExitVisibleItemAction( "exit id", model );
        mockery.checking( new Expectations() {{
            oneOf( model ).findExitByID( "exit id" );
            will( returnValue( null ) );
            ignoring( model );
        }});

        action.enact();
    }
}

