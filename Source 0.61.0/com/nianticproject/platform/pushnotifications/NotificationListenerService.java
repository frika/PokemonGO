package com.nianticproject.platform.pushnotifications;

import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.gcm.GcmListenerService;
import com.voxelbusters.nativeplugins.defines.Keys;

public class NotificationListenerService extends GcmListenerService {
    private static final String TAG = "NotificationListener";

    public void onMessageReceived(String from, Bundle data) {
        Log.d(TAG, "Message: " + data.getString(Keys.MESSAGE));
    }
}
