package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.TakeAnItem;
import com.chewielouie.textadventure.action.ExamineAnItem;

public class Location implements ModelLocation {
    private String id;
    private String description;
    private List<Exit> exits = new ArrayList<Exit>();
    private List<Item> items = new ArrayList<Item>();
    private UserInventory inventory;

    public Location( String locationId, String description,
            UserInventory inventory ) {
        this.id = locationId;
        this.description = description;
        this.inventory = inventory;
    }

    public UserInventory inventory() {
        return inventory;
    }

    public void addExit( Exit exit ) {
        exits.add( exit );
    }

    public boolean exitable( Exit exit ) {
        if( exit.visible() )
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

    public void setId( String id ) {
        this.id = id;
    }

    public List<Exit> visibleExits() {
        List<Exit> visibleExits = new ArrayList<Exit>();
        for( Exit e : exits )
            if( e.visible() )
                visibleExits.add( e );
        return visibleExits;
    }

    public List<Exit> exitsIncludingInvisibleOnes() {
        return exits;
    }

    public String description() {
        return description + itemsPostAmble();
    }

    public void setLocationDescription( String description ) {
        this.description = description;
    }

    private String itemsPostAmble() {
        String itemsPostAmble = "";
        if( items.size() > 0 ) {
            itemsPostAmble = "\nThere is ";
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

    public List<Item> takeableItems() {
        List<Item> takeableItems = new ArrayList<Item>();
        for( Item item : items() )
            if( item.takeable() )
                takeableItems.add( item );
        return takeableItems;
    }

    public List<Action> actions() {
        List<Action> actions = new ArrayList<Action>();
        if( takeableItems().size() > 0 )
            actions.add( new TakeAnItem( takeableItems(), inventory, this ) );
        if( items().size() > 0 )
            actions.add( new ExamineAnItem( items() ) );
        return actions;
    }
}

