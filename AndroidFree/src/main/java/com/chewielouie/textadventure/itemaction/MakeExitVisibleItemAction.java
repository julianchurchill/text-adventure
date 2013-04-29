package com.chewielouie.textadventure.itemaction;

import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.TextAdventureModel;
import java.util.ArrayList;
import java.util.List;

public class MakeExitVisibleItemAction implements ItemAction {
    private String exitID;
    private TextAdventureModel model;

    public MakeExitVisibleItemAction( String exitID, 
           TextAdventureModel model ) {
        this.exitID = exitID;
        this.model = model;
    }

    public TextAdventureModel model() {
        return model;
    }

    public void enact() {
        Exit exit = model.findExitByID( exitID );
        if( exit != null )
            exit.setVisible();
    }

    public String name() {
        return "make exit visible";
    }

    public List<String> arguments() {
        List<String> args = new ArrayList<String>();
        args.add( this.exitID );
        return args;
    }
}

