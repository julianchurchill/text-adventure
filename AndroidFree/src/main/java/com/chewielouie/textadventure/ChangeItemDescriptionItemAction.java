package com.chewielouie.textadventure;

public class ChangeItemDescriptionItemAction implements ItemAction {
    private String description;
    private Item item;

    public ChangeItemDescriptionItemAction( String description, Item item ) {
        this.description = description;
        this.item = item;
    }

    public void enact() {
        item.setDescription( description );
    }
}

