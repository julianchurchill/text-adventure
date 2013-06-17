package com.chewielouie.textadventure.item;

import com.chewielouie.textadventure.itemaction.ItemAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NormalItem implements Item, TalkPhraseSink, TalkPhraseSource {
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
    private boolean visible = true;
    private List<ItemAction> onExamineActions = new ArrayList<ItemAction>();
    private String examineText = "";
    private boolean examineActionIsRepeatable = true;
    private boolean examined = false;
    private boolean canTalkTo = false;

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
        private String content;

        public Phrase( String content ) {
            this.content = content;
        }

        public String content() {
            return content;
        }
    }

    private Map<String, Phrase> initialPhrases = new HashMap<String, Phrase>();

    public void addInitialPhrase( String id, String content ) {
        canTalkTo = true;
        initialPhrases.put( id, new Phrase( content ) );
    }

    public void addResponse( String id, String response ) {
    }

    public void addFollowUpPhrase( String parentId, String newPhraseId, String phrase ) {
    }

    public void addActionInResponseTo( String id, ItemAction action ) {
    }

    public List<String> initialPhraseIds() {
        List<String> ids = new ArrayList<String>();
        ids.addAll( initialPhrases.keySet() );
        return ids;
    }

    public String shortPhraseById( String id ) {
        return "";
    }

    public String phraseById( String id ) {
        return initialPhrases.get( id ).content();
    }

    public String responseToPhraseById( String id ) {
        return "";
    }

    public List<String> followOnPhrasesIdsForPhraseById( String id ) {
        return new ArrayList<String>();
    }

    public void executeActionsForPhraseById( String id ) {
    }
}
