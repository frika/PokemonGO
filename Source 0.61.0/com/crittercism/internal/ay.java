package com.crittercism.internal;

import android.os.Build.VERSION;
import org.json.JSONArray;

public final class ay implements ax {
    public static final String f219a;

    static {
        f219a = String.format("%s %d (%s)", new Object[]{"Logcat data is not collected for Android APIs before", Integer.valueOf(16), "Jellybean"});
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static org.json.JSONArray m212a(java.io.InputStream r4) {
        /*
        r1 = new org.json.JSONArray;
        r1.<init>();
        r2 = new java.io.BufferedReader;
        r0 = new java.io.InputStreamReader;
        r0.<init>(r4);
        r2.<init>(r0);
        r0 = 0;
    L_0x0010:
        r3 = r2.readLine();	 Catch:{ IOException -> 0x002a }
        if (r3 == 0) goto L_0x001f;
    L_0x0016:
        r1.put(r3);	 Catch:{ IOException -> 0x002a }
        r0 = r0 + 1;
        r3 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r0 <= r3) goto L_0x0010;
    L_0x001f:
        r2.close();	 Catch:{ IOException -> 0x0023 }
    L_0x0022:
        return r1;
    L_0x0023:
        r0 = move-exception;
        r2 = "LogcatProfiler unable to close input stream";
        com.crittercism.internal.cc.m361c(r2, r0);
        goto L_0x0022;
    L_0x002a:
        r0 = move-exception;
        r3 = "LogcatProfiler encountered an IOException when attempting to read stream.";
        com.crittercism.internal.cc.m361c(r3, r0);	 Catch:{ all -> 0x003b }
        r2.close();	 Catch:{ IOException -> 0x0034 }
        goto L_0x0022;
    L_0x0034:
        r0 = move-exception;
        r2 = "LogcatProfiler unable to close input stream";
        com.crittercism.internal.cc.m361c(r2, r0);
        goto L_0x0022;
    L_0x003b:
        r0 = move-exception;
        r2.close();	 Catch:{ IOException -> 0x0040 }
    L_0x003f:
        throw r0;
    L_0x0040:
        r1 = move-exception;
        r2 = "LogcatProfiler unable to close input stream";
        com.crittercism.internal.cc.m361c(r2, r1);
        goto L_0x003f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.crittercism.internal.ay.a(java.io.InputStream):org.json.JSONArray");
    }

    public final JSONArray m213a() {
        JSONArray jSONArray = new JSONArray();
        Process process = null;
        if (VERSION.SDK_INT < 16) {
            jSONArray.put(f219a);
            jSONArray.put("API level is " + VERSION.SDK_INT + "(" + VERSION.CODENAME + ")");
        } else {
            String num;
            try {
                num = Integer.toString(100);
                process = new ProcessBuilder(new String[]{"logcat", "-t", num, "-v", "time"}).redirectErrorStream(true).start();
                jSONArray = m212a(process.getInputStream());
                if (process != null) {
                    process.destroy();
                }
            } catch (Throwable e) {
                cc.m358b("Unable to collect logcat data", e);
                jSONArray.put("Unable to collect logcat data due to a(n) " + e.getClass().getName());
                num = e.getMessage();
                if (num != null) {
                    jSONArray.put(num);
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Throwable th) {
                if (process != null) {
                    process.destroy();
                }
            }
        }
        return jSONArray;
    }
}
