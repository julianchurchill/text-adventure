package com.chewielouie.textadventure;

import java.util.List;

public interface UserInventory {
    public List<Item> inventoryItems();
    public void addToInventory( Item item );
}



