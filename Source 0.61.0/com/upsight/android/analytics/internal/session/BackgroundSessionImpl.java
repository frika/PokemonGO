package com.upsight.android.analytics.internal.session;

import android.content.Context;
import com.upsight.android.analytics.internal.session.SessionInitializer.Type;
import com.upsight.android.internal.util.PreferencesHelper;

public class BackgroundSessionImpl implements Session {
    private final Context mContext;
    private final Type mType;

    private BackgroundSessionImpl(BackgroundSessionInitializer initializer, Context context) {
        this.mType = initializer.getType();
        this.mContext = context;
    }

    public static BackgroundSessionImpl create(Context context, BackgroundSessionInitializer initializer) {
        return new BackgroundSessionImpl(initializer, context);
    }

    public Type getType() {
        return this.mType;
    }

    public Integer getCampaignID() {
        return null;
    }

    public Integer getMessageID() {
        return null;
    }

    public Integer getSessionNumber() {
        return null;
    }

    public Long getStartTimestamp() {
        return null;
    }

    public Long getPreviousTos() {
        return Long.valueOf(PreferencesHelper.getLong(this.mContext, Session.PREFERENCES_KEY_PAST_SESSION_TIME, 0));
    }

    public void updateDuration(Context context, long lastKnownTime) {
    }
}
