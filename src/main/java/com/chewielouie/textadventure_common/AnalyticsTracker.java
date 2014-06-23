package com.chewielouie.textadventure_common;

import java.util.Map;

public interface AnalyticsTracker {
    abstract public void send(Map<String, String> params);
}
