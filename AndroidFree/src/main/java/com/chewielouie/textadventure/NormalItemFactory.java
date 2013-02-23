package com.chewielouie.textadventure;

public class NormalItemFactory implements ItemFactory {
    private ItemActionFactory itemActionFactory;

    public NormalItemFactory( ItemActionFactory factory ) {
        this.itemActionFactory = factory;
    }

    public Item create() {
        return new NormalItem( "", "", itemActionFactory );
    }
}

