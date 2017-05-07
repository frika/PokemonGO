package com.upsight.android.analytics.session;

import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.provider.UpsightSessionContext;

public interface UpsightSessionCallbacks {
    void onResume(UpsightSessionContext upsightSessionContext, UpsightSessionInfo upsightSessionInfo);

    void onResumed(UpsightContext upsightContext);

    void onStart(UpsightSessionContext upsightSessionContext, UpsightSessionInfo upsightSessionInfo);

    void onStarted(UpsightContext upsightContext);
}
