package com.chewielouie.textadventure.item;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.chewielouie.textadventure.ActionHistory;
import com.chewielouie.textadventure.ActionHistoryAction;
import com.chewielouie.textadventure.itemaction.ItemAction;

@RunWith(JMock.class)
public class RecordableItemTests {

    private Mockery mockery = new Mockery();

    @Test
    public void decorator_calls_target_for_description() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).description(); will( returnValue( "original description" ) );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, actionHistory );

    	assertThat( decoratedItem.description(), is( "original description" ) );
    }

    @Test
    public void decorator_calls_target_for_set_description() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setDescription( "new description" );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	new RecordableItem( item, actionHistory ).setDescription( "new description" );
    }

    @Test
    public void decorator_calls_target_for_name() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).name(); will( returnValue( "original name" ) );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, actionHistory );

    	assertThat( decoratedItem.name(), is( "original name" ) );
    }

    @Test
    public void decorator_calls_target_for_set_name() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setName( "new name" );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	new RecordableItem( item, actionHistory ).setName( "new name" );
    }

    @Test
    public void decorator_calls_target_for_id() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).id(); will( returnValue( "original id" ) );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, actionHistory );

    	assertThat( decoratedItem.id(), is( "original id" ) );
    }

    @Test
    public void decorator_calls_target_for_set_id() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setId( "new id" );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	new RecordableItem( item, actionHistory ).setId( "new id" );
    }

    @Test
    public void decorator_calls_target_for_countable_noun_prefix() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).countableNounPrefix(); will( returnValue( "original value" ) );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, actionHistory );

    	assertThat( decoratedItem.countableNounPrefix(), is( "original value" ) );
    }

    @Test
    public void decorator_calls_target_for_set_countable_noun_prefix() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setCountableNounPrefix( "new value" );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	new RecordableItem( item, actionHistory ).setCountableNounPrefix( "new value" );
    }

    @Test
    public void decorator_calls_target_for_mid_sentence_cased_name() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).midSentenceCasedName(); will( returnValue( "original value" ) );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, actionHistory );

    	assertThat( decoratedItem.midSentenceCasedName(), is( "original value" ) );
    }

    @Test
    public void decorator_calls_target_for_set_mid_sentence_cased_name() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setMidSentenceCasedName( "new value" );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	new RecordableItem( item, actionHistory ).setMidSentenceCasedName( "new value" );
    }

    @Test
    public void decorator_calls_target_for_takeable() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).takeable(); will( returnValue( true ) );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, actionHistory );

    	assertThat( decoratedItem.takeable(), is( true ) );
    }

    @Test
    public void decorator_calls_target_for_set_untakeable() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setUntakeable();
            ignoring( item );
            ignoring( actionHistory );
        }});
    	new RecordableItem( item, actionHistory ).setUntakeable();
    }

    @Test
    public void decorator_calls_target_for_can_be_used_with() {
    	final Item item = mockery.mock( Item.class, "item" );
    	final Item otherItem = mockery.mock( Item.class, "other item" );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).canBeUsedWith( otherItem );
            will( returnValue( true ) );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, actionHistory );

    	assertThat( decoratedItem.canBeUsedWith( otherItem ), is( true ) );
    }

    @Test
    public void decorator_calls_target_for_set_can_be_used_with() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setCanBeUsedWith( "item id" );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	new RecordableItem( item, actionHistory ).setCanBeUsedWith( "item id" );
    }

    @Test
    public void decorator_calls_target_for_can_be_used_with_item_ids() {
    	final Item item = mockery.mock( Item.class );
    	final List<String> itemIDs = mockery.mock( List.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).canBeUsedWithItemIDs();
            will( returnValue( itemIDs ) );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, actionHistory );

    	assertThat( decoratedItem.canBeUsedWithItemIDs(), is( itemIDs ) );
    }

    @Test
    public void decorator_calls_target_for_used_with_text() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).usedWithText(); will( returnValue( "text" ) );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, actionHistory );

    	assertThat( decoratedItem.usedWithText(), is( "text" ) );
    }

    @Test
    public void decorator_calls_target_for_set_used_with_text() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setUsedWithText( "text" );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	new RecordableItem( item, actionHistory ).setUsedWithText( "text" );
    }

    @Test
    public void decorator_calls_target_for_set_use_is_not_repeatable() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setUseIsNotRepeatable();
            ignoring( item );
            ignoring( actionHistory );
        }});
    	new RecordableItem( item, actionHistory ).setUseIsNotRepeatable();
    }

    @Test
    public void decorator_calls_target_for_use_is_not_repeatable() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).useIsNotRepeatable(); will( returnValue( true ) );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, actionHistory );

    	assertThat( decoratedItem.useIsNotRepeatable(), is( true ) );
    }

    @Test
    public void decorator_calls_target_for_add_on_use_action() {
    	final Item item = mockery.mock( Item.class );
    	final ItemAction itemAction = mockery.mock( ItemAction.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).addOnUseAction( itemAction );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	new RecordableItem( item, actionHistory ).addOnUseAction( itemAction );
    }

    @Test
    public void decorator_calls_target_for_use() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).use();
            ignoring( item );
            ignoring( actionHistory );
        }});
    	new RecordableItem( item, actionHistory ).use();
    }

    @Test
    public void decorator_calls_target_for_actions() {
    	final Item item = mockery.mock( Item.class );
    	final List<ItemAction> actions = mockery.mock( List.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).actions(); will( returnValue( actions ) );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, actionHistory );

    	assertThat( decoratedItem.actions(), is( actions ) );
    }

    @Test
    public void decorator_calls_target_for_set_visible() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setVisible( true );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	new RecordableItem( item, actionHistory ).setVisible( true );
    }

    @Test
    public void decorator_calls_target_for_visible() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).visible(); will( returnValue( true ) );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, actionHistory );

    	assertThat( decoratedItem.visible(), is( true ) );
    }

    @Test
    public void decorator_calls_target_for_examine() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).examine();
            ignoring( item );
            ignoring( actionHistory );
        }});
    	new RecordableItem( item, actionHistory ).examine();
    }

    @Test
    public void decorator_calls_target_for_add_on_examine_action() {
    	final Item item = mockery.mock( Item.class );
    	final ItemAction itemAction = mockery.mock( ItemAction.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).addOnExamineAction( itemAction );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	new RecordableItem( item, actionHistory ).addOnExamineAction( itemAction );
    }

    @Test
    public void decorator_calls_target_for_examine_text() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).examineText(); will( returnValue( "text" ) );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, actionHistory );

    	assertThat( decoratedItem.examineText(), is( "text" ) );
    }

    @Test
    public void decorator_calls_target_for_set_examine_text() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setExamineText( "text" );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	new RecordableItem( item, actionHistory ).setExamineText( "text" );
    }

    @Test
    public void decorator_calls_target_for_set_examine_action_is_not_repeatable() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).setExamineActionIsNotRepeatable();
            ignoring( item );
            ignoring( actionHistory );
        }});
    	new RecordableItem( item, actionHistory ).setExamineActionIsNotRepeatable();
    }

    @Test
    public void decorator_calls_target_for_examine_action_is_not_repeatable() {
    	final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            oneOf( item ).examineActionIsNotRepeatable(); will( returnValue( true ) );
            ignoring( item );
            ignoring( actionHistory );
        }});
    	RecordableItem decoratedItem = new RecordableItem( item, actionHistory );

    	assertThat( decoratedItem.examineActionIsNotRepeatable(), is( true ) );
    }

    @Test
    public void decorator_records_use_of_item_in_history() {
        final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            allowing( item ).id(); will( returnValue( "item id" ) );
            ignoring( item );
            oneOf( actionHistory ).addAction( "item id", ActionHistoryAction.ITEM_USED );
            ignoring( actionHistory );
        }});
        new RecordableItem( item, actionHistory ).use();
    }

    @Test
    public void decorator_records_examine_of_item_in_history() {
        final Item item = mockery.mock( Item.class );
        final ActionHistory actionHistory = mockery.mock( ActionHistory.class );
        mockery.checking( new Expectations() {{
            allowing( item ).id(); will( returnValue( "item id" ) );
            ignoring( item );
            oneOf( actionHistory ).addAction( "item id", ActionHistoryAction.ITEM_EXAMINED );
            ignoring( actionHistory );
        }});
        new RecordableItem( item, actionHistory ).examine();
    }
}
