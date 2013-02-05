package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ShowInventory;

public class TextAdventurePresenter implements RendersView, UserActionHandler {
    private final TextAdventureView view;
    private final TextAdventureModel model;
    private List<Action> defaultActions = new ArrayList<Action>();
    private String actionText = "";

    public TextAdventurePresenter( TextAdventureView v,
           TextAdventureModel m ) {
        this.view = v;
        this.model = m;
        defaultActions.add( new ShowInventory( m ) );
    }

    public void render() {
        view.showMainText( model.currentLocationDescription() );
        view.showLocationExits( model.currentLocationExits() );
        view.setActions( defaultActions );
    }

    public void moveThroughExit( Exit exit ) {
        model.moveThroughExit( exit );
        actionText = "";
        render();
    }

    public void enact( Action action ) {
        action.trigger();
        if( action.userTextAvailable() ) {
            actionText += action.userText() + "\n\n";
            view.showMainText( model.currentLocationDescription()
                    + "\n\n" + actionText );
        }
        if( action.userMustChooseFollowUpAction() )
            view.giveUserImmediateActionChoice( action.followUpActions() );
    }

    public List<Action> defaultActions() {
        return defaultActions;
    }
}

