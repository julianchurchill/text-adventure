package com.chewielouie.textadventure.item;

public class NullItemDecorator implements ItemDecorator {
    public Item decorate( Item item ) {
    	return item;
    }
}
