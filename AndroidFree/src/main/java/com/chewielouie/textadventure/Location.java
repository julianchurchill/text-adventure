package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionFactory;
import com.chewielouie.textadventure.item.Item;

public class Location implements ModelLocation {
    private String id;
    private String description;
    private List<Exit> exits = new ArrayList<Exit>();
    private List<Item> items = new ArrayList<Item>();
    private UserInventory inventory;
    private ActionFactory actionFactory;
    private int x = 0;
    private int y = 0;

    public Location( String locationId, String description,
            UserInventory inventory, ActionFactory factory ) {
        this.id = locationId;
        this.description = description;
        this.inventory = inventory;
        this.actionFactory = factory;
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
        return description;
    }

    public String availableItemsText() {
        return itemsPostAmble();
    }

    public void setLocationDescription( String description ) {
        this.description = description;
    }

    private String itemsPostAmble() {
        List<Item> visibleItems = visibleItems();
        if( visibleItems.size() == 0 )
            return "";

        String itemsPostAmble = "There is ";
        for( Item item : visibleItems )
            itemsPostAmble += item.countableNounPrefix() + " " +
                              item.midSentenceCasedName() +
                              itemPostfix( item, visibleItems );
        return itemsPostAmble;
    }

    private String itemPostfix( Item item, List<Item> items ) {
        if( item == lastOf( items ) )
            return " here.\n";
        else if( item == secondTolastOf( items ) )
            return " and ";
        return  ", ";
    }

    private Item lastOf( List<Item> items ) {
        return items.get( items.size() - 1 );
    }

    private Item secondTolastOf( List<Item> items ) {
        return items.get( items.size() - 2 );
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
        if( actionFactory != null ) {
            if( takeableItems().size() > 0 )
                actions.add( actionFactory.createTakeAnItemAction( takeableItems(), inventory, this ) );
            if( visibleItems().size() > 0 )
                actions.add( actionFactory.createExamineAnItemAction( visibleItems() ) );
            addTalkToActions( actions );
        }
        return actions;
    }

    private void addTalkToActions( List<Action> actions ) {
        for( Item item : visibleItems() )
            if( item.canTalkTo() )
                actions.add( actionFactory.createTalkToAction( item ) );
    }

    private List<Item> visibleItems() {
        List<Item> visibleItems = new ArrayList<Item>();
        for( Item item : items )
            if( item.visible() )
                visibleItems.add( item );
        return visibleItems;
    }

    private List<Item> takeableItems() {
        List<Item> takeableItems = new ArrayList<Item>();
        for( Item item : visibleItems() )
            if( item.takeable() )
                takeableItems.add( item );
        return takeableItems;
    }

    public void setX( int x ) {
        this.x = x;
    }

    public void setY( int y ) {
        this.y = y;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }
}

