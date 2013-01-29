package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.List;

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
}

