package com.chewielouie.textadventure;

import java.util.List;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.item.Item;

public interface ModelLocation {
    public void addExit( Exit exit );
    public boolean exitable( Exit exit );
    public String exitDestinationFor( Exit exit );
    public String id();
    public void setId( String id );
    public String description();
    public String availableItemsText();
    public void setLocationDescription( String description );
    public List<Exit> visibleExits();
    public List<Exit> exitsIncludingInvisibleOnes();
    public List<Action> actions();
    public List<Item> items();
    public void addItem( Item item );
    public void removeItem( Item item );
    public void setX( int x );
    public void setY( int y );
    public int x();
    public int y();
    public void setAreaID( String id );
    public String areaID();
    public void setTextForFirstEntry( String text );
    public String contextualText();
    public void exited();
}


