package com.chewielouie.textadventure_common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.lang.StringBuffer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.AlignmentSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserActionHandler;
import com.chewielouie.textadventure.action.ActionFactory;

public class MainTextFormatter {
    private TextView text_output;
    private String mainText = "";
    private String itemsText = "";
    private List<Exit> exits;
    private SpannableStringBuilder builder;
    private String allText = "";
    private Context context;
    private final ScrollView scrollViewContainingTextView;
    private String previousMainText;
    private int oldDescriptionLineCount_t = 0;
    private int descriptionLineCount = 0;
    private TextAdventureModel model;
    private final UserActionHandler userActionHandler;
    private final ActionFactory actionFactory;

    public MainTextFormatter( TextView output, String text, String itemsText, List<Exit> exits,
            Context context, ScrollView scrollView, String previousMainText, int oldDescriptionLineCount_t,
            TextAdventureModel model, UserActionHandler userActionHandler, ActionFactory actionFactory ) {
        this.text_output = output;
        this.mainText = text;
        this.itemsText = itemsText;
        this.exits = exits;
        this.context = context;
        this.scrollViewContainingTextView = scrollView;
        this.previousMainText = previousMainText;
        this.oldDescriptionLineCount_t = oldDescriptionLineCount_t;
        this.model = model;
        this.userActionHandler = userActionHandler;
        this.actionFactory = actionFactory;
    }

    public String allText() {
        return allText;
    }

    public void format() {
        builder = new SpannableStringBuilder();
        addMainContentText();
        addItemsText();
        addExitsText();

        text_output.setText( builder, TextView.BufferType.SPANNABLE );
    }

    private void addMainContentText() {
        parseHTMLContent( replaceNewlinesWithHTMLBreaks( mainText ) );
        ensureAllImagesAreCentered();
        scrollToTopOfNewMainContent();
        allText += mainText;
    }

    private String replaceNewlinesWithHTMLBreaks( String t ) {
        return t.replace( "\n", "<br/>" );
    }

    private void scrollToTopOfNewMainContent() {
        text_output.setText( builder, TextView.BufferType.SPANNABLE );
        if( newDescriptionIsBigger() ) {
            if( newDescriptionStartsWithTheOld() )
                scrollToFirstNewDescriptionLine();
            else
                scrollToTopOfMainText();
        }
        else if( newDescriptionIsSmaller() || newDescriptionIsDifferent() )
            scrollToTopOfMainText();
        this.descriptionLineCount = text_output.getLineCount();
    }

    public int descriptionLineCount() {
        return this.descriptionLineCount;
    }

    private boolean newDescriptionIsBigger() {
        return text_output.getLineCount() > this.oldDescriptionLineCount_t;
    }

    private boolean newDescriptionIsSmaller() {
        return text_output.getLineCount() < this.oldDescriptionLineCount_t;
    }

    private boolean newDescriptionStartsWithTheOld() {
        return mainText.startsWith( previousMainText );
    }

    private boolean newDescriptionIsDifferent() {
        return mainText.equals( previousMainText ) == false;
    }

    private void scrollToFirstNewDescriptionLine() {
        final int lastLineInOldDescription = this.oldDescriptionLineCount_t - 1;
        scrollViewContainingTextView.post(new Runnable() {
            public void run() {
                Rect bounds = new Rect();
                if( lastLineInOldDescription > 0 ) {
                    int firstNewDescriptionLine = lastLineInOldDescription + 1;
                    text_output.getLineBounds( firstNewDescriptionLine, bounds );
                }
                scrollViewContainingTextView.smoothScrollTo( 0, bounds.top - textViewLineHeightPadding() );
            }
        });
    }

    private int textViewLineHeightPadding() {
        // TextView.getLineSpacineExtra() is not available until API level 16 (4.1)
        // so use getCompoundPaddingTop() as the next best thing
        return text_output.getCompoundPaddingTop();
    }

    private void scrollToTopOfMainText() {
        scrollViewContainingTextView.post(new Runnable() {
            public void run() {
                scrollViewContainingTextView.smoothScrollTo( 0, 0 );
            }
        });
    }

    private void parseHTMLContent( String textToParse ) {
        builder.append( Html.fromHtml( textToParse, imgGetter, null ) );
    }

    private ImageGetter imgGetter = new ImageGetter() {
        public Drawable getDrawable( String source ) {
            String drawableString = "drawable/" + removeImageExtension( source );
            int imageResource = context.getResources().getIdentifier( drawableString, null, context.getPackageName() );
            Drawable drawable = context.getResources().getDrawable( imageResource );
            drawable.setBounds( 0, 0, drawable.getIntrinsicWidth(),
                                      drawable.getIntrinsicHeight() );
            return drawable;
        }

        private String removeImageExtension( String filename ) {
            return filename.replace( ".png", "" );
        }
    };

    private void ensureAllImagesAreCentered() {
        Object[] obj = builder.getSpans(0, builder.length(), ImageSpan.class);
        if( obj != null ) {
            for (int i = 0; i < obj.length; i++) {
                int start = builder.getSpanStart(obj[i]);
                int end = builder.getSpanEnd(obj[i]);
                builder.setSpan( new AlignmentSpan.Standard( Layout.Alignment.ALIGN_CENTER ), start, end, 0 );
            }
        }
    }

    private void addItemsText() {
        if( itemsText != "" ) {
            allText += "\n" + itemsText;
            appendToSpannableStringBuilderWithItalicStyle( "\n" + itemsText );
        }
    }

    private void appendToSpannableStringBuilderWithItalicStyle( String toAppend ) {
        int startIndex = builder.length();
        builder.append( toAppend );
        builder.setSpan( new StyleSpan( Typeface.ITALIC ), startIndex, builder.length(), 0 );
    }

    private void addExitsText() {
        if( exits.size() == 0 ) {
            allText += "\nThere are no visible exits.";
            appendToSpannableStringBuilderWithItalicStyle( "\nThere are no visible exits." );
        }
        else
            addHyperLinkExits();
    }

    private void addHyperLinkExits() {
        allText += "\nThe following exits are visible: ";
        appendToSpannableStringBuilderWithItalicStyle( "\nThe following exits are visible: " );

        String prefix = "";
        for( Exit exit : orderForDisplay( exits ) ) {
            addHyperLinkForExit( prefix, exit );
            prefix = ", ";
        }
        terminateSpannableClickRegion();
        enableClickableLinkCallbacks();
    }

    private void addHyperLinkForExit( String prefix, Exit exit ) {
        int startIndex = builder.length() + prefix.length();
        int endIndex = startIndex + exit.label().length();
        String exitPhrase = prefix + exit.label();
        builder.append( exitPhrase );
        allText += exitPhrase;
        addExitActionHandler( startIndex, endIndex, exit );
        builder.setSpan( new ForegroundColorSpan( selectExitColor( exit ) ),
                      startIndex, endIndex, 0 );
    }

    private void terminateSpannableClickRegion() {
        // These additional spaces stop the spannable click region for the last
        // spannable from extending all the way to the edge of the text view
        // and to the bottom of the  text view too
        float widthOfASingleTab = text_output.getPaint().measureText( "\t" );
        String fullWidthLineOfTabs = "";
        for( float size = 0; size < text_output.getWidth(); size += widthOfASingleTab )
            fullWidthLineOfTabs += "\t";
        builder.append( fullWidthLineOfTabs );
    }

    private List<Exit> orderForDisplay( List<Exit> exits ) {
        List<Exit> sortedExits = new ArrayList<Exit>( exits );
        Collections.sort( sortedExits,
            new Comparator<Exit>() {
                @Override
                public int compare( Exit e1, Exit e2 ) {
                    return rankedValueOf( e1.directionHint() ) - rankedValueOf( e2.directionHint() );
                }
            });
        return sortedExits;
    }

    private int rankedValueOf( Exit.DirectionHint d ) {
        if(      d == Exit.DirectionHint.North ) return 10;
        else if( d == Exit.DirectionHint.West ) return 20;
        else if( d == Exit.DirectionHint.East ) return 30;
        else if( d == Exit.DirectionHint.South ) return 40;
        return 70;
    }

    private int PURPLE = Color.rgb( 130, 0, 186 );
    private int DARK_GREEN = Color.rgb( 0, 100, 0 );

    private int selectExitColor( Exit exit )
    {
        if(      exit.directionHint() == Exit.DirectionHint.North ) return PURPLE;
        else if( exit.directionHint() == Exit.DirectionHint.South ) return Color.RED;
        else if( exit.directionHint() == Exit.DirectionHint.East ) return Color.BLUE;
        else if( exit.directionHint() == Exit.DirectionHint.West ) return DARK_GREEN;
        return Color.MAGENTA;
    }

    private void addExitActionHandler( int startIndex, int endIndex, Exit exit ) {
        final Exit finalExit = exit;
        final UserActionHandler finalUserActionHandler = userActionHandler;
        final ActionFactory factory = actionFactory;
        final TextAdventureModel finalModel = model;
        ClickableSpan c = new ClickableSpan() {
            @Override
            public void onClick( View view ) {
                finalUserActionHandler.enact( factory.createExitAction( finalExit, finalModel ) );
            }
        };
        builder.setSpan( c, startIndex, endIndex, 0 );
    }

    private void enableClickableLinkCallbacks() {
        text_output.setMovementMethod( LinkMovementMethod.getInstance() );
    }
}

