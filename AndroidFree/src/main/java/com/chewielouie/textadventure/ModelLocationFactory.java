package com.chewielouie.textadventure;

public interface ModelLocationFactory {
    public ModelLocation create();
    public ModelLocation create( String name );
    public ModelLocation create( String name, String description );
}


