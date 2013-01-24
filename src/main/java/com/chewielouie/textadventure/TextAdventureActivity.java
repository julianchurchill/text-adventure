package com.chewielouie.textadventure;

import android.app.Activity;
import android.os.Bundle;

public class TextAdventureActivity extends Activity {
    private RendersView rendersView;

    public TextAdventureActivity() {
        super();
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
}

