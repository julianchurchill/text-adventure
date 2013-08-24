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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
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
import android.widget.ImageView;
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
    private static final int SHOW_MAP_MENU_ITEM = 3;
    private static String oldJSONFormatSaveFileName = "save_file_1";
    private static String actionHistorySaveFileName = "action_history_save_file_1";
    private static String shared_prefs_root_key = "com.chewielouie.textadventure";
    private static int default_font_size = 16;
    private static String font_size_key = shared_prefs_root_key + ".fontsize";

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
    private ImageView map_view;
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
    private MovementMonitor movementMonitor = null;
    private Map<String, Integer> areaIDsToMaskIDs;

    public TextAdventureActivity() {
        Map<String, Integer> aMap = new HashMap<String, Integer>();
        aMap.put( "church-area", getDrawableIDByName( "church_mask" ) );
        aMap.put( "mine-area", getDrawableIDByName( "mine_mask" ) );
        aMap.put( "town-area", getDrawableIDByName( "town_mask" ) );
        aMap.put( "friary-area", getDrawableIDByName( "friary_mask" ) );
        areaIDsToMaskIDs = Collections.unmodifiableMap( aMap );
    }

    private int getDrawableIDByName( String name ) {
        return getResources().getIdentifier( name, "drawable", getPackageName() );
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
        main_text_output.setTextSize( getFontSize() );
        score_text_view = findTextView( R.id.score_text_view );
        available_actions_view = (LinearLayout)findViewById( R.id.available_actions );
        map_view = (ImageView)findViewById( R.id.map_view );
    }

    private class LoadTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... args) {
            if( saveFileExists() )
                loadGame();
            else
                createNewGame();

            if( saveJSONFileExists() ) {
                BasicModelV1_0ToActionListConverter c
                    = new BasicModelV1_0ToActionListConverter( model, inventory,
                                                               actionFactory(), logger );
                JSONToActionListConverter j
                    = new JSONToActionListConverter( TextAdventureActivity.this, oldJSONFormatSaveFileName, c );
                replayActions( j.actions() );
            }
            return null;
        }

         protected void onPostExecute(Void result) {
            rendersView.resetAndRender();
            if( progressDialog != null ) {
                progressDialog.dismiss();
            }
         }
    }

    @Override
    public void onResume() {
        super.onResume();

        progressDialog = ProgressDialog.show(this, "Starting...", "Loading game...", true, false);
        new LoadTask().execute();
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
        InputStream inputStream = null;
        try {
            inputStream = openFileInput( actionHistorySaveFileName );
        } catch( FileNotFoundException e ) {
            System.err.println("exception thrown: " + e.toString() );
        } catch( IOException e ) {
            System.err.println("exception thrown: " + e.toString() );
        }
        if( inputStream == null )
            return "";
        return readRawTextFile( inputStream, actionHistorySaveFileName );
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
                                     modelContent() );
        movementMonitor = new MovementMonitor( model );
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
            actionFactory = new RecordableActionFactory( new UserActionFactory(),
                                                         actionHistory() );
        return actionFactory;
    }

    private ActionHistory actionHistory() {
        if( actionHistory == null )
            actionHistory = new BasicActionHistory();
        return actionHistory;
    }

    private String modelContent() {
        return readRawTextFileFromResource( R.raw.model_content );
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

    public void showAvailableItemsText( String s ) {
        availableItemsText = s;
        updateMainText();
    }

    private void updateMainText() {
        scrollToTopOfNewDescriptionContent();

        SpannableStringBuilder text = new SpannableStringBuilder( mainTextContent );
        addItemsText( text );
        addExitsText( text );

        main_text_output.setText( text, TextView.BufferType.SPANNABLE );
    }

    private void scrollToTopOfNewDescriptionContent() {
        main_text_output.setText( mainTextContent, TextView.BufferType.SPANNABLE );
        if( newDescriptionIsBigger() ) {
            if( newDescriptionStartsWithTheOld() )
                scrollToFirstNewDescriptionLine();
            else
                scrollToTopOfMainText();
        }
        else if( newDescriptionIsSmaller() || newDescriptionIsDifferent() )
            scrollToTopOfMainText();
        oldMainTextContent = mainTextContent;
        oldDescriptionLineCount = main_text_output.getLineCount();
    }

    private boolean newDescriptionIsBigger() {
        return main_text_output.getLineCount() > oldDescriptionLineCount;
    }

    private boolean newDescriptionIsSmaller() {
        return main_text_output.getLineCount() < oldDescriptionLineCount;
    }

    private boolean newDescriptionStartsWithTheOld() {
        return mainTextContent.startsWith( oldMainTextContent );
    }

    private boolean newDescriptionIsDifferent() {
        return mainTextContent.equals( oldMainTextContent ) == false;
    }

    private void scrollToFirstNewDescriptionLine() {
        final int lastLineInOldDescription = oldDescriptionLineCount - 1;
        final ScrollView scrollView = (ScrollView)findViewById(
                R.id.main_text_output_scroll_view );
        scrollView.post(new Runnable() {
            public void run() {
                Rect bounds = new Rect();
                if( lastLineInOldDescription > 0 ) {
                    int firstNewDescriptionLine = lastLineInOldDescription + 1;
                    main_text_output.getLineBounds( firstNewDescriptionLine, bounds );
                }
                scrollView.smoothScrollTo( 0, bounds.top );
            }
        });
    }

    private void scrollToTopOfMainText() {
        final ScrollView scrollView = (ScrollView)findViewById(
                R.id.main_text_output_scroll_view );
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.smoothScrollTo( 0, 0 );
            }
        });
    }

    private void addItemsText( SpannableStringBuilder text ) {
        if( availableItemsText != "" )
            text.append( "\n" + availableItemsText );
    }

    private void addExitsText( SpannableStringBuilder text ) {
        if( exits.size() == 0 )
            text.append( "\nThere are no visible exits." );
        else
            addHyperLinkExits( text );
    }

    private void addHyperLinkExits( SpannableStringBuilder text ) {
        text.append( "\nThe following exits are visible: " );

        String prefix = "";
        for( Exit exit : orderForDisplay( exits ) ) {
            addHyperLinkForExit( prefix, exit, text );
            prefix = ", ";
        }
        // This additional space stops the spannable click region for the last
        // spannable from extending all the way to the edge of the text view
        text.append( " " );
        enableClickableLinkCallbacks();
    }

    private void addHyperLinkForExit( String prefix, Exit exit,
                                      SpannableStringBuilder text ) {
        int startIndex = text.length() + prefix.length();
        int endIndex = startIndex + exit.label().length();
        text.append( prefix + exit.label() );
        addExitActionHandler( text, startIndex, endIndex, exit );
        text.setSpan( new ForegroundColorSpan( selectExitColor( exit ) ),
                      startIndex, endIndex, 0 );
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

    private int LIGHT_BLUE = Color.rgb( 0, 154, 255 );
    private int PURPLE = Color.rgb( 130, 0, 186 );

    private int selectExitColor( Exit exit )
    {
        if(      exit.directionHint() == Exit.DirectionHint.North ) return PURPLE;
        else if( exit.directionHint() == Exit.DirectionHint.South ) return Color.RED;
        else if( exit.directionHint() == Exit.DirectionHint.East ) return Color.BLUE;
        else if( exit.directionHint() == Exit.DirectionHint.West ) return LIGHT_BLUE;
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
                doAction( finalUserActionHandler, factory.createExitAction( finalExit,
                                                                            finalModel ) );
            }
        };
        text.setSpan( c, startIndex, endIndex, 0 );
    }

    public void useExit( Exit exit ) {
        doAction( userActionHandler, actionFactory().createExitAction( exit, model ) );
    }

    private void enableClickableLinkCallbacks() {
        main_text_output.setMovementMethod( LinkMovementMethod.getInstance() );
    }

    public void showLocationExits( List<Exit> exits ) {
        this.exits = exits;

        updateMainText();
    }

    public void showAreaName( String s ) {
        TextView location_area  = findTextView( R.id.location_text_view );
        location_area.setText( s );
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
        button.setTypeface( Typeface.SERIF );
        LayoutParams lp = new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT );
        available_actions_view.addView( button, lp );

        actionButtons.put( button, action );
    }

    public Map<Button,Action> actionButtons() {
        return actionButtons;
    }

    public void onClick( View v ) {
        if( v instanceof Button && actionButtons.containsKey( (Button)v ) )
            doAction( userActionHandler, actionButtons.get( (Button)v ) );
    }

    private void doAction( final UserActionHandler actionHandler, Action action ) {
        actionHandler.enact( action );
    }

    @Override
    public boolean onPrepareOptionsMenu( Menu menu )
    {
        menu.clear();
        menu.add( Menu.NONE, ABOUT_MENU_ITEM, Menu.NONE, getText( R.string.about ) );
        menu.add( Menu.NONE, NEW_GAME_MENU_ITEM, Menu.NONE, getText( R.string.new_game ) );
        menu.add( Menu.NONE, OPTIONS_MENU_ITEM, Menu.NONE, getText( R.string.options ) );
        menu.add( Menu.NONE, SHOW_MAP_MENU_ITEM, Menu.NONE, getText( R.string.show_map ) );
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
            case SHOW_MAP_MENU_ITEM:
                showMap();
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
                    rendersView.render();
                }

            })
            .setNegativeButton( R.string.no, null )
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

    private void showOptionsDialog() {
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View options_view = getLayoutInflater().inflate( R.layout.options_dialog, null );
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setView( options_view );
        builder.setTitle( R.string.options_title );
        final TextView options_font_example_text = (TextView)options_view.findViewById( R.id.options_font_example_text );
        options_font_example_text.setTextSize( main_text_output.getTextSize() );
        final SeekBar options_font_size_picker = (SeekBar)options_view.findViewById( R.id.options_font_size_picker );
        final int minFontSize = 8;
        int font_size = getFontSize();;
        options_font_size_picker.setProgress( font_size - minFontSize );
        options_font_example_text.setTextSize( font_size );
        options_font_size_picker.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged( SeekBar bar, int newValue, boolean fromUser ) {
                options_font_example_text.setTextSize( newValue + minFontSize );
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
                saveFontSize( options_font_size_picker.getProgress() + minFontSize );
                main_text_output.setTextSize( getFontSize() );
            }
        });
        builder.setNegativeButton( R.string.options_cancel, null );
        AlertDialog dialog = builder.create();
        dialog.show();
        // The FILL_PARENT for width from the xml is ignored for some reason by android
        // This fix must be done after show() to override the incorrect width = WRAP_CONTENT setting
        dialog.getWindow().setLayout( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT );
    }

    private void showMap() {
        map_view.setImageBitmap(
            new MapImageCreator( getResources(),
                                 findExploredAreaMaskIDs(),
                                 R.drawable.map )
            .create() );
        map_view.setScaleType( ImageView.ScaleType.CENTER );
        map_view.setVisibility( View.VISIBLE );
    }

    private int[] findExploredAreaMaskIDs() {
        int[] exploredMasks = new int[movementMonitor.exploredAreas().size()];
        int i = 0;
        for( String areaID : movementMonitor.exploredAreas() )
            exploredMasks[i++] = areaIDsToMaskIDs.get( areaID );
        return exploredMasks;
    }

    public void currentScore( int score ) {
        currentScore = score;
        updateScore();
    }

    private void updateScore() {
        int percentage = 0;
        if ( maximumScore != 0 )
            percentage = (int) (((float)currentScore / (float)maximumScore) * (float)100);
        score_text_view.setText( Integer.toString( percentage ) + "% " + getText( R.string.completed ) );
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
        if( map_view.getVisibility() == View.VISIBLE )
            map_view.setVisibility( View.GONE );
        else if( userActionHandler.inAnActionChain() )
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

