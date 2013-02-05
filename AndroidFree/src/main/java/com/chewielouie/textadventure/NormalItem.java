package com.chewielouie.textadventure;

public class NormalItem implements Item {
    private String name = "";
    private String description = "";
    private String countableNounPrefix = "a";
    private String midSentenceCasedName = null;

    public NormalItem( String name, String description ) {
        this.name = name;
        this.description = description;
    }

    public NormalItem( String name, String description,
           String countableNounPrefix ) {
        this( name, description );
        this.countableNounPrefix = countableNounPrefix;
    }

    public NormalItem( String name, String description,
           String countableNounPrefix, String midSentenceCasedName ) {
        this( name, description, countableNounPrefix );
        this.midSentenceCasedName = midSentenceCasedName;
    }

    public String description() {
        return description;
    }

    public String name() {
        return name;
    }

    public String countableNounPrefix() {
        return countableNounPrefix;
    }

    public String midSentenceCasedName() {
        if( midSentenceCasedName == null )
            return name().toLowerCase();
        return midSentenceCasedName;
    }

    @Override
    public boolean equals( Object o ) {
        if( !(o instanceof NormalItem) )
            return false;
        NormalItem other = (NormalItem)o;
        return name.equals( other.name ) &&
               description.equals( other.description );
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        result = prime * result + description.hashCode();
        return result;
    }
}

