package com.chewielouie.textadventure;

public class LocationFactory implements ModelLocationFactory {
    private UserInventory inventory;

    public LocationFactory( UserInventory inventory ) {
        this.inventory = inventory;
    }

    public ModelLocation create() {
        return new Location( "", "", inventory );
    }
}



