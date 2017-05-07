package com.crittercism.internal;

import com.crittercism.error.CRXMLHttpRequestException;

public final class bk {
    public static final int f306a;
    public static final int f307b;
    public static final int f308c;
    public static final int f309d;
    public static final int f310e;
    private static final /* synthetic */ int[] f311f;

    static {
        f306a = 1;
        f307b = 2;
        f308c = 3;
        f309d = 4;
        f310e = 5;
        f311f = new int[]{f306a, f307b, f308c, f309d, f310e};
    }

    public static int m272a(Throwable th) {
        int i = f309d - 1;
        if (th instanceof CRXMLHttpRequestException) {
            return f310e - 1;
        }
        return i;
    }
}
