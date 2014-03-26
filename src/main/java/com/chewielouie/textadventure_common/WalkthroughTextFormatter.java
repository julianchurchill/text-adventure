package com.chewielouie.textadventure_common;

import java.lang.StringBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class WalkthroughTextFormatter
{
    private static final String specialFlagsToMakeDotMatchNewlines = "(?s)";
    private static final Pattern scoreWithDigitsRegex = Pattern.compile(
        specialFlagsToMakeDotMatchNewlines + "# score (\\d+).*" );
    private static final Pattern scoreWithIncrementRegex = Pattern.compile(
        specialFlagsToMakeDotMatchNewlines + "# score \\+(\\d+).*" );
    private static final String lineEndingsRegex = "\\r?\\n|\\r";

    private int observedScore = 0;
    private int activeWalkthroughPosition = 0;
    private int score = 0;

    public WalkthroughTextFormatter( TextView textView, String walkthroughText, int score )
    {
        this.score = score;
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append( removeNonPrintableWalkthroughLines( walkthroughText ) );
        int startIndex = 0;
        int endIndex = activeWalkthroughPosition;
        int flags = 0;
        builder.setSpan( new ForegroundColorSpan( Color.DKGRAY ), startIndex, endIndex, flags );
        textView.setText( builder, TextView.BufferType.SPANNABLE );
    }

    private String removeNonPrintableWalkthroughLines( String text ) {
        String[] lines = text.split( lineEndingsRegex );
        StringBuffer buffer = new StringBuffer();
        for( int i = 0; i < lines.length; ++i )
        {
            if( i != (lines.length-1) )
                lines[i] += System.getProperty( "line.separator" );
            if( isPrintableWalkthroughLine( lines[i] ) )
                buffer.append( lines[i] );
            updateActivePositionInWalkthrough( lines[i] );
        }
        return buffer.toString();
    }

    private void updateActivePositionInWalkthrough( String line ) {
        Matcher scoreWithIncrementMatcher = scoreWithIncrementRegex.matcher( line );
        if( scoreWithIncrementMatcher.matches() )
            observedScore += Integer.parseInt( scoreWithIncrementMatcher.group(1) );
        else
        {
            Matcher scoreWithDigitsMatcher = scoreWithDigitsRegex.matcher( line );
            if( scoreWithDigitsMatcher.matches() )
                observedScore = Integer.parseInt( scoreWithDigitsMatcher.group(1) );
        }
        if( isPrintableWalkthroughLine( line ) && observedScore < score )
            activeWalkthroughPosition += line.length();
    }

    private boolean isPrintableWalkthroughLine( String text ) {
        return text.startsWith( "#" ) == false;
    }
}
