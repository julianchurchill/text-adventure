package com.chewielouie.textadventure.itemaction;

import com.chewielouie.textadventure.item.Item;
import java.util.ArrayList;
import java.util.List;

public class ChangeItemNameItemAction implements ItemAction {
    private String name;
    private Item item;

    public ChangeItemNameItemAction( String name, Item item ) {
        this.name = name;
        this.item = item;
    }

    public Item item() {
        return item;
    }

    public void enact() {
        item.setName( name );
    }

    public String name() {
        return "change item name";
    }

    public List<String> arguments() {
        List<String> args = new ArrayList<String>();
        args.add( this.name );
        return args;
    }
}

