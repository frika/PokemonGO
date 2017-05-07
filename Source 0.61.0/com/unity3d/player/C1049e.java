package com.unity3d.player;

import android.os.Build.VERSION;

/* renamed from: com.unity3d.player.e */
public final class C1049e {
    static final boolean f680a;
    static final boolean f681b;
    static final C1045b f682c;

    static {
        boolean z = true;
        f680a = VERSION.SDK_INT >= 21;
        if (VERSION.SDK_INT < 23) {
            z = false;
        }
        f681b = z;
        f682c = z ? new C1048d() : null;
    }
}
