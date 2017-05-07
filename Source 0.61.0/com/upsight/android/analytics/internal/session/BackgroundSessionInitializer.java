package com.upsight.android.analytics.internal.session;

import com.upsight.android.analytics.internal.session.SessionInitializer.Type;

public class BackgroundSessionInitializer implements SessionInitializer {
    public Type getType() {
        return Type.BACKGROUND;
    }

    public Integer getCampaignID() {
        return null;
    }

    public Integer getMessageID() {
        return null;
    }
}
