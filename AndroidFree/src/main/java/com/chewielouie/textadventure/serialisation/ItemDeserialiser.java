package com.chewielouie.textadventure.serialisation;

import com.chewielouie.textadventure.item.Item;

public interface ItemDeserialiser {
    public void deserialise( Item item, String content );
}

