package com.chewielouie.textadventure.itemaction;

import com.chewielouie.textadventure.TextAdventureModel;
import java.util.ArrayList;
import java.util.List;

public class DestroyItemItemAction implements ItemAction {
    private String itemID;
    private TextAdventureModel model;

    public DestroyItemItemAction( String itemID, TextAdventureModel model ) {
        this.itemID = itemID;
        this.model = model;
    }

    public TextAdventureModel model() {
        return model;
    }

    public void enact() {
        model.destroyItem( itemID );
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


