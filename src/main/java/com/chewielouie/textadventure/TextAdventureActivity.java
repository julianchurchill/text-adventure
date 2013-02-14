package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import com.chewielouie.textadventure.action.Action;

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
    private List<Action> actions = new ArrayList<Action>();
    private List<Action> immediateActions = null;

    public TextAdventureActivity() {
        TextAdventurePresenter p = new TextAdventurePresenter( this, createModel() );
        this.rendersView = p;
        this.userActionHandler = p;
    }

    public TextAdventureActivity( RendersView r ) {
        this();
        this.rendersView = r;
    }

    public TextAdventureActivity( UserActionHandler u ) {
        this();
        this.userActionHandler = u;
    }
        //Location startloc = new Location( "startloc", "You are in an empty wasteland that stretches for miles and miles.", inventory, f );
        //startloc.addExit( new Exit( "North", "busytown", Exit.DirectionHint.North ) );
        //startloc.addExit( new Exit( "East", "eastloc", Exit.DirectionHint.East ) );
        //startloc.addExit( new Exit( "West", "westloc", Exit.DirectionHint.West ) );
        //startloc.addItem( new NormalItem( "Skeleton key", "It is rather dirty and has clearly been carved from some poor unfortunates metacarpal." ) );
        //startloc.addItem( new NormalItem( "Banana peel", "It is yellow and a little moldy.", "some" ) );
        //startloc.addItem( new NormalItem( "Sand", "The fine grains run through your fingers, reminding you of passing time and an urgent task that must be done.", "some" ) );
        //Location busytown = new Location( "busytown", "You are in a busy town. There is a clock tower to the north.", inventory, f );
        //busytown.addExit( new Exit( "North", "clocktower", Exit.DirectionHint.North ) );
        //busytown.addExit( new Exit( "South", "startloc", Exit.DirectionHint.South ) );
        //Location clocktower = new Location( "clocktower", "You stand before a mighty clock tower. The clock goes TICK!", inventory, f );
        //clocktower.addExit( new Exit( "South", "busytown", Exit.DirectionHint.South ) );
        //clocktower.addItem( new NormalItem( "Minute Hand of the Clock Tower", "It is quite heavy, ornate and made of iron.", "a", "Minute Hand of the Clock Tower" ) );
        //Location eastloc = new Location( "eastloc", "You are in the middle of a vast and endless ocean. Of despair.", inventory, f );
        //eastloc.addExit( new Exit( "West", "startloc", Exit.DirectionHint.West ) );
        //Location westloc = new Location( "westloc", "You are in the precise centre of the universe. You are disappointed to find there is nothing here.", inventory, f );
        //westloc.addExit( new Exit( "East", "startloc", Exit.DirectionHint.East ) );

    private String demoContent() {
        String content = "";
        content += "INVENTORY ITEM\n";
        content += "item name:Pocket lint\n";
        content += "item description:It's fluffy and shaped like an inverted belly button.\n";
        content += "item countable noun prefix:some\n";
        content += "INVENTORY ITEM\n";
        content += "item name:Multitool\n";
        content += "item description:It's got a lot of tools on it. You feel like a man of the wilderness when you wield this formidable contraption.\n";

        content += "LOCATION\n";
        content += "location id:startloc\n";
        content += "location description:You are in an empty wasteland that stretches for miles and miles.\n";
        content += "exit label:North\nexit destination:busytown\nexit direction hint:North\n";
        content += "exit label:East\nexit destination:eastloc\nexit direction hint:East\n";
        content += "exit label:West\nexit destination:westloc\nexit direction hint:West\n";
        content += "ITEM\n";
        content += "item name:Skeleton key\n";
        content += "item description:It is rather dirty and has clearly been carved from some poor unfortunates metacarpal.\n";
        content += "item countable noun prefix:a\n";
        content += "ITEM\n";
        content += "item name:Banana peel\n";
        content += "item description:It is yellow and a little moldy.\n";
        content += "item countable noun prefix:some\n";
        content += "ITEM\n";
        content += "item name:Sand\n";
        content += "item description:The fine grains run through your fingers, reminding you of passing time and an urgent task that must be done.\n";
        content += "item countable noun prefix:some\n";

        content += "LOCATION\n";
        content += "location id:busytown\n";
        content += "location description:You are in a busy town. There is a clock tower to the north.\n";
        content += "exit label:North\nexit destination:clocktower\nexit direction hint:North\n";
        content += "exit label:South\nexit destination:startloc\nexit direction hint:South\n";
        content += "ITEM\n";
        content += "item name:Dust of the Ancients\n";
        content += "item description:This dust looks pretty powerful, for dust.\n";
        content += "item countable noun prefix:some\n";
        content += "item mid sentence cased name:Dust of the Ancients\n";

        content += "LOCATION\n";
        content += "location id:clocktower\n";
        content += "location description:You stand before a mighty clock tower. The clock goes TICK!\n";
        content += "exit label:South\nexit destination:busytown\nexit direction hint:South\n";
        content += "ITEM\n";
        content += "item name:Minute Hand of the Clock Tower\n";
        content += "item description:It is quite heavy, ornate and made of iron.\n";
        content += "item mid sentence cased name:Minute Hand of the Clock Tower\n";
        content += "item countable noun prefix:a\n";

        content += "LOCATION\n";
        content += "location id:eastloc\n";
        content += "location description:You are in the middle of a vast and endless ocean. Of despair.\n";
        content += "exit label:West\nexit destination:startloc\nexit direction hint:West\n";

        content += "LOCATION\n";
        content += "location id:westloc\n";
        content += "location description:You are in the precise centre of the universe. You are disappointed to find there is nothing here.\n";
        content += "exit label:East\nexit destination:startloc\nexit direction hint:East\n";
        return content;
    }

    private TextAdventureModel createModel() {
        BasicModel model = new BasicModel();
        UserInventory inventory = model;
        ItemFactory itemFactory = new NormalItemFactory();
        new PlainTextModelPopulator( model,
                                     new LocationFactory( inventory, itemFactory ),
                                     inventory,
                                     itemFactory,
                                     demoContent() );
        return model;
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
        registerForContextMenu( main_text_output );
    }

    private TextView findTextView( int id ) {
        return (TextView)findViewById( id );
    }

    private List<Action> menuActions() {
        if( immediateActions != null )
            return immediateActions;
        return actions;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        for( Action action : menuActions() )
            menu.add( action.label() );
    }

    @Override
    public boolean onContextItemSelected( MenuItem menuItem ) {
        CharSequence menuLabel = menuItem.getTitle();
        boolean actionOriginatedFromImmediateActions = false;
        if( menuActions() == immediateActions )
            actionOriginatedFromImmediateActions = true;

        for( Action action : menuActions() ) {
            if( menuLabel.equals( action.label() ) ) {
                userActionHandler.enact( action );
                break;
            }
        }

        if( actionOriginatedFromImmediateActions )
            immediateActions = null;
        return true;
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
        this.actions = actions;
    }

    public void giveUserImmediateActionChoice( List<Action> actions ) {
        this.immediateActions = actions;
        closeContextMenu();
        openContextMenu( main_text_output );
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
    }

    private void deliverExitActionFor( TextView dir_label ) {
        if( exits.size() > 0 )
            userActionHandler.moveThroughExit(
               directions_and_exits.get( dir_label ) );
    }
}

