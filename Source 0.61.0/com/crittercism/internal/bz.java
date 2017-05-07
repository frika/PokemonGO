package com.crittercism.internal;

import android.content.Context;
import android.content.SharedPreferences;

public final class bz {
    public SharedPreferences f402a;

    public bz(Context context) {
        this.f402a = context.getSharedPreferences("com.crittercism.usersettings", 0);
        if (!this.f402a.contains("sessionIDSetting")) {
            this.f402a.edit().putInt("sessionIDSetting", 0).commit();
        }
    }
}
