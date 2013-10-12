package com.chewielouie.textadventure.serialisation;

import com.chewielouie.textadventure.DeserialiserUtils;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.TalkPhraseSink;
import com.chewielouie.textadventure.itemaction.ItemAction;
import com.chewielouie.textadventure.itemaction.ItemActionFactory;
import java.util.ArrayList;
import java.util.List;

public class PlainTextItemDeserialiser implements ItemDeserialiser {
    private final String argumentSeperator = ":";
    private final String itemNameTag = "item name:";
    private final String itemDescriptionTag = "item description:";
    private final String itemIDTag = "item id:";
    private final String itemCountableNounPrefixTag = "item countable noun prefix:";
    private final String itemMidSentenceCasedNameTag = "item mid sentence cased name:";
    private final String itemIsProperNounTag = "item is proper noun:";
    private final String itemIsPluralTag = "item is plural:";
    private final String itemIsUntakeableTag = "item is untakeable:";
    private final String itemCanBeUsedWithTag = "item can be used with:";
    private final String itemSuccessfulUseMessageTag = "item successful use message:";
    private final String itemUseIsNotRepeatableTag = "item use is not repeatable:";
    private final String itemUseActionTag = "item use action:";
    private final String itemVisibilityTag = "item visibility:";
    private final String itemExamineMessageTag = "item examine message:";
    private final String itemExamineActionIsNotRepeatableTag = "item examine action is not repeatable:";
    private final String itemOnExamineActionTag = "item on examine action:";
    private final String itemInitialTalkPhraseTag = "item talk initial phrase:";
    private final String itemTalkResponseToTag = "item talk response to:";
    private final String itemTalkFollowUpPhraseTag = "item talk follow up phrase to:";
    private final String itemTalkActionTag = "item talk action in response to:";
    private Item item;
    private String content;
    private ItemActionFactory itemActionFactory;
    private TalkPhraseSink talkPhraseSink;

    public PlainTextItemDeserialiser( ItemActionFactory itemActionFactory ) {
        this.itemActionFactory = itemActionFactory;
    }

    public void deserialise( Item item, String content ) {
        this.item = item;
        this.content = content;

        extractBasicProperties();
        extractItemUseProperties();
        extractItemVisibilityProperties();
        extractItemExamineProperties();
    }

    private void extractBasicProperties() {
        item.setName( extractNewlineDelimitedValueFor( itemNameTag ) );
        item.setDescription( extractNewlineDelimitedValueFor( itemDescriptionTag ) );
        item.setId( extractNewlineDelimitedValueFor( itemIDTag ) );
        item.setCountableNounPrefix( extractNewlineDelimitedValueFor( itemCountableNounPrefixTag ) );
        String m = extractNewlineDelimitedValueFor( itemMidSentenceCasedNameTag );
        if( m != "" )
            item.setMidSentenceCasedName( m );

        if( findTagWithNoArgument( itemIsProperNounTag ) )
            item.setProperNoun();

        if( findTagWithNoArgument( itemIsPluralTag ) )
            item.setPlural();

        if( findTagWithNoArgument( itemIsUntakeableTag ) )
            item.setUntakeable();

        extractTalkPhraseInfo();
    }

    private void extractItemUseProperties() {
        int fromIndex = findNextCanBeUsedWithIndex( 0 );
        while( fromIndex != DeserialiserUtils.NOT_FOUND ) {
            String usedWithItemID = DeserialiserUtils.extractNewlineDelimitedValueFor(
                itemCanBeUsedWithTag, content, fromIndex );
            item.setUsedWithTextFor( usedWithItemID, extractSuccessfulUseMessage( fromIndex ) );

            String itemUseContent = contentUptoNextCanBeUsedWith( fromIndex );
            if( findTagWithNoArgumentIn( itemUseContent, itemUseIsNotRepeatableTag ) )
                item.setUseIsNotRepeatableFor( usedWithItemID );

            extractItemUseActions( usedWithItemID, itemUseContent );
            fromIndex = findNextCanBeUsedWithIndex( fromIndex+1 );
        }
    }

    private int findNextCanBeUsedWithIndex( int fromIndex ) {
        return DeserialiserUtils.findTagFrom( fromIndex, itemCanBeUsedWithTag, content );
    }

    private String extractSuccessfulUseMessage( int fromIndex ) {
        return convertEncodedNewLines(
            DeserialiserUtils.extractNewlineDelimitedValueFor(
                itemSuccessfulUseMessageTag, content, fromIndex ) );
    }

    private String convertEncodedNewLines( String input ) {
        return input.replace( "<newline>", "\n" );
    }

    private String contentUptoNextCanBeUsedWith( int fromIndex ) {
        int startIndex = fromIndex == -1 ? 0 : fromIndex;
        int nextCanBeUsedWithTag = DeserialiserUtils.findTagFrom(
            fromIndex+1, itemCanBeUsedWithTag, content );
        if( nextCanBeUsedWithTag == DeserialiserUtils.NOT_FOUND )
            return content.substring( startIndex );
        return content.substring( startIndex, nextCanBeUsedWithTag );
    }

    private boolean findTagWithNoArgument( String tag ) {
        return findTag( tag ) != DeserialiserUtils.NOT_FOUND;
    }

    private boolean findTagWithNoArgumentIn( String lookInHere, String tag ) {
        return DeserialiserUtils.findTag( tag, lookInHere ) != DeserialiserUtils.NOT_FOUND;
    }

    private int findTag( String tag ) {
        return DeserialiserUtils.findTag( tag, content );
    }

    private String extractNewlineDelimitedValueFor( String tag ) {
        return DeserialiserUtils.extractNewlineDelimitedValueFor( tag, content );
    }

    private String extractValueUpToNewline( int startOfValue ) {
        return DeserialiserUtils.extractValueUpToNewline( startOfValue, content );
    }

    private void extractItemUseActions( String usedWithItemID, String itemUseContent ) {
        List<ItemAction> actions = new ItemActionDeserialiser(
            itemUseContent, itemUseActionTag, item, itemActionFactory ).extract();
        for( ItemAction action : actions )
            item.addOnUseActionFor( usedWithItemID, action );
    }

    private int findTagFrom( int start, String tag ) {
        return DeserialiserUtils.findTagFrom( start, tag, content );
    }

    private void extractItemVisibilityProperties() {
        String visibility = extractNewlineDelimitedValueFor( itemVisibilityTag );
        if( visibility.equals( "invisible" ) )
            item.setVisible( false );
        else
            item.setVisible( true );
    }

    private void extractItemExamineProperties() {
        item.setExamineText( extractNewlineDelimitedValueFor( itemExamineMessageTag ) );
        if( findTagWithNoArgument( itemExamineActionIsNotRepeatableTag ) )
            item.setExamineActionIsNotRepeatable();
        extractItemOnExamineActions();
    }

    private void extractItemOnExamineActions() {
        List<ItemAction> actions = new ItemActionDeserialiser( content,
            itemOnExamineActionTag, item, itemActionFactory ).extract();
        for( ItemAction action : actions )
            item.addOnExamineAction( action );
    }

    private void extractTalkPhraseInfo() {
        talkPhraseSink = item.getTalkPhraseSink();
        if( talkPhraseSink != null ) {
            extractInitialTalkPhrases();
            extractTalkResponses();
            extractFollowUpPhrases();
            extractTalkActions( item );
        }
    }

    private void extractInitialTalkPhrases() {
        for( IdAndArgPair pair : extractAllIdAndArgPairs( itemInitialTalkPhraseTag ) )
            talkPhraseSink.addInitialPhrase( pair.id,
                                             pair.arg.split( argumentSeperator )[0],
                                             pair.arg.split( argumentSeperator )[1] );
    }

    private void extractTalkResponses() {
        for( IdAndArgPair pair : extractAllIdAndArgPairs( itemTalkResponseToTag ) )
            talkPhraseSink.addResponse( pair.id, pair.arg );
    }

    private void extractFollowUpPhrases() {
        int tagLoc = DeserialiserUtils.NOT_FOUND;
        while( (tagLoc = findTagFrom( tagLoc, itemTalkFollowUpPhraseTag )) != DeserialiserUtils.NOT_FOUND )
            extractSingleFollowUpPhrase( tagLoc );
    }

    private void extractTalkActions( Item item ) {
        for( IdAndArgPair pair : extractAllIdAndArgPairs( itemTalkActionTag ) ) {
            ItemAction itemAction = itemActionFactory.create( pair.arg, item );
            talkPhraseSink.addActionInResponseTo( pair.id, itemAction );
        }
    }

    private void extractSingleFollowUpPhrase( int tagLoc ) {
        int startOfId = tagLoc + itemTalkFollowUpPhraseTag.length();
        int argumentSeperatorIndex = findTagFrom( startOfId, argumentSeperator );
        if( argumentSeperatorIndex != DeserialiserUtils.NOT_FOUND ) {
            String parentPhraseId = content.substring( startOfId, argumentSeperatorIndex );
            int startOfNewId = argumentSeperatorIndex + 1;
            String restOfLine = extractValueUpToNewline( startOfNewId );
            if( restOfLine.indexOf( ":" ) == DeserialiserUtils.NOT_FOUND )
                talkPhraseSink.addFollowUpPhrase( parentPhraseId, restOfLine );
            else
                talkPhraseSink.addFollowUpPhrase( parentPhraseId,
                                                  restOfLine.split( argumentSeperator )[0],
                                                  restOfLine.split( argumentSeperator )[1],
                                                  restOfLine.split( argumentSeperator )[2] );
        }
    }

    class IdAndArgPair {
        public String id;
        public String arg;

        IdAndArgPair( String id, String arg ) {
            this.id = id;
            this.arg = arg;
        }
    }

    private List<IdAndArgPair> extractAllIdAndArgPairs( String tag ) {
        List<IdAndArgPair> pairs = new ArrayList<IdAndArgPair>();
        int currentLoc = DeserialiserUtils.NOT_FOUND;
        while( (currentLoc = findTagFrom( currentLoc, tag )) != DeserialiserUtils.NOT_FOUND ) {
            int startOfId = currentLoc + tag.length();
            IdAndArgPair pair = extractIdAndArgPair( startOfId );
            if( pair != null )
                pairs.add( pair );
        }
        return pairs;
    }

    private IdAndArgPair extractIdAndArgPair( int startOfId ) {
        int argumentSeperatorIndex = findTagFrom( startOfId, argumentSeperator );
        if( argumentSeperatorIndex != DeserialiserUtils.NOT_FOUND ) {
            String id = content.substring( startOfId, argumentSeperatorIndex );
            String arg = extractValueUpToNewline( argumentSeperatorIndex + 1 );
            return new IdAndArgPair( id, arg );
        }
        return null;
    }
}
