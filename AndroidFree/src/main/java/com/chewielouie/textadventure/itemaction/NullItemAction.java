package com.chewielouie.textadventure.itemaction;

import java.util.ArrayList;
import java.util.List;

public class NullItemAction implements ItemAction {
    public void enact() {
    }

    public String name() {
        return "NullItemAction";
    }

    public List<String> arguments() {
        return new ArrayList<String>();
    }
}

