package com.chewielouie.textadventure;

public class LocationFactory implements ModelLocationFactory {
    private UserInventory inventory;
    private ItemFactory itemFactory;

    public LocationFactory( UserInventory inventory, ItemFactory itemFactory ) {
        this.inventory = inventory;
        this.itemFactory = itemFactory;
    }

    public ModelLocation create() {
        return new Location( "", "", inventory, itemFactory );
    }
}



