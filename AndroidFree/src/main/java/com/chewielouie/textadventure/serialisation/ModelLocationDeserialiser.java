package com.chewielouie.textadventure.serialisation;

import com.chewielouie.textadventure.ModelLocation;

public interface ModelLocationDeserialiser {
    public void deserialise( ModelLocation location, String content );
}

