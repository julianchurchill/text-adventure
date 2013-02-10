package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.TakeAnItem;

public class Location implements ModelLocation {
    private String id;
    private String description;
    private List<Exit> exits = new ArrayList<Exit>();
    private List<Item> items = new ArrayList<Item>();
    private UserInventory inventory;

    public Location( String locationId, String description, UserInventory inventory ) {
        this.id = locationId;
        this.description = description;
        this.inventory = inventory;
    }

    public void addExit( Exit exit ) {
        exits.add( exit );
    }

    public boolean exitable( Exit exit ) {
        for( Exit e : exits )
            if( e.equals( exit ) )
                return true;
        return false;
    }

    public String exitDestinationFor( Exit exit ) {
        return exit.destination();
    }

    public String id() {
        return this.id;
    }

    public List<Exit> exits() {
        return exits;
    }

    public String description() {
        return description + itemsPostAmble();
    }

    private String itemsPostAmble() {
        String itemsPostAmble = "";
        if( items.size() > 0 ) {
            itemsPostAmble = " There is ";
            for( int i = 0; i < items.size(); i++ ) {
                if( items.size() > 1 ) {
                    if( i != 0 && i != (items.size()-1) )
                        itemsPostAmble += ", ";
                    if( i == (items.size()-1) )
                        itemsPostAmble += " and ";
                }
                itemsPostAmble += items.get(i).countableNounPrefix() + " " +
                              items.get(i).midSentenceCasedName();
            }
            itemsPostAmble += " here.";
        }
        return itemsPostAmble;
    }

    public void addItem( Item item ) {
        items.add( item );
    }

    public void removeItem( Item item ) {
        items.remove( item );
    }

    public List<Item> items() {
        return items;
    }

    public List<Action> actions() {
        List<Action> actions = new ArrayList<Action>();
        if( items().size() > 0 )
            actions.add( new TakeAnItem( items, inventory, this ) );
        return actions;
    }

    public void deserialise( String content ) {
    }
}

