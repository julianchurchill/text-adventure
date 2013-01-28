package com.chewielouie.textadventure;

public class TextAdventurePresenter implements RendersView, UserActionHandler {
    private final TextAdventureView view;
    private final TextAdventureModel model;

    public TextAdventurePresenter( TextAdventureView v,
           TextAdventureModel m ) {
        this.view = v;
        this.model = m;
    }

    public void render() {
        view.showLocationDescription( model.currentLocationDescription() );
        view.showLocationExits( model.currentLocationExits() );
    }

    public void moveThroughExit( String exitLabel ) {
        model.moveThroughExit( exitLabel );
        render();
    }
}

