package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.item.Item;

public class UseWithSpecificItem implements Action {
    private List<Action> followUpActions = new ArrayList<Action>();
    private Item originalItem;
    private Item targetItem;
    private String userText = "";

    public UseWithSpecificItem( Item original, Item target ) {
        this.originalItem = original;
        this.targetItem = target;
    }

    public ActionFactory actionFactory() {
        return null;
    }

    public Item targetItem() {
        return targetItem;
    }

    public Item itemBeingUsed() {
        return originalItem;
    }

    public String label() {
        return targetItem.name();
    }

    public void trigger() {
        if( targetItem != null && originalItem != null )
            userText = createUsePrefix() + targetItem.useWith( originalItem );
    }

    private String createUsePrefix() {
        return "You use " + pronounAndSpacingFor( originalItem )
                          + originalItem.midSentenceCasedName() +
               " with "   + pronounAndSpacingFor( targetItem )
                          + targetItem.midSentenceCasedName() + ". ";
    }

    private String pronounAndSpacingFor( Item item ) {
        if( item.properNoun() )
            return "";
        return "the ";
    }

    public boolean userMustChooseFollowUpAction() {
        return false;
    }

    public List<Action> followUpActions() {
        return followUpActions;
    }

    public boolean userTextAvailable() {
        return true;
    }

    public String userText() {
        return userText;
    }

    public String name() {
        return "use with specific item";
    }

    @Override
    public boolean equals( Object o ) {
        if( !(o instanceof UseWithSpecificItem) )
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}

