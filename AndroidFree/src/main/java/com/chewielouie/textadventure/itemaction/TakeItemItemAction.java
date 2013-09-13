
package com.chewielouie.textadventure.itemaction;

import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.TextAdventureModel;
import java.util.ArrayList;
import java.util.List;

public class TakeItemItemAction implements ItemAction {
    private TextAdventureModel model;
    private String itemID = "";

    public TakeItemItemAction( String arguments,
                               TextAdventureModel model ) {
        this.model = model;
        if( arguments != null )
            itemID = arguments;
    }

    public TextAdventureModel model() {
        return model;
    }

    public void enact() {
        model.moveItemToInventory( itemID );
    }

    public String name() {
        return "take item";
    }

    public List<String> arguments() {
        List<String> args = new ArrayList<String>();
        args.add( this.itemID );
        return args;
    }
}
