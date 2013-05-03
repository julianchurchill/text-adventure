package com.chewielouie.textadventure;

public class RecordableModelDecorator implements ModelDecorator {
    private ActionHistory actionHistory;

    public RecordableModelDecorator( ActionHistory actionHistory ) {
        this.actionHistory = actionHistory;
    }

    public TextAdventureModel decorate( TextAdventureModel model ) {
        return new RecordableModel( model, actionHistory );
    }
}
