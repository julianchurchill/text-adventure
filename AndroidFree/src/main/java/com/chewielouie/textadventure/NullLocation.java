package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.action.Action;

public class NullLocation implements ModelLocation {
    public void addExit( Exit exit ) {
    }

    public boolean exitable( Exit exit ) {
        return false;
    }

    public String exitDestinationFor( Exit exit ) {
        return "";
    }

    public String id() {
        return "";
    }

    public List<Exit> exits() {
        return new ArrayList<Exit>();
    }

    public String description() {
        return "";
    }

    public List<Action> actions() {
        return new ArrayList<Action>();
    }
}

