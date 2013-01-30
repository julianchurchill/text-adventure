package com.chewielouie.textadventure;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class TextAdventureActivity extends Activity implements TextAdventureView {
    private RendersView rendersView;
    private UserActionHandler userActionHandler;
    private List<Exit> exits = new ArrayList<Exit>();
    private TextView top_direction_label;
    private TextView bottom_direction_label;
    private TextView right_direction_label;
    private TextView left_direction_label;

    public TextAdventureActivity() {
        Location loc1 = new Location( "loc1", "You are in an empty wasteland that stretches for miles and miles." );
        loc1.addExit( new Exit( "North", "loc2" ) );
        loc1.addExit( new Exit( "South", "loc1" ) );
        loc1.addExit( new Exit( "East", "loc1" ) );
        loc1.addExit( new Exit( "West", "loc1" ) );
        Location loc2 = new Location( "loc2", "You are in a busy town. There is a clock tower to the north." );
        loc2.addExit( new Exit( "North", "loc3" ) );
        loc2.addExit( new Exit( "South", "loc1" ) );
        Location loc3 = new Location( "loc3", "You stand before a mighty clock tower. The clock goes TICK!" );
        loc3.addExit( new Exit( "South", "loc2" ) );
        BasicModel model = new BasicModel();
        model.addLocation( loc1 );
        model.addLocation( loc2 );
        model.addLocation( loc3 );
        TextAdventurePresenter p = new TextAdventurePresenter( this, model );
        this.rendersView = p;
        this.userActionHandler = p;
    }

    public TextAdventureActivity( RendersView r ) {
        this.rendersView = r;
    }

    public TextAdventureActivity( UserActionHandler u ) {
        this.userActionHandler = u;
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
        final TextView t = (TextView)findViewById( R.id.main_text_output );
        t.setText( s );
    }

    public void showLocationExits( List<Exit> exits ) {
        this.exits = exits;

        Exit northExit = null;
        Exit southExit = null;
        Exit eastExit = null;
        Exit westExit = null;
        for( Exit e : exits ) {
            if( e.directionHint() == Exit.DirectionHint.North )
                northExit = e;
            else if( e.directionHint() == Exit.DirectionHint.South )
                southExit = e;
            else if( e.directionHint() == Exit.DirectionHint.East )
                eastExit = e;
            else if( e.directionHint() == Exit.DirectionHint.West )
                westExit = e;
        }

        int exitWithNoDirHintIndex = setDirectionLabel( top_direction_label,
                northExit, 0 );
        exitWithNoDirHintIndex = setDirectionLabel( bottom_direction_label,
                southExit, exitWithNoDirHintIndex );
        exitWithNoDirHintIndex = setDirectionLabel( right_direction_label,
                eastExit, exitWithNoDirHintIndex );
        exitWithNoDirHintIndex = setDirectionLabel( left_direction_label,
                westExit, exitWithNoDirHintIndex );
    }

    private int setDirectionLabel( TextView dir_label, Exit directionExit,
           int indexToStartLookingForUndirectedExits ) {
        int exitWithNoDirHintIndex = nextExitWithoutADirectionHint(
                indexToStartLookingForUndirectedExits );
        String label = "";
        if( directionExit != null )
            label = directionExit.label();
        else if( exitWithNoDirHintIndex < exits.size() ) {
            label = exits.get( exitWithNoDirHintIndex ).label();
            exitWithNoDirHintIndex++;
        }
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

    @Override
    public boolean dispatchTouchEvent( MotionEvent e ) {
        if( e.getActionMasked() == MotionEvent.ACTION_UP && exits.size() > 0 )
            userActionHandler.moveThroughExit( exits.get( 0 ) );
        return super.dispatchTouchEvent( e );
    }
}

