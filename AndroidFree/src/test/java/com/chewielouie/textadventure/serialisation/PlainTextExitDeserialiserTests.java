package com.chewielouie.textadventure.serialisation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.itemaction.ItemAction;
import com.chewielouie.textadventure.itemaction.ItemActionFactory;

@RunWith(JMock.class)
public class PlainTextExitDeserialiserTests {

    private Mockery mockery = new Mockery();

    @Test
    public void deserialise_extracts_exit_label() {
        final Exit exit = mockery.mock( Exit.class );
        PlainTextExitDeserialiser d = new PlainTextExitDeserialiser( null );
        mockery.checking( new Expectations() {{
            oneOf( exit ).setLabel( "label" );
            ignoring( exit );
        }});

        d.deserialise( exit,
                       "exit label:label\n" +
                       "exit destination:destination" );
    }

    @Test
    public void deserialise_extracts_exit_destination() {
        final Exit exit = mockery.mock( Exit.class );
        PlainTextExitDeserialiser d = new PlainTextExitDeserialiser( null );
        mockery.checking( new Expectations() {{
            oneOf( exit ).setDestination( "destination" );
            ignoring( exit );
        }});

        d.deserialise( exit,
                       "exit label:label\n" +
                       "exit destination:destination" );
    }

    @Test
    public void deserialise_extracts_exit_direction_hint() {
        final Exit exit = mockery.mock( Exit.class );
        PlainTextExitDeserialiser d = new PlainTextExitDeserialiser( null );
        mockery.checking( new Expectations() {{
            oneOf( exit ).setDirectionHint( Exit.DirectionHint.East );
            ignoring( exit );
        }});

        d.deserialise( exit,
                       "exit label:label\n" +
                       "exit destination:destination\n" +
                       "exit direction hint:East" );
    }

    @Test
    public void deserialise_exit_direction_hint_defaults_to_dont_care_if_not_specified() {
        final Exit exit = mockery.mock( Exit.class );
        PlainTextExitDeserialiser d = new PlainTextExitDeserialiser( null );
        mockery.checking( new Expectations() {{
            oneOf( exit ).setDirectionHint( Exit.DirectionHint.DontCare );
            ignoring( exit );
        }});

        d.deserialise( exit,
                       "exit label:label\n" +
                       "exit destination:destination\n" );
    }

    @Test
    public void deserialise_exit_is_visible_by_default() {
        final Exit exit = mockery.mock( Exit.class );
        PlainTextExitDeserialiser d = new PlainTextExitDeserialiser( null );
        mockery.checking( new Expectations() {{
            never( exit ).setInvisible();
            ignoring( exit );
        }});

        d.deserialise( exit,
                       "exit label:label\n" +
                       "exit destination:destination\n" );
    }

    @Test
    public void deserialise_extracts_exit_is_not_visible() {
        final Exit exit = mockery.mock( Exit.class );
        PlainTextExitDeserialiser d = new PlainTextExitDeserialiser( null );
        mockery.checking( new Expectations() {{
            oneOf( exit ).setInvisible();
            ignoring( exit );
        }});

        d.deserialise( exit,
                       "exit label:label\n" +
                       "exit destination:destination\n" +
                       "exit is not visible:" );
    }

    @Test
    public void deserialise_extracts_exit_id() {
        final Exit exit = mockery.mock( Exit.class );
        PlainTextExitDeserialiser d = new PlainTextExitDeserialiser( null );
        mockery.checking( new Expectations() {{
            oneOf( exit ).setID( "exit id" );
            ignoring( exit );
        }});

        d.deserialise( exit,
                       "exit label:label\n" +
                       "exit destination:destination\n" +
                       "exit id:exit id" );
    }

    @Test
    public void deserialise_extracts_multiple_exit_on_use_actions() {
        Exit exit = mock( Exit.class );
        ItemActionFactory factory = mock( ItemActionFactory.class );
        ItemAction action1 = mock( ItemAction.class );
        ItemAction action2 = mock( ItemAction.class );
        when( factory.create( "action name1:params1", null ) ).thenReturn( action1 );
        when( factory.create( "action name2:params2", null ) ).thenReturn( action2 );
        PlainTextExitDeserialiser d = new PlainTextExitDeserialiser( factory );

        d.deserialise( exit,
                       "exit label:label\n" +
                       "exit destination:destination\n" +
                       "exit on use action:action name1:params1\n" +
                       "exit on use action:action name2:params2" );

        verify( exit ).addOnUseAction( action1 );
        verify( exit ).addOnUseAction( action2 );
    }
}

