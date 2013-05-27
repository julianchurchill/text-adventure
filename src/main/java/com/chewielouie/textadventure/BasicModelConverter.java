package com.chewielouie.textadventure;

import com.chewielouie.textadventure.action.Action;
import java.util.List;

public interface BasicModelConverter {
    public List<Action> inferActionsFrom( TextAdventureModel model );
}

