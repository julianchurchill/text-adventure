package com.chewielouie.textadventure.itemaction;

import com.chewielouie.textadventure.item.Item;
import java.util.ArrayList;
import java.util.List;

public class ChangeItemDescriptionItemAction implements ItemAction {
    private String description;
    private Item item;

    public ChangeItemDescriptionItemAction( String description, Item item ) {
        this.description = description;
        this.item = item;
    }

    public Item item() {
        return item;
    }

    public void enact() {
        item.setDescription( description );
    }

    public String name() {
        return "change item description";
    }

    public List<String> arguments() {
        List<String> args = new ArrayList<String>();
        args.add( this.description );
        return args;
    }
}

