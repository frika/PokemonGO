package android.support.v4.media.session;

import android.app.PendingIntent;
import android.content.Context;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.os.SystemClock;
import android.support.v4.media.session.MediaSessionCompatApi14.Callback;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.exoplayer.util.MimeTypes;
import com.mopub.volley.DefaultRetryPolicy;

public class MediaSessionCompatApi18 {
    private static final long ACTION_SEEK_TO = 256;

    static class OnPlaybackPositionUpdateListener<T extends Callback> implements android.media.RemoteControlClient.OnPlaybackPositionUpdateListener {
        protected final T mCallback;

        public OnPlaybackPositionUpdateListener(T callback) {
            this.mCallback = callback;
        }

        public void onPlaybackPositionUpdate(long newPositionMs) {
            this.mCallback.onSeekTo(newPositionMs);
        }
    }

    public static Object createPlaybackPositionUpdateListener(Callback callback) {
        return new OnPlaybackPositionUpdateListener(callback);
    }

    public static void registerMediaButtonEventReceiver(Context context, PendingIntent pi) {
        ((AudioManager) context.getSystemService(MimeTypes.BASE_TYPE_AUDIO)).registerMediaButtonEventReceiver(pi);
    }

    public static void unregisterMediaButtonEventReceiver(Context context, PendingIntent pi) {
        ((AudioManager) context.getSystemService(MimeTypes.BASE_TYPE_AUDIO)).unregisterMediaButtonEventReceiver(pi);
    }

    public static void setState(Object rccObj, int state, long position, float speed, long updateTime) {
        long currTime = SystemClock.elapsedRealtime();
        if (state == 3 && position > 0) {
            long diff = 0;
            if (updateTime > 0) {
                diff = currTime - updateTime;
                if (speed > 0.0f && speed != DefaultRetryPolicy.DEFAULT_BACKOFF_MULT) {
                    diff = (long) (((float) diff) * speed);
                }
            }
            position += diff;
        }
        ((RemoteControlClient) rccObj).setPlaybackState(MediaSessionCompatApi14.getRccStateFromState(state), position, speed);
    }

    public static void setTransportControlFlags(Object rccObj, long actions) {
        ((RemoteControlClient) rccObj).setTransportControlFlags(getRccTransportControlFlagsFromActions(actions));
    }

    public static void setOnPlaybackPositionUpdateListener(Object rccObj, Object onPositionUpdateObj) {
        ((RemoteControlClient) rccObj).setPlaybackPositionUpdateListener((android.media.RemoteControlClient.OnPlaybackPositionUpdateListener) onPositionUpdateObj);
    }

    static int getRccTransportControlFlagsFromActions(long actions) {
        int transportControlFlags = MediaSessionCompatApi14.getRccTransportControlFlagsFromActions(actions);
        if ((ACTION_SEEK_TO & actions) != 0) {
            return transportControlFlags | AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
        }
        return transportControlFlags;
    }
}
