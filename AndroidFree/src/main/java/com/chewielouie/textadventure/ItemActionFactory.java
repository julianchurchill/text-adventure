package com.chewielouie.textadventure;

import com.chewielouie.textadventure.item.Item;

public interface ItemActionFactory {
    public ItemAction create( String content, Item item );
}

