package com.chewielouie.textadventure;

public class TextAdventureActivity extends TextAdventureCommonActivity {

    public TextAdventureActivity() {
        super();
    }

    public TextAdventureActivity( RendersView r ) {
        super( r );
    }

    public TextAdventureActivity( BasicModelFactory modelFactory ) {
        super( modelFactory );
    }

    public TextAdventureActivity( RendersView r, BasicModelFactory modelFactory ) {
        super( r, modelFactory );
    }

    public TextAdventureActivity( UserActionHandler u ) {
        super( u );
    }
}

