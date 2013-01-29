package com.chewielouie.textadventure;

import java.util.List;

public interface ModelLocation {
    public void addExit( Exit exit );
    public boolean exitable( Exit exit );
    public String exitDestinationFor( Exit exit );
    public String id();
    public List<Exit> exits();
    public String description();
}


