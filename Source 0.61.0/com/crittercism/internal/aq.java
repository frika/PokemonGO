package com.crittercism.internal;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Debug;
import android.os.Debug.MemoryInfo;
import android.view.Display;
import android.view.WindowManager;
import com.crittercism.integrations.PluginException;
import com.crittercism.internal.aw.C0195b;
import com.google.android.gms.location.places.Place;
import com.mopub.volley.DefaultRetryPolicy;
import com.voxelbusters.nativeplugins.defines.Keys.Twitter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.BuildConfig;

public final class aq implements bf {
    public long f150a;
    JSONArray f151b;
    JSONArray f152c;
    public String f153d;
    public String f154e;
    JSONArray f155f;
    public JSONArray f156g;
    public String f157h;
    JSONObject f158i;
    int f159j;
    boolean f160k;
    String f161l;
    public float f162m;

    /* renamed from: com.crittercism.internal.aq.a */
    public static class C0196a implements C0195b<aq> {
        private C0196a() {
        }

        public final /* synthetic */ bf m134a(File file) {
            return C0196a.m133b(file);
        }

        public final /* synthetic */ void m135a(bf bfVar, OutputStream outputStream) {
            aq aqVar = (aq) bfVar;
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("currentThreadID", aqVar.f150a);
                jSONObject.put("breadcrumbs", aqVar.f151b);
                jSONObject.put("txns", aqVar.f152c);
                jSONObject.put("exceptionName", aqVar.f153d);
                jSONObject.put("exceptionReason", aqVar.f154e);
                jSONObject.put("stacktrace", aqVar.f155f);
                jSONObject.put("threads", aqVar.f156g);
                jSONObject.put("ts", aqVar.f157h);
                jSONObject.put("rate", (double) aqVar.f162m);
                jSONObject.put("appState", aqVar.f158i);
                jSONObject.put("suspectLineIndex", aqVar.f159j);
                jSONObject.put("isPluginException", aqVar.f160k);
                jSONObject.put("fileName", aqVar.f161l);
                outputStream.write(jSONObject.toString().getBytes());
            } catch (JSONException e) {
                throw new IOException(e.getMessage());
            }
        }

        private static aq m133b(File file) {
            try {
                JSONObject jSONObject = new JSONObject(cd.m366b(file));
                aq aqVar = new aq();
                aqVar.f150a = jSONObject.getLong("currentThreadID");
                aqVar.f151b = jSONObject.getJSONArray("breadcrumbs");
                aqVar.f152c = jSONObject.getJSONArray("txns");
                aqVar.f153d = jSONObject.getString("exceptionName");
                aqVar.f154e = jSONObject.getString("exceptionReason");
                aqVar.f155f = jSONObject.getJSONArray("stacktrace");
                aqVar.f156g = jSONObject.optJSONArray("threads");
                aqVar.f157h = jSONObject.getString("ts");
                aqVar.f162m = (float) jSONObject.getDouble("rate");
                aqVar.f158i = jSONObject.getJSONObject("appState");
                aqVar.f159j = jSONObject.getInt("suspectLineIndex");
                aqVar.f160k = jSONObject.getBoolean("isPluginException");
                aqVar.f161l = jSONObject.getString("fileName");
                return aqVar;
            } catch (JSONException e) {
                throw new IOException(e.getMessage());
            }
        }
    }

    /* renamed from: com.crittercism.internal.aq.b */
    public static class C0197b extends bv {
        private String f148c;
        private String f149d;

        public C0197b(String str, String str2, String str3, String str4) {
            super(str, str2);
            this.f148c = str3;
            this.f149d = str4;
        }

        public final bs m137a(as asVar, List<? extends bf> list) {
            URL url = new URL(asVar.f178b, this.f149d);
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("app_id", this.a);
                jSONObject.put("hashed_device_id", this.b);
                jSONObject.put("library_version", "5.8.2");
                JSONArray jSONArray = new JSONArray();
                for (bf g : list) {
                    jSONArray.put(g.m139g());
                }
                jSONObject.put(this.f148c, jSONArray);
                return bs.m314a(url, this.a, jSONObject);
            } catch (Throwable e) {
                throw ((IOException) new IOException(e.getMessage()).initCause(e));
            }
        }
    }

    public aq(Throwable th, au auVar, long j) {
        this.f151b = new JSONArray();
        this.f152c = new JSONArray();
        this.f154e = BuildConfig.FLAVOR;
        this.f155f = new JSONArray();
        this.f157h = cf.f482a.m373a();
        this.f159j = -1;
        this.f160k = false;
        this.f161l = be.f269a.m269a();
        this.f162m = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        this.f160k = th instanceof PluginException;
        this.f161l = be.f269a.m269a();
        this.f158i = new JSONObject();
        try {
            Object valueOf;
            int i;
            JSONObject putOpt = this.f158i.putOpt("activity", auVar.f209g).putOpt("app_version", auVar.f203a.f46a).putOpt("app_version_code", auVar.m177a()).putOpt("arch", System.getProperty("os.arch"));
            String str = "battery_level";
            Intent registerReceiver = auVar.f204b.getApplicationContext().registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            if (registerReceiver != null) {
                valueOf = Double.valueOf(1.0d);
                int intExtra = registerReceiver.getIntExtra("level", -1);
                double intExtra2 = (double) registerReceiver.getIntExtra("scale", -1);
                if (intExtra >= 0 && intExtra2 > 0.0d) {
                    valueOf = Double.valueOf(((double) intExtra) / intExtra2);
                }
            } else {
                valueOf = null;
            }
            putOpt = putOpt.putOpt(str, valueOf).putOpt("carrier", auVar.m179b()).putOpt("mobile_country_code", auVar.m180c()).putOpt("mobile_network_code", auVar.m181d()).putOpt("disk_space_free", auVar.m187j()).putOpt("disk_space_total", auVar.m188k()).putOpt("dpi", auVar.m182e()).putOpt("xdpi", Float.valueOf(auVar.m183f())).putOpt("ydpi", Float.valueOf(auVar.m184g())).putOpt("locale", auVar.m186i());
            str = "logcat";
            if (auVar.f205c.f103a) {
                valueOf = auVar.f206d.m211a();
            } else {
                valueOf = null;
            }
            MemoryInfo memoryInfo = new MemoryInfo();
            Debug.getMemoryInfo(memoryInfo);
            putOpt = putOpt.putOpt(str, valueOf).putOpt("memory_usage", Integer.valueOf((memoryInfo.otherPss + (memoryInfo.dalvikPss + memoryInfo.nativePss)) * Place.TYPE_SUBLOCALITY_LEVEL_2)).putOpt("memory_total", au.m176l()).putOpt("mobile_network", auVar.m178a(0)).putOpt(Models.CONTENT_DIRECTORY, Build.MODEL).putOpt(Twitter.NAME, new String());
            str = "orientation";
            i = auVar.f204b.getResources().getConfiguration().orientation;
            if (i == 0) {
                Display defaultDisplay = ((WindowManager) auVar.f204b.getSystemService("window")).getDefaultDisplay();
                if (defaultDisplay.getWidth() == defaultDisplay.getHeight()) {
                    i = 3;
                } else if (defaultDisplay.getWidth() > defaultDisplay.getHeight()) {
                    i = 2;
                } else {
                    i = 1;
                }
            }
            putOpt.putOpt(str, Integer.valueOf(i)).putOpt("sd_space_free", auVar.m189m()).putOpt("sd_space_total", auVar.m190n()).putOpt("system", "android").putOpt("system_version", VERSION.RELEASE).putOpt("wifi", auVar.m178a(1));
        } catch (JSONException e) {
        }
        this.f151b = new JSONArray();
        this.f150a = j;
        this.f153d = m140a(th);
        if (th.getMessage() != null) {
            this.f154e = th.getMessage();
        }
        if (!this.f160k) {
            this.f159j = m142c(th);
        }
        for (Object put : m141b(th)) {
            this.f155f.put(put);
        }
    }

    private aq() {
        this.f151b = new JSONArray();
        this.f152c = new JSONArray();
        this.f154e = BuildConfig.FLAVOR;
        this.f155f = new JSONArray();
        this.f157h = cf.f482a.m373a();
        this.f159j = -1;
        this.f160k = false;
        this.f161l = be.f269a.m269a();
        this.f162m = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
    }

    public final void m143a(av<at> avVar) {
        this.f151b = avVar.m192a();
    }

    public final void m144a(Collection<ca> collection) {
        this.f152c = new JSONArray();
        for (ca b : collection) {
            this.f152c.put(b.m341b());
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String m140a(java.lang.Throwable r3) {
        /*
        r2 = this;
        r0 = r2.f160k;
        if (r0 == 0) goto L_0x000c;
    L_0x0004:
        r3 = (com.crittercism.integrations.PluginException) r3;
        r0 = r3.getExceptionName();
    L_0x000a:
        return r0;
    L_0x000b:
        r3 = r0;
    L_0x000c:
        r0 = r3.getClass();
        r1 = r0.getName();
        r0 = r3.getCause();
        if (r0 == 0) goto L_0x001c;
    L_0x001a:
        if (r0 != r3) goto L_0x000b;
    L_0x001c:
        r0 = r1;
        goto L_0x000a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.crittercism.internal.aq.a(java.lang.Throwable):java.lang.String");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String[] m141b(java.lang.Throwable r3) {
        /*
        r1 = new java.io.StringWriter;
        r1.<init>();
        r2 = new java.io.PrintWriter;
        r2.<init>(r1);
    L_0x000a:
        r3.printStackTrace(r2);
        r0 = r3.getCause();
        if (r0 == 0) goto L_0x0015;
    L_0x0013:
        if (r0 != r3) goto L_0x0020;
    L_0x0015:
        r0 = r1.toString();
        r1 = "\n";
        r0 = r0.split(r1);
        return r0;
    L_0x0020:
        r3 = r0;
        goto L_0x000a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.crittercism.internal.aq.b(java.lang.Throwable):java.lang.String[]");
    }

    private static int m142c(Throwable th) {
        StackTraceElement[] stackTrace = th.getStackTrace();
        int i = 0;
        while (i < stackTrace.length) {
            try {
                Object obj;
                Class cls = Class.forName(stackTrace[i].getClassName());
                for (ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader(); systemClassLoader != null; systemClassLoader = systemClassLoader.getParent()) {
                    if (cls.getClassLoader() == systemClassLoader) {
                        obj = 1;
                        break;
                    }
                }
                obj = null;
                if (obj == null) {
                    return i + 1;
                }
                i++;
            } catch (ClassNotFoundException e) {
            }
        }
        return -1;
    }

    public final String m145f() {
        return this.f161l;
    }

    public final /* synthetic */ Object m146g() {
        Map hashMap = new HashMap();
        hashMap.put("app_state", this.f158i);
        hashMap.put("current_thread_id", Long.valueOf(this.f150a));
        hashMap.put("systemBreadcrumbs", this.f151b);
        if (this.f152c != null && this.f152c.length() > 0) {
            hashMap.put("transactions", this.f152c);
        }
        hashMap.put("exception_name", this.f153d);
        hashMap.put("exception_reason", this.f154e);
        hashMap.put("platform", "android");
        if (this.f156g != null) {
            hashMap.put("threads", this.f156g);
        }
        hashMap.put("ts", this.f157h);
        hashMap.put("rate", Float.valueOf(this.f162m));
        hashMap.put("unsymbolized_stacktrace", this.f155f);
        if (!this.f160k) {
            hashMap.put("suspect_line_index", Integer.valueOf(this.f159j));
        }
        return new JSONObject(hashMap);
    }
}
