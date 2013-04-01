package com.chewielouie.textadventure.serialisation;

import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.itemaction.ItemAction;
import com.chewielouie.textadventure.itemaction.ItemActionFactory;

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
    private final String itemVisibilityTag = "item visibility:";
    private final String itemExamineMessageTag = "item examine message:";
    private final String itemExamineActionIsNotRepeatableTag = "item examine action is not repeatable:";
    private Item item;
    private String content;
    private ItemActionFactory itemActionFactory;

    public PlainTextItemDeserialiser( ItemActionFactory itemActionFactory ) {
        this.itemActionFactory = itemActionFactory;
    }

    public void deserialise( Item item, String content ) {
        this.item = item;
        this.content = content;

        item.setName( extractNewlineDelimitedValueFor( itemNameTag ) );
        item.setDescription( extractNewlineDelimitedValueFor( itemDescriptionTag ) );
        item.setId( extractNewlineDelimitedValueFor( itemIDTag ) );
        item.setCountableNounPrefix( extractNewlineDelimitedValueFor( itemCountableNounPrefixTag ) );
        String m = extractNewlineDelimitedValueFor( itemMidSentenceCasedNameTag );
        if( m != "" )
            item.setMidSentenceCasedName( m );

        if( findTagWithNoArgument( itemIsUntakeableTag ) )
            item.setUntakeable();

        extractItemUseProperties();
        extractItemVisibilityProperties();

        item.setExamineText( extractNewlineDelimitedValueFor( itemExamineMessageTag ) );
        if( findTagWithNoArgument( itemExamineActionIsNotRepeatableTag ) )
            item.setExamineActionIsNotRepeatable();
    }

    private void extractItemUseProperties() {
        item.setCanBeUsedWith( extractNewlineDelimitedValueFor( itemCanBeUsedWithTag ) );
        item.setUsedWithText( extractNewlineDelimitedValueFor( itemSuccessfulUseMessageTag ) );

        if( findTagWithNoArgument( itemUseIsNotRepeatableTag ) )
            item.setUseIsNotRepeatable();

        extractItemActions();
    }

    private void extractItemVisibilityProperties() {
        String visibility = extractNewlineDelimitedValueFor( itemVisibilityTag );
        if( visibility.equals( "invisible" ) )
            item.setVisible( false );
        else
            item.setVisible( true );
    }

    private boolean findTagWithNoArgument( String tag ) {
        return findTag( tag ) != -1;
    }

    private int findTag( String tag ) {
        return content.indexOf( tag );
    }

    private String extractNewlineDelimitedValueFor( String tag ) {
        int startOfTag = findTag( tag );
        if( startOfTag == -1 )
            return "";
        return extractValueUpToNewline( startOfTag + tag.length() );
    }

    private String extractValueUpToNewline( int startOfValue ) {
        int endOfTag = content.indexOf( "\n", startOfValue );
        if( endOfTag == -1 )
            endOfTag = content.length();
        return content.substring( startOfValue, endOfTag );
    }

    private void extractItemActions() {
        if( itemActionFactory != null ) {
            int nextTag = -1;
            while( (nextTag=findTagFrom( nextTag, itemUseActionTag )) != -1 )
                item.addOnUseAction( createAction( nextTag ) );
        }
    }

    private ItemAction createAction( int startOfActionValue ) {
        return itemActionFactory.create(
                    extractValueUpToNewline( startOfActionValue +
                                             itemUseActionTag.length() ),
                    item );
    }

    private int findTagFrom( int start, String tag ) {
        return content.indexOf( tag, start + 1 );
    }
}

