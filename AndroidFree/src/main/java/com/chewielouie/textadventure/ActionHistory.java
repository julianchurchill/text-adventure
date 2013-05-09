package com.chewielouie.textadventure;

import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionParameters;
import com.chewielouie.textadventure.item.Item;

public interface ActionHistory {
    public void addActionWithParameters( Action action, ActionParameters params );
}
