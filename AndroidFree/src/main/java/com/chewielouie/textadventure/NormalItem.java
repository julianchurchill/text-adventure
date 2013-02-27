package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.List;

public class NormalItem implements Item {
    private String name = "";
    private String description = "";
    private String countableNounPrefix = "a";
    private String midSentenceCasedName = null;
    private boolean takeable = true;
    private String id = "";
    private String canBeUsedWithTargetID;
    private String usedWithText = "";
    private boolean useIsRepeatable = true;
    private boolean used = false;
    private List<ItemAction> onUseActions = new ArrayList<ItemAction>();
    private ItemActionFactory itemActionFactory = null;

    public NormalItem( String name, String description ) {
        this.name = name;
        this.description = description;
    }

    public NormalItem( String name, String description, ItemActionFactory f ) {
        this( name, description );
        this.itemActionFactory = f;
    }

    public NormalItem( String name, String description,
           String countableNounPrefix ) {
        this( name, description );
        this.countableNounPrefix = countableNounPrefix;
    }

    public NormalItem( String name, String description,
           String countableNounPrefix, String midSentenceCasedName ) {
        this( name, description, countableNounPrefix );
        this.midSentenceCasedName = midSentenceCasedName;
    }

    public String description() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public String name() {
        return name;
    }

    public String countableNounPrefix() {
        return countableNounPrefix;
    }

    public void setCountableNounPrefix( String prefix ) {
        this.countableNounPrefix = prefix;
    }

    public String midSentenceCasedName() {
        if( midSentenceCasedName == null )
            return name().toLowerCase();
        return midSentenceCasedName;
    }

    public void setMidSentenceCasedName( String casedName ) {
        this.midSentenceCasedName = casedName;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void setUntakeable() {
        takeable = false;
    }

    public boolean takeable() {
        return takeable;
    }

    public String id() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public boolean useIsRepeatable() {
        return useIsRepeatable;
    }

    @Override
    public boolean equals( Object o ) {
        if( !(o instanceof NormalItem) )
            return false;
        NormalItem other = (NormalItem)o;
        return name.equals( other.name ) &&
               description.equals( other.description );
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        result = prime * result + description.hashCode();
        return result;
    }

    public boolean canBeUsedWith( Item item ) {
        if( item.id() == "" )
            return false;
        return item.id().equals( canBeUsedWithTargetID );
    }

    public void setCanBeUsedWith( String itemID ) {
        this.canBeUsedWithTargetID = itemID;
    }

    public void setUseIsNotRepeatable() {
        useIsRepeatable = false;
    }

    public String usedWithText() {
        return usedWithText;
    }

    public void setUsedWithText( String text ) {
        this.usedWithText = text;
    }

    public void use() {
        if( itemCanBeUsedNow() )
            for( ItemAction action : onUseActions )
                action.enact();
        else
            usedWithText = "You have already done that.";
        used = true;
    }

    private boolean itemCanBeUsedNow() {
        return !used || (used && useIsRepeatable);
    }

    public void addOnUseAction( ItemAction action ) {
        onUseActions.add( action );
    }

    public ItemActionFactory itemActionFactory() {
        return itemActionFactory;
    }

    public void deserialise( String content ) {
        new Deserialiser( itemActionFactory ).deserialise( this, content );
    }

    class Deserialiser {
        private final String itemNameTag = "item name:";
        private final String itemDescriptionTag = "item description:";
        private final String itemIDTag = "item id:";
        private final String itemCountableNounPrefixTag = "item countable noun prefix:";
        private final String itemMidSentenceCasedNameTag = "item mid sentence cased name:";
        private final String itemIsUntakeableTag = "item is untakeable:";
        private final String itemCanBeUsedWithTag = "item can be used with:";
        private final String itemSuccessfulUseMessageTag = "item successful use message:";
        private final String itemUseIsNotRepeatableTag = "item use is not repeatable:";
        private final String itemUseActionTag = "item use action:";
        private int startOfLastFoundTag = -1;
        private Item item;
        private String content;
        private ItemActionFactory itemActionFactory;

        public Deserialiser( ItemActionFactory itemActionFactory ) {
            this.itemActionFactory = itemActionFactory;
        }

        public void deserialise( Item item, String content ) {
            this.item = item;
            this.content = content;
            startOfLastFoundTag = -1;

            deserialiseItems();
        }

        private int findTag( String tag ) {
            return content.indexOf( tag, startOfLastFoundTag + 1 );
        }

        private String extractNewlineDelimitedValueFor( String tag ) {
            int startOfTag = findTag( tag );
            if( startOfTag == -1 )
                return "";
            startOfLastFoundTag = startOfTag;
            return extractValueUpToNewline( startOfTag + tag.length() );
        }

        private String extractValueUpToNewline( int startOfValue ) {
            int endOfTag = content.indexOf( "\n", startOfLastFoundTag );
            if( endOfTag == -1 )
                endOfTag = content.length();
            return content.substring( startOfValue, endOfTag );
        }

        private void deserialiseItems() {
            item.setName( extractNewlineDelimitedValueFor( itemNameTag ) );
            item.setDescription( extractNewlineDelimitedValueFor( itemDescriptionTag ) );
            item.setId( extractNewlineDelimitedValueFor( itemIDTag ) );
            item.setCountableNounPrefix( extractNewlineDelimitedValueFor( itemCountableNounPrefixTag ) );
            String m = extractNewlineDelimitedValueFor( itemMidSentenceCasedNameTag );
            if( m != "" )
                item.setMidSentenceCasedName( m );

            if( findTagWithNoArgument( itemIsUntakeableTag ) )
                item.setUntakeable();

            item.setCanBeUsedWith( extractNewlineDelimitedValueFor( itemCanBeUsedWithTag ) );
            item.setUsedWithText( extractNewlineDelimitedValueFor( itemSuccessfulUseMessageTag ) );

            if( findTagWithNoArgument( itemUseIsNotRepeatableTag ) )
                item.setUseIsNotRepeatable();

            extractItemActions();
        }

        private void extractItemActions() {
            if( itemActionFactory != null ) {
                while( findTagAndUpdatePosition( itemUseActionTag ) ) {
                    ItemAction action =
                        itemActionFactory.create(
                            extractValueUpToNewline( startOfLastFoundTag +
                                                     itemUseActionTag.length() ),
                            item );
                    item.addOnUseAction( action );
                }
            }
        }

        private boolean findTagAndUpdatePosition( String tag ) {
            return findTagWithNoArgument( tag );
        }

        private boolean findTagWithNoArgument( String tag ) {
            int startOfTag = findTag( tag );
            if( startOfTag != -1 ) {
                startOfLastFoundTag = startOfTag;
                return true;
            }
            return false;
        }
    }
}

