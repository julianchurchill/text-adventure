package com.chewielouie.textadventure.itemaction;

import com.chewielouie.textadventure.Logger;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.ItemDecorator;
import com.chewielouie.textadventure.ModelDecorator;

public class LoggableNormalItemActionFactory implements ItemActionFactory {
    private Logger logger;
    private TextAdventureModel model;
    private NormalItemActionFactory factory;

    public LoggableNormalItemActionFactory( Logger logger, TextAdventureModel model ) {
        this.logger = logger;
        this.model = model;
        this.factory = new NormalItemActionFactory( model );
    }

    public void setItemDecorator( ItemDecorator d ) {
        factory.setItemDecorator(d );
    }

    public void setModelDecorator( ModelDecorator d ) {
        factory.setModelDecorator(d );
    }

    public ItemAction create( String content, Item item ) {
        ItemAction action = factory.create( content, item );
        if( action instanceof NullItemAction )
            return new LoggableNullItemAction( logger, content );
        return new LoggableItemAction( action, logger, content, item );
    }
}

