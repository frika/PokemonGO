package com.upsight.android.googlepushservices.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import com.upsight.android.Upsight;

public final class PushBroadcastReceiver extends WakefulBroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (Upsight.isEnabled(context)) {
            WakefulBroadcastReceiver.startWakefulService(context, intent.setComponent(new ComponentName(context.getPackageName(), PushIntentService.class.getName())));
        }
    }
}
