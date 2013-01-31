package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;
import com.chewielouie.textadventure.action.Action;

public class TextAdventureActivity extends Activity implements TextAdventureView {
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
        Location startloc = new Location( "startloc", "You are in an empty wasteland that stretches for miles and miles." );
        startloc.addExit( new Exit( "North", "busytown", Exit.DirectionHint.North ) );
        startloc.addExit( new Exit( "East", "eastloc", Exit.DirectionHint.East ) );
        startloc.addExit( new Exit( "West", "westloc", Exit.DirectionHint.West ) );
        Location busytown = new Location( "busytown", "You are in a busy town. There is a clock tower to the north." );
        busytown.addExit( new Exit( "North", "clocktower", Exit.DirectionHint.North ) );
        busytown.addExit( new Exit( "South", "startloc", Exit.DirectionHint.South ) );
        Location clocktower = new Location( "clocktower", "You stand before a mighty clock tower. The clock goes TICK!" );
        clocktower.addExit( new Exit( "South", "busytown", Exit.DirectionHint.South ) );
        Location eastloc = new Location( "eastloc", "You are in the middle of a vast and endless ocean. Of despair." );
        eastloc.addExit( new Exit( "West", "startloc", Exit.DirectionHint.West ) );
        Location westloc = new Location( "westloc", "You are in the precise centre of the universe. You are disappointed to find there is nothing here." );
        westloc.addExit( new Exit( "East", "startloc", Exit.DirectionHint.East ) );
        BasicModel model = new BasicModel();
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
    }

    private TextView findTextView( int id ) {
        return (TextView)findViewById( id );
    }

    @Override
    public void onResume() {
        super.onResume();
        rendersView.render();
    }

    public void showLocationDescription( String s ) {
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
    }

    @Override
    public boolean dispatchTouchEvent( MotionEvent e ) {
        if( e.getActionMasked() == MotionEvent.ACTION_UP && exits.size() > 0 ) {
            if( touchIsInTopQuandrant( e ) )
                deliverExitActionFor( top_direction_label );
            else if( touchIsInBottomQuandrant( e ) )
                deliverExitActionFor( bottom_direction_label );
            else if( touchIsInRightQuandrant( e ) )
                deliverExitActionFor( right_direction_label );
            else if( touchIsInLeftQuandrant( e ) )
                deliverExitActionFor( left_direction_label );
        }
        return super.dispatchTouchEvent( e );
    }

    private int main_text_height() {
        if( main_text_output.getHeight() != 0 )
            return main_text_output.getHeight();
        return main_text_output.getLayoutParams().height;
    }

    private int main_text_width() {
        if( main_text_output.getWidth() != 0 )
            return main_text_output.getWidth();
        return main_text_output.getLayoutParams().width;
    }

    private boolean touchIsInTopQuandrant( MotionEvent e ) {
        if( touchIsToTheLeft( e ) )
            return e.getX() > e.getY();
        return e.getX() < (main_text_height() - e.getY());
    }

    private boolean touchIsToTheLeft( MotionEvent e ) {
        return e.getX() < (main_text_width()/2);
    }

    private boolean touchIsInBottomQuandrant( MotionEvent e ) {
        if( touchIsToTheLeft( e ) )
            return e.getX() > (main_text_height() - e.getY());
        return e.getX() < e.getY();
    }

    private boolean touchIsInRightQuandrant( MotionEvent e ) {
        if( touchIsToTheTop( e ) )
            return e.getX() > (main_text_height() - e.getY());
        return e.getX() > e.getY();
    }

    private boolean touchIsToTheTop( MotionEvent e ) {
        return e.getY() < (main_text_height()/2);
    }

    private boolean touchIsInLeftQuandrant( MotionEvent e ) {
        if( touchIsToTheTop( e ) )
            return e.getX() < e.getY();
        return e.getX() < (main_text_height() - e.getY());
    }

    private void deliverExitActionFor( TextView dir_label ) {
        userActionHandler.moveThroughExit(
               directions_and_exits.get( dir_label ) );
    }
}

