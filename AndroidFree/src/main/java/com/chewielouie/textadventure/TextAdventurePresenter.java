package com.chewielouie.textadventure;

interface TextAdventureView {
    public void showRoomText( String s );
}

interface TextAdventureModel {
    public String currentRoomText();
}

public class TextAdventurePresenter {
    private final TextAdventureView view;
    private final TextAdventureModel model;

    public TextAdventurePresenter( TextAdventureView v,
           TextAdventureModel m ) {
        this.view = v;
        this.model = m;
    }

    public void render() {
        view.showRoomText( model.currentRoomText() );
    }
}

