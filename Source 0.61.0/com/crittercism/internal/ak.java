package com.crittercism.internal;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import com.crittercism.app.CrittercismConfig;
import spacemadness.com.lunarconsole.BuildConfig;

public final class ak {
    public String f46a;
    public int f47b;

    public ak(Context context, CrittercismConfig crittercismConfig) {
        this.f46a = BuildConfig.VERSION_NAME;
        this.f47b = 0;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            this.f46a = packageInfo.versionName;
            this.f47b = packageInfo.versionCode;
        } catch (NameNotFoundException e) {
        }
        String customVersionName = crittercismConfig.getCustomVersionName();
        if (customVersionName != null && customVersionName.length() > 0) {
            this.f46a = customVersionName;
        }
        if (crittercismConfig.isVersionCodeToBeIncludedInVersionString()) {
            this.f46a += "-" + Integer.toString(this.f47b);
        }
    }
}
