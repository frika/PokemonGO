package com.upsight.android.analytics.provider;

import android.content.Context;
import com.upsight.android.UpsightContext;

public class UpsightSessionContext {
    private UpsightContext mUpsight;

    public UpsightSessionContext(UpsightContext upsight) {
        this.mUpsight = upsight;
    }

    public Context getApplicationContext() {
        return this.mUpsight.getApplicationContext();
    }

    UpsightContext getUpsightContext() {
        return this.mUpsight;
    }
}
