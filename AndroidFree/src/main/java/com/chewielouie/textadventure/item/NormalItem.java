package com.chewielouie.textadventure.item;

import com.chewielouie.textadventure.itemaction.ItemAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NormalItem implements Item, TalkPhraseSink, TalkPhraseSource {

    private class ItemUseInfo {
        private final static String itemsAlreadyUsedText = "You have already done that.";
        private Set<ItemAction> actions = new HashSet<ItemAction>();
        private boolean useIsRepeatable = true;
        private String usedWithText = "";
        private boolean hasBeenUsed = false;

        public void setUsedWithText( String text ) {
            usedWithText = text;
        }

        public void setUseIsNotRepeatable() {
            useIsRepeatable = false;
        }

        public void setHasBeenUsed() {
            hasBeenUsed = true;
        }

        public void addOnUseAction( ItemAction action ) {
            actions.add( action );
        }

        public boolean itemCanBeUsedNow() {
            return !hasBeenUsed || useIsRepeatable;
        }

        public String use() {
            if( itemCanBeUsedNow() ) {
                for( ItemAction a : actions )
                    a.enact();
                hasBeenUsed = true;
                return usedWithText;
            }
            return itemsAlreadyUsedText;
        }
    }

    private final static String itemsCannotBeUsedTogetherUsedWithText = "Nothing happens.";
    private String name = "";
    private String description = "";
    private String countableNounPrefix = "a";
    private String midSentenceCasedName = null;
    private boolean takeable = true;
    private String id = "";
    private Map<String, ItemUseInfo> itemUseInfos = new HashMap<String, ItemUseInfo>();
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

    public void addOnUseAction( ItemAction action ) {
        onUseActions.add( action );
    }

    public void use() {
        if( itemCanBeUsedNow() )
            for( ItemAction action : onUseActions )
                action.enact();
        else
            setUsedWithText( "You have already done that." );
        used = true;
    }

    private boolean itemCanBeUsedNow() {
        return !used || useIsRepeatable;
    }

    public List<ItemAction> actions() {
        return onUseActions;
    }

    public void setUsedWithTextFor( String withItemID, String text ) {
        getItemUseInfo( withItemID ).setUsedWithText( text );
    }

    private ItemUseInfo getItemUseInfo( String withItemID ) {
        if( itemUseInfos.containsKey( withItemID ) == false )
            itemUseInfos.put( withItemID, new ItemUseInfo() );
        return itemUseInfos.get( withItemID );
    }

    public void setUseIsNotRepeatableFor( String withItemID ) {
        getItemUseInfo( withItemID ).setUseIsNotRepeatable();
    }

    public void addOnUseActionFor( String withItemID, ItemAction action ) {
        getItemUseInfo( withItemID ).addOnUseAction( action );
    }

    public String useWith( Item withItem ) {
        if( itemCanBeUsedAtAllWith( withItem ) == false )
            return itemsCannotBeUsedTogetherUsedWithText;
        return getItemUseInfo( withItem.id() ).use();
    }

    private boolean itemCanBeUsedAtAllWith( Item withItem ) {
        return itemUseInfos.containsKey( withItem.id() );
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
        if( phraseActions.containsKey( id ) )
            for( ItemAction a : phraseActions.get( id ) )
                a.enact();
    }
}
