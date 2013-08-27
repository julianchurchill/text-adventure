package com.chewielouie.textadventure_common;

import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.action.Action;
import java.util.List;

public interface BasicModelConverter {
    public List<Action> inferActionsFrom( TextAdventureModel model );
}

