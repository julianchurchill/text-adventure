package com.chewielouie.textadventure;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
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

    private static final int ABOUT_MENU_ITEM = 0;
    private static final int NEW_GAME_MENU_ITEM = 1;
    private static String saveFileName = "save_file_1";

    private RendersView rendersView;
    private boolean externallySuppliedViewRenderer = false;
    private UserActionHandler userActionHandler;
    private boolean externallySuppliedUserActionHandler = false;
    private List<Exit> exits = new ArrayList<Exit>();
    private TextView main_text_output;
    private TextView score_text_view;
    private String mainTextContent = "";
    private Map<TextView,Exit> directions_and_exits =
        new HashMap<TextView,Exit>();
    private Map<Button,Action> actionButtons = new HashMap<Button,Action>();
    private LinearLayout available_actions_view;
    private int currentScore = 0;
    private int maximumScore = 0;
    private BasicModel model;

    public TextAdventureActivity() {
    }

    public TextAdventureActivity( RendersView r ) {
        this();
        this.rendersView = r;
        this.externallySuppliedViewRenderer = true;
    }

    public TextAdventureActivity( UserActionHandler u ) {
        this();
        this.userActionHandler = u;
        this.externallySuppliedUserActionHandler = true;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        main_text_output = findTextView( R.id.main_text_output );
        score_text_view = findTextView( R.id.ruby_count );
        available_actions_view = (LinearLayout)findViewById( R.id.available_actions );
    }

    @Override
    public void onResume() {
        super.onResume();

        if( saveFileExists() )
            loadGame();
        else
            createNewGame();
    }

    private boolean saveFileExists() {
        String[] files = fileList();
        for( String file : files )
            if( file.equals( saveFileName ) )
                return true;
        return false;
    }

    private void loadGame() {
        try {
            FileInputStream inputStream = openFileInput( saveFileName );
            JsonReader jr = new JsonReader( inputStream );
            model = (BasicModel) jr.readObject();
            jr.close();
        } catch( FileNotFoundException e ) {
            System.err.println("exception thrown: " + e.toString() );
        } catch( IOException e ) {
            System.err.println("exception thrown: " + e.toString() );
        }
        setupPresenter();
    }

    private void createNewGame() {
        createNewGameModel();
        setupPresenter();
    }

    private void createNewGameModel() {
        model = new BasicModel();
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

    private void setupPresenter() {
        TextAdventurePresenter p = new TextAdventurePresenter( this, model,
               (UserInventory)model );
        if( externallySuppliedViewRenderer == false )
            this.rendersView = p;
        if( externallySuppliedUserActionHandler == false )
            this.userActionHandler = p;

        rendersView.render();
    }

    private TextView findTextView( int id ) {
        return (TextView)findViewById( id );
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

    @Override
    public boolean onPrepareOptionsMenu( Menu menu )
    {
        menu.clear();
        menu.add( Menu.NONE, ABOUT_MENU_ITEM, Menu.NONE, getText( R.string.about ) );
        menu.add( Menu.NONE, NEW_GAME_MENU_ITEM, Menu.NONE, getText( R.string.new_game ) );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        boolean retVal = true;
        switch( item.getItemId() )
        {
            case ABOUT_MENU_ITEM:
                showAboutDialog();
                break;
            case NEW_GAME_MENU_ITEM:
                showNewGameConfirmationDialog();
                break;
            default:
                retVal = super.onOptionsItemSelected( item );
                break;
        }
        return retVal;
    }

    private void showAboutDialog() {
        Dialog dialog = new Dialog( this );
        dialog.setContentView( R.layout.about_dialog );

        String versionName = "";
        try {
            versionName = getPackageManager().getPackageInfo( getPackageName(), 0 ).versionName;
        }
        catch ( PackageManager.NameNotFoundException e ) {
        }
        dialog.setTitle( getResources().getString( R.string.app_name ) +
                         " " + versionName );

        dialog.show();
    }

    private void showNewGameConfirmationDialog() {
        new AlertDialog.Builder( this )
            .setIcon( android.R.drawable.ic_dialog_alert )
            .setTitle( R.string.new_game_title )
            .setMessage( R.string.new_game_confirmation_dialog_text )
            .setPositiveButton( R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick( DialogInterface dialog, int which ) {
                    createNewGame();
                }

            })
            .setNegativeButton( R.string.no, null )
            .show();
    }

    public void currentScore( int score ) {
        currentScore = score;
        updateScore();
    }

    private void updateScore() {
        score_text_view.setText( Integer.toString( currentScore ) + "/" +
                                 Integer.toString( maximumScore ) );
    }

    public void maximumScore( int score ) {
        maximumScore = score;
        updateScore();
    }

    // @See http://stackoverflow.com/questions/3988478/block-back-button-in-android?lq=1
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (  Integer.valueOf(android.os.Build.VERSION.SDK) < 7 //Instead use android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            // Take care of calling this method on earlier versions of
            // the platform where it doesn't exist.
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if( userActionHandler.inAnActionChain() )
            userActionHandler.cancelActionChain();
        else
            finish();
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            FileOutputStream outputStream = openFileOutput( saveFileName, Context.MODE_PRIVATE );
            JsonWriter jw = new JsonWriter( outputStream );
            jw.write( model );
            jw.close();
        } catch( FileNotFoundException e ) {
        } catch( IOException e ) {
        }
    }
}

