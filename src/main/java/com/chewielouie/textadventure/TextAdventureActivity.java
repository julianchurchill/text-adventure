package com.chewielouie.textadventure;

import android.app.Activity;
import android.os.Bundle;

public class TextAdventureActivity extends Activity
{
    public TextAdventureActivity() {
        super();
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}

