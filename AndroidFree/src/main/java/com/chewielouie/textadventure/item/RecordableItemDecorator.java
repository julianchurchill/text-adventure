package com.chewielouie.textadventure.item;

import com.chewielouie.textadventure.ActionHistory;

public class RecordableItemDecorator implements ItemDecorator {
	private ActionHistory actionHistory;

	public RecordableItemDecorator( ActionHistory history ) {
		this.actionHistory = history;
	}

    public Item decorate( Item item ) {
    	return new RecordableItem( item, actionHistory );
    }
}
