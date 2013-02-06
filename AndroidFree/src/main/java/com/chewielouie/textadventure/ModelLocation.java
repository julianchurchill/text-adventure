package com.chewielouie.textadventure;

import java.util.List;
import com.chewielouie.textadventure.action.Action;

public interface ModelLocation {
    public void addExit( Exit exit );
    public boolean exitable( Exit exit );
    public String exitDestinationFor( Exit exit );
    public String id();
    public List<Exit> exits();
    public String description();
    public List<Action> actions();
    public void removeItem( Item item );
}


