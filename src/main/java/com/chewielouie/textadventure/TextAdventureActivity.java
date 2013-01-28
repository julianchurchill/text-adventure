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
    private List<String> exits = new ArrayList<String>();
    private TextView top_direction_label;
    private TextView bottom_direction_label;
    private TextView right_direction_label;
    private TextView left_direction_label;

    public TextAdventureActivity() {
        this.rendersView = new TextAdventurePresenter( this, new BasicModel() );
        //this.userActionHandler = this.rendersView;
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

    public void showLocationExits( List<String> exits ) {
        this.exits = exits;

        if( exits.size() > 0 )
            top_direction_label.setText( exits.get( 0 ) );
        if( exits.size() > 1 )
            bottom_direction_label.setText( exits.get( 1 ) );
        if( exits.size() > 2 )
            right_direction_label.setText( exits.get( 2 ) );
        if( exits.size() > 3 )
            left_direction_label.setText( exits.get( 3 ) );
    }

    @Override
    public boolean dispatchTouchEvent( MotionEvent e ) {
        if( exits.size() > 0 )
            userActionHandler.moveThroughExit( exits.get( 0 ) );
        return super.dispatchTouchEvent( e );
    }
}

