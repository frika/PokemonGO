package com.upsight.mediation.log;

import android.os.Build.VERSION;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.Log;
import android.webkit.WebView;
import com.google.android.exoplayer.upstream.UdpDataSource;

public class FuseLog {
    private static boolean INTERNAL;
    public static boolean LOG;
    private static LogBuffer buffer;
    public static boolean debug;
    private static boolean testingMode;
    public static boolean veryDebug;

    static {
        buffer = new LogBuffer(100, UdpDataSource.DEFAULT_MAX_PACKET_SIZE);
        debug = false;
        veryDebug = false;
        INTERNAL = false;
        testingMode = false;
        LOG = true;
    }

    public static void enableInternalLogging() {
        buffer = new LogBuffer(Callback.DEFAULT_DRAG_ANIMATION_DURATION, UdpDataSource.DEFAULT_MAX_PACKET_SIZE);
        debug = true;
        veryDebug = true;
        INTERNAL = true;
        if (VERSION.SDK_INT >= 19) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    public static void clearBuffer() {
        if (INTERNAL) {
            buffer = new LogBuffer(Callback.DEFAULT_DRAG_ANIMATION_DURATION, UdpDataSource.DEFAULT_MAX_PACKET_SIZE);
        }
    }

    public static void public_e(String tag, String s) {
        if (!testingMode) {
            buffer.append("e", tag, s);
            Log.e(tag, s);
        }
    }

    public static void public_e(String tag, String s, Exception e) {
        if (!testingMode) {
            buffer.append("e", tag, s);
            Log.e(tag, s);
        }
    }

    public static void public_w(String tag, String s) {
        if (!testingMode) {
            buffer.append("w", tag, s);
            Log.w(tag, s);
        }
    }

    public static void public_w(String tag, String s, Throwable th) {
        if (!testingMode) {
            buffer.append("w", tag, s);
            Log.w(tag, s, th);
        }
    }

    public static void m605e(String tag, String s) {
        if (!testingMode) {
            buffer.append("e", tag, s);
            if (debug) {
                Log.e(tag, s);
            }
        }
    }

    public static void m606e(String tag, String s, Throwable th) {
        if (!testingMode) {
            buffer.append("e", tag, s);
            if (debug) {
                Log.e(tag, s, th);
            }
        }
    }

    public static void m609w(String tag, String s) {
        if (!testingMode) {
            buffer.append("w", tag, s);
            if (debug) {
                Log.w(tag, s);
            }
        }
    }

    public static void m610w(String tag, String s, Throwable th) {
        if (!testingMode) {
            buffer.append("w", tag, s);
            if (debug) {
                Log.w(tag, s, th);
            }
        }
    }

    public static void m607i(String tag, String s) {
        if (!testingMode) {
            buffer.append("i", tag, s);
            if (debug) {
                Log.i(tag, s);
            }
        }
    }

    public static void m604d(String tag, String s) {
        if (!testingMode) {
            buffer.append("d", tag, s);
            if (veryDebug) {
                Log.d(tag, s);
            }
        }
    }

    public static void m608v(String tag, String s) {
        if (!testingMode) {
            buffer.append("v", tag, s);
            if (veryDebug) {
                Log.v(tag, s);
            }
        }
    }

    public static void internal(String tag, String s) {
        if (!testingMode) {
            buffer.append("INTERNAL", tag, s);
            if (INTERNAL) {
                Log.i(tag, "INTERNAL | " + s);
            }
        }
    }

    public static String[] getLogHistory() {
        return buffer.getLog();
    }

    public static void TOAST(String text) {
        if (!testingMode) {
        }
    }

    public static void disableForTests() {
        testingMode = true;
    }
}
