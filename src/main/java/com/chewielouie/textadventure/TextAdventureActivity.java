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
import android.os.Bundle;
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

public class TextAdventureActivity extends Activity implements TextAdventureView, OnClickListener {
    private RendersView rendersView;
    private UserActionHandler userActionHandler;
    private List<Exit> exits = new ArrayList<Exit>();
    private TextView top_direction_label;
    private TextView bottom_direction_label;
    private TextView right_direction_label;
    private TextView left_direction_label;
    private TextView main_text_output;
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
        top_direction_label = findTextView( R.id.top_direction_label );
        top_direction_label.setOnClickListener( this );
        bottom_direction_label = findTextView( R.id.bottom_direction_label );
        bottom_direction_label.setOnClickListener( this );
        right_direction_label = findTextView( R.id.right_direction_label );
        right_direction_label.setOnClickListener( this );
        left_direction_label = findTextView( R.id.left_direction_label );
        left_direction_label.setOnClickListener( this );
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
        ItemActionFactory itemActionFactory = new NormalItemActionFactory( model );
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
        main_text_output.setText( s );
        scrollToBottomOfMainText();
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

        Exit northExit = findExitWithDirectionHint( Exit.DirectionHint.North );
        Exit southExit = findExitWithDirectionHint( Exit.DirectionHint.South );
        Exit eastExit = findExitWithDirectionHint( Exit.DirectionHint.East );
        Exit westExit = findExitWithDirectionHint( Exit.DirectionHint.West );

        int exitWithNoDirHintIndex = setDirectionLabel( top_direction_label,
                northExit, 0 );
        exitWithNoDirHintIndex = setDirectionLabel( bottom_direction_label,
                southExit, exitWithNoDirHintIndex );
        exitWithNoDirHintIndex = setDirectionLabel( right_direction_label,
                eastExit, exitWithNoDirHintIndex );
        exitWithNoDirHintIndex = setDirectionLabel( left_direction_label,
                westExit, exitWithNoDirHintIndex );
    }

    private Exit findExitWithDirectionHint( Exit.DirectionHint d ) {
        for( Exit e : exits )
            if( e.directionHint() == d )
                return e;
        return null;
    }

    private int setDirectionLabel( TextView dir_label, Exit directionExit,
           int indexToStartLookingForUndirectedExits ) {
        int exitWithNoDirHintIndex = nextExitWithoutADirectionHint(
                indexToStartLookingForUndirectedExits );
        String label = "";
        directions_and_exits.remove( dir_label );
        if( directionExit != null )
            directions_and_exits.put( dir_label, directionExit );
        else if( exitWithNoDirHintIndex < exits.size() ) {
            directions_and_exits.put( dir_label, exits.get( exitWithNoDirHintIndex ) );
            exitWithNoDirHintIndex++;
        }
        if( directions_and_exits.containsKey( dir_label ) )
            label = directions_and_exits.get( dir_label ).label();
        dir_label.setText( label );
        return exitWithNoDirHintIndex;
    }

    private int nextExitWithoutADirectionHint( int startAt ) {
        int i = startAt;
        for( ; i < exits.size(); i++ )
            if( exits.get( i ).directionHint() == Exit.DirectionHint.DontCare )
                break;
        return i;
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
        if( v == top_direction_label )
            deliverExitActionFor( top_direction_label );
        else if( v == bottom_direction_label )
            deliverExitActionFor( bottom_direction_label );
        else if( v == right_direction_label )
            deliverExitActionFor( right_direction_label );
        else if( v == left_direction_label )
            deliverExitActionFor( left_direction_label );
        else if( v instanceof Button && actionButtons.containsKey( (Button)v ) )
            userActionHandler.enact( actionButtons.get( (Button)v ) );
    }

    private void deliverExitActionFor( TextView dir_label ) {
        if( exits.size() > 0 )
            if( directions_and_exits.get( dir_label ) != null )
                userActionHandler.moveThroughExit(
                   directions_and_exits.get( dir_label ) );
    }
}

