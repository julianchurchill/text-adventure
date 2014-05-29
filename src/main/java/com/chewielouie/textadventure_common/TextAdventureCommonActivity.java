package com.chewielouie.textadventure_common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.StringBuilder;
import java.lang.StringBuffer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Html;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.chewielouie.textadventure.BasicModel;
import com.chewielouie.textadventure.BasicModelFactory;
import com.chewielouie.textadventure.Exit;
import com.chewielouie.textadventure.LocationFactory;
import com.chewielouie.textadventure.LocationExitFactory;
import com.chewielouie.textadventure.Logger;
import com.chewielouie.textadventure.PlainTextModelPopulator;
import com.chewielouie.textadventure.RendersView;
import com.chewielouie.textadventure.StdoutLogger;
import com.chewielouie.textadventure.TextAdventurePresenter;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.TextAdventureView;
import com.chewielouie.textadventure.UserActionHandler;
import com.chewielouie.textadventure.UserInventory;
import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionFactory;
import com.chewielouie.textadventure.action.ActionHistory;
import com.chewielouie.textadventure.action.BasicActionHistory;
import com.chewielouie.textadventure.action.LoggableActionFactory;
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

public abstract class TextAdventureCommonActivity extends Activity implements TextAdventureView, OnClickListener, OnInitListener {

    abstract protected Bitmap getMap();
    abstract protected int R_drawable_tta_button();
    abstract protected int R_drawable_tta_button(String buttonName);
    abstract protected int R_id_available_actions();
    abstract protected int R_id_location_text_view();
    abstract protected int R_id_map_view();
    abstract protected int R_id_walkthrough_scroll_view();
    abstract protected int R_id_walkthrough_text_view();
    abstract protected int R_id_main_text_output();
    abstract protected int R_id_main_text_output_scroll_view();
    abstract protected int R_id_options_font_example_text();
    abstract protected int R_id_options_font_size_picker();
    abstract protected int R_id_options_tts_enabled();
    abstract protected int R_id_score_text_view();
    abstract protected int R_id_new_game_welcome_dialog_continue_button();
    abstract protected int R_id_whats_new_dialog_continue_button();
    abstract protected int R_layout_about_dialog();
    abstract protected int R_layout_main();
    abstract protected int R_layout_options_dialog();
    abstract protected int R_layout_new_game_welcome_dialog();
    abstract protected int R_layout_whats_new_dialog();
    abstract protected int R_raw_model_content();
    abstract protected int R_string_about();
    abstract protected int R_string_app_name();
    abstract protected int R_string_completed();
    abstract protected int R_string_new_game();
    abstract protected int R_string_new_game_title();
    abstract protected int R_string_new_game_confirmation_dialog_text();
    abstract protected int R_string_no();
    abstract protected int R_string_options();
    abstract protected int R_string_options_title();
    abstract protected int R_string_options_save();
    abstract protected int R_string_options_cancel();
    abstract protected int R_string_show_map();
    abstract protected int R_string_yes();
    abstract protected int R_string_walkthrough();
    abstract protected int R_string_quick_hint();
    abstract protected int R_string_loading();
    abstract protected int R_string_new_game_welcome_dialog_title();
    abstract protected int R_string_new_game_welcome_dialog_text();
    abstract protected int R_string_new_game_welcome_dialog_continue_button();
    abstract protected Field[] R_raw_class_getFields();
    abstract protected int R_style_WaypointDialogTheme();

    private static final int NUMBER_OF_TTA_BUTTONS = 7;
    private static final int TEXT_TO_SPEECH_DATA_CHECK_CODE = 0;

    public static final int ABOUT_MENU_ITEM = 0;
    public static final int NEW_GAME_MENU_ITEM = 1;
    public static final int OPTIONS_MENU_ITEM = 2;
    public static final int SHOW_MAP_MENU_ITEM = 3;
    public static final int DEBUG_WAYPOINTS_MENU_ITEM = 4;
    public static final int WALKTHROUGH_MENU_ITEM = 5;
    public static final int QUICK_HINT_MENU_ITEM = 6;
    private static String oldJSONFormatSaveFileName = "save_file_1";
    private static String actionHistorySaveFileName = "action_history_save_file_1";
    private static String shared_prefs_root_key = "com.chewielouie.textadventure";
    private static int default_font_size = 16;
    private static String font_size_key = shared_prefs_root_key + ".fontsize";
    private static final int minimumFontSize = 8;
    private static boolean default_text_to_speech_enabled = false;
    private static String text_to_speech_enabled_key = shared_prefs_root_key + ".texttospeechenabled";
    private boolean textToSpeechEnabled = default_text_to_speech_enabled;
    private static String whats_new_last_viewed_version_key = shared_prefs_root_key + ".whatsnewlastviewedversion";

    private RendersView rendersView;
    private boolean externallySuppliedViewRenderer = false;
    private UserActionHandler userActionHandler;
    private boolean externallySuppliedUserActionHandler = false;
    private List<Exit> exits = new ArrayList<Exit>();
    private TextView main_text_output;
    private TextView score_text_view;
    private String mainTextContent = "";
    private String oldMainTextContent = "";
    private int oldDescriptionLineCount = 0;
    private String availableItemsText = "";
    private Map<TextView,Exit> directions_and_exits =
        new HashMap<TextView,Exit>();
    private Map<Button,Action> actionButtons = new HashMap<Button,Action>();
    private LinearLayout available_actions_view;
    private ZoomableImageView map_view;
    private ScrollView walkthrough_scroll_view;
    private TextView walkthrough_text_view;
    private int currentScore = 0;
    private int maximumScore = 0;
    private BasicModel model = null;
    private UserInventory inventory = null;
    private ActionFactory actionFactory = null;
    private ActionHistory actionHistory = null;
    private BasicModelFactory externalModelFactory = null;
    private BasicModelFactory internalModelFactory = null;
    private Logger logger = new StdoutLogger();
    private ProgressDialog progressDialog = null;
    protected MovementMonitor movementMonitor = null;
    private Map<String, Integer> areaIDsToMaskIDs;
    private String externallySuppliedModelContent = null;
    private boolean loading = false;
    private AsyncTask loadingTask = null;
    private TextToSpeech textToSpeech = null;
    private Dialog newGameWelcomeDialog = null;
    private Dialog whatsNewDialog = null;

    public TextAdventureCommonActivity() {
    }

    public TextAdventureCommonActivity( RendersView r ) {
        this();
        this.rendersView = r;
        this.externallySuppliedViewRenderer = true;
    }

    public TextAdventureCommonActivity( BasicModelFactory modelFactory ) {
        this();
        this.externalModelFactory = modelFactory;
    }

    public TextAdventureCommonActivity( RendersView r, BasicModelFactory modelFactory ) {
        this();
        this.rendersView = r;
        this.externallySuppliedViewRenderer = true;
        this.externalModelFactory = modelFactory;
    }

    public TextAdventureCommonActivity( UserActionHandler u ) {
        this();
        this.userActionHandler = u;
        this.externallySuppliedUserActionHandler = true;
    }

    public void setModelContent( String content ) {
        this.externallySuppliedModelContent = content;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView(R_layout_main());
        main_text_output = findTextView( R_id_main_text_output() );
        main_text_output.setTextSize( getFontSize() );
        score_text_view = findTextView( R_id_score_text_view() );
        available_actions_view = (LinearLayout)findViewById( R_id_available_actions() );
        map_view = (ZoomableImageView)findViewById( R_id_map_view() );
        walkthrough_scroll_view = (ScrollView)findViewById( R_id_walkthrough_scroll_view() );
        walkthrough_text_view = (TextView)findViewById( R_id_walkthrough_text_view() );

        reconfigureTextToSpeech();
    }

    private void reconfigureTextToSpeech() {
        if( getTextToSpeechEnabled() )
            initialiseTextToSpeech();
        else
            shutdownTextToSpeech();
    }

    private boolean textToSpeechRunning() {
        return textToSpeech != null;
    }

    private void shutdownTextToSpeech() {
        if( textToSpeechRunning() ) {
            textToSpeech.shutdown();
            textToSpeech = null;
        }
    }

    private void initialiseTextToSpeech() {
        Intent checkIntent = new Intent();
        checkIntent.setAction( TextToSpeech.Engine.ACTION_CHECK_TTS_DATA );
        startActivityForResult( checkIntent, TEXT_TO_SPEECH_DATA_CHECK_CODE );
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        if( requestCode == TEXT_TO_SPEECH_DATA_CHECK_CODE ) {
            if( resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS )
                textToSpeech = new TextToSpeech( this, this );
            else
                installTextToSpeechData();
        }
    }

    private void installTextToSpeechData() {
        Intent installIntent = new Intent();
        installIntent.setAction( TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA );
        startActivity( installIntent );
    }

    @Override
    public void onInit (int status) {
        textToSpeech.setLanguage( Locale.UK );
    }

    private class LoadTask extends AsyncTask<Void, Void, Void> {
        boolean newGame = false;

        protected Void doInBackground(Void... args) {
            if( saveFileExists() )
                loadGame();
            else
            {
                newGame = true;
                createNewGame();
            }

            if( saveJSONFileExists() ) {
                BasicModelV1_0ToActionListConverter c
                    = new BasicModelV1_0ToActionListConverter( model, inventory,
                                                               actionFactory(), logger );
                JSONToActionListConverter j
                    = new JSONToActionListConverter( TextAdventureCommonActivity.this, oldJSONFormatSaveFileName, c );
                replayActions( j.actions() );
            }
            return null;
        }

         protected void onPostExecute(Void result) {
            if( newGame )
                completedLoadingNewGame();
            else
                completedLoadingSavedGame();
         }
    }

    private void completedLoadingNewGame() {
        rendersView.render();
        endLoading();
        showNewGameWelcomeDialog();
    }

    private void completedLoadingSavedGame() {
        rendersView.render();
        endLoading();
    }

    private void endLoading() {
        if( progressDialog != null )
        {
            progressDialog.dismiss();
            progressDialog = null;
        }
        loading = false;
    }

    private void showNewGameWelcomeDialog() {
        newGameWelcomeDialog = new Dialog( this );
        newGameWelcomeDialog.setContentView( R_layout_new_game_welcome_dialog() );
        Button b = (Button)newGameWelcomeDialog.findViewById( R_id_new_game_welcome_dialog_continue_button() );
        b.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                newWelcomeDialogCloseActions();
            }
        });
        newGameWelcomeDialog.setOnCancelListener( new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                newWelcomeDialogCloseActions();
            }
        });
        newGameWelcomeDialog.show();
    }

    private void newWelcomeDialogCloseActions() {
        newGameWelcomeDialog.dismiss();
        newGameWelcomeDialog = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if( needToShowWhatsNew() )
            showWhatsNewDialog();
        else
            startGame();
    }

    protected boolean needToShowWhatsNew() {
        return getWhatsNewLastViewedVersion() < currentVersion();
        // return true;
    }

    private int getWhatsNewLastViewedVersion() {
        return getPrefs().getInt( whats_new_last_viewed_version_key, 0 );
    }

    private void updateWhatsNewLastViewedVersion() {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putInt( whats_new_last_viewed_version_key, currentVersion() );
        editor.apply();
    }

    private int currentVersion() {
        int versionCode = 0;
        try {
            versionCode = getPackageManager().getPackageInfo( getPackageName(), 0 ).versionCode;
        }
        catch ( PackageManager.NameNotFoundException e ) {
        }
        return versionCode;
    }

    private void showWhatsNewDialog() {
        whatsNewDialog = new Dialog( this );
        whatsNewDialog.setContentView( R_layout_whats_new_dialog() );
        Button b = (Button)whatsNewDialog.findViewById( R_id_whats_new_dialog_continue_button() );
        b.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                whatsNewDialogCloseActions();
            }
        });
        whatsNewDialog.setOnCancelListener( new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                whatsNewDialogCloseActions();
            }
        });
        whatsNewDialog.show();
    }

    private void whatsNewDialogCloseActions() {
        whatsNewDialog.dismiss();
        whatsNewDialog = null;
        updateWhatsNewLastViewedVersion();
        startGame();
    }

    private void startGame() {
        progressDialog = ProgressDialog.show(this, "Starting...", "Loading game...", true, false);
        loading = true;
        loadingTask = new LoadTask().execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loadingTask.cancel( true );
        loading = false;
        shutdownTextToSpeech();
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
        loadGameFromString( loadSerialisedActionHistory( actionHistorySaveFileName ) );
    }

    private void loadGameFromString( String content ) {
        createNewGame();
        replayActions( new ActionHistoryDeserialiser( actionFactory, inventory, model )
                            .deserialise( content ) );
    }

    private String loadSerialisedActionHistory( String filename ) {
        InputStream inputStream = null;
        try {
            inputStream = openFileInput( filename );
        } catch( FileNotFoundException e ) {
            System.err.println("exception thrown: " + e.toString() );
        } catch( IOException e ) {
            System.err.println("exception thrown: " + e.toString() );
        }
        if( inputStream == null )
            return "";
        return readRawTextFile( inputStream, filename );
    }

    private String readRawTextFile( InputStream input, String fileID ) {
        BufferedReader buffreader = new BufferedReader( new InputStreamReader( input ) );
        String line;
        StringBuilder text = new StringBuilder();
        try {
            while (( line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            System.out.println("Exception thrown while reading " + fileID );
            System.out.println( e.getMessage() );
            return null;
        }
        return text.toString();
    }

    private void replayActions( List<Action> actions ) {
        if( actions != null ) {
            rendersView.disableViewUpdates();
            actionHistory().clear();
            for( Action action : actions )
                userActionHandler.enact( action );
            rendersView.enableViewUpdates();
        }
    }

    private void createNewGame() {
        resetActionHistory();
        createNewGameModel();
        setupPresenter();
    }

    private void resetActionHistory() {
        actionHistory().clear();
    }

    private void createNewGameModel() {
        model = (BasicModel)modelFactory().createModel();
        movementMonitor = new MovementMonitor( model );
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
                new PlainTextExitDeserialiser( itemActionFactory ) ),
            itemDeserialiser,
            modelContent() );
        loadMapImages();
    }

    private void loadMapImages() {
        Map<String, Integer> aMap = new HashMap<String, Integer>();
        for( String areaID : model.areaIDs() )
            aMap.put( areaID, getDrawableIDByName( areaID + "_mask" ) );
        areaIDsToMaskIDs = Collections.unmodifiableMap( aMap );
    }

    private int getDrawableIDByName( String name ) {
        return getResources().getIdentifier( name, "drawable", getPackageName() );
    }

    public BasicModelFactory modelFactory() {
        if( externalModelFactory != null )
            return externalModelFactory;
        else if( internalModelFactory == null )
            internalModelFactory = new BasicModelFactory();
        return internalModelFactory;
    }

    private ActionFactory actionFactory() {
        if( actionFactory == null )
        {
            RecordableActionFactory r = new RecordableActionFactory(
                new UserActionFactory(), actionHistory() );
            if( isDebugMode() )
                actionFactory = new LoggableActionFactory( logger, r );
            else
                actionFactory = r;
        }
       return actionFactory;
    }

    private boolean isDebugMode()
    {
        return (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    private ActionHistory actionHistory() {
        if( actionHistory == null )
            actionHistory = new BasicActionHistory();
        return actionHistory;
    }

    private String modelContent() {
        if( externallySuppliedModelContent != null )
            return externallySuppliedModelContent;
        return readRawTextFileFromResource( R_raw_model_content() );
    }

    private String readRawTextFileFromResource( String resourceName ) {
        return readRawTextFileFromResource(
            getResources().getIdentifier( resourceName, "raw", getPackageName() ) );
    }

    private String readRawTextFileFromResource( int resId ) {
        return readRawTextFile( getResources().openRawResource( resId ), "resource file with id " + resId );
    }

    private void setupPresenter() {
        TextAdventurePresenter p = new TextAdventurePresenter(
            this, model, (UserInventory)model, actionFactory() );
        if( externallySuppliedViewRenderer == false )
            this.rendersView = p;
        if( externallySuppliedUserActionHandler == false )
            this.userActionHandler = p;
    }

    private TextView findTextView( int id ) {
        return (TextView)findViewById( id );
    }

    public void showMainText( String s ) {
        mainTextContent = s;
        updateMainText();
    }

    private String removeHTMLMarkup( String s ) {
        return Html.fromHtml( s ).toString();
    }

    public void showAvailableItemsText( String s ) {
        availableItemsText = s;
        updateMainText();
    }

    private void updateMainText() {
        ScrollView scrollView = (ScrollView)findViewById( R_id_main_text_output_scroll_view() );
        MainTextFormatter m = new MainTextFormatter( main_text_output,
                            mainTextContent, availableItemsText, exits, this, scrollView,
                            oldMainTextContent, oldDescriptionLineCount, model, userActionHandler, actionFactory() );
        m.format();
        speak( removeHTMLMarkup( m.allText() ) );
        oldMainTextContent = mainTextContent;
        oldDescriptionLineCount = m.descriptionLineCount();
    }

    private void speak( String text ) {
        if( textToSpeechRunning() )
            textToSpeech.speak( text, TextToSpeech.QUEUE_FLUSH, null );
    }

    public void useExit( Exit exit ) {
        userActionHandler.enact( actionFactory().createExitAction( exit, model ) );
    }

    public void showLocationExits( List<Exit> exits ) {
        this.exits = exits;

        updateMainText();
    }

    public void showAreaName( String s ) {
        TextView location_area  = findTextView( R_id_location_text_view() );
        location_area.setText( s );
    }

    public void setActions( List<Action> actions ) {
        actionButtons.clear();
        available_actions_view.removeAllViews();
        for( Action action : actions )
            makeActionButton( action, actions.indexOf(action));
    }

    private void makeActionButton( Action action, int index ) {
        Button button = new Button( this );
        button.setText( action.label() );
        button.setOnClickListener( this );
        button.setTypeface( Typeface.SERIF );
        String buttonResource = String.format("tta_button%02d", index % NUMBER_OF_TTA_BUTTONS);
        button.setBackgroundResource( R_drawable_tta_button(buttonResource) );
        LayoutParams lp = new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT );
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
    public boolean onPrepareOptionsMenu( Menu menu ) {
        menu.clear();
        menu.add( Menu.NONE, ABOUT_MENU_ITEM, Menu.NONE, getText( R_string_about() ) );
        menu.add( Menu.NONE, NEW_GAME_MENU_ITEM, Menu.NONE, getText( R_string_new_game() ) );
        menu.add( Menu.NONE, OPTIONS_MENU_ITEM, Menu.NONE, getText( R_string_options() ) );
        menu.add( Menu.NONE, SHOW_MAP_MENU_ITEM, Menu.NONE, getText( R_string_show_map() ) );
        menu.add( Menu.NONE, QUICK_HINT_MENU_ITEM, Menu.NONE, getText( R_string_quick_hint() ) );
        menu.add( Menu.NONE, WALKTHROUGH_MENU_ITEM, Menu.NONE, getText( R_string_walkthrough() ) );
        if( isWaypointsMenuAvailable() )
            menu.add( Menu.NONE, DEBUG_WAYPOINTS_MENU_ITEM, Menu.NONE, "Waypoints" );
        return true;
    }

    private boolean isWaypointsMenuAvailable() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
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
            case SHOW_MAP_MENU_ITEM:
                showMap();
                break;
            case DEBUG_WAYPOINTS_MENU_ITEM:
                if( isWaypointsMenuAvailable() )
                    showWaypointsList();
                break;
            case WALKTHROUGH_MENU_ITEM:
                showWalkthrough();
                break;
            case QUICK_HINT_MENU_ITEM:
                showQuickHint();
                break;
            default:
                retVal = super.onOptionsItemSelected( item );
                break;
        }
        return retVal;
    }

    private void showAboutDialog() {
        Dialog dialog = new Dialog( this );
        dialog.setContentView( R_layout_about_dialog() );

        String versionName = "";
        try {
            versionName = getPackageManager().getPackageInfo( getPackageName(), 0 ).versionName;
        }
        catch ( PackageManager.NameNotFoundException e ) {
        }
        dialog.setTitle( getResources().getString( R_string_app_name() ) +
                         " v" + versionName );

        dialog.show();
    }

    private void showNewGameConfirmationDialog() {
        new AlertDialog.Builder( this )
            .setIcon( android.R.drawable.ic_dialog_alert )
            .setTitle( R_string_new_game_title() )
            .setMessage( R_string_new_game_confirmation_dialog_text() )
            .setPositiveButton( R_string_yes(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick( DialogInterface dialog, int which ) {
                    createNewGame();
                    completedLoadingNewGame();
                }

            })
            .setNegativeButton( R_string_no(), null )
            .show();
    }

    private SharedPreferences getPrefs() {
        return getSharedPreferences( shared_prefs_root_key, MODE_PRIVATE );
    }

    private int getFontSize() {
        return getPrefs().getInt( font_size_key, default_font_size );
    }

    private void saveFontSize( int font_size ) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putInt( font_size_key, font_size );
        editor.apply();
    }

    private boolean getTextToSpeechEnabled() {
        textToSpeechEnabled = getPrefs().getBoolean( text_to_speech_enabled_key, default_text_to_speech_enabled );
        return textToSpeechEnabled;
    }

    private void saveTextToSpeechEnabled( boolean tts_on ) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putBoolean( text_to_speech_enabled_key, tts_on );
        editor.apply();
    }

    private void showOptionsDialog() {
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View options_view = getLayoutInflater().inflate( R_layout_options_dialog(), null );
        prepareFontSizePicker( options_view );
        prepareTTSEnabled( options_view );

        AlertDialog dialog = prepareOptionsDialogBuilder( options_view ).create();
        dialog.show();
        // The FILL_PARENT for width from the xml is ignored for some reason by android
        // This fix must be done after show() to override the incorrect width = WRAP_CONTENT setting
        dialog.getWindow().setLayout( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT );
    }

    private void prepareFontSizePicker( View options_view ) {
        final SeekBar options_font_size_picker = (SeekBar)options_view.findViewById( R_id_options_font_size_picker() );
        final TextView options_font_example_text = (TextView)options_view.findViewById( R_id_options_font_example_text() );
        options_font_example_text.setTextSize( main_text_output.getTextSize() );
        int font_size = getFontSize();;
        options_font_size_picker.setProgress( font_size - minimumFontSize );
        options_font_example_text.setTextSize( font_size );
        options_font_size_picker.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged( SeekBar bar, int newValue, boolean fromUser ) {
                options_font_example_text.setTextSize( newValue + minimumFontSize );
            }
            @Override
            public void onStartTrackingTouch( SeekBar seekBar ) {
            }
            @Override
            public void onStopTrackingTouch( SeekBar seekBar ) {
            }
        });
    }

    private void prepareTTSEnabled( View options_view ) {
        final CheckBox options_tts_enabled = (CheckBox)options_view.findViewById( R_id_options_tts_enabled() );
        options_tts_enabled.setChecked( getTextToSpeechEnabled() );
    }

    private AlertDialog.Builder prepareOptionsDialogBuilder( View options_view ) {
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setView( options_view );
        builder.setTitle( R_string_options_title() );
        final SeekBar options_font_size_picker = (SeekBar)options_view.findViewById( R_id_options_font_size_picker() );
        final CheckBox options_tts_enabled = (CheckBox)options_view.findViewById( R_id_options_tts_enabled() );
        builder.setPositiveButton( R_string_options_save(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                saveFontSize( options_font_size_picker.getProgress() + minimumFontSize );
                main_text_output.setTextSize( getFontSize() );
                saveTextToSpeechEnabled( options_tts_enabled.isChecked() );
                reconfigureTextToSpeech();
            }
        });
        builder.setNegativeButton( R_string_options_cancel(), null );
        return builder;
    }

    private void showMap() {
        map_view.setVisibility( View.VISIBLE );
        map_view.setBitmap(getMap());
    }


    private final String walkthroughTextFilename = "walkthrough";

    private void showWalkthrough() {
        walkthrough_scroll_view.setVisibility( View.VISIBLE );
        walkthrough_text_view.setText( R_string_loading() );
        new WalkthroughTextFormatter( walkthrough_text_view, readRawTextFileFromResource( walkthroughTextFilename ), currentScore );
    }

    private void showQuickHint() {
        Toast.makeText(getApplicationContext(), findQuickHint(), Toast.LENGTH_LONG).show();
    }

    private String findQuickHint() {
        return WalkthroughTextFormat.findQuickHint( readRawTextFileFromResource( walkthroughTextFilename ), currentScore );
    }

    private void showWaypointsList() {
        ContextThemeWrapper waypointThemedContext = new ContextThemeWrapper( this, R_style_WaypointDialogTheme() );
        AlertDialog.Builder builder = new AlertDialog.Builder( waypointThemedContext );
        builder.setTitle("Choose waypoint - CURRENT GAME WILL BE OVERWRITTEN");
        builder.setNegativeButton( R_string_options_cancel(),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
            waypointThemedContext, android.R.layout.select_dialog_singlechoice);
        fillListWithWaypoints( arrayAdapter );
        builder.setAdapter(arrayAdapter,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName = arrayAdapter.getItem(which);
                    String resourceName = "waypoint_" + strName;
                    loadGameFromString( readRawTextFileFromResource( resourceName ) );
                    completedLoadingSavedGame();
                }
            });
        builder.show();
    }

    private void fillListWithWaypoints( ArrayAdapter<String> arrayAdapter )
    {
        String waypointResourcePrefix = "waypoint_";
        Field[] fields = R_raw_class_getFields();
        for(Field f : fields)
        {
            try {
               if( f.getName().startsWith( waypointResourcePrefix ) )
                    arrayAdapter.add( f.getName().substring( waypointResourcePrefix.length() ) );
            } catch (IllegalArgumentException e) {
            }
        }
    }

    public void currentScore( int score ) {
        currentScore = score;
        updateScore();
    }

    private void updateScore() {
        int percentage = 0;
        if ( maximumScore != 0 )
            percentage = (int) (((float)currentScore / (float)maximumScore) * (float)100);
        score_text_view.setText( Integer.toString( percentage ) + "% " + getText( R_string_completed() ) );
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
        if( whatsNewDialog != null )
            whatsNewDialogCloseActions();
        else if( newGameWelcomeDialog != null )
            newWelcomeDialogCloseActions();
        else if( map_view.getVisibility() == View.VISIBLE )
            map_view.setVisibility( View.GONE );
        else if( walkthrough_scroll_view.getVisibility() == View.VISIBLE )
            walkthrough_scroll_view.setVisibility( View.GONE );
        else if( userActionHandler.inAnActionChain() )
            userActionHandler.cancelActionChain();
        else
            finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        if( loading ) {
            loadingTask.cancel( true );
            endLoading();
        }
        else {
            writeActionHistorySaveFile();
            if( saveJSONFileExists() )
                deleteFile( oldJSONFormatSaveFileName );
        }
    }

    private void writeActionHistorySaveFile() {
        byte[] bytes = new ActionHistorySerialiser( actionHistory() ).serialise().getBytes();
        try {
            FileOutputStream outputStream = openFileOutput( actionHistorySaveFileName,
                                                            Context.MODE_PRIVATE );
            outputStream.write( bytes );
            outputStream.close();
        } catch( FileNotFoundException e ) {
            e.printStackTrace();
        } catch( IOException e ) {
            e.printStackTrace();
        }
    }
}
