package com.chewielouie.textadventure.itemaction;

import com.chewielouie.textadventure.Logger;
import com.chewielouie.textadventure.item.Item;
import java.util.List;

public class LoggableItemAction implements ItemAction {
    private ItemAction action;
    private Logger logger;
    private String content;
    private Item item;

    public LoggableItemAction( ItemAction action, Logger logger, String content, Item item ) {
        this.action = action;
        this.logger = logger;
        this.content = content;
        this.item = item;
    }

    public void enact() {
        if( item != null )
            logger.log( "'" + action.name() + "' item action enacted on item id '" +
                item.id() + "' item name '" + item.name() + "' with content '" +
                content + "'" );
        else
            logger.log( "'" + action.name() + "' item action enacted on null item" +
                " with content '" + content + "'" );
        action.enact();
    }

    public String name() {
        return action.name();
    }

    public List<String> arguments() {
        return action.arguments();
    }
}

