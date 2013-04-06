package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ShowInventory;
import com.chewielouie.textadventure.action.TakeAnItem;

public class TextAdventurePresenter implements RendersView, UserActionHandler {
    private final TextAdventureView view;
    private final TextAdventureModel model;
    private List<Action> defaultActions = new ArrayList<Action>();
    private String actionText = "";

    public TextAdventurePresenter( TextAdventureView v,
           TextAdventureModel m, UserInventory inventory ) {
        this.view = v;
        this.model = m;
        defaultActions.add( new ShowInventory( inventory, m ) );
    }

    public void render() {
        view.showMainText( model.currentLocationDescription() );
        view.showLocationExits( model.currentLocationExits() );
        notifyViewOfAvailableActions();
        view.currentScore( model.currentScore() );
        view.maximumScore( model.maximumScore() );
    }

    private void notifyViewOfAvailableActions() {
        List<Action> availableActions = new ArrayList<Action>( defaultActions );
        availableActions.addAll( model.currentLocation().actions() );
        view.setActions( availableActions );
    }

    public void moveThroughExit( Exit exit ) {
        model.moveThroughExit( exit );
        actionText = "";
        render();
    }

    public void enact( Action action ) {
        action.trigger();
        view.showLocationExits( model.currentLocationExits() );
        if( action.userTextAvailable() ) {
            actionText += action.userText() + "\n\n";
            view.showMainText( model.currentLocationDescription()
                    + "\n\n" + actionText );
        }
        if( action.userMustChooseFollowUpAction() )
            view.setActions( action.followUpActions() );
        else
            notifyViewOfAvailableActions();
        view.currentScore( model.currentScore() );
        view.maximumScore( model.maximumScore() );
    }

    public List<Action> defaultActions() {
        return defaultActions;
    }

    public boolean inAnActionChain() {
        return false;
    }

    public void cancelActionChain() {
    }
}

