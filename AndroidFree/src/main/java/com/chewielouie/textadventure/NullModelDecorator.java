package com.chewielouie.textadventure;

public class NullModelDecorator implements ModelDecorator {
    public TextAdventureModel decorate( TextAdventureModel model ) {
    	return model;
    }
}
