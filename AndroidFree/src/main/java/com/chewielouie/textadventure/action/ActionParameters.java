package com.chewielouie.textadventure.action;

import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.ModelLocation;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.TalkPhraseSource;

public class ActionParameters {
    private Item item;
    private Item extraItem;
    private Exit exit;
    private ModelLocation location;
    private String string;
    private TalkPhraseSource talkable;

    public ActionParameters( Item item ) {
        this.item = item;
    }

    public ActionParameters( ModelLocation location ) {
        this.location = location;
    }

    public ActionParameters( Item item, ModelLocation location ) {
        this.item = item;
        this.location = location;
    }

    public ActionParameters( Item item, Item targetItem ) {
        this.item = item;
        this.extraItem = targetItem;
    }

    public ActionParameters( Exit exit ) {
        this.exit = exit;
    }

    public ActionParameters( String string, Item item ) {
        this.string = string;
        this.item = item;
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

    public ModelLocation location() {
        return location;
    }

    public String string() {
        return string;
    }

    public TalkPhraseSource talkPhraseSource() {
        return talkable;
    }
}
