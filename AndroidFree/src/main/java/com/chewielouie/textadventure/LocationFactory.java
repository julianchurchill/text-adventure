package com.chewielouie.textadventure;

import com.chewielouie.textadventure.action.ActionFactory;

public class LocationFactory implements ModelLocationFactory {
    private UserInventory inventory;
    private ActionFactory actionFactory;

    public LocationFactory( UserInventory inventory, ActionFactory factory) {
        this.inventory = inventory;
        this.actionFactory = factory;
    }

    public ModelLocation create() {
        return new Location( "", "", inventory, actionFactory );
    }
}

