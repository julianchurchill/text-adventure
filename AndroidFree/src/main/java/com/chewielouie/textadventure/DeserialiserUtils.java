package com.chewielouie.textadventure;

public class DeserialiserUtils {
    public static String extractNewlineDelimitedValueFor( String tag, String content ) {
        return extractNewlineDelimitedValueFor( tag, content, -1 );
    }

    public static String extractNewlineDelimitedValueFor( String tag, String content, int from ) {
        int startOfTag = content.indexOf( tag, from );
        if( startOfTag == -1 || isStartOfALine( content, startOfTag ) == false )
            return "";
        int endOfTag = content.indexOf( "\n", startOfTag );
        if( endOfTag == -1 )
            endOfTag = content.length();
        return content.substring( startOfTag + tag.length(), endOfTag );
    }

    public static boolean isStartOfALine( String content, int index ) {
        return index == 0 ||
               (index > 0 && content.charAt( index-1 ) == '\n');
    }
}
