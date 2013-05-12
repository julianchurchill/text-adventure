package com.chewielouie.textadventure.action;

public interface ActionHistory {
    public void addActionWithParameters( Action action, ActionParameters params );
    public int size();
    public ActionRecord getRecord( int index );
    public void clear();
}
