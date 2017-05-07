package com.upsight.mediation.mraid.internal;

import com.upsight.mediation.log.FuseLog;

public class MRAIDLog {
    private static final String TAG = "MRAID";

    public static void m611d(String msg) {
        FuseLog.m604d(TAG, msg);
    }

    public static void m613e(String msg) {
        FuseLog.m605e(TAG, msg);
    }

    public static void m615i(String msg) {
        FuseLog.m607i(TAG, msg);
    }

    public static void m617v(String msg) {
        FuseLog.m608v(TAG, msg);
    }

    public static void m619w(String msg) {
        FuseLog.m609w(TAG, msg);
    }

    public static void m612d(String subTag, String msg) {
        FuseLog.m604d(TAG, "[" + subTag + "] " + msg);
    }

    public static void m614e(String subTag, String msg) {
        FuseLog.m605e(TAG, "[" + subTag + "] " + msg);
    }

    public static void m616i(String subTag, String msg) {
        FuseLog.m607i(TAG, "[" + subTag + "] " + msg);
    }

    public static void m618v(String subTag, String msg) {
        FuseLog.m608v(TAG, "[" + subTag + "] " + msg);
    }

    public static void m620w(String subTag, String msg) {
        FuseLog.m609w(TAG, "[" + subTag + "] " + msg);
    }
}
