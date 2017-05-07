package com.crittercism.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.UUID;

public final class bw {
    private SharedPreferences f397a;
    private SharedPreferences f398b;
    private Context f399c;

    public bw(Context context) {
        if (context == null) {
            throw new NullPointerException("context was null");
        }
        this.f399c = context;
        this.f397a = context.getSharedPreferences("com.crittercism.usersettings", 0);
        this.f398b = context.getSharedPreferences("com.crittercism.prefs", 0);
        if (this.f397a == null) {
            throw new NullPointerException("prefs were null");
        } else if (this.f398b == null) {
            throw new NullPointerException("legacy prefs were null");
        }
    }

    private static String m326b() {
        String str = null;
        try {
            str = UUID.randomUUID().toString();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
        return str;
    }

    public final String m327a() {
        String string = this.f397a.getString("hashedDeviceID", null);
        if (string == null) {
            string = this.f398b.getString("com.crittercism.prefs.did", null);
            if (string != null && m325a(string)) {
                Editor edit = this.f398b.edit();
                edit.remove("com.crittercism.prefs.did");
                edit.commit();
            }
        }
        if (string != null) {
            return string;
        }
        string = m326b();
        m325a(string);
        return string;
    }

    private boolean m325a(String str) {
        Editor edit = this.f397a.edit();
        edit.putString("hashedDeviceID", str);
        return edit.commit();
    }
}
