package com.nianticproject.platform.pushnotifications;

import android.util.Log;
import com.google.android.gms.iid.InstanceIDListenerService;

public class NotificationInstanceIdService extends InstanceIDListenerService {
    private static final String TAG = "NotificationIdService";

    public void onTokenRefresh() {
        Log.v(TAG, "onTokenRefresh");
    }
}
