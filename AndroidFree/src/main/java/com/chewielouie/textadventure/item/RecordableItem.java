package com.chewielouie.textadventure.item;

import java.util.List;
import com.chewielouie.textadventure.ActionHistory;
import com.chewielouie.textadventure.itemaction.ItemAction;

public class RecordableItem implements Item {
    private Item itemToRecord;
    private ActionHistory actionHistory;

	public RecordableItem( Item itemToRecord, ActionHistory history ) {
        this.itemToRecord = itemToRecord;
        this.actionHistory = history;
	}

    public Item item() {
        return itemToRecord;
    }

    public ActionHistory actionHistory() {
        return actionHistory;
    }

    public String description() {
        return itemToRecord.description();
    }

    public void setDescription( String description ) {
        itemToRecord.setDescription( description );
    }

    public String name() {
        return itemToRecord.name();
    }

    public void setName( String name ) {
        itemToRecord.setName( name );
    }

    public String id() {
        return itemToRecord.id();
    }

    public void setId( String id ) {
        itemToRecord.setId( id );
    }

    public String countableNounPrefix() {
        return itemToRecord.countableNounPrefix();
    }

    public void setCountableNounPrefix( String prefix ) {
        itemToRecord.setCountableNounPrefix( prefix );
    }

    public String midSentenceCasedName() {
        return itemToRecord.midSentenceCasedName();
    }

    public void setMidSentenceCasedName( String casedName ) {
        itemToRecord.setMidSentenceCasedName( casedName );
    }

    public boolean takeable() {
        return itemToRecord.takeable();
    }

    public void setUntakeable() {
        itemToRecord.setUntakeable();
    }

    public boolean canBeUsedWith( Item item ) {
        return itemToRecord.canBeUsedWith( item );
    }

    public void setCanBeUsedWith( String itemID ) {
        itemToRecord.setCanBeUsedWith( itemID );
    }

    public List<String> canBeUsedWithItemIDs() {
        return itemToRecord.canBeUsedWithItemIDs();
    }

    public String usedWithText() {
        return itemToRecord.usedWithText();
    }

    public void setUsedWithText( String text ) {
        itemToRecord.setUsedWithText( text );
    }

    public void setUseIsNotRepeatable() {
        itemToRecord.setUseIsNotRepeatable();
    }

    public boolean useIsNotRepeatable() {
        return itemToRecord.useIsNotRepeatable();
    }

    public void addOnUseAction( ItemAction action ) {
        itemToRecord.addOnUseAction( action );
    }

    public void use() {
        itemToRecord.use();
    }

    public List<ItemAction> actions() {
        return itemToRecord.actions();
    }

    public void setVisible( boolean visible ) {
        itemToRecord.setVisible( visible );
    }

    public boolean visible() {
        return itemToRecord.visible();
    }

    public void examine() {
        itemToRecord.examine();
    }

    public void addOnExamineAction( ItemAction action ) {
        itemToRecord.addOnExamineAction( action );
    }

    public String examineText() {
        return itemToRecord.examineText();
    }

    public void setExamineText( String text ) {
        itemToRecord.setExamineText( text );
    }

    public void setExamineActionIsNotRepeatable() {
        itemToRecord.setExamineActionIsNotRepeatable();
    }

    public boolean examineActionIsNotRepeatable() {
        return itemToRecord.examineActionIsNotRepeatable();
    }
}
