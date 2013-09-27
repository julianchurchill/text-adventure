package com.chewielouie.textadventure.action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.item.Item;

@RunWith(JMock.class)
public class ExamineTests {

    private Mockery mockery = new Mockery();

    @Test
    public void name_is_correct() {
        assertEquals( "examine", new Examine( null ).name() );
    }

    @Test
    public void user_text_contains_item_description() {
        final Item item = mockery.mock( Item.class );
        Examine action = new Examine( item );
        mockery.checking( new Expectations() {{
            allowing( item ).description();
            will( returnValue( "description" ) );
            allowing( item ).midSentenceCasedName();
            will( returnValue( "name" ) );
            ignoring( item );
        }});

        action.trigger();
        assertEquals( "You examine the name. description", action.userText() );
    }

    @Test
    public void user_text_available_is_true() {
        Examine action = new Examine( null );

        assertTrue( action.userTextAvailable() );
    }

    @Test
    public void label_is_composed_from_item_name() {
        final Item item = mockery.mock( Item.class );
        Examine action = new Examine( item );
        mockery.checking( new Expectations() {{
            allowing( item ).midSentenceCasedName();
            will( returnValue( "item name" ) );
            ignoring( item );
        }});

        assertEquals( "Examine item name", action.label() );
    }

    @Test
    public void user_does_not_have_to_choose_a_follow_up_action() {
        Examine action = new Examine( null );

        assertFalse( action.userMustChooseFollowUpAction() );
    }

    @Test
    public void there_are_no_follow_up_actions() {
        Examine action = new Examine( null );

        assertEquals( 0, action.followUpActions().size() );
    }

    @Test
    public void on_trigger_item_is_notified() {
        final Item item = mockery.mock( Item.class );
        Examine action = new Examine( item );
        mockery.checking( new Expectations() {{
            oneOf( item ).examine();
            ignoring( item );
        }});

        action.trigger();
    }

    @Test
    public void user_text_includes_examine_text_from_item() {
        final Item item = mockery.mock( Item.class );
        Examine action = new Examine( item );
        mockery.checking( new Expectations() {{
            allowing( item ).examineText();
            will( returnValue( "examine text" ) );
            ignoring( item );
        }});

        action.trigger();

        assertTrue( action.userText().endsWith( "\n\nexamine text" ) );
    }

    @Test
    public void on_examine_triggers_are_run_after_saving_name_and_description() {
        final Item item = mockery.mock( Item.class );
        Examine action = new Examine( item );
        final Sequence actions_after_description =
            mockery.sequence( "actions_after_description" );
        mockery.checking( new Expectations() {{
            oneOf( item ).midSentenceCasedName();
            inSequence( actions_after_description );
            oneOf( item ).description();
            inSequence( actions_after_description );
            oneOf( item ).examine();
            inSequence( actions_after_description );
            ignoring( item );
        }});

        action.trigger();
    }

    @Test
    public void examine_text_does_not_include_the_for_proper_nouns() {
        Item item = mock( Item.class );
        when( item.midSentenceCasedName() ).thenReturn( "Elvis" );
        when( item.properNoun() ).thenReturn( true );
        Examine action = new Examine( item );

        action.trigger();

        assertThat( action.userText(), containsString( "examine Elvis." ) );
    }
}


