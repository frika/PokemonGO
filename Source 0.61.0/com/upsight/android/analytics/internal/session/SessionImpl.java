package com.upsight.android.analytics.internal.session;

import android.content.Context;
import com.google.android.exoplayer.text.Cue;
import com.upsight.android.analytics.internal.session.SessionInitializer.Type;
import com.upsight.android.analytics.session.UpsightSessionInfo;
import com.upsight.android.internal.util.PreferencesHelper;

public class SessionImpl implements Session {
    private final Integer mCampaignId;
    private final Long mInitialSessionStartTs;
    private final Integer mMessageId;
    private final Long mPastSessionTime;
    private final Integer mSessionNum;
    private final Type mType;

    private SessionImpl(SessionInitializer initializer, Integer sessionNum, Long initialSessionStartTs, Long pastSessionTime) {
        this.mType = initializer.getType();
        this.mCampaignId = initializer.getCampaignID();
        this.mMessageId = initializer.getMessageID();
        this.mSessionNum = sessionNum;
        this.mInitialSessionStartTs = initialSessionStartTs;
        this.mPastSessionTime = pastSessionTime;
    }

    public static Session create(Context context, Clock clock, SessionInitializer initializer) {
        int sessionNum = PreferencesHelper.getInt(context, Session.PREFERENCES_KEY_SESSION_NUM, Cue.TYPE_UNSET);
        long initialSessionStartTs = PreferencesHelper.getLong(context, Session.PREFERENCES_KEY_SESSION_START_TS, Long.MIN_VALUE);
        if (sessionNum == Cue.TYPE_UNSET || initialSessionStartTs == Long.MIN_VALUE) {
            return incrementAndCreate(context, clock, initializer);
        }
        return new SessionImpl(initializer, Integer.valueOf(sessionNum), Long.valueOf(initialSessionStartTs), Long.valueOf(PreferencesHelper.getLong(context, Session.PREFERENCES_KEY_PAST_SESSION_TIME, 0)));
    }

    public static Session incrementAndCreate(Context context, Clock clock, SessionInitializer initializer) {
        int sessionNum = PreferencesHelper.getInt(context, Session.PREFERENCES_KEY_SESSION_NUM, 0) + 1;
        long currentTime = clock.currentTimeSeconds();
        PreferencesHelper.putInt(context, Session.PREFERENCES_KEY_SESSION_NUM, sessionNum);
        PreferencesHelper.putLong(context, Session.PREFERENCES_KEY_SESSION_START_TS, currentTime);
        long pastSessionTime = PreferencesHelper.getLong(context, Session.PREFERENCES_KEY_PAST_SESSION_TIME, 0) + PreferencesHelper.getLong(context, Session.PREFERENCES_KEY_CURRENT_SESSION_DURATION, 0);
        PreferencesHelper.putLong(context, Session.PREFERENCES_KEY_CURRENT_SESSION_DURATION, 0);
        PreferencesHelper.putLong(context, Session.PREFERENCES_KEY_PAST_SESSION_TIME, pastSessionTime);
        return new SessionImpl(initializer, Integer.valueOf(sessionNum), Long.valueOf(currentTime), Long.valueOf(pastSessionTime));
    }

    public static UpsightSessionInfo getLatestSessionInfo(Context context) {
        int lastKnownSessionNum = PreferencesHelper.getInt(context, Session.PREFERENCES_KEY_SESSION_NUM, 0);
        long lastKnownSessionStartTs = PreferencesHelper.getLong(context, Session.PREFERENCES_KEY_SESSION_START_TS, 0);
        if (lastKnownSessionNum <= 0 || lastKnownSessionStartTs <= 0) {
            return UpsightSessionInfo.NONE;
        }
        return new UpsightSessionInfo(lastKnownSessionNum, lastKnownSessionStartTs);
    }

    public Type getType() {
        return this.mType;
    }

    public synchronized Integer getCampaignID() {
        return this.mCampaignId;
    }

    public synchronized Integer getMessageID() {
        return this.mMessageId;
    }

    public synchronized Integer getSessionNumber() {
        return this.mSessionNum;
    }

    public synchronized Long getStartTimestamp() {
        return this.mInitialSessionStartTs;
    }

    public Long getPreviousTos() {
        return this.mPastSessionTime;
    }

    public void updateDuration(Context context, long lastKnownTime) {
        PreferencesHelper.putLong(context, Session.PREFERENCES_KEY_CURRENT_SESSION_DURATION, lastKnownTime - this.mInitialSessionStartTs.longValue());
    }
}
