package com.upsight.android.analytics.internal.session;

import android.support.annotation.NonNull;
import com.upsight.android.analytics.session.UpsightSessionInfo;

public interface SessionManager {
    public static final String CONFIGURATION_SUBTYPE = "upsight.configuration.session_manager";
    public static final String DEFAULT_CONFIGURATION = "{\"session_gap\": 120}";
    public static final String SESSION_CAMPAIGN_ID = "campaign_id";
    public static final String SESSION_EXTRA = "session_extra";
    public static final String SESSION_MESSAGE_ID = "message_id";

    Session getBackgroundSession();

    UpsightSessionInfo getLatestSessionInfo();

    Session getSession();

    Session startSession(@NonNull SessionInitializer sessionInitializer);

    void stopSession();
}
