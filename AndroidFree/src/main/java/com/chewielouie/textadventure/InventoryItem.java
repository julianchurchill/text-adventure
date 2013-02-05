package com.chewielouie.textadventure;

public class InventoryItem implements Item {
    private String name = "";
    private String description = "";

    public InventoryItem( String name, String description ) {
        this.name = name;
        this.description = description;
    }

    public String description() {
        return description;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals( Object o ) {
        if( !(o instanceof InventoryItem) )
            return false;
        InventoryItem other = (InventoryItem)o;
        return name.equals( other.name ) &&
               description.equals( other.description );
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        result = prime * result + description.hashCode();
        return 1;
    }
}

