package com.chewielouie.textadventure.item;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.itemaction.ItemAction;

public class NormalItem implements Item {
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

    public void setUseIsNotRepeatable() {
        useIsRepeatable = false;
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
            usedWithText = "You have already done that.";
        used = true;
    }

    private boolean itemCanBeUsedNow() {
        return !used || (used && useIsRepeatable);
    }

    public void addOnUseAction( ItemAction action ) {
        onUseActions.add( action );
    }
}
