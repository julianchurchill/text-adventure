package com.chewielouie.textadventure_common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WalkthroughTextFormat
{
    private static final String lineEndingsRegex = "\\r?\\n|\\r";
    private static final String specialFlagsToMakeDotMatchNewlines = "(?s)";
    private static final Pattern scoreWithDigitsRegex = Pattern.compile(
        specialFlagsToMakeDotMatchNewlines + "# score (\\d+).*" );
    private static final Pattern scoreWithIncrementRegex = Pattern.compile(
        specialFlagsToMakeDotMatchNewlines + "# score \\+(\\d+).*" );
    private static final Pattern quickHintRegex = Pattern.compile(
        specialFlagsToMakeDotMatchNewlines + "# quick hint:(.*)" );

    public static String[] splitIntoLines( String text ) {
        return text.split( lineEndingsRegex );
    }

    public static boolean isPrintableLine( String line ) {
        return line.startsWith( "#" ) == false;
    }

    public static int updateCurrentScoreBasedOnLineOfText( int currentScore, String line ) {
        int newScore = currentScore;
        Matcher scoreWithIncrementMatcher = WalkthroughTextFormat.scoreWithIncrementRegex.matcher( line );
        if( scoreWithIncrementMatcher.matches() )
            newScore += Integer.parseInt( scoreWithIncrementMatcher.group(1) );
        else
        {
            Matcher scoreWithDigitsMatcher = WalkthroughTextFormat.scoreWithDigitsRegex.matcher( line );
            if( scoreWithDigitsMatcher.matches() )
                newScore = Integer.parseInt( scoreWithDigitsMatcher.group(1) );
        }
        return newScore;
    }

    public static String findQuickHint( String text, int score ) {
        String quickHint = "No hint available at this time";
        int scoreSoFarInWalkthrough = 0;
        String[] lines = WalkthroughTextFormat.splitIntoLines( text );
        for( int i = 0; i < lines.length; ++i ) {
            scoreSoFarInWalkthrough = WalkthroughTextFormat.updateCurrentScoreBasedOnLineOfText( scoreSoFarInWalkthrough, lines[i] );
            if( WalkthroughTextFormat.lineHasQuickHint( lines[i] ) )
                quickHint = WalkthroughTextFormat.extractQuickHint( lines[i] );
            if( scoreSoFarInWalkthrough > score )
                break;
        }
        return quickHint;
    }

    private static boolean lineHasQuickHint( String line ) {
        return WalkthroughTextFormat.quickHintRegex.matcher( line ).matches();
    }

    private static String extractQuickHint( String line ) {
        Matcher quickHintMatcher = WalkthroughTextFormat.quickHintRegex.matcher( line );
        if( quickHintMatcher.matches() )
            return quickHintMatcher.group( 1 );
        return "";
    }
}
