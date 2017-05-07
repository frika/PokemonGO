package com.crittercism.internal;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import java.math.BigInteger;
import org.json.JSONException;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.BuildConfig;

public final class au {
    public ak f203a;
    Context f204b;
    ao f205c;
    ax f206d;
    public String f207e;
    public bz f208f;
    public String f209g;
    private bw f210h;
    private C0204a f211i;

    /* renamed from: com.crittercism.internal.au.a */
    interface C0204a {
        String m164a();

        String m165b();

        String m166c();

        String m167d();
    }

    /* renamed from: com.crittercism.internal.au.b */
    static class C0205b implements C0204a {
        private C0205b() {
        }

        public final String m169b() {
            int i = VERSION.SDK_INT;
            if (VERSION.SDK_INT < 18) {
                return null;
            }
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return BigInteger.valueOf(statFs.getAvailableBlocksLong()).multiply(BigInteger.valueOf(statFs.getBlockSizeLong())).toString();
        }

        public final String m168a() {
            int i = VERSION.SDK_INT;
            if (VERSION.SDK_INT < 18) {
                return null;
            }
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return BigInteger.valueOf(statFs.getBlockCountLong()).multiply(BigInteger.valueOf(statFs.getBlockSizeLong())).toString();
        }

        public final String m171d() {
            int i = VERSION.SDK_INT;
            if (VERSION.SDK_INT < 18) {
                return null;
            }
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return BigInteger.valueOf(statFs.getAvailableBlocksLong()).multiply(BigInteger.valueOf(statFs.getBlockSizeLong())).toString();
        }

        public final String m170c() {
            int i = VERSION.SDK_INT;
            if (VERSION.SDK_INT < 18) {
                return null;
            }
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return BigInteger.valueOf(statFs.getBlockCountLong()).multiply(BigInteger.valueOf(statFs.getBlockSizeLong())).toString();
        }
    }

    /* renamed from: com.crittercism.internal.au.c */
    static class C0206c implements C0204a {
        private C0206c() {
        }

        public final String m173b() {
            int i = VERSION.SDK_INT;
            if (VERSION.SDK_INT >= 18) {
                return null;
            }
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return BigInteger.valueOf((long) statFs.getAvailableBlocks()).multiply(BigInteger.valueOf((long) statFs.getBlockSize())).toString();
        }

        public final String m172a() {
            int i = VERSION.SDK_INT;
            if (VERSION.SDK_INT >= 18) {
                return null;
            }
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return BigInteger.valueOf((long) statFs.getBlockCount()).multiply(BigInteger.valueOf((long) statFs.getBlockSize())).toString();
        }

        public final String m175d() {
            int i = VERSION.SDK_INT;
            if (VERSION.SDK_INT >= 18) {
                return null;
            }
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return BigInteger.valueOf((long) statFs.getAvailableBlocks()).multiply(BigInteger.valueOf((long) statFs.getBlockSize())).toString();
        }

        public final String m174c() {
            int i = VERSION.SDK_INT;
            if (VERSION.SDK_INT >= 18) {
                return null;
            }
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return BigInteger.valueOf((long) statFs.getBlockCount()).multiply(BigInteger.valueOf((long) statFs.getBlockSize())).toString();
        }
    }

    public au(ak akVar, Context context, ao aoVar, String str) {
        this.f203a = akVar;
        this.f204b = context;
        this.f205c = aoVar;
        this.f206d = new ay();
        this.f207e = str;
        this.f210h = new bw(context);
        this.f208f = new bz(context);
        if (VERSION.SDK_INT >= 18) {
            this.f211i = new C0205b();
        } else {
            this.f211i = new C0206c();
        }
    }

    public final Integer m177a() {
        return Integer.valueOf(this.f203a.f47b);
    }

    public final String m179b() {
        try {
            return ((TelephonyManager) this.f204b.getSystemService("phone")).getNetworkOperatorName();
        } catch (Exception e) {
            return BuildConfig.FLAVOR;
        }
    }

    public final Integer m180c() {
        int parseInt;
        try {
            String networkOperator = ((TelephonyManager) this.f204b.getSystemService("phone")).getNetworkOperator();
            if (networkOperator != null) {
                parseInt = Integer.parseInt(networkOperator.substring(0, 3));
                return Integer.valueOf(parseInt);
            }
        } catch (Exception e) {
        }
        parseInt = 0;
        return Integer.valueOf(parseInt);
    }

    public final Integer m181d() {
        Integer valueOf = Integer.valueOf(0);
        try {
            String networkOperator = ((TelephonyManager) this.f204b.getSystemService("phone")).getNetworkOperator();
            if (networkOperator != null) {
                return Integer.valueOf(Integer.parseInt(networkOperator.substring(3)));
            }
        } catch (Exception e) {
        }
        return valueOf;
    }

    public final Float m182e() {
        return Float.valueOf(this.f204b.getResources().getDisplayMetrics().density);
    }

    public final float m183f() {
        return this.f204b.getResources().getDisplayMetrics().xdpi;
    }

    public final float m184g() {
        return this.f204b.getResources().getDisplayMetrics().ydpi;
    }

    public final String m185h() {
        String str = BuildConfig.FLAVOR;
        if (this.f210h != null) {
            return this.f210h.m327a();
        }
        return str;
    }

    public final String m186i() {
        String language = this.f204b.getResources().getConfiguration().locale.getLanguage();
        if (language == null || language.length() == 0) {
            return "en";
        }
        return language;
    }

    public final String m187j() {
        try {
            return this.f211i.m165b();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            return null;
        }
    }

    public final String m188k() {
        try {
            return this.f211i.m164a();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            return null;
        }
    }

    public static Long m176l() {
        return Long.valueOf(Runtime.getRuntime().maxMemory());
    }

    public final String m189m() {
        try {
            return this.f211i.m167d();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            return null;
        }
    }

    public final String m190n() {
        try {
            return this.f211i.m166c();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            return null;
        }
    }

    final JSONObject m178a(int i) {
        if (!this.f205c.f104b) {
            return null;
        }
        if (!ConnectivityManager.isNetworkTypeValid(i)) {
            return null;
        }
        NetworkInfo networkInfo = ((ConnectivityManager) this.f204b.getSystemService("connectivity")).getNetworkInfo(i);
        JSONObject jSONObject = new JSONObject();
        if (networkInfo != null) {
            try {
                jSONObject.put("available", networkInfo.isAvailable());
                jSONObject.put("connected", networkInfo.isConnected());
                if (!networkInfo.isConnected()) {
                    jSONObject.put("connecting", networkInfo.isConnectedOrConnecting());
                }
                jSONObject.put("failover", networkInfo.isFailover());
                if (i != 0) {
                    return jSONObject;
                }
                jSONObject.put("roaming", networkInfo.isRoaming());
                return jSONObject;
            } catch (JSONException e) {
                return null;
            }
        }
        jSONObject.put("available", false);
        jSONObject.put("connected", false);
        jSONObject.put("connecting", false);
        jSONObject.put("failover", false);
        if (i != 0) {
            return jSONObject;
        }
        jSONObject.put("roaming", false);
        return jSONObject;
    }
}
