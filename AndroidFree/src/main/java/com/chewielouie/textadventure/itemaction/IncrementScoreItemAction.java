package com.chewielouie.textadventure.itemaction;

import com.chewielouie.textadventure.TextAdventureModel;
import java.util.ArrayList;
import java.util.List;

public class IncrementScoreItemAction implements ItemAction {
    private TextAdventureModel model;

    public IncrementScoreItemAction( TextAdventureModel model ) {
        this.model = model;
    }

    public TextAdventureModel model() {
        return model;
    }

    public void enact() {
        model.setCurrentScore( model.currentScore() + 1 );
    }

    public String name() {
        return "increment score";
    }

    public List<String> arguments() {
        List<String> args = new ArrayList<String>();
        return args;
    }
}



