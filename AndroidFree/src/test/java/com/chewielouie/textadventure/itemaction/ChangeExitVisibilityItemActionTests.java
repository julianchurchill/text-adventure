package com.chewielouie.textadventure.itemaction;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.TextAdventureModel;

@RunWith(JMock.class)
public class ChangeExitVisibilityItemActionTests {

    private Mockery mockery = new Mockery();

    @Test
    public void enact_finds_exit_in_the_model() {
        final TextAdventureModel model =
            mockery.mock( TextAdventureModel.class );
        ChangeExitVisibilityItemAction action =
            new ChangeExitVisibilityItemAction( "exitid:visible", model );
        final Exit exit = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            oneOf( model ).findExitByID( "exitid" );
            will( returnValue( exit ) );
            ignoring( model );
            ignoring( exit );
        }});

        action.enact();
    }

    @Test
    public void enact_makes_exit_visible() {
        final TextAdventureModel model =
            mockery.mock( TextAdventureModel.class );
        ChangeExitVisibilityItemAction action =
            new ChangeExitVisibilityItemAction( "exitid:visible", model );
        final Exit exit = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            allowing( model ).findExitByID( "exitid" );
            will( returnValue( exit ) );
            ignoring( model );
            oneOf( exit ).setVisible();
            ignoring( exit );
        }});

        action.enact();
    }

    @Test
    public void enact_makes_exit_invisible() {
        final TextAdventureModel model =
            mockery.mock( TextAdventureModel.class );
        ChangeExitVisibilityItemAction action =
            new ChangeExitVisibilityItemAction( "exitid:invisible", model );
        final Exit exit = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            allowing( model ).findExitByID( "exitid" );
            will( returnValue( exit ) );
            ignoring( model );
            oneOf( exit ).setInvisible();
            ignoring( exit );
        }});

        action.enact();
    }

    @Test
    public void enact_makes_exit_visible_by_default() {
        final TextAdventureModel model =
            mockery.mock( TextAdventureModel.class );
        ChangeExitVisibilityItemAction action =
            new ChangeExitVisibilityItemAction( "exitid:unknown", model );
        final Exit exit = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            allowing( model ).findExitByID( "exitid" );
            will( returnValue( exit ) );
            ignoring( model );
            oneOf( exit ).setVisible();
            ignoring( exit );
        }});

        action.enact();
    }

    @Test
    public void arguments_include_itemID() {
        final TextAdventureModel model =
            mockery.mock( TextAdventureModel.class );
        ChangeExitVisibilityItemAction action =
            new ChangeExitVisibilityItemAction( "exitid:visible", model );
        mockery.checking( new Expectations() {{
            ignoring( model );
        }});

        assertEquals( "exitid", action.arguments().get( 0 ) );
    }

    @Test
    public void arguments_include_visibility_for_visible() {
        final TextAdventureModel model =
            mockery.mock( TextAdventureModel.class );
        ChangeExitVisibilityItemAction action =
            new ChangeExitVisibilityItemAction( "exitid:visible", model );
        mockery.checking( new Expectations() {{
            ignoring( model );
        }});

        assertEquals( "visible", action.arguments().get( 1 ) );
    }

    @Test
    public void arguments_include_visibility_for_invisible() {
        final TextAdventureModel model =
            mockery.mock( TextAdventureModel.class );
        ChangeExitVisibilityItemAction action =
            new ChangeExitVisibilityItemAction( "exitid:invisible", model );
        mockery.checking( new Expectations() {{
            ignoring( model );
        }});

        assertEquals( "invisible", action.arguments().get( 1 ) );
    }
}
