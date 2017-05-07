package com.crittercism.internal;

import android.content.Context;
import android.net.ConnectivityManager;

/* renamed from: com.crittercism.internal.c */
public final class C0230c {
    public ConnectivityManager f403a;

    public C0230c(Context context) {
        if (context == null) {
            cc.m353a("Given a null Context.");
            return;
        }
        if (context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) == 0) {
            this.f403a = (ConnectivityManager) context.getSystemService("connectivity");
        } else {
            cc.m353a("Add android.permission.ACCESS_NETWORK_STATE to AndroidManifest.xml to get more detailed OPTMZ data");
        }
    }
}
