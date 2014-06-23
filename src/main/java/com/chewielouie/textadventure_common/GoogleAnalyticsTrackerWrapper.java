package com.chewielouie.textadventure_common;

import java.util.Map;
import com.google.android.gms.analytics.Tracker;

public class GoogleAnalyticsTrackerWrapper implements AnalyticsTracker {
    private Tracker tracker;

    public GoogleAnalyticsTrackerWrapper( Tracker t ) {
        tracker = t;
    }

    public void send(Map<String, String> params) {
        tracker.send( params );
    }
}
