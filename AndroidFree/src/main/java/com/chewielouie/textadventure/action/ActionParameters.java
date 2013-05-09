package com.chewielouie.textadventure.action;

import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.item.Item;

public class ActionParameters {
    private Item item;
    private Item extraItem;
    private Exit exit;

    public ActionParameters( Item item ) {
        this.item = item;
    }

    public ActionParameters( Item item, Item targetItem ) {
        this.item = item;
        this.extraItem = targetItem;
    }

    public ActionParameters( Exit exit ) {
        this.exit = exit;
    }

    public Item item() {
        return item;
    }

    public Item extraItem() {
        return extraItem;
    }

    public Exit exit() {
        return exit;
    }
}
