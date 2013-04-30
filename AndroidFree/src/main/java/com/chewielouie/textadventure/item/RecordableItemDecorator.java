package com.chewielouie.textadventure.item;

import com.chewielouie.textadventure.ActionHistory;

public class RecordableItemDecorator implements ItemDecorator {
	public RecordableItemDecorator( ActionHistory history ) {
	}

    public Item decorate( Item item ) {
    	return new RecordableItem( null, null );
    }
}
