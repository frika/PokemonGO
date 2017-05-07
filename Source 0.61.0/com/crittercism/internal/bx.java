package com.crittercism.internal;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONException;
import org.json.JSONObject;

public final class bx {
    public SharedPreferences f400a;

    public bx(Context context) {
        this.f400a = context.getSharedPreferences("com.crittercism.usersettings", 0);
        if (this.f400a.contains("optOutStatusSettings")) {
            try {
                this.f400a.edit().putBoolean("isOptedOut", new JSONObject(this.f400a.getString("optOutStatusSettings", null)).getBoolean("optOutStatus")).commit();
            } catch (JSONException e) {
            }
            this.f400a.edit().remove("optOutStatusSettings").commit();
        }
    }

    public final boolean m328a() {
        return this.f400a.getBoolean("isOptedOut", false);
    }
}
