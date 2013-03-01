package com.chewielouie.textadventure.item;

public class NormalItemFactory implements ItemFactory {
    public Item create() {
        return new NormalItem();
    }
}

