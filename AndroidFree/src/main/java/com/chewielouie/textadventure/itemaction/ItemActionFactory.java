package com.chewielouie.textadventure.itemaction;

import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.ItemDecorator;
import com.chewielouie.textadventure.ModelDecorator;

public interface ItemActionFactory {
    public ItemAction create( String content, Item item );
    public void setItemDecorator( ItemDecorator d );
    public void setModelDecorator( ModelDecorator d );
}

