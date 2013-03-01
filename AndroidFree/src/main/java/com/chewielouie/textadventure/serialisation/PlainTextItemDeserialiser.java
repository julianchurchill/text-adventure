package com.chewielouie.textadventure.serialisation;

import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.ItemAction;
import com.chewielouie.textadventure.ItemActionFactory;

public class PlainTextItemDeserialiser implements ItemDeserialiser {
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
    private int startOfLastFoundTag;
    private Item item;
    private String content;
    private ItemActionFactory itemActionFactory;

    public PlainTextItemDeserialiser( ItemActionFactory itemActionFactory ) {
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

