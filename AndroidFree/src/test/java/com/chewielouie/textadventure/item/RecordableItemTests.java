package com.chewielouie.textadventure.item;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
// import com.chewielouie.textadventure.ActionHistory;
import com.chewielouie.textadventure.itemaction.ItemAction;

@RunWith(JMock.class)
public class RecordableItemTests {

    private Mockery mockery = new Mockery();

    @Test
    public void decorator_calls_target_for_description() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).description(); will( returnValue( "original description" ) );
            ignoring( item );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, null );

    	assertThat( decoratedItem.description(), is( "original description" ) );
    }

    @Test
    public void decorator_calls_target_for_set_description() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setDescription( "new description" );
            ignoring( item );
        }});
    	new RecordableItem( item, null ).setDescription( "new description" );
    }

    @Test
    public void decorator_calls_target_for_name() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).name(); will( returnValue( "original name" ) );
            ignoring( item );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, null );

    	assertThat( decoratedItem.name(), is( "original name" ) );
    }

    @Test
    public void decorator_calls_target_for_set_name() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setName( "new name" );
            ignoring( item );
        }});
    	new RecordableItem( item, null ).setName( "new name" );
    }

    @Test
    public void decorator_calls_target_for_id() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).id(); will( returnValue( "original id" ) );
            ignoring( item );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, null );

    	assertThat( decoratedItem.id(), is( "original id" ) );
    }

    @Test
    public void decorator_calls_target_for_set_id() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setId( "new id" );
            ignoring( item );
        }});
    	new RecordableItem( item, null ).setId( "new id" );
    }

    @Test
    public void decorator_calls_target_for_countable_noun_prefix() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).countableNounPrefix(); will( returnValue( "original value" ) );
            ignoring( item );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, null );

    	assertThat( decoratedItem.countableNounPrefix(), is( "original value" ) );
    }

    @Test
    public void decorator_calls_target_for_set_countable_noun_prefix() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setCountableNounPrefix( "new value" );
            ignoring( item );
        }});
    	new RecordableItem( item, null ).setCountableNounPrefix( "new value" );
    }

    @Test
    public void decorator_calls_target_for_mid_sentence_cased_name() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).midSentenceCasedName(); will( returnValue( "original value" ) );
            ignoring( item );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, null );

    	assertThat( decoratedItem.midSentenceCasedName(), is( "original value" ) );
    }

    @Test
    public void decorator_calls_target_for_set_mid_sentence_cased_name() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setMidSentenceCasedName( "new value" );
            ignoring( item );
        }});
    	new RecordableItem( item, null ).setMidSentenceCasedName( "new value" );
    }

    @Test
    public void decorator_calls_target_for_takeable() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).takeable(); will( returnValue( true ) );
            ignoring( item );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, null );

    	assertThat( decoratedItem.takeable(), is( true ) );
    }

    @Test
    public void decorator_calls_target_for_set_untakeable() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setUntakeable();
            ignoring( item );
        }});
    	new RecordableItem( item, null ).setUntakeable();
    }

    @Test
    public void decorator_calls_target_for_can_be_used_with() {
    	final Item item = mockery.mock( Item.class, "item" );
    	final Item otherItem = mockery.mock( Item.class, "other item" );
        mockery.checking( new Expectations() {{
            oneOf( item ).canBeUsedWith( otherItem );
            will( returnValue( true ) );
            ignoring( item );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, null );

    	assertThat( decoratedItem.canBeUsedWith( otherItem ), is( true ) );
    }

    @Test
    public void decorator_calls_target_for_set_can_be_used_with() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setCanBeUsedWith( "item id" );
            ignoring( item );
        }});
    	new RecordableItem( item, null ).setCanBeUsedWith( "item id" );
    }

    @Test
    public void decorator_calls_target_for_can_be_used_with_item_ids() {
    	final Item item = mockery.mock( Item.class );
    	final List<String> itemIDs = mockery.mock( List.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).canBeUsedWithItemIDs();
            will( returnValue( itemIDs ) );
            ignoring( item );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, null );

    	assertThat( decoratedItem.canBeUsedWithItemIDs(), is( itemIDs ) );
    }

    @Test
    public void decorator_calls_target_for_used_with_text() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).usedWithText(); will( returnValue( "text" ) );
            ignoring( item );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, null );

    	assertThat( decoratedItem.usedWithText(), is( "text" ) );
    }

    @Test
    public void decorator_calls_target_for_set_used_with_text() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setUsedWithText( "text" );
            ignoring( item );
        }});
    	new RecordableItem( item, null ).setUsedWithText( "text" );
    }

    @Test
    public void decorator_calls_target_for_set_use_is_not_repeatable() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setUseIsNotRepeatable();
            ignoring( item );
        }});
    	new RecordableItem( item, null ).setUseIsNotRepeatable();
    }

    @Test
    public void decorator_calls_target_for_use_is_not_repeatable() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).useIsNotRepeatable(); will( returnValue( true ) );
            ignoring( item );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, null );

    	assertThat( decoratedItem.useIsNotRepeatable(), is( true ) );
    }

    @Test
    public void decorator_calls_target_for_add_on_use_action() {
    	final Item item = mockery.mock( Item.class );
    	final ItemAction itemAction = mockery.mock( ItemAction.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).addOnUseAction( itemAction );
            ignoring( item );
        }});
    	new RecordableItem( item, null ).addOnUseAction( itemAction );
    }

    @Test
    public void decorator_calls_target_for_use() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).use();
            ignoring( item );
        }});
    	new RecordableItem( item, null ).use();
    }

    @Test
    public void decorator_calls_target_for_actions() {
    	final Item item = mockery.mock( Item.class );
    	final List<ItemAction> actions = mockery.mock( List.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).actions(); will( returnValue( actions ) );
            ignoring( item );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, null );

    	assertThat( decoratedItem.actions(), is( actions ) );
    }

    @Test
    public void decorator_calls_target_for_set_visible() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setVisible( true );
            ignoring( item );
        }});
    	new RecordableItem( item, null ).setVisible( true );
    }

    @Test
    public void decorator_calls_target_for_visible() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).visible(); will( returnValue( true ) );
            ignoring( item );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, null );

    	assertThat( decoratedItem.visible(), is( true ) );
    }

    @Test
    public void decorator_calls_target_for_examine() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).examine();
            ignoring( item );
        }});
    	new RecordableItem( item, null ).examine();
    }

    @Test
    public void decorator_calls_target_for_add_on_examine_action() {
    	final Item item = mockery.mock( Item.class );
    	final ItemAction itemAction = mockery.mock( ItemAction.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).addOnExamineAction( itemAction );
            ignoring( item );
        }});
    	new RecordableItem( item, null ).addOnExamineAction( itemAction );
    }

    @Test
    public void decorator_calls_target_for_examine_text() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).examineText(); will( returnValue( "text" ) );
            ignoring( item );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, null );

    	assertThat( decoratedItem.examineText(), is( "text" ) );
    }

    @Test
    public void decorator_calls_target_for_set_examine_text() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setExamineText( "text" );
            ignoring( item );
        }});
    	new RecordableItem( item, null ).setExamineText( "text" );
    }

    @Test
    public void decorator_calls_target_for_set_examine_action_is_not_repeatable() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setExamineActionIsNotRepeatable();
            ignoring( item );
        }});
    	new RecordableItem( item, null ).setExamineActionIsNotRepeatable();
    }

    @Test
    public void decorator_calls_target_for_examine_action_is_not_repeatable() {
    	final Item item = mockery.mock( Item.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).examineActionIsNotRepeatable(); will( returnValue( true ) );
            ignoring( item );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, null );

    	assertThat( decoratedItem.examineActionIsNotRepeatable(), is( true ) );
    }

    // test all method calls that change the item are recorded
    // public void decorator_records_call_to_set_description_in_action_history()
}
