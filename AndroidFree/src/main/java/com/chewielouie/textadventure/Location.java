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
    private ItemFactory itemFactory = null;
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

    public void deserialise( String content ) {
        deserialiser.parse( content );
    }

    class Deserialiser {
        private final String locationIDTag = "location id:";
        private final String locationDescriptionTag = "location description:";
        private final String exitLabelTag = "exit label:";
        private final String exitDestinationTag = "exit destination:";
        private final String exitDirectionHintTag = "exit direction hint:";
        private final String itemTag = "ITEM\n";
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
                        startOfDescription + locationDescriptionTag.length(),
                        findEndOfDescription() );
        }

        private int findEndOfDescription() {
            int endOfDescription = content.indexOf( exitLabelTag );
            if( endOfDescription == -1 ) {
                endOfDescription = content.indexOf( itemTag );
                if( endOfDescription == -1 )
                    endOfDescription = content.length();
            }
            return endOfDescription;
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
            if( itemFactory != null ) {
                while( itemAvailable() ) {
                    int startOfItem = content.indexOf( itemTag, startOfLastFoundTag+1 );
                    int endOfItem = content.indexOf( itemTag, startOfItem+1 );
                    if( endOfItem == -1 ) {
                        endOfItem = content.indexOf( locationIDTag, startOfItem+1 );
                        if( endOfItem == -1 )
                            endOfItem = content.length();
                    }
                    Item item = itemFactory.create();
                    item.deserialise(
                      content.substring( startOfItem + itemTag.length(), endOfItem ) );
                    addItem( item );
                    startOfLastFoundTag = startOfItem;
                }
            }
        }

        private boolean itemAvailable() {
            return content.indexOf( itemTag, startOfLastFoundTag+1 ) != -1;
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

