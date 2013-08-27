package com.chewielouie.textadventure_common;

import android.view.View;

public interface ShortTouchHandler {
    public void topQuadrantTouch( View v );
    public void bottomQuadrantTouch( View v );
    public void rightQuadrantTouch( View v );
    public void leftQuadrantTouch( View v );
}

