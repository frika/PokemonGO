package com.crittercism.internal;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.SparseArray;
import java.text.ParseException;

/* renamed from: com.crittercism.internal.a */
public enum C0176a {
    MOBILE(0),
    WIFI(1),
    UNKNOWN(2),
    NOT_CONNECTED(3);
    
    private static SparseArray<C0176a> f29f;
    int f31e;

    static {
        SparseArray sparseArray = new SparseArray();
        f29f = sparseArray;
        sparseArray.put(0, MOBILE);
        f29f.put(1, WIFI);
    }

    private C0176a(int i) {
        this.f31e = i;
    }

    public static C0176a m24a(ConnectivityManager connectivityManager) {
        if (connectivityManager == null) {
            return UNKNOWN;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return NOT_CONNECTED;
        }
        C0176a c0176a = (C0176a) f29f.get(activeNetworkInfo.getType());
        if (c0176a == null) {
            return UNKNOWN;
        }
        return c0176a;
    }

    public static C0176a m23a(int i) {
        for (C0176a c0176a : C0176a.values()) {
            if (c0176a.f31e == i) {
                return c0176a;
            }
        }
        throw new ParseException("Unknown status code: " + Integer.toString(i), 0);
    }
}
