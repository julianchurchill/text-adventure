package com.chewielouie.textadventure.item;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.jmock.*;
import org.junit.Test;
// import com.chewielouie.textadventure.TextAdventureModel;

public class RecordableItemDecoratorTests {

    private Mockery mockery = new Mockery();

    @Test
    public void decorator_wraps_items_with_a_recordable_item() {    
    	RecordableItemDecorator decorator =
    			new RecordableItemDecorator( null );

    	Item item = mockery.mock( Item.class );
    	Item decoratedItem = decorator.decorate( item );

    	assertThat( decoratedItem, is( instanceOf( RecordableItem.class ) ) );
    }

    // @Test
    // public void decorator_passes_wrapped_item_to_new_item() {    
    	// RecordableItemDecorator decorator =
    	// 		new RecordableItemDecorator( null );

    // 	Item item = mockery.mock( Item.class );
    // 	Item decoratedItem = decorator.decorate( item );

    // 	assertThat( decoratedItem.item(), is( item ) );
    // }

    // @Test
    // public void decorator_passes_action_history_to_new_item() {    
    // 	RecordableItemDecorator decorator =
    // 			new RecordableItemDecorator( actionHistory );
	
    // 	Item decoratedItem = decorator.decorate( item );

    // 	assertThat( decoratedItem.actionHistory(), is( actionHistory ) );
    // }
}
