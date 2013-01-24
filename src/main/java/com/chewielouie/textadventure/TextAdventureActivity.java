package com.chewielouie.textadventure;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TextAdventureActivity extends Activity implements TextAdventureView {
    private RendersView rendersView;

    public TextAdventureActivity() {
        this.rendersView = new TextAdventurePresenter( this, new BasicModel() );
    }

    public TextAdventureActivity( RendersView r ) {
        this.rendersView = r;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public void onResume() {
        rendersView.render();
    }

    public void showRoomText( String s ) {
        final TextView t = (TextView)findViewById( R.id.main_text_output );
        t.setText( s );
    }
}

