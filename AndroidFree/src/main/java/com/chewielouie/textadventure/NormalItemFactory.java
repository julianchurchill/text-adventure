package com.chewielouie.textadventure;

public class NormalItemFactory implements ItemFactory {

    public Item create() {
        return new NormalItem( "", "" );
    }
}

