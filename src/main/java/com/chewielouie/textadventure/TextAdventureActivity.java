package com.chewielouie.textadventure;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.serialisation.ItemDeserialiser;
import com.chewielouie.textadventure.serialisation.PlainTextExitDeserialiser;
import com.chewielouie.textadventure.serialisation.PlainTextItemDeserialiser;
import com.chewielouie.textadventure.serialisation.PlainTextModelLocationDeserialiser;
import com.chewielouie.textadventure.item.ItemFactory;
import com.chewielouie.textadventure.item.NormalItemFactory;
import com.chewielouie.textadventure.itemaction.ItemActionFactory;
import com.chewielouie.textadventure.itemaction.NormalItemActionFactory;
import com.chewielouie.textadventure.itemaction.LoggableNormalItemActionFactory;

public class TextAdventureActivity extends Activity implements TextAdventureView, OnClickListener {
    private RendersView rendersView;
    private UserActionHandler userActionHandler;
    private List<Exit> exits = new ArrayList<Exit>();
    private TextView main_text_output;
    private String mainTextContent = "";
    private Map<TextView,Exit> directions_and_exits =
        new HashMap<TextView,Exit>();
    private Map<Button,Action> actionButtons = new HashMap<Button,Action>();
    private LinearLayout available_actions_view;

    public TextAdventureActivity() {
    }

    public TextAdventureActivity( RendersView r ) {
        this();
        this.rendersView = r;
    }

    public TextAdventureActivity( UserActionHandler u ) {
        this();
        this.userActionHandler = u;

    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        main_text_output = findTextView( R.id.main_text_output );
        available_actions_view = (LinearLayout)findViewById( R.id.available_actions );

        BasicModel model = createModel();
        TextAdventurePresenter p = new TextAdventurePresenter( this, model,
               (UserInventory)model );
        if( this.rendersView == null )
            this.rendersView = p;
        if( this.userActionHandler == null )
            this.userActionHandler = p;
    }

    private BasicModel createModel() {
        BasicModel model = new BasicModel();
        UserInventory inventory = model;
        Logger logger = new StdoutLogger();
        ItemActionFactory itemActionFactory = new LoggableNormalItemActionFactory( logger, model );
        ItemFactory itemFactory = new NormalItemFactory();
        ItemDeserialiser itemDeserialiser =
            new PlainTextItemDeserialiser( itemActionFactory );
        new PlainTextModelPopulator( model,
                                     new LocationFactory( inventory ),
                                     inventory,
                                     itemFactory,
                                     new PlainTextModelLocationDeserialiser(
                                         itemFactory, new LocationExitFactory(),
                                         itemDeserialiser,
                                         new PlainTextExitDeserialiser() ),
                                     itemDeserialiser,
                                     demoContent() );
        return model;
    }

    private String demoContent() {
        return readRawTextFile( R.raw.demo_model_content );
    }

    private String readRawTextFile( int resId ) {
        BufferedReader buffreader = new BufferedReader(
            new InputStreamReader( getResources().openRawResource( resId ) ) );
        String line;
        StringBuilder text = new StringBuilder();
        try {
            while (( line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            System.out.println("Exception thrown while reading resource file " +
                    "with id " + resId );
            System.out.println( e.getMessage() );
            return null;
        }
        return text.toString();
    }

    private TextView findTextView( int id ) {
        return (TextView)findViewById( id );
    }

    @Override
    public void onResume() {
        super.onResume();
        rendersView.render();
    }

    public void showMainText( String s ) {
        mainTextContent = s;
        updateMainText();
        scrollToBottomOfMainText();
    }

    private void updateMainText() {
        SpannableStringBuilder text = new SpannableStringBuilder(
                mainTextContent );
        addHyperLinkExits( text );

        enableClickableLinkCallbacks();
        main_text_output.setText( text, TextView.BufferType.SPANNABLE );
    }

    private void addHyperLinkExits( SpannableStringBuilder text ) {
        if( exits.size() > 0 )
            text.append( "\n\nThe following exits are visible: " );
        else
            text.append( "\n\nThere are no visible exits." );

        int color = Color.GREEN;
        String prefix = "";
        for( Exit exit : exits ) {
            int startIndex = text.length() + prefix.length();
            int endIndex = startIndex + exit.label().length();
            text.append( prefix + exit.label() );
            addExitActionHandler( text, startIndex, endIndex, exit );
            text.setSpan( new ForegroundColorSpan( color ),
                          startIndex, endIndex, 0 );
            color = rotateExitColor( color );
            prefix = ", ";
        }
    }

    private void addExitActionHandler( SpannableStringBuilder text,
            int startIndex, int endIndex, Exit exit ) {
        final Exit finalExit = exit;
        final UserActionHandler finalUserActionHandler = userActionHandler;
        ClickableSpan c = new ClickableSpan() {
            @Override
            public void onClick( View view ) {
                finalUserActionHandler.moveThroughExit( finalExit );
            }
        };
        text.setSpan( c, startIndex, endIndex, 0 );
    }

    private int rotateExitColor( int currentColor ) {
        if( currentColor == Color.GREEN ) return Color.BLUE;
        else if( currentColor == Color.BLUE ) return Color.RED;
        else if( currentColor == Color.RED ) return Color.GREEN;
        return Color.GREEN;
    }

    private void enableClickableLinkCallbacks() {
        main_text_output.setMovementMethod( LinkMovementMethod.getInstance() );
    }

    private void scrollToBottomOfMainText() {
        final ScrollView scrollView = (ScrollView)findViewById(
                R.id.main_text_output_scroll_view );
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.smoothScrollTo( 0, main_text_output.getBottom() );
            }
        });
    }

    public void showLocationExits( List<Exit> exits ) {
        this.exits = exits;

        updateMainText();
    }

    public void setActions( List<Action> actions ) {
        actionButtons.clear();
        available_actions_view.removeAllViews();
        for( Action action : actions )
            makeActionButton( action );
    }

    private void makeActionButton( Action action ) {
        Button button = new Button( this );
        button.setText( action.label() );
        button.setOnClickListener( this );
        LayoutParams lp = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
        available_actions_view.addView( button, lp );

        actionButtons.put( button, action );
    }

    public void onClick( View v ) {
        if( v instanceof Button && actionButtons.containsKey( (Button)v ) )
            userActionHandler.enact( actionButtons.get( (Button)v ) );
    }
}

