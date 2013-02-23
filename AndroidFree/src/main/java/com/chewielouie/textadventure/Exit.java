package com.chewielouie.textadventure;

public interface Exit {
    public enum DirectionHint { DontCare, North, South, East, West };
    public String label();
    public String destination();
    public DirectionHint directionHint();
    public boolean visible();
}

