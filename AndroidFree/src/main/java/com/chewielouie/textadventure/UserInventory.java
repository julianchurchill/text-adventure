package com.chewielouie.textadventure;

import java.util.List;
import com.chewielouie.textadventure.item.Item;

public interface UserInventory {
    public List<Item> inventoryItems();
    public void addToInventory( Item item );
}



