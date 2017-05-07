package com.nianticproject.platform.pushnotifications;

import android.content.Context;
import android.support.annotation.Nullable;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import java.io.IOException;

public class NotificationManager {
    private static final String TAG = "NotificationManager";
    private Context mContext;
    private String mSenderId;

    public static NotificationManager getInstance(Context context, String senderId) {
        return new NotificationManager(context, senderId);
    }

    public NotificationManager(Context context, String senderId) {
        this.mContext = context;
        this.mSenderId = senderId;
    }

    @Nullable
    public String notificationRegisterGmsId() throws IOException {
        return InstanceID.getInstance(this.mContext).getToken(this.mSenderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
    }

    @Nullable
    public void notificationUnregisterGmsId() throws IOException {
        InstanceID.getInstance(this.mContext).deleteInstanceID();
    }
}
