package com.upsight.android.analytics.internal.session;

import com.upsight.android.analytics.internal.session.SessionInitializer.Type;

public class StandardSessionInitializer implements SessionInitializer {
    public Type getType() {
        return Type.STANDARD;
    }

    public Integer getCampaignID() {
        return null;
    }

    public Integer getMessageID() {
        return null;
    }
}
