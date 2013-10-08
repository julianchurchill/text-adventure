package com.chewielouie.textadventure.item;

import com.chewielouie.textadventure.itemaction.ItemAction;
import java.util.List;

public interface Item {
    public String description();
    public void setDescription( String description );
    public String name();
    public void setName( String name );
    public String id();
    public void setId( String id );
    public String countableNounPrefix();
    public void setCountableNounPrefix( String prefix );
    public String midSentenceCasedName();
    public void setMidSentenceCasedName( String casedName );
    public boolean properNoun();
    public void setProperNoun();
    public void setPlural();
    public boolean plural();
    public boolean takeable();
    public void setUntakeable();

    public boolean canBeUsedWith( Item item );
    public void setCanBeUsedWith( String itemID );
    public List<String> canBeUsedWithItemIDs();
    public String usedWithText();
    public void setUsedWithText( String text );
    public void setUseIsNotRepeatable();
    public boolean useIsNotRepeatable();
    public void addOnUseAction( ItemAction action );
    public void use();
    public List<ItemAction> actions();

    public void setUsedWithTextFor( String withItemID, String text );
    public void setUseIsNotRepeatableFor( String withItemID );
    public void addOnUseActionFor( String withItemID, ItemAction action );
    public String useWith( Item withItem );

    public void setVisible( boolean visible );
    public boolean visible();
    public void examine();
    public void addOnExamineAction( ItemAction action );
    public String examineText();
    public void setExamineText( String text );
    public void setExamineActionIsNotRepeatable();
    public boolean examineActionIsNotRepeatable();
    public boolean canTalkTo();
    public TalkPhraseSource getTalkPhraseSource();
    public TalkPhraseSink getTalkPhraseSink();
}

