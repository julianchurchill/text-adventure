package com.chewielouie.textadventure.itemaction;

import com.chewielouie.textadventure.item.Item;
import java.util.ArrayList;
import java.util.List;

public class DestroyItemItemAction implements ItemAction {
    private String itemID;

    public DestroyItemItemAction( String itemID ) {
        this.itemID = itemID;
    }

    public void enact() {
    }

    public String name() {
        return "destroy item";
    }

    public List<String> arguments() {
        List<String> args = new ArrayList<String>();
        args.add( this.itemID );
        return args;
    }
}


