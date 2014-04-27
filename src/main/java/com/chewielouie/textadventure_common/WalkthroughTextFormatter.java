package com.chewielouie.textadventure_common;

import java.lang.StringBuffer;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import com.chewielouie.textadventure_common.WalkthroughTextFormat;

public class WalkthroughTextFormatter
{
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
        String[] lines = WalkthroughTextFormat.splitIntoLines( text );
        StringBuffer buffer = new StringBuffer();
        for( int i = 0; i < lines.length; ++i )
        {
            if( i != (lines.length-1) )
                lines[i] += System.getProperty( "line.separator" );
            if( WalkthroughTextFormat.isPrintableLine( lines[i] ) )
                buffer.append( lines[i] );
            updateActivePositionInWalkthrough( lines[i] );
        }
        return buffer.toString();
    }

    private void updateActivePositionInWalkthrough( String line ) {
        observedScore = WalkthroughTextFormat.updateCurrentScoreBasedOnLineOfText( observedScore, line );
        if( WalkthroughTextFormat.isPrintableLine( line ) && observedScore < score )
            activeWalkthroughPosition += line.length();
    }
}
