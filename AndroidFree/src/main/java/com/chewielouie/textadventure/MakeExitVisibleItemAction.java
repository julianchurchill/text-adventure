package com.chewielouie.textadventure;

import com.chewielouie.textadventure.item.Item;

public class MakeExitVisibleItemAction implements ItemAction {
    private String exitID;
    private TextAdventureModel model;

    public MakeExitVisibleItemAction( String exitID, Item item,
           TextAdventureModel model ) {
        this.exitID = exitID;
        this.model = model;
    }

    public void enact() {
        Exit exit = model.findExitByID( exitID );
        if( exit != null )
            exit.setVisible();
    }
}

