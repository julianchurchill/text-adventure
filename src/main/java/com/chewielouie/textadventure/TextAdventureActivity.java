package com.chewielouie.textadventure;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.StringBuilder;
import java.lang.StringBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import android.widget.SeekBar;
import android.widget.TextView;
import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionFactory;
import com.chewielouie.textadventure.action.ActionHistory;
import com.chewielouie.textadventure.action.BasicActionHistory;
import com.chewielouie.textadventure.action.RecordableActionFactory;
import com.chewielouie.textadventure.action.UserActionFactory;
import com.chewielouie.textadventure.serialisation.ActionHistoryDeserialiser;
import com.chewielouie.textadventure.serialisation.ActionHistorySerialiser;
import com.chewielouie.textadventure.serialisation.ItemDeserialiser;
import com.chewielouie.textadventure.serialisation.PlainTextExitDeserialiser;
import com.chewielouie.textadventure.serialisation.PlainTextItemDeserialiser;
import com.chewielouie.textadventure.serialisation.PlainTextModelLocationDeserialiser;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.ItemFactory;
import com.chewielouie.textadventure.item.NormalItemFactory;
import com.chewielouie.textadventure.itemaction.ItemActionFactory;
import com.chewielouie.textadventure.itemaction.NormalItemActionFactory;
import com.chewielouie.textadventure.itemaction.LoggableNormalItemActionFactory;

public class TextAdventureActivity extends Activity implements TextAdventureView, OnClickListener {

    private static final int ABOUT_MENU_ITEM = 0;
    private static final int NEW_GAME_MENU_ITEM = 1;
    private static final int OPTIONS_MENU_ITEM = 2;
    private static String oldJSONFormatSaveFileName = "save_file_1";
    private static String actionHistorySaveFileName = "action_history_save_file_1";

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
    private BasicModel model = null;
    private UserInventory inventory = null;
    private ActionFactory actionFactory = null;
    private ActionHistory actionHistory = null;
    private ViewDisabler viewDisabler = null;
    private BasicModelFactory externalModelFactory = null;
    private BasicModelFactory internalModelFactory = null;
    private Logger logger = new StdoutLogger();

    public TextAdventureActivity() {
    }

    public TextAdventureActivity( RendersView r ) {
        this();
        this.rendersView = r;
        this.externallySuppliedViewRenderer = true;
    }

    public TextAdventureActivity( BasicModelFactory modelFactory ) {
        this();
        this.externalModelFactory = modelFactory;
    }

    public TextAdventureActivity( RendersView r, BasicModelFactory modelFactory ) {
        this();
        this.rendersView = r;
        this.externallySuppliedViewRenderer = true;
        this.externalModelFactory = modelFactory;
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

        if( saveJSONFileExists() ) {
            BasicModelV1_0ToActionListConverter c
                = new BasicModelV1_0ToActionListConverter( model, inventory,
                                                           actionFactory(), logger );
            JSONToActionListConverter j
                = new JSONToActionListConverter( this, oldJSONFormatSaveFileName, c );
            replayActions( j.actions() );
        }
    }

    private boolean saveJSONFileExists() {
        return fileExists( oldJSONFormatSaveFileName );
    }

    private boolean fileExists( String filename ) {
        String[] files = fileList();
        for( String file : files )
            if( file.equals( filename ) )
                return true;
        return false;
    }

    private boolean saveFileExists() {
        return fileExists( actionHistorySaveFileName );
    }

    private void loadGame() {
        createNewGame();
        replayActions( new ActionHistoryDeserialiser( actionFactory, inventory, model )
                            .deserialise( loadSerialisedActionHistory() ) );
    }

    private String loadSerialisedActionHistory() {
        StringBuffer serialisedHistory = new StringBuffer("");
        try {
            FileInputStream inputStream = openFileInput( actionHistorySaveFileName );
            int ch;
            while( (ch=inputStream.read()) != -1 )
                serialisedHistory.append( (char)ch );
        } catch( FileNotFoundException e ) {
            System.err.println("exception thrown: " + e.toString() );
        } catch( IOException e ) {
            System.err.println("exception thrown: " + e.toString() );
        }
        return serialisedHistory.toString();
    }

    private void replayActions( List<Action> actions ) {
        if( actions != null ) {
            viewDisabler.on();
            actionHistory.clear();
            for( Action action : actions )
                userActionHandler.enact( action );
            viewDisabler.off();
            rendersView.resetAndRender();
        }
    }

    private void createNewGame() {
        createNewGameModel();
        setupPresenter();
    }

    private void createNewGameModel() {
        model = (BasicModel)modelFactory().createModel();
        inventory = model;
        ItemActionFactory itemActionFactory = new LoggableNormalItemActionFactory( logger, model );
        ItemFactory itemFactory = new NormalItemFactory();
        ItemDeserialiser itemDeserialiser =
            new PlainTextItemDeserialiser( itemActionFactory );
        new PlainTextModelPopulator( model,
                                     new LocationFactory( inventory, actionFactory() ),
                                     inventory,
                                     itemFactory,
                                     new PlainTextModelLocationDeserialiser(
                                         itemFactory, new LocationExitFactory(),
                                         itemDeserialiser,
                                         new PlainTextExitDeserialiser() ),
                                     itemDeserialiser,
                                     demoContent() );
    }

    public BasicModelFactory modelFactory() {
        if( externalModelFactory != null )
            return externalModelFactory;
        else if( internalModelFactory == null )
            internalModelFactory = new BasicModelFactory();
        return internalModelFactory;
    }

    private ActionFactory actionFactory() {
        if( actionFactory == null ) {
            actionHistory = new BasicActionHistory();
            actionFactory = new RecordableActionFactory( new UserActionFactory(),
                                                         actionHistory );
        }
        return actionFactory;
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
        viewDisabler = new ViewDisabler( this );
        TextAdventurePresenter p = new TextAdventurePresenter(
            viewDisabler, model, (UserInventory)model, actionFactory() );
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
            text.append( "\nThe following exits are visible: " );
        else
            text.append( "\nThere are no visible exits." );

        String prefix = "";
        for( Exit exit : orderForDisplay( exits ) ) {
            int startIndex = text.length() + prefix.length();
            int endIndex = startIndex + exit.label().length();
            text.append( prefix + exit.label() );
            addExitActionHandler( text, startIndex, endIndex, exit );
            text.setSpan( new ForegroundColorSpan( selectExitColor( exit ) ),
                          startIndex, endIndex, 0 );
            prefix = ", ";
        }
        // This additional space stops the spannable click region for the last
        // spannable from extending all the way to the edge of the text view
        if( exits.size() > 0 )
            text.append( " " );
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
        else if( d == Exit.DirectionHint.South ) return 20;
        else if( d == Exit.DirectionHint.East ) return 30;
        else if( d == Exit.DirectionHint.West ) return 40;
        return 70;
    }

    private int selectExitColor( Exit exit )
    {
        if(      exit.directionHint() == Exit.DirectionHint.North ) return Color.GREEN;
        else if( exit.directionHint() == Exit.DirectionHint.South ) return Color.RED;
        else if( exit.directionHint() == Exit.DirectionHint.East ) return Color.BLUE;
        else if( exit.directionHint() == Exit.DirectionHint.West ) return Color.YELLOW;
        return Color.MAGENTA;
    }

    private void addExitActionHandler( SpannableStringBuilder text,
            int startIndex, int endIndex, Exit exit ) {
        final Exit finalExit = exit;
        final UserActionHandler finalUserActionHandler = userActionHandler;
        final ActionFactory factory = actionFactory();
        final TextAdventureModel finalModel = model;
        ClickableSpan c = new ClickableSpan() {
            @Override
            public void onClick( View view ) {
                finalUserActionHandler.enact( factory.createExitAction( finalExit,
                                                                        finalModel ) );
            }
        };
        text.setSpan( c, startIndex, endIndex, 0 );
    }

    public void useExit( Exit exit ) {
        userActionHandler.enact( actionFactory().createExitAction( exit, model ) );
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

    public Map<Button,Action> actionButtons() {
        return actionButtons;
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
        menu.add( Menu.NONE, OPTIONS_MENU_ITEM, Menu.NONE, getText( R.string.options ) );
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
            case OPTIONS_MENU_ITEM:
                showOptionsDialog();
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

    private void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View options_view = getLayoutInflater().inflate( R.layout.options_dialog, null );
        builder.setView( options_view );
        builder.setTitle( R.string.options_title );
        final TextView options_font_example_text = (TextView)options_view.findViewById( R.id.options_font_example_text );
        options_font_example_text.setTextSize( main_text_output.getTextSize() );
        final SeekBar options_font_size_picker = (SeekBar)options_view.findViewById( R.id.options_font_size_picker );
        // Editor editor = getSharedPreferences();
        // float font_size = editor.get( key );
        // options_font_size_picker.setValue( font_size );
        final int minTextSize = 8;
        options_font_size_picker.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged( SeekBar bar, int newValue, boolean fromUser ) {
                options_font_example_text.setTextSize( newValue + minTextSize );
            }
            @Override
            public void onStartTrackingTouch( SeekBar seekBar ) {
            }
            @Override
            public void onStopTrackingTouch( SeekBar seekBar ) {
            }
        });
        builder.setPositiveButton( R.string.options_save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                main_text_output.setTextSize( options_font_size_picker.getProgress() + minTextSize );
                // Editor editor = getSharedPreferences();
                // editor.edit();
                // editor.put( key, font_size );
                // editor.commit();
            }
        });
        builder.setNegativeButton( R.string.options_cancel, null );
        builder.create().show();
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
        writeActionHistorySaveFile();
        if( saveJSONFileExists() )
            deleteFile( oldJSONFormatSaveFileName );
    }

    private void writeActionHistorySaveFile() {
        try {
            FileOutputStream outputStream = openFileOutput( actionHistorySaveFileName,
                                                            Context.MODE_PRIVATE );
            outputStream.write(
                new ActionHistorySerialiser( actionHistory ).serialise().getBytes() );
            outputStream.close();
        } catch( FileNotFoundException e ) {
            e.printStackTrace();
        } catch( IOException e ) {
            e.printStackTrace();
        }
    }
}

