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
import android.view.View.OnTouchListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import com.chewielouie.textadventure.action.Action;

public class TextAdventureActivity extends Activity implements TextAdventureView, ShortTouchHandler {
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
    private ShortTouchDisseminator shortTouchDisseminator;

    public TextAdventureActivity() {
        TextAdventurePresenter p = createPresenter();
        this.rendersView = p;
        this.userActionHandler = p;
    }

    public TextAdventureActivity( RendersView r ) {
        this.rendersView = r;
    }

    public TextAdventureActivity( UserActionHandler u ) {
        TextAdventurePresenter p = createPresenter();
        this.rendersView = p;
        this.userActionHandler = u;
    }

    public TextAdventurePresenter createPresenter() {
        BasicModel model = new BasicModel();
        UserInventory inventory = model;
        Location startloc = new Location( "startloc", "You are in an empty wasteland that stretches for miles and miles.", inventory );
        startloc.addExit( new Exit( "North", "busytown", Exit.DirectionHint.North ) );
        startloc.addExit( new Exit( "East", "eastloc", Exit.DirectionHint.East ) );
        startloc.addExit( new Exit( "West", "westloc", Exit.DirectionHint.West ) );
        startloc.addItem( new NormalItem( "Skeleton key", "It is rather dirty and has clearly been carved from some poor unfortunates metacarpal." ) );
        Location busytown = new Location( "busytown", "You are in a busy town. There is a clock tower to the north.", inventory );
        busytown.addExit( new Exit( "North", "clocktower", Exit.DirectionHint.North ) );
        busytown.addExit( new Exit( "South", "startloc", Exit.DirectionHint.South ) );
        Location clocktower = new Location( "clocktower", "You stand before a mighty clock tower. The clock goes TICK!", inventory );
        clocktower.addExit( new Exit( "South", "busytown", Exit.DirectionHint.South ) );
        Location eastloc = new Location( "eastloc", "You are in the middle of a vast and endless ocean. Of despair.", inventory );
        eastloc.addExit( new Exit( "West", "startloc", Exit.DirectionHint.West ) );
        Location westloc = new Location( "westloc", "You are in the precise centre of the universe. You are disappointed to find there is nothing here.", inventory );
        westloc.addExit( new Exit( "East", "startloc", Exit.DirectionHint.East ) );
        model.addLocation( startloc );
        model.addLocation( busytown );
        model.addLocation( clocktower );
        model.addLocation( eastloc );
        model.addLocation( westloc );
        return new TextAdventurePresenter( this, model );
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        top_direction_label = findTextView( R.id.top_direction_label );
        bottom_direction_label = findTextView( R.id.bottom_direction_label );
        right_direction_label = findTextView( R.id.right_direction_label );
        left_direction_label = findTextView( R.id.left_direction_label );
        main_text_output = findTextView( R.id.main_text_output );
        shortTouchDisseminator = new ShortTouchDisseminator( this );
        shortTouchDisseminator.setView( main_text_output );
        main_text_output.setOnTouchListener( shortTouchDisseminator );
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

    public void topQuadrantTouch( View v ) {
        if( v == main_text_output )
            deliverExitActionFor( top_direction_label );
    }

    public void bottomQuadrantTouch( View v ) {
        if( v == main_text_output )
            deliverExitActionFor( bottom_direction_label );
    }

    public void rightQuadrantTouch( View v ) {
        if( v == main_text_output )
            deliverExitActionFor( right_direction_label );
    }

    public void leftQuadrantTouch( View v ) {
        if( v == main_text_output )
            deliverExitActionFor( left_direction_label );
    }

    private void deliverExitActionFor( TextView dir_label ) {
        if( exits.size() > 0 )
            userActionHandler.moveThroughExit(
               directions_and_exits.get( dir_label ) );
    }

    public boolean onLongClick( View v ) {
        return shortTouchDisseminator.onLongClick( v );
    }

    public boolean onTouch( View v, MotionEvent e ) {
        return shortTouchDisseminator.onTouch( v, e );
    }

    class ShortTouchDisseminator implements OnTouchListener, OnLongClickListener {
        private ShortTouchHandler handler;
        private boolean longPressOnView = false;
        private View viewToObserve;

        public ShortTouchDisseminator( ShortTouchHandler h ) {
            this.handler = h;
        }

        public void setView( View v ) {
            this.viewToObserve = v;
            this.viewToObserve.setOnLongClickListener( this );
        }

        public boolean onLongClick( View v ) {
            if( v == viewToObserve )
                longPressOnView = true;
            return false;
        }

        public boolean onTouch( View v, MotionEvent e ) {
            if( v == viewToObserve &&
                  e.getActionMasked() == MotionEvent.ACTION_UP &&
                  longPressOnView == false ) {
                if( touchIsInTopQuandrant( e ) )
                    handler.topQuadrantTouch( v );
                else if( touchIsInBottomQuandrant( e ) )
                    handler.bottomQuadrantTouch( v );
                else if( touchIsInRightQuandrant( e ) )
                    handler.rightQuadrantTouch( v );
                else if( touchIsInLeftQuandrant( e ) )
                    handler.leftQuadrantTouch( v );
            }

            if( v == viewToObserve &&
                  e.getActionMasked() == MotionEvent.ACTION_UP &&
                  longPressOnView == true )
                longPressOnView = false;
            return false;
        }

        private int view_height() {
            if( viewToObserve.getHeight() != 0 )
                return viewToObserve.getHeight();
            return viewToObserve.getLayoutParams().height;
        }

        private int view_width() {
            if( viewToObserve.getWidth() != 0 )
                return viewToObserve.getWidth();
            return viewToObserve.getLayoutParams().width;
        }

        private float x_adjusted_for_width_height_ratio( MotionEvent e ) {
            return e.getX() * (float)view_height() / (float)view_width();
        }

        private boolean touchIsInTopQuandrant( MotionEvent e ) {
            if( touchIsToTheLeft( e ) )
                return x_adjusted_for_width_height_ratio( e ) > e.getY();
            return x_adjusted_for_width_height_ratio( e ) <
                (view_height() - e.getY());
        }

        private boolean touchIsToTheLeft( MotionEvent e ) {
            return e.getX() < (view_width()/2);
        }

        private boolean touchIsInBottomQuandrant( MotionEvent e ) {
            if( touchIsToTheLeft( e ) )
                return x_adjusted_for_width_height_ratio( e ) >
                   (view_height() - e.getY());
            return x_adjusted_for_width_height_ratio( e ) < e.getY();
        }

        private boolean touchIsInRightQuandrant( MotionEvent e ) {
            if( touchIsToTheTop( e ) )
                return x_adjusted_for_width_height_ratio( e ) > (view_height() - e.getY());
            return x_adjusted_for_width_height_ratio( e ) > e.getY();
        }

        private boolean touchIsToTheTop( MotionEvent e ) {
            return e.getY() < (view_height()/2);
        }

        private boolean touchIsInLeftQuandrant( MotionEvent e ) {
            if( touchIsToTheTop( e ) )
                return x_adjusted_for_width_height_ratio( e ) < e.getY();
            return x_adjusted_for_width_height_ratio( e ) < (view_height() - e.getY());
        }
    }
}

