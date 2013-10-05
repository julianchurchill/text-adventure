package com.chewielouie.textadventure.item;

import com.chewielouie.textadventure.itemaction.ItemAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NormalItem implements Item, TalkPhraseSink, TalkPhraseSource {
    private String name = "";
    private String description = "";
    private String countableNounPrefix = "a";
    private String midSentenceCasedName = null;
    private boolean takeable = true;
    private String id = "";
    private Map<String, Set<ItemAction>> onUseActionsWithItem = new HashMap<String, Set<ItemAction>>();
    private Map<String, Boolean> useIsRepeatableFor = new HashMap<String, Boolean>();
    private Map<String, String> usedWithTextFor = new HashMap<String, String>();
    private Map<Item, Boolean> itemUsedWith = new HashMap<Item, Boolean>();
    private final static String itemsCannotBeUsedTogetherUsedWithText = "Nothing happens.";
    private final static String itemsAlreadyUsedText = "You have already done that.";
    private String canBeUsedWithTargetID;
    private String usedWithText = "";
    private boolean useIsRepeatable = true;
    private boolean used = false;
    private List<ItemAction> onUseActions = new ArrayList<ItemAction>();
    private boolean visible = true;
    private List<ItemAction> onExamineActions = new ArrayList<ItemAction>();
    private String examineText = "";
    private boolean examineActionIsRepeatable = true;
    private boolean examined = false;
    private boolean canTalkTo = false;
    private boolean plural = false;
    private boolean properNoun = false;

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

    public boolean properNoun() {
        return properNoun;
    }

    public void setProperNoun() {
        this.properNoun = true;
    }

    public void setPlural() {
        this.plural = true;
    }

    public boolean plural() {
        return this.plural;
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

    public List<String> canBeUsedWithItemIDs() {
        List<String> ids = new ArrayList<String>();
        if( this.canBeUsedWithTargetID != "" )
            ids.add( this.canBeUsedWithTargetID );
        return ids;
    }

    public void setUseIsNotRepeatable() {
        useIsRepeatable = false;
    }

    public boolean useIsNotRepeatable() {
        return !this.useIsRepeatable;
    }

    public String usedWithText() {
        return usedWithText;
    }

    public void setUsedWithText( String text ) {
        this.usedWithText = text;
    }

    public void setUsedWithTextFor( String withItemID, String text ) {
        usedWithTextFor.put( withItemID, text );
    }

    public void setUseIsNotRepeatableFor( String withItemID ) {
        useIsRepeatableFor.put( withItemID, false );
    }

    public void addOnUseActionFor( String withItemID, ItemAction action ) {
        if( onUseActionsWithItem.containsKey( withItemID ) == false )
            onUseActionsWithItem.put( withItemID , new HashSet<ItemAction>() );
        onUseActionsWithItem.get( withItemID ).add( action );
    }

    public String useWith( Item withItem ) {
        if( itemCanBeUsedAtAllWith( withItem ) == false )
            return itemsCannotBeUsedTogetherUsedWithText;

        if( itemCanBeUsedNowWith( withItem ) ) {
            executeActionsForId( onUseActionsWithItem, withItem.id() );
            itemUsedWith.put( withItem, true );
            return usedWithTextFor.get( withItem.id() );
        }

        return itemsAlreadyUsedText;
    }

    private boolean itemCanBeUsedAtAllWith( Item withItem ) {
        return onUseActionsWithItem.containsKey( withItem.id() ) ||
               usedWithTextFor.containsKey( withItem.id() );
    }

    private boolean itemCanBeUsedNowWith( Item withItem ) {
        return !hasItemBeenUsedWith( withItem ) || isUseRepeatableFor( withItem.id() );
    }

    private boolean hasItemBeenUsedWith( Item withItem ) {
        if( itemUsedWith.containsKey( withItem ) )
            return itemUsedWith.get( withItem );
        return false;
    }

    private boolean isUseRepeatableFor( String withItemID ) {
        if( useIsRepeatableFor.containsKey( withItemID ) )
            return useIsRepeatableFor.get( withItemID );
        return true;
    }

    private void executeActionsForId( Map<String, Set<ItemAction>> actions,
        String id ) {
        if( actions.containsKey( id ) )
            for( ItemAction a : actions.get( id ) )
                a.enact();
    }

    public void use() {
        if( itemCanBeUsedNow() )
            for( ItemAction action : onUseActions )
                action.enact();
        else
            setUsedWithText( itemsAlreadyUsedText );
        used = true;
    }

    private boolean itemCanBeUsedNow() {
        return !used || useIsRepeatable;
    }

    public void addOnUseAction( ItemAction action ) {
        onUseActions.add( action );
    }

    public List<ItemAction> actions() {
        return onUseActions;
    }

    public void setVisible( boolean visible ) {
        this.visible = visible;
    }

    public boolean visible() {
        return visible;
    }

    public void examine() {
        if( itemCanBeExaminedNow() )
            for( ItemAction action : onExamineActions )
                action.enact();
        else
            setExamineText( "" );
        examined = true;
    }

    private boolean itemCanBeExaminedNow() {
        return !examined || examineActionIsRepeatable;
    }

    public void addOnExamineAction( ItemAction action ) {
        onExamineActions.add( action );
    }

    public String examineText() {
        return examineText;
    }

    public void setExamineText( String text ) {
        this.examineText = text;
    }

    public void setExamineActionIsNotRepeatable() {
        examineActionIsRepeatable = false;
    }

    public boolean examineActionIsNotRepeatable() {
        return !examineActionIsRepeatable;
    }

    public boolean canTalkTo() {
        return canTalkTo;
    }

    public TalkPhraseSource getTalkPhraseSource() {
        return this;
    }

    public TalkPhraseSink getTalkPhraseSink() {
        return this;
    }

    private class Phrase {
        private String shortContent;
        private String content;

        public Phrase( String content ) {
            this.content = content;
        }

        public Phrase( String shortContent, String content ) {
            this.shortContent = shortContent;
            this.content = content;
        }

        public String shortContent() {
            return shortContent;
        }

        public String content() {
            return content;
        }
    }

    private List<String> initialPhraseIds = new ArrayList<String>();
    private Map<String, Phrase> phrases = new HashMap<String, Phrase>();
    private Map<String, Phrase> responses = new HashMap<String, Phrase>();
    private Map<String, Set<String>> followUpPhrases = new HashMap<String, Set<String>>();
    private Map<String, Set<ItemAction>> phraseActions = new HashMap<String, Set<ItemAction>>();

    public void addInitialPhrase( String id, String shortContent, String content ) {
        canTalkTo = true;
        initialPhraseIds.add( id );
        phrases.put( id, new Phrase( shortContent, content ) );
    }

    public void addResponse( String id, String response ) {
        responses.put( id, new Phrase( response ) );
    }

    public void addFollowUpPhrase( String parentId, String newPhraseId, String shortContent, String content ) {
        addFollowUpPhrase( parentId, newPhraseId );
        phrases.put( newPhraseId, new Phrase( shortContent, content ) );
    }

    public void addFollowUpPhrase( String parentId, String newPhraseId ) {
        if( followUpPhrases.containsKey( parentId ) == false )
            followUpPhrases.put( parentId, new HashSet<String>() );
        followUpPhrases.get( parentId ).add( newPhraseId );
    }

    public void addActionInResponseTo( String id, ItemAction action ) {
        if( phraseActions.containsKey( id ) == false )
            phraseActions.put( id, new HashSet<ItemAction>() );
        phraseActions.get( id ).add( action );
    }

    public List<String> initialPhraseIds() {
        return initialPhraseIds;
    }

    public String shortPhraseById( String id ) {
        if( phrases.containsKey( id ) == false )
            return "";
        return phrases.get( id ).shortContent();
    }

    public String phraseById( String id ) {
        if( phrases.containsKey( id ) == false )
            return "";
        return phrases.get( id ).content();
    }

    public String responseToPhraseById( String id ) {
        if( responses.containsKey( id ) == false )
            return "";
        return responses.get( id ).content();
    }

    public List<String> followOnPhrasesIdsForPhraseById( String id ) {
        if( followUpPhrases.containsKey( id ) )
            return new ArrayList<String>( followUpPhrases.get( id ) );
        return new ArrayList<String>();
    }

    public void executeActionsForPhraseById( String id ) {
        executeActionsForId( phraseActions, id );
    }
}
