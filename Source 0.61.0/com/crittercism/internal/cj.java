package com.crittercism.internal;

import android.content.Context;
import spacemadness.com.lunarconsole.BuildConfig;

public final class cj {
    public am f491a;
    public C0254d f492b;
    public C0230c f493c;
    public String f494d;

    public cj(am amVar, C0254d c0254d, Context context) {
        this.f491a = amVar;
        this.f492b = c0254d;
        this.f493c = new C0230c(context);
        this.f494d = m378a(context);
    }

    private static String m378a(Context context) {
        try {
            return cd.m363a(context, "www/error.js");
        } catch (Throwable e) {
            cc.m359b(e);
            return BuildConfig.FLAVOR;
        }
    }
}
