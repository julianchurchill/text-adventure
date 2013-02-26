package com.chewielouie.textadventure;

public class LocationExit implements Exit {
    private String label = new String();
    private String destination = new String();
    private DirectionHint directionHint = DirectionHint.DontCare;
    private boolean visible = true;
    private String id = "";
    private Deserialiser deserialiser = new Deserialiser();

    public LocationExit() {
    }

    public LocationExit( String label ) {
        this.label = label;
    }

    public LocationExit( String label, String destinationLabel ) {
        this( label );
        this.destination = destinationLabel;
    }

    public LocationExit( String label, String destinationLabel, DirectionHint d ) {
        this( label, destinationLabel );
        this.directionHint = d;
    }

    public String label() {
        return this.label;
    }

    public void setLabel( String label ) {
        this.label = label;
    }

    public String destination() {
        return this.destination;
    }

    public void setDestination( String destination ) {
        this.destination = destination;
    }

    public DirectionHint directionHint() {
        return this.directionHint;
    }

    public void setDirectionHint( DirectionHint d ) {
        this.directionHint = d;
    }

    public boolean visible() {
        return visible;
    }

    public void setInvisible() {
        visible = false;
    }

    public void setVisible() {
        visible = true;
    }

    public String id() {
        return id;
    }

    public void setID( String id ) {
        this.id = id;
    }

    @Override
    public boolean equals( Object o ) {
        if( !(o instanceof LocationExit) )
            return false;
        LocationExit other = (LocationExit)o;
        return label == other.label &&
               destination == other.destination &&
               directionHint == other.directionHint;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + label.hashCode();
        result = prime * result + destination.hashCode();
        result = prime * result + directionHint.hashCode();
        return result;
    }

    public void deserialise( String content ) {
        deserialiser.deserialise( this, content );
    }

    class Deserialiser {
        private final String exitLabelTag = "exit label:";
        private final String exitDestinationTag = "exit destination:";
        private final String exitDirectionHintTag = "exit direction hint:";
        private final String exitIsNotVisibleTag = "exit is not visible:";
        private final String exitIDTag = "exit id:";
        private int startOfLastFoundTag = -1;
        private String content;

        public void deserialise( Exit exit, String content ) {
            this.content = content;
            exit.setLabel( extractNewlineDelimitedValueFor( exitLabelTag ) );
            exit.setDestination(
                    extractNewlineDelimitedValueFor( exitDestinationTag ) );
            exit.setDirectionHint( stringToDirectionHint(
                extractNewlineDelimitedValueFor( exitDirectionHintTag ) ) );
            if( exitNotVisibleIsSpecifiedDiscardIt() )
                exit.setInvisible();
            exit.setID( extractExitID() );
        }

        private String extractNewlineDelimitedValueFor( String tag ) {
            int startOfTag = content.indexOf( tag, startOfLastFoundTag + 1 );
            if( startOfTag == -1 )
                return "";
            startOfLastFoundTag = startOfTag;
            int endOfTag = content.indexOf( "\n", startOfTag );
            if( endOfTag == -1 )
                endOfTag = content.length();
            return content.substring( startOfTag + tag.length(), endOfTag );
        }

        private boolean exitNotVisibleIsSpecifiedDiscardIt() {
            int startOfTag = content.indexOf( exitIsNotVisibleTag, startOfLastFoundTag + 1 );
            if( startOfTag != -1 ) {
                extractNewlineDelimitedValueFor( exitIsNotVisibleTag );
                return true;
            }
            return false;
        }

        private String extractExitID() {
            int startOfTag = content.indexOf( exitIDTag, startOfLastFoundTag + 1 );
            if( startOfTag != -1 )
                return extractNewlineDelimitedValueFor( exitIDTag );
            return "";
        }

        private Exit.DirectionHint stringToDirectionHint( String hint ) {
            if( hint.equals( "North" ) )
                return Exit.DirectionHint.North;
            if( hint.equals( "South" ) )
                return Exit.DirectionHint.South;
            if( hint.equals( "East" ) )
                return Exit.DirectionHint.East;
            if( hint.equals( "West" ) )
                return Exit.DirectionHint.West;
            return Exit.DirectionHint.DontCare;
        }
    }
}

