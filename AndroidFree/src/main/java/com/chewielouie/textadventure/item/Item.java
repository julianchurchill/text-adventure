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
    public String usedWithText();
    public void setUsedWithText( String text );
    public void setUseIsNotRepeatable();
    public void addOnUseAction( ItemAction action );
    public void use();
    public List<ItemAction> actions();
}

