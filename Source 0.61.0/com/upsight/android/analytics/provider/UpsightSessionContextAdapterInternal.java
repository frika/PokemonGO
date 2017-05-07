package com.upsight.android.analytics.provider;

import com.upsight.android.UpsightContext;

public class UpsightSessionContextAdapterInternal {
    public static UpsightContext from(UpsightSessionContext upsight) {
        return upsight.getUpsightContext();
    }
}
