package com.upsight.android.analytics.session;

import com.upsight.android.Upsight;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;

public class UpsightSessionInfo {
    public static final UpsightSessionInfo NONE;
    public static final int SESSION_NUMBER_INVALID = 0;
    public static final long START_TIMESTAMP_INVALID = 0;
    public final int sessionNumber;
    public final long startTimestamp;

    static {
        NONE = new UpsightSessionInfo(0, 0);
    }

    public static UpsightSessionInfo getLatest(UpsightContext upsight) throws IllegalArgumentException {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            return extension.getApi().getLatestSessionInfo();
        }
        upsight.getLogger().m576e(Upsight.LOG_TAG, "com.upsight.extension.analytics must be registered in your Android Manifest", new Object[0]);
        return NONE;
    }

    public UpsightSessionInfo(int sessionNumber, long startTimestamp) {
        this.sessionNumber = sessionNumber;
        this.startTimestamp = startTimestamp;
    }
}
