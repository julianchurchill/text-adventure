package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ShowInventory;

public class TextAdventurePresenter implements RendersView, UserActionHandler {
    private final TextAdventureView view;
    private final TextAdventureModel model;
    private List<Action> defaultActions = new ArrayList<Action>();

    public TextAdventurePresenter( TextAdventureView v,
           TextAdventureModel m ) {
        this.view = v;
        this.model = m;
        defaultActions.add( new ShowInventory() );
    }

    public void render() {
        view.showLocationDescription( model.currentLocationDescription() );
        view.showLocationExits( model.currentLocationExits() );
        view.setActions( defaultActions );
    }

    public void moveThroughExit( Exit exit ) {
        model.moveThroughExit( exit );
        render();
    }

    public void enact( Action action ) {
        action.trigger();
        if( action.userMustChooseFollowUpAction() )
            view.giveUserImmediateActionChoice( action.followUpActions() );
    }
}

