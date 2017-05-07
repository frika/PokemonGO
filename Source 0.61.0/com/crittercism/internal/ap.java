package com.crittercism.internal;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.android.exoplayer.hls.HlsChunkSource;
import com.mopub.volley.DefaultRetryPolicy;
import java.util.Iterator;
import java.util.LinkedList;

public final class ap {
    public static final C0194d f108A;
    public static final C0192b f109B;
    public static final C0191a f110C;
    public static final C0191a f111D;
    public static final C0191a f112E;
    public static final C0191a f113F;
    public static final C0191a f114G;
    public static final C0194d f115H;
    public static final C0191a f116a;
    public static final C0191a f117b;
    public static final C0194d f118c;
    public static final C0192b f119d;
    public static final C0191a f120e;
    public static final C0191a f121f;
    public static final C0194d f122g;
    public static final C0192b f123h;
    public static final C0191a f124i;
    public static final C0191a f125j;
    public static final C0194d f126k;
    public static final C0192b f127l;
    public static final C0191a f128m;
    public static final C0191a f129n;
    public static final C0194d f130o;
    public static final C0192b f131p;
    public static final C0191a f132q;
    public static final C0191a f133r;
    public static final C0194d f134s;
    public static final C0192b f135t;
    public static final C0191a f136u;
    public static final C0191a f137v;
    public static final C0194d f138w;
    public static final C0192b f139x;
    public static final C0191a f140y;
    public static final C0191a f141z;
    public final LinkedList<C0193c> f142I;
    private String f143J;
    private Context f144K;
    private SharedPreferences f145L;

    /* renamed from: com.crittercism.internal.ap.e */
    public static abstract class C0190e<T> {
        protected String f106a;
        protected T f107b;

        public abstract T m115a(SharedPreferences sharedPreferences);

        public abstract void m117a(SharedPreferences sharedPreferences, T t);

        public C0190e(String str, T t) {
            this.f106a = str;
            this.f107b = t;
        }

        public final String m116a() {
            return this.f106a;
        }

        public final T m118b() {
            return this.f107b;
        }
    }

    /* renamed from: com.crittercism.internal.ap.a */
    public static class C0191a extends C0190e<Boolean> {
        public final /* synthetic */ void m120a(SharedPreferences sharedPreferences, Object obj) {
            sharedPreferences.edit().putBoolean(this.a, ((Boolean) obj).booleanValue()).commit();
        }

        public C0191a(String str, Boolean bool) {
            super(str, bool);
        }

        public final /* synthetic */ Object m119a(SharedPreferences sharedPreferences) {
            return Boolean.valueOf(sharedPreferences.getBoolean(this.a, ((Boolean) this.b).booleanValue()));
        }
    }

    /* renamed from: com.crittercism.internal.ap.b */
    public static class C0192b extends C0190e<Float> {
        public final /* synthetic */ void m122a(SharedPreferences sharedPreferences, Object obj) {
            sharedPreferences.edit().putFloat(this.a, ((Float) obj).floatValue()).commit();
        }

        public C0192b(String str, Float f) {
            super(str, f);
        }

        public final /* synthetic */ Object m121a(SharedPreferences sharedPreferences) {
            return Float.valueOf(sharedPreferences.getFloat(this.a, ((Float) this.b).floatValue()));
        }
    }

    /* renamed from: com.crittercism.internal.ap.c */
    public interface C0193c {
        void m123a(ap apVar, String str);
    }

    /* renamed from: com.crittercism.internal.ap.d */
    public static class C0194d extends C0190e<Long> {
        public final /* synthetic */ void m125a(SharedPreferences sharedPreferences, Object obj) {
            sharedPreferences.edit().putLong(this.a, ((Long) obj).longValue()).commit();
        }

        public C0194d(String str, Long l) {
            super(str, l);
        }

        public final /* synthetic */ Object m124a(SharedPreferences sharedPreferences) {
            return Long.valueOf(sharedPreferences.getLong(this.a, ((Long) this.b).longValue()));
        }
    }

    static {
        f116a = new C0191a("data.apm.enabled", Boolean.valueOf(true));
        f117b = new C0191a("reporter.apm.enabled", Boolean.valueOf(false));
        f118c = new C0194d("reporter.apm.frequency", Long.valueOf(HlsChunkSource.DEFAULT_MAX_BUFFER_TO_SWITCH_DOWN_MS));
        f119d = new C0192b("data.apm.rate", Float.valueOf(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        f120e = new C0191a("data.crash.enabled", Boolean.valueOf(true));
        f121f = new C0191a("reporter.crash.enabled", Boolean.valueOf(true));
        f122g = new C0194d("reporter.crash.frequency", Long.valueOf(0));
        f123h = new C0192b("data.crash.rate", Float.valueOf(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        f124i = new C0191a("data.he.enabled", Boolean.valueOf(true));
        f125j = new C0191a("reporter.he.enabled", Boolean.valueOf(true));
        f126k = new C0194d("reporter.he.frequency", Long.valueOf(HlsChunkSource.DEFAULT_PLAYLIST_BLACKLIST_MS));
        f127l = new C0192b("data.he.rate", Float.valueOf(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        f128m = new C0191a("data.ndk.enabled", Boolean.valueOf(true));
        f129n = new C0191a("reporter.ndk.enabled", Boolean.valueOf(true));
        f130o = new C0194d("reporter.ndk.frequency", Long.valueOf(10000));
        f131p = new C0192b("data.ndk.rate", Float.valueOf(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        f132q = new C0191a("data.metadata.enabled", Boolean.valueOf(true));
        f133r = new C0191a("reporter.metadata.enabled", Boolean.valueOf(true));
        f134s = new C0194d("reporter.metadata.frequency", Long.valueOf(10000));
        f135t = new C0192b("data.metadata.rate", Float.valueOf(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        f136u = new C0191a("data.appload.enabled", Boolean.valueOf(true));
        f137v = new C0191a("reporter.appload.enabled", Boolean.valueOf(true));
        f138w = new C0194d("reporter.appload.frequency", Long.valueOf(10000));
        f139x = new C0192b("data.appload.rate", Float.valueOf(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        f140y = new C0191a("data.userflow.enabled", Boolean.valueOf(true));
        f141z = new C0191a("reporter.userflow.enabled", Boolean.valueOf(false));
        f108A = new C0194d("reporter.userflow.frequency", Long.valueOf(HlsChunkSource.DEFAULT_MAX_BUFFER_TO_SWITCH_DOWN_MS));
        f109B = new C0192b("data.userflow.rate", Float.valueOf(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        f110C = new C0191a("data.breadcrumb.foreground.enabled", Boolean.valueOf(true));
        f111D = new C0191a("data.breadcrumb.activity.enabled", Boolean.valueOf(true));
        f112E = new C0191a("data.breadcrumb.networkstate.enabled", Boolean.valueOf(true));
        f113F = new C0191a("data.breadcrumb.networkstats.enabled", Boolean.valueOf(true));
        f114G = new C0191a("data.breadcrumb.exception.enabled", Boolean.valueOf(true));
        f115H = new C0194d("userflow.defaultTimeoutMs", Long.valueOf(3600000));
    }

    public static C0194d m127a(String str, long j) {
        return new C0194d("userflow.timeouts." + str, Long.valueOf(j));
    }

    public ap(Context context, String str) {
        this.f142I = new LinkedList();
        this.f143J = str;
        this.f144K = context;
    }

    private SharedPreferences m126a() {
        if (this.f145L == null) {
            this.f145L = this.f144K.getSharedPreferences("com.crittercism.settings." + this.f143J, 0);
        }
        return this.f145L;
    }

    public final <T> T m128a(C0190e<T> c0190e) {
        return c0190e.m115a(m126a());
    }

    public final <T> void m129a(C0190e<T> c0190e, T t) {
        c0190e.m117a(m126a(), t);
        Iterator it = this.f142I.iterator();
        while (it.hasNext()) {
            ((C0193c) it.next()).m123a(this, c0190e.f106a);
        }
    }

    public final void m130b(String str, long j) {
        m129a(m127a(str, j), Long.valueOf(j));
    }
}
