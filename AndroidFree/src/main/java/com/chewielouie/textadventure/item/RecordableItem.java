package com.chewielouie.textadventure.item;

import java.util.List;
import com.chewielouie.textadventure.ActionHistory;
import com.chewielouie.textadventure.itemaction.ItemAction;

public class RecordableItem implements Item {
	public RecordableItem( Item itemToRecord, ActionHistory history ) {
	}

    public String description() {return "";}
    public void setDescription( String description ) {}
    public String name() {return "";}
    public void setName( String name ) {}
    public String id() {return "";}
    public void setId( String id ) {}
    public String countableNounPrefix() {return "";}
    public void setCountableNounPrefix( String prefix ) {}
    public String midSentenceCasedName() {return "";}
    public void setMidSentenceCasedName( String casedName ) {}
    public boolean takeable() {return false;}
    public void setUntakeable() {}
    public boolean canBeUsedWith( Item item ) {return false;};
    public void setCanBeUsedWith( String itemID ) {}
    public List<String> canBeUsedWithItemIDs() {return null;}
    public String usedWithText() {return "";}
    public void setUsedWithText( String text ) {}
    public void setUseIsNotRepeatable() {}
    public boolean useIsNotRepeatable() {return false;}
    public void addOnUseAction( ItemAction action ) {}
    public void use() {}
    public List<ItemAction> actions() {return null;}
    public void setVisible( boolean visible ) {}
    public boolean visible() {return false;}
    public void examine() {}
    public void addOnExamineAction( ItemAction action ) {}
    public String examineText() {return "";}
    public void setExamineText( String text ) {}
    public void setExamineActionIsNotRepeatable() {}
    public boolean examineActionIsNotRepeatable() {return false;}
}
