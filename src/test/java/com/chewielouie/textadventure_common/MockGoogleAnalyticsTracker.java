package com.chewielouie.textadventure_common;

import java.util.Map;
import com.google.android.gms.analytics.Tracker;

public class MockGoogleAnalyticsTracker implements AnalyticsTracker {
    public void send(Map<String, String> params) {};
}
