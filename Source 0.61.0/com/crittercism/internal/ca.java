package com.crittercism.internal;

import android.os.Build;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import com.crittercism.internal.aw.C0195b;
import com.google.android.exoplayer.util.NalUnitUtil;
import com.mopub.volley.DefaultRetryPolicy;
import com.upsight.mediation.ads.adapters.NetworkWrapper;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import com.voxelbusters.nativeplugins.defines.Keys.Twitter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class ca implements bf {
    String f430a;
    long f431b;
    public int f432c;
    long f433d;
    long f434e;
    int f435f;
    int f436g;
    String f437h;
    List<C0232b> f438i;
    float f439j;

    /* renamed from: com.crittercism.internal.ca.a */
    public static class C0231a {
        public String f404a;
        public long f405b;
        public int f406c;
        public long f407d;
        int f408e;

        public C0231a() {
            this.f408e = C0235e.f424a;
        }

        public final ca m330a() {
            return new ca(this.f404a, this.f405b, this.f406c, this.f407d, this.f408e);
        }
    }

    /* renamed from: com.crittercism.internal.ca.b */
    static class C0232b implements Comparable<C0232b> {
        public int f409a;
        public long f410b;

        public final /* bridge */ /* synthetic */ int compareTo(@NonNull Object obj) {
            C0232b c0232b = (C0232b) obj;
            if (this.f410b < c0232b.f410b) {
                return -1;
            }
            if (this.f410b == c0232b.f410b) {
                return 0;
            }
            return 1;
        }

        public C0232b(int i, long j) {
            this.f409a = i;
            this.f410b = j;
        }

        public C0232b(JSONObject jSONObject) {
            this.f409a = C0233c.m331a()[jSONObject.getInt(Keys.TYPE)];
            this.f410b = jSONObject.getLong("time");
        }
    }

    /* renamed from: com.crittercism.internal.ca.c */
    enum C0233c {
        ;

        public static int[] m331a() {
            return (int[]) f413c.clone();
        }

        static {
            f411a = 1;
            f412b = 2;
            f413c = new int[]{f411a, f412b};
        }
    }

    /* renamed from: com.crittercism.internal.ca.d */
    public enum C0234d {
        ;

        public static int[] m332a() {
            return (int[]) f423j.clone();
        }

        static {
            f414a = 1;
            f415b = 2;
            f416c = 3;
            f417d = 4;
            f418e = 5;
            f419f = 6;
            f420g = 7;
            f421h = 8;
            f422i = 9;
            f423j = new int[]{f414a, f415b, f416c, f417d, f418e, f419f, f420g, f421h, f422i};
        }
    }

    /* renamed from: com.crittercism.internal.ca.e */
    public enum C0235e {
        ;

        public static int[] m333a() {
            return (int[]) f427d.clone();
        }

        static {
            f424a = 1;
            f425b = 2;
            f426c = 3;
            f427d = new int[]{f424a, f425b, f426c};
        }
    }

    /* renamed from: com.crittercism.internal.ca.f */
    public static class C0236f extends bv {
        private au f428c;
        private av<at> f429d;

        public final bs m335a(as asVar, List<? extends bf> list) {
            URL url = new URL(asVar.f179c + "/api/v1/transactions");
            try {
                JSONObject jSONObject = new JSONObject();
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("appID", this.f428c.f207e);
                jSONObject2.put("deviceID", this.f428c.m185h());
                jSONObject2.put("crPlatform", "android");
                jSONObject2.put("crVersion", "5.8.2");
                jSONObject2.put("deviceModel", Build.MODEL);
                jSONObject2.put("osName", "android");
                jSONObject2.put("osVersion", VERSION.RELEASE);
                jSONObject2.put("carrier", this.f428c.m179b());
                jSONObject2.put("mobileCountryCode", this.f428c.m180c());
                jSONObject2.put("mobileNetworkCode", this.f428c.m181d());
                jSONObject2.put("appVersion", this.f428c.f203a.f46a);
                jSONObject2.put("locale", this.f428c.m186i());
                jSONObject.put("appState", jSONObject2);
                JSONArray jSONArray = new JSONArray();
                for (bf g : list) {
                    jSONArray.put(g.m139g());
                }
                jSONObject.put("transactions", jSONArray);
                if (C0236f.m334a(list)) {
                    jSONObject.put("systemBreadcrumbs", this.f429d.m192a());
                    jSONObject.put("breadcrumbs", new JSONObject());
                    jSONObject.put("endpoints", new JSONArray());
                }
                return bs.m314a(url, this.a, jSONObject);
            } catch (Throwable e) {
                throw ((IOException) new IOException(e.getMessage()).initCause(e));
            }
        }

        private static boolean m334a(List<? extends bf> list) {
            for (bf bfVar : list) {
                int i = ((ca) bfVar).f435f;
                if (i != C0234d.f416c && i != C0234d.f422i && i != C0234d.f421h) {
                    return true;
                }
            }
            return false;
        }

        public C0236f(av<at> avVar, au auVar) {
            super(auVar.f207e, auVar.m185h());
            this.f428c = auVar;
            this.f429d = avVar;
        }
    }

    /* renamed from: com.crittercism.internal.ca.g */
    public static class C0237g implements C0195b<ca> {
        private C0237g() {
        }

        public final /* synthetic */ bf m337a(File file) {
            return C0237g.m336b(file);
        }

        public final /* synthetic */ void m338a(bf bfVar, OutputStream outputStream) {
            ca caVar = (ca) bfVar;
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put(Twitter.NAME, caVar.f430a);
                jSONObject.put(GameServices.STATE, caVar.f435f - 1);
                jSONObject.put(NetworkWrapper.TIMEOUT, caVar.f431b);
                jSONObject.put(GameServices.SCORE_VALUE, caVar.f432c);
                jSONObject.put("startTime", caVar.f433d);
                jSONObject.put("endTime", caVar.f434e);
                jSONObject.put("sequenceNumber", caVar.f437h);
                jSONObject.put("rate", (double) caVar.f439j);
                jSONObject.put(Keys.TYPE, caVar.f436g - 1);
                JSONArray jSONArray = new JSONArray();
                for (C0232b c0232b : caVar.f438i) {
                    jSONArray.put(new JSONObject().put(Keys.TYPE, c0232b.f409a - 1).put("time", c0232b.f410b));
                }
                jSONObject.put("lifeCycleTransitions", jSONArray);
                outputStream.write(jSONObject.toString().getBytes("UTF8"));
            } catch (JSONException e) {
                throw new IOException(e.getMessage());
            }
        }

        private static ca m336b(File file) {
            try {
                JSONObject jSONObject = new JSONObject(cd.m366b(file));
                ca caVar = new ca();
                caVar.f430a = jSONObject.getString(Twitter.NAME);
                caVar.f435f = C0234d.m332a()[jSONObject.getInt(GameServices.STATE)];
                caVar.f431b = jSONObject.getLong(NetworkWrapper.TIMEOUT);
                caVar.f432c = jSONObject.getInt(GameServices.SCORE_VALUE);
                caVar.f433d = jSONObject.getLong("startTime");
                caVar.f434e = jSONObject.getLong("endTime");
                caVar.f437h = jSONObject.getString("sequenceNumber");
                caVar.f439j = (float) jSONObject.getDouble("rate");
                caVar.f436g = C0235e.m333a()[jSONObject.getInt(Keys.TYPE)];
                JSONArray jSONArray = jSONObject.getJSONArray("lifeCycleTransitions");
                for (int i = 0; i < jSONArray.length(); i++) {
                    caVar.f438i.add(new C0232b(jSONArray.getJSONObject(i)));
                }
                return caVar;
            } catch (JSONException e) {
                throw new IOException(e.getMessage());
            }
        }
    }

    public final /* synthetic */ Object m343g() {
        return m341b();
    }

    public ca(String str, long j, int i, long j2, int i2) {
        this.f431b = -1;
        this.f432c = -1;
        this.f434e = -1;
        this.f435f = C0234d.f415b;
        this.f436g = C0235e.f424a;
        this.f437h = be.f269a.m269a();
        this.f438i = new LinkedList();
        this.f439j = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        if (str.length() > NalUnitUtil.EXTENDED_SAR) {
            str = str.substring(0, NalUnitUtil.EXTENDED_SAR);
        }
        this.f430a = str;
        this.f432c = i;
        this.f433d = j;
        this.f431b = j2;
        this.f436g = i2;
    }

    private ca() {
        this.f431b = -1;
        this.f432c = -1;
        this.f434e = -1;
        this.f435f = C0234d.f415b;
        this.f436g = C0235e.f424a;
        this.f437h = be.f269a.m269a();
        this.f438i = new LinkedList();
        this.f439j = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
    }

    public final void m340a(int i, long j) {
        if (this.f435f == C0234d.f415b) {
            this.f434e = j;
            if (m339a() > this.f431b) {
                this.f435f = C0234d.f419f;
            } else {
                this.f435f = i;
            }
        }
    }

    public final long m339a() {
        long currentTimeMillis;
        long j = 0;
        long j2 = this.f434e;
        if (this.f434e < 0) {
            currentTimeMillis = System.currentTimeMillis();
        } else {
            currentTimeMillis = j2;
        }
        Collections.sort(this.f438i);
        Iterator it = this.f438i.iterator();
        while (it.hasNext()) {
            C0232b c0232b = (C0232b) it.next();
            if (c0232b.f410b < this.f433d) {
                it.remove();
            } else if (c0232b.f410b > currentTimeMillis) {
                it.remove();
            }
        }
        if (this.f435f == C0234d.f419f) {
            return this.f431b;
        }
        if (this.f436g == C0235e.f426c || this.f438i.size() == 0) {
            return currentTimeMillis - this.f433d;
        }
        int i;
        int i2 = C0233c.f412b;
        if (((C0232b) this.f438i.get(0)).f409a == C0233c.f412b) {
            i = C0233c.f411a;
        } else {
            i = i2;
        }
        long j3 = this.f433d;
        i2 = i;
        for (C0232b c0232b2 : this.f438i) {
            if (c0232b2.f410b >= this.f433d) {
                if (c0232b2.f410b > currentTimeMillis) {
                    break;
                }
                if (i2 == C0233c.f412b) {
                    j += c0232b2.f410b - j3;
                }
                j3 = c0232b2.f410b;
                i2 = c0232b2.f409a;
            }
        }
        if (i2 == C0233c.f412b) {
            return j + (currentTimeMillis - j3);
        }
        return j;
    }

    public final JSONArray m341b() {
        JSONArray jSONArray = null;
        try {
            Object obj;
            JSONArray put = new JSONArray().put(this.f430a).put(this.f435f - 1).put(((double) this.f431b) / 1000.0d);
            if (this.f432c == -1) {
                obj = JSONObject.NULL;
            } else {
                obj = Integer.valueOf(this.f432c);
            }
            jSONArray = put.put(obj).put(new JSONObject()).put(cf.f482a.m374a(new Date(this.f433d))).put(cf.f482a.m374a(new Date(this.f434e)));
            if (VERSION.SDK_INT >= 14) {
                jSONArray.put(((double) m339a()) / 1000.0d);
            } else {
                jSONArray.put(JSONObject.NULL);
            }
        } catch (JSONException e) {
        }
        return jSONArray;
    }

    public final String m342f() {
        return this.f437h;
    }
}
