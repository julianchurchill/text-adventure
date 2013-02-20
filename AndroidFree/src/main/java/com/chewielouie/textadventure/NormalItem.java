package com.chewielouie.textadventure;

public class NormalItem implements Item {
    private String name = "";
    private String description = "";
    private String countableNounPrefix = "a";
    private String midSentenceCasedName = null;
    private boolean takeable = true;
    private String id = "";
    private String canBeUsedWithTargetID;
    private String successfulUseMessage = "";
    private boolean useIsRepeatable = true;

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

    public void setName( String name ) {
        this.name = name;
    }

    public void setUntakeable() {
        takeable = false;
    }

    public boolean takeable() {
        return takeable;
    }

    public String id() {
        return id;
    }

    public boolean useIsRepeatable() {
        return useIsRepeatable;
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

    public boolean canBeUsedWith( Item item ) {
        if( item.id() == "" )
            return false;
        return item.id().equals( canBeUsedWithTargetID );
    }

    public String usedWithText() {
        return successfulUseMessage;
    }

    public void deserialise( String content ) {
        new Deserialiser().parse( content );
    }

    class Deserialiser {
        private final String itemNameTag = "item name:";
        private final String itemDescriptionTag = "item description:";
        private final String itemIDTag = "item id:";
        private final String itemCountableNounPrefixTag = "item countable noun prefix:";
        private final String itemMidSentenceCasedNameTag = "item mid sentence cased name:";
        private final String itemIsUntakeableTag = "item is untakeable:";
        private final String itemCanBeUsedWithTag = "item can be used with:";
        private final String itemSuccessfulUseMessageTag = "item successful use message:";
        private final String itemUseIsNotRepeatableTag = "item use is not repeatable:";
        private String content;
        private int startOfLastFoundTag = -1;

        public void parse( String content ) {
            this.content = content;
            startOfLastFoundTag = -1;

            deserialiseItems();
        }

        private int findTag( String tag ) {
            return content.indexOf( tag, startOfLastFoundTag + 1 );
        }

        private String extractNewlineDelimitedValueFor( String tag ) {
            int startOfTag = findTag( tag );
            if( startOfTag == -1 )
                return "";
            startOfLastFoundTag = startOfTag;
            int endOfTag = content.indexOf( "\n", startOfTag );
            if( endOfTag == -1 )
                endOfTag = content.length();
            return content.substring( startOfTag + tag.length(), endOfTag );
        }

        private void deserialiseItems() {
            name = extractNewlineDelimitedValueFor( itemNameTag );
            description = extractNewlineDelimitedValueFor( itemDescriptionTag );
            id = extractNewlineDelimitedValueFor( itemIDTag );
            countableNounPrefix = extractNewlineDelimitedValueFor( itemCountableNounPrefixTag );
            String m = extractNewlineDelimitedValueFor( itemMidSentenceCasedNameTag );
            if( m != "" )
                midSentenceCasedName = m;

            if( findTagWithNoArgument( itemIsUntakeableTag ) )
                takeable = false;

            canBeUsedWithTargetID = extractNewlineDelimitedValueFor( itemCanBeUsedWithTag );
            successfulUseMessage = extractNewlineDelimitedValueFor( itemSuccessfulUseMessageTag );

            if( findTagWithNoArgument( itemUseIsNotRepeatableTag ) )
                useIsRepeatable = false;
        }

        private boolean findTagWithNoArgument( String tag ) {
            int startOfTag = findTag( tag );
            if( startOfTag != -1 ) {
                startOfLastFoundTag = startOfTag;
                return true;
            }
            return false;
        }
    }
}

