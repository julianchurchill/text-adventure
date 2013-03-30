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
    public void setVisible( boolean visible );
    public boolean visible();
    public void examine();
    public void addOnExamineAction( ItemAction action );
    public String examineText();
    public void setExamineText( String text );
}

