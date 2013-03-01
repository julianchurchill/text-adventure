package com.chewielouie.textadventure.itemaction;

import com.chewielouie.textadventure.item.Item;

public class ChangeItemNameItemAction implements ItemAction {
    private String name;
    private Item item;

    public ChangeItemNameItemAction( String name, Item item ) {
        this.name = name;
        this.item = item;
    }

    public void enact() {
        item.setName( name );
    }
}

