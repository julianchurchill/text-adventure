package com.chewielouie.textadventure.serialisation;

import com.chewielouie.textadventure.Item;

public interface ItemDeserialiser {
    public void deserialise( Item item, String content );
}

