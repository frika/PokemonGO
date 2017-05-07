package com.crittercism.internal;

import android.content.Context;
import com.crittercism.app.CrittercismConfig;

public final class ao {
    public boolean f103a;
    public boolean f104b;
    private boolean f105c;

    public ao(Context context, CrittercismConfig crittercismConfig) {
        this.f103a = crittercismConfig.isLogcatReportingEnabled();
        this.f104b = m114a("android.permission.ACCESS_NETWORK_STATE", context);
        this.f105c = m114a("android.permission.GET_TASKS", context);
    }

    private static boolean m114a(String str, Context context) {
        return context.getPackageManager().checkPermission(str, context.getPackageName()) == 0;
    }

    public static boolean m113a(Context context, String str) {
        return context.getPackageManager().checkPermission(str, context.getPackageName()) != -1;
    }
}
