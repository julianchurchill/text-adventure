package com.chewielouie.textadventure;

public class DeserialiserUtils {
    public static final int NOT_FOUND = -1;

    public static String extractNewlineDelimitedValueFor( String tag, String content ) {
        return extractNewlineDelimitedValueFor( tag, content, -1 );
    }

    public static String extractNewlineDelimitedValueFor( String tag, String content, int from ) {
        int startOfTag = content.indexOf( tag, from );
        if( startOfTag == NOT_FOUND  || isStartOfALine( content, startOfTag ) == false )
            return "";
        int endOfTag = content.indexOf( "\n", startOfTag );
        if( endOfTag == NOT_FOUND )
            endOfTag = content.length();
        return content.substring( startOfTag + tag.length(), endOfTag );
    }

    public static boolean isStartOfALine( String content, int index ) {
        return index == 0 ||
               (index > 0 && content.charAt( index-1 ) == '\n');
    }

    public static int findTag( String tag, String content ) {
        return DeserialiserUtils.findTagFrom( 0, tag, content );
    }

    public static int findTagFrom( int start, String tag, String content ) {
        return content.indexOf( tag, start + 1 );
    }

    public static String extractValueUpToNewline( int startOfValue, String content ) {
        int endOfTag = content.indexOf( "\n", startOfValue );
        if( endOfTag == NOT_FOUND )
            endOfTag = content.length();
        return content.substring( startOfValue, endOfTag );
    }

}
