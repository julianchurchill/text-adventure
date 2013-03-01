package com.chewielouie.textadventure.itemaction;

import com.chewielouie.textadventure.item.Item;

public interface ItemActionFactory {
    public ItemAction create( String content, Item item );
}

