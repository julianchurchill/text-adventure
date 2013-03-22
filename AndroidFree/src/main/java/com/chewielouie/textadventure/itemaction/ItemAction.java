package com.chewielouie.textadventure.itemaction;

import java.util.List;

public interface ItemAction {
    public void enact();
    public String name();
    public List<String> arguments();
}

