package com.upsight.android.analytics.internal.session;

public interface SessionInitializer {

    public enum Type {
        STANDARD,
        BACKGROUND,
        PUSH
    }

    Integer getCampaignID();

    Integer getMessageID();

    Type getType();
}
