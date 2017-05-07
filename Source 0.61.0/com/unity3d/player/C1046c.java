package com.unity3d.player;

import android.util.Log;

/* renamed from: com.unity3d.player.c */
final class C1046c {
    protected static boolean f675a;

    static {
        f675a = false;
    }

    protected static void Log(int i, String str) {
        if (!f675a) {
            if (i == 6) {
                Log.e("Unity", str);
            }
            if (i == 5) {
                Log.w("Unity", str);
            }
        }
    }
}
