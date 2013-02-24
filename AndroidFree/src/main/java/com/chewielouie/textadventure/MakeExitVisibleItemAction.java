package com.chewielouie.textadventure;

public class MakeExitVisibleItemAction implements ItemAction {
    private Item item;

    public MakeExitVisibleItemAction( String exitID, Item item ) {
        this.item = item;
    }

    public void enact() {
    }
}

