package com.chewielouie.textadventure;

public interface Item {
    public String description();
    public String name();
    public String countableNounPrefix();
    public String midSentenceCasedName();
    public void setName( String name );
    public boolean takeable();
    public void deserialise( String content );
}

