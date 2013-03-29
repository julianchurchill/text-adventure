package com.chewielouie.textadventure;

public class StdoutLogger implements Logger {
    public void log( String text ) {
        System.out.println( text );
    }
}

