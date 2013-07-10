package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionFactory;
import com.chewielouie.textadventure.action.TakeAnItem;

public class TextAdventurePresenter implements RendersView, UserActionHandler, ModelEventSubscriber {
    private final TextAdventureView view;
    private final TextAdventureModel model;
    private List<Action> defaultActions = new ArrayList<Action>();
    private String actionText = "";
    private boolean inAnActionChain = false;

    public TextAdventurePresenter( TextAdventureView v,
           TextAdventureModel m, UserInventory inventory,
           ActionFactory a ) {
        this.view = v;
        this.model = m;
        if( model != null )
            model.subscribeForEvents( this );
        if( a != null )
            defaultActions.add( a.createShowInventoryAction( inventory, m ) );
    }

    public void render() {
        resetActionsToStartOfChain();
        updateView();
    }

    private void updateView() {
        if( actionText != "" )
            view.showMainText( model.currentLocationDescription()
                    + "\n" + actionText );
        else
            view.showMainText( model.currentLocationDescription() );
        view.showAvailableItemsText( model.availableItemsText() );
        view.showLocationExits( model.currentLocationExits() );
        view.currentScore( model.currentScore() );
        view.maximumScore( model.maximumScore() );
    }

    private void resetActionsToStartOfChain() {
        inAnActionChain = false;
        notifyViewOfAvailableActions();
    }

    private void notifyViewOfAvailableActions() {
        List<Action> availableActions = new ArrayList<Action>( defaultActions );
        availableActions.addAll( model.currentLocation().actions() );
        view.setActions( availableActions );
    }

    public void enact( Action action ) {
        action.trigger();

        if( action.userTextAvailable() ) {
            if( actionText != "" )
                actionText += "\n";
            actionText += action.userText() + "\n";
        }

        if( action.userMustChooseFollowUpAction() )
            continueActionChain( action );
        else
            resetActionsToStartOfChain();

        updateView();
    }

    private void continueActionChain( Action action ) {
        inAnActionChain = true;
        view.setActions( action.followUpActions() );
    }

    public List<Action> defaultActions() {
        return defaultActions;
    }

    public boolean inAnActionChain() {
        return inAnActionChain;
    }

    public void cancelActionChain() {
        resetActionsToStartOfChain();
    }

    public void resetAndRender() {
        actionText = "";
        render();
    }

    public void currentLocationChanged() {
        resetAndRender();
    }

    public void clearDefaultActions() {
        defaultActions = new ArrayList<Action>();
    }
}

