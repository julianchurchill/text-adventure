package com.chewielouie.textadventure;

public class InventoryItem implements Item {
    private String name = "";
    private String description = "";

    public InventoryItem( String name, String description ) {
        this.name = name;
        this.description = description;
    }
}

