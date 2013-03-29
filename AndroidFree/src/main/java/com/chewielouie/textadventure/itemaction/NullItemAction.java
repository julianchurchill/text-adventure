package com.chewielouie.textadventure.itemaction;

import java.util.ArrayList;
import java.util.List;

public class NullItemAction implements ItemAction {
    private String content;

    public NullItemAction( String content ) {
        this.content = content;
    }

    public void enact() {
        System.out.println( "Warning: NullItemAction enacted with content '" + content + "'" );
    }

    public String name() {
        return "NullItemAction";
    }

    public List<String> arguments() {
        return new ArrayList<String>();
    }
}

