package com.chewielouie.textadventure;

public class LocationExitFactory implements ExitFactory {
    public Exit create() {
        return new LocationExit();
    }
}

