package com.chewielouie.textadventure.serialisation;

import static org.junit.Assert.*;

import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.Exit;

@RunWith(JMock.class)
public class PlainTextExitDeserialiserTests {

    private Mockery mockery = new Mockery();

    @Test
    public void deserialise_extracts_exit_label() {
        final Exit exit = mockery.mock( Exit.class );
        PlainTextExitDeserialiser d = new PlainTextExitDeserialiser();
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
        PlainTextExitDeserialiser d = new PlainTextExitDeserialiser();
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
        PlainTextExitDeserialiser d = new PlainTextExitDeserialiser();
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
        PlainTextExitDeserialiser d = new PlainTextExitDeserialiser();
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
        PlainTextExitDeserialiser d = new PlainTextExitDeserialiser();
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
        PlainTextExitDeserialiser d = new PlainTextExitDeserialiser();
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
        PlainTextExitDeserialiser d = new PlainTextExitDeserialiser();
        mockery.checking( new Expectations() {{
            oneOf( exit ).setID( "exit id" );
            ignoring( exit );
        }});

        d.deserialise( exit,
                       "exit label:label\n" +
                       "exit destination:destination\n" +
                       "exit id:exit id" );
    }
}

