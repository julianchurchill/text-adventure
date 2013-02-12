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
    private ItemFactory itemFactory;
    private Deserialiser deserialiser = new Deserialiser();

    public Location( String locationId, String description,
            UserInventory inventory, ItemFactory itemFactory ) {
        this.id = locationId;
        this.description = description;
        this.inventory = inventory;
        this.itemFactory = itemFactory;
    }

    public UserInventory inventory() {
        return inventory;
    }

    public ItemFactory itemFactory() {
        return itemFactory;
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
        deserialiser.parse( content );
    }

    class Deserialiser {
        private final String locationIDTag = "location id:";
        private final String locationDescriptionTag = "location description:";
        private final String exitLabelTag = "exit label:";
        private final String exitDestinationTag = "exit destination:";
        private final String exitDirectionHintTag = "exit direction hint:";
        private final String itemNameTag = "item name:";
        private final String itemDescriptionTag = "item description:";
        private final String itemCountableNounPrefixTag = "item countable noun prefix:";
        private final String itemMidSentenceCasedNameTag = "item mid sentence cased name:";
        private String content;
        private int startOfLastFoundTag = -1;

        public void parse( String content ) {
            this.content = content;
            startOfLastFoundTag = -1;

            id = extractNewlineDelimitedValueFor( locationIDTag );
            deserialiseDescription();
            deserialiseExits();
            deserialiseItems();
        }

        private String extractNewlineDelimitedValueFor( String tag ) {
            int startOfTag = content.indexOf( tag, startOfLastFoundTag + 1 );
            if( startOfTag == -1 )
                return "";
            startOfLastFoundTag = startOfTag;
            int endOfTag = content.indexOf( "\n", startOfTag );
            if( endOfTag == -1 )
                endOfTag = content.length();
            return content.substring( startOfTag + tag.length(), endOfTag );
        }

        private void deserialiseDescription() {
            int startOfDescription = content.indexOf( locationDescriptionTag );
            if( startOfDescription != -1 )
                description = content.substring(
                        startOfDescription + locationDescriptionTag.length() );
        }

        private void deserialiseExits() {
            String exitLabel;
            while( (exitLabel=extractNewlineDelimitedValueFor( exitLabelTag )) != "" )
                addExit( new Exit(
                    exitLabel,
                    extractNewlineDelimitedValueFor( exitDestinationTag ),
                    stringToDirectionHint(
                        extractNewlineDelimitedValueFor( exitDirectionHintTag ) ) ) );
        }

        private void deserialiseItems() {
            String itemName;
            while( (itemName=extractNewlineDelimitedValueFor( itemNameTag )) != "" )
                addItem( itemFactory.create(
                    itemName,
                    extractNewlineDelimitedValueFor( itemDescriptionTag ),
                    extractNewlineDelimitedValueFor( itemCountableNounPrefixTag ),
                    extractNewlineDelimitedValueFor( itemMidSentenceCasedNameTag ) ) );
        }

        private Exit.DirectionHint stringToDirectionHint( String hint ) {
            if( hint.equals( "North" ) )
                return Exit.DirectionHint.North;
            if( hint.equals( "South" ) )
                return Exit.DirectionHint.South;
            if( hint.equals( "East" ) )
                return Exit.DirectionHint.East;
            if( hint.equals( "West" ) )
                return Exit.DirectionHint.West;
            return Exit.DirectionHint.DontCare;
        }
    }
}

