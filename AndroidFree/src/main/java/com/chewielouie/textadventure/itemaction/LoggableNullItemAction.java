package com.chewielouie.textadventure.itemaction;

import com.chewielouie.textadventure.Logger;

public class LoggableNullItemAction extends NullItemAction {
    private Logger logger;

    public LoggableNullItemAction( Logger logger, String content ) {
        super( content );
        this.logger = logger;
    }

    public void enact() {
        logger.log( "Warning: NullItemAction enacted with content '" + content + "'" );
        super.enact();
    }
}

