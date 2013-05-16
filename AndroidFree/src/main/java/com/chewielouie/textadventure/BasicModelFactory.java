package com.chewielouie.textadventure;

public class BasicModelFactory implements ModelFactory {
    public TextAdventureModel createModel() {
        return new BasicModel();
    }
}
