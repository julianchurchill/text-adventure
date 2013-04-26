package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.item.Item;

public class ExamineAnItem implements Action {
    private List<Action> followUpActions = new ArrayList<Action>();
    private List<Item> items = new ArrayList<Item>();

    public ExamineAnItem( List<Item> items, ActionFactory factory ) {
        this.items = items;
        if( factory != null )
            for( Item item : items )
                followUpActions.add( factory.createExamineAction( item ) );
    }

    public List<Item> items() {
        return items;
    }

    public String label() {
        return "Examine an item";
    }

    public void trigger() {
    }

    public boolean userMustChooseFollowUpAction() {
        return true;
    }

    public List<Action> followUpActions() {
        return followUpActions;
    }

    public boolean userTextAvailable() {
        return false;
    }

    public String userText() {
        return "";
    }

    @Override
    public boolean equals( Object o ) {
        if( !(o instanceof ExamineAnItem) )
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}



