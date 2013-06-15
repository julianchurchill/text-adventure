package com.chewielouie.textadventure.serialisation;

import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.TalkPhraseSink;
import com.chewielouie.textadventure.itemaction.ItemAction;
import com.chewielouie.textadventure.itemaction.ItemActionFactory;
import java.util.ArrayList;
import java.util.List;

public class PlainTextItemDeserialiser implements ItemDeserialiser {
    private final int NOT_FOUND = -1;
    private final String argumentSeperator = ":";
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
    private final String itemOnExamineActionTag = "item on examine action:";
    private final String itemInitialTalkPhraseTag = "item talk initial phrase:";
    private final String itemTalkResponseToTag = "item talk response to:";
    private final String itemTalkFollowUpPhraseTag = "item talk follow up phrase to:";
    private Item item;
    private String content;
    private ItemActionFactory itemActionFactory;

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

        if( findTagWithNoArgument( itemIsUntakeableTag ) )
            item.setUntakeable();

        extractTalkPhraseInfo();
    }

    private void extractItemUseProperties() {
        item.setCanBeUsedWith( extractNewlineDelimitedValueFor( itemCanBeUsedWithTag ) );
        item.setUsedWithText( extractNewlineDelimitedValueFor( itemSuccessfulUseMessageTag ) );

        if( findTagWithNoArgument( itemUseIsNotRepeatableTag ) )
            item.setUseIsNotRepeatable();

        extractItemUseActions();
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
        List<ItemAction> actions =
            extractMultipleActions( itemOnExamineActionTag );
        for( ItemAction action : actions )
            item.addOnExamineAction( action );
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

    private void extractItemUseActions() {
        List<ItemAction> actions = extractMultipleActions( itemUseActionTag );
        for( ItemAction action : actions )
            item.addOnUseAction( action );
    }

    private List<ItemAction> extractMultipleActions( String triggerEventTag ) {
        List<ItemAction> actions = new ArrayList<ItemAction>();
        if( itemActionFactory != null ) {
            int nextTag = -1;
            while( (nextTag=findTagFrom( nextTag, triggerEventTag )) != -1 )
                actions.add( createAction( nextTag, triggerEventTag ) );
        }
        return actions;
    }

    private ItemAction createAction( int startOfAction,
            String triggerEventTag ) {
        return itemActionFactory.create(
                    extractValueUpToNewline( startOfAction +
                                             triggerEventTag.length() ),
                    item );
    }

    private int findTagFrom( int start, String tag ) {
        return content.indexOf( tag, start + 1 );
    }

    private void extractTalkPhraseInfo() {
        TalkPhraseSink talkPhraseSink = item.getTalkPhraseSink();
        if( talkPhraseSink != null ) {
            extractInitialTalkPhrases( talkPhraseSink );
            extractTalkResponses( talkPhraseSink );
            extractFollowUpPhrases( talkPhraseSink );
        }
    }

    private void extractInitialTalkPhrases( TalkPhraseSink talkPhraseSink ) {
        for( IdAndArgPair pair : extractAllIdAndArgPairs( itemInitialTalkPhraseTag,
                                                          talkPhraseSink ) )
            talkPhraseSink.addInitialPhrase( pair.id, pair.arg );
    }

    private void extractTalkResponses( TalkPhraseSink talkPhraseSink ) {
        for( IdAndArgPair pair : extractAllIdAndArgPairs( itemTalkResponseToTag,
                                                          talkPhraseSink ) )
            talkPhraseSink.addResponse( pair.id, pair.arg );
    }

    private void extractFollowUpPhrases( TalkPhraseSink talkPhraseSink ) {
        int tagLoc = findTag( itemTalkFollowUpPhraseTag );
        if( tagLoc != NOT_FOUND ) {
            int startOfId = tagLoc + itemTalkFollowUpPhraseTag.length();
            int argumentSeperatorIndex = findTagFrom( startOfId, argumentSeperator );
            if( argumentSeperatorIndex != NOT_FOUND ) {
                String parentPhraseId = content.substring( startOfId, argumentSeperatorIndex );
                int startOfNewId = argumentSeperatorIndex + 1;
                IdAndArgPair pair = extractIdAndArgPair( startOfNewId );
                if( pair != null )
                    talkPhraseSink.addFollowUpPhrase( parentPhraseId, pair.id, pair.arg );
            }
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

    private List<IdAndArgPair> extractAllIdAndArgPairs( String tag, TalkPhraseSink talkPhraseSink ) {
        List<IdAndArgPair> pairs = new ArrayList<IdAndArgPair>();
        int currentLoc = NOT_FOUND;
        while( (currentLoc = findTagFrom( currentLoc, tag )) != NOT_FOUND ) {
            int startOfId = currentLoc + tag.length();
            IdAndArgPair pair = extractIdAndArgPair( startOfId );
            if( pair != null )
                pairs.add( pair );
        }
        return pairs;
    }

    private IdAndArgPair extractIdAndArgPair( int startOfId ) {
        int argumentSeperatorIndex = findTagFrom( startOfId, argumentSeperator );
        if( argumentSeperatorIndex != NOT_FOUND ) {
            String id = content.substring( startOfId, argumentSeperatorIndex );
            String arg = extractValueUpToNewline( argumentSeperatorIndex + 1 );
            return new IdAndArgPair( id, arg );
        }
        return null;
    }
}
