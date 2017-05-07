package com.upsight.android.analytics.internal.session;

import android.content.Context;

public interface Session extends SessionInitializer {
    public static final String PREFERENCES_KEY_CURRENT_SESSION_DURATION = "current_session_duration";
    public static final String PREFERENCES_KEY_PAST_SESSION_TIME = "past_session_time";
    public static final String PREFERENCES_KEY_SESSION_NUM = "session_num";
    public static final String PREFERENCES_KEY_SESSION_START_TS = "session_start_ts";
    public static final int SESSION_NUM_BASE_OFFSET = 0;

    Long getPreviousTos();

    Integer getSessionNumber();

    Long getStartTimestamp();

    void updateDuration(Context context, long j);
}
