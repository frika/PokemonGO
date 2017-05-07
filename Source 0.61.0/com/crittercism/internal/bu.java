package com.crittercism.internal;

import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.support.annotation.Nullable;
import com.crittercism.internal.ap.C0191a;
import com.crittercism.internal.ap.C0193c;
import com.crittercism.internal.ap.C0194d;
import com.crittercism.internal.av.C0207a;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class bu implements C0207a {
    final ScheduledExecutorService f379a;
    av f380b;
    public C0224a f381c;
    volatile boolean f382d;
    public volatile ScheduledFuture f383e;
    public volatile Future f384f;
    public volatile Future f385g;
    public boolean f386h;
    public ConnectivityManager f387i;
    public Object f388j;
    private as f389k;
    private final ExecutorService f390l;
    private bv f391m;
    private long f392n;
    private boolean f393o;
    private boolean f394p;
    private long f395q;
    private String f396r;

    /* renamed from: com.crittercism.internal.bu.a */
    public interface C0224a {
        void m310a(bt btVar);
    }

    /* renamed from: com.crittercism.internal.bu.1 */
    class C02251 implements Runnable {
        final /* synthetic */ bu f369a;

        C02251(bu buVar) {
            this.f369a = buVar;
        }

        public final void run() {
            this.f369a.m324c();
            this.f369a.f383e = null;
        }
    }

    /* renamed from: com.crittercism.internal.bu.2 */
    class C02262 implements Runnable {
        final /* synthetic */ bs f370a;
        final /* synthetic */ List f371b;
        final /* synthetic */ bu f372c;

        C02262(bu buVar, bs bsVar, List list) {
            this.f372c = buVar;
            this.f370a = bsVar;
            this.f371b = list;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void run() {
            /*
            r5 = this;
            r1 = 1;
            r0 = 0;
            r2 = r5.f370a;
            r3 = r2.m315a();
            r2 = r3.f366a;
            r4 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
            if (r2 < r4) goto L_0x0055;
        L_0x000e:
            r2 = r3.f366a;
            r4 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
            if (r2 >= r4) goto L_0x0055;
        L_0x0014:
            r2 = r1;
        L_0x0015:
            if (r2 != 0) goto L_0x0026;
        L_0x0017:
            r2 = r3.f366a;
            r4 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
            if (r2 >= r4) goto L_0x0023;
        L_0x001d:
            r2 = r3.f368c;
            r2 = r2 instanceof java.net.SocketTimeoutException;
            if (r2 == 0) goto L_0x0024;
        L_0x0023:
            r0 = r1;
        L_0x0024:
            if (r0 != 0) goto L_0x0037;
        L_0x0026:
            r0 = r5.f372c;
            r1 = r5.f371b;
            r2 = r0.f379a;
            r4 = new com.crittercism.internal.bu$3;
            r4.<init>(r0, r1);
            r1 = r2.submit(r4);
            r0.f385g = r1;
        L_0x0037:
            r0 = r5.f372c;
            r0 = r0.f381c;
            if (r0 == 0) goto L_0x0044;
        L_0x003d:
            r0 = r5.f372c;
            r0 = r0.f381c;
            r0.m310a(r3);
        L_0x0044:
            r0 = r5.f372c;
            r1 = 0;
            r0.f384f = r1;
            r0 = r5.f372c;
            r0 = r0.f382d;
            if (r0 == 0) goto L_0x0054;
        L_0x004f:
            r0 = r5.f372c;
            r0.m322b();
        L_0x0054:
            return;
        L_0x0055:
            r2 = r0;
            goto L_0x0015;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.crittercism.internal.bu.2.run():void");
        }
    }

    /* renamed from: com.crittercism.internal.bu.3 */
    class C02273 implements Runnable {
        final /* synthetic */ List f373a;
        final /* synthetic */ bu f374b;

        C02273(bu buVar, List list) {
            this.f374b = buVar;
            this.f373a = list;
        }

        public final void run() {
            this.f374b.f380b.m195a(this.f373a);
            this.f374b.f385g = null;
        }
    }

    /* renamed from: com.crittercism.internal.bu.4 */
    public class C02284 extends NetworkCallback {
        final /* synthetic */ bu f375a;

        public C02284(bu buVar) {
            this.f375a = buVar;
        }

        public final void onAvailable(Network network) {
            if (this.f375a.f382d) {
                this.f375a.m322b();
            }
        }

        public final void onLosing(Network network, int maxMsToLive) {
            cc.m362d("onLosing: " + network);
        }

        public final void onLost(Network network) {
        }

        public final void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        }

        public final void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
        }
    }

    /* renamed from: com.crittercism.internal.bu.b */
    class C0229b implements C0193c {
        final /* synthetic */ bu f376a;
        private C0191a f377b;
        private C0194d f378c;

        public C0229b(bu buVar, C0191a c0191a, C0194d c0194d) {
            this.f376a = buVar;
            this.f377b = c0191a;
            this.f378c = c0194d;
        }

        public final void m316a(ap apVar, String str) {
            if (this.f377b.m116a().equals(str)) {
                this.f376a.m323b(((Boolean) apVar.m128a(this.f377b)).booleanValue());
            } else if (this.f378c.m116a().equals(str)) {
                this.f376a.m320a(((Long) apVar.m128a(this.f378c)).longValue(), TimeUnit.MILLISECONDS);
            }
        }
    }

    public bu(as asVar, ScheduledExecutorService scheduledExecutorService, ExecutorService executorService, av avVar, bv bvVar, String str, ap apVar, C0191a c0191a, C0194d c0194d) {
        this.f393o = false;
        this.f394p = true;
        this.f395q = 0;
        this.f382d = false;
        this.f386h = true;
        this.f389k = asVar;
        this.f379a = scheduledExecutorService;
        this.f390l = executorService;
        this.f380b = avVar;
        this.f391m = bvVar;
        this.f380b.m193a((C0207a) this);
        this.f392n = ((Long) apVar.m128a(c0194d)).longValue();
        this.f393o = ((Boolean) apVar.m128a(c0191a)).booleanValue();
        this.f396r = str;
        apVar.f142I.add(new C0229b(this, c0191a, c0194d));
    }

    public final void m319a() {
        this.f382d = true;
        m322b();
    }

    public final synchronized Future m322b() {
        Future future;
        Object obj = 1;
        synchronized (this) {
            Object obj2 = !m317a(this.f383e) ? 1 : null;
            if (m317a(this.f384f)) {
                obj = null;
            }
            if (this.f393o && !this.f394p && obj2 == null && r0 == null) {
                try {
                    this.f383e = this.f379a.schedule(new C02251(this), m318d(), TimeUnit.MILLISECONDS);
                } catch (Throwable e) {
                    cc.m354a("Unable to schedule sending data", e);
                }
                future = this.f383e;
            } else {
                future = null;
            }
        }
        return future;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    final synchronized void m324c() {
        /*
        r4 = this;
        r1 = 0;
        r0 = 1;
        monitor-enter(r4);
        r2 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x0054 }
        r4.f395q = r2;	 Catch:{ all -> 0x0054 }
        r2 = r4.f394p;	 Catch:{ all -> 0x0054 }
        if (r2 != 0) goto L_0x0013;
    L_0x000d:
        r2 = r4.f387i;	 Catch:{ all -> 0x0054 }
        if (r2 != 0) goto L_0x0015;
    L_0x0011:
        if (r0 != 0) goto L_0x002f;
    L_0x0013:
        monitor-exit(r4);
        return;
    L_0x0015:
        r2 = r4.f387i;	 Catch:{ all -> 0x0054 }
        r2 = r2.getActiveNetworkInfo();	 Catch:{ all -> 0x0054 }
        if (r2 == 0) goto L_0x002d;
    L_0x001d:
        r3 = r2.getType();	 Catch:{ all -> 0x0054 }
        r2 = r2.isConnected();	 Catch:{ all -> 0x0054 }
        if (r2 == 0) goto L_0x002d;
    L_0x0027:
        r2 = r4.f386h;	 Catch:{ all -> 0x0054 }
        if (r2 != 0) goto L_0x0011;
    L_0x002b:
        if (r3 == r0) goto L_0x0011;
    L_0x002d:
        r0 = r1;
        goto L_0x0011;
    L_0x002f:
        r0 = r4.f380b;	 Catch:{ all -> 0x0054 }
        r0 = r0.m198c();	 Catch:{ all -> 0x0054 }
        r1 = 0;
        r4.f382d = r1;	 Catch:{ all -> 0x0054 }
        r1 = r0.size();	 Catch:{ all -> 0x0054 }
        if (r1 == 0) goto L_0x0013;
    L_0x003e:
        r1 = r4.f391m;	 Catch:{ IOException -> 0x0057 }
        r2 = r4.f389k;	 Catch:{ IOException -> 0x0057 }
        r1 = r1.m136a(r2, r0);	 Catch:{ IOException -> 0x0057 }
        r2 = r4.f390l;	 Catch:{ all -> 0x0054 }
        r3 = new com.crittercism.internal.bu$2;	 Catch:{ all -> 0x0054 }
        r3.<init>(r4, r1, r0);	 Catch:{ all -> 0x0054 }
        r0 = r2.submit(r3);	 Catch:{ all -> 0x0054 }
        r4.f384f = r0;	 Catch:{ all -> 0x0054 }
        goto L_0x0013;
    L_0x0054:
        r0 = move-exception;
        monitor-exit(r4);
        throw r0;
    L_0x0057:
        r1 = move-exception;
        r1 = r0.iterator();	 Catch:{ all -> 0x0054 }
    L_0x005c:
        r0 = r1.hasNext();	 Catch:{ all -> 0x0054 }
        if (r0 == 0) goto L_0x0013;
    L_0x0062:
        r0 = r1.next();	 Catch:{ all -> 0x0054 }
        r0 = (com.crittercism.internal.bf) r0;	 Catch:{ all -> 0x0054 }
        r2 = r4.f380b;	 Catch:{ all -> 0x0054 }
        r0 = r0.m138f();	 Catch:{ all -> 0x0054 }
        r2.m194a(r0);	 Catch:{ all -> 0x0054 }
        goto L_0x005c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.crittercism.internal.bu.c():void");
    }

    public final synchronized void m320a(long j, TimeUnit timeUnit) {
        this.f392n = timeUnit.toMillis(j);
    }

    public final synchronized void m321a(boolean z) {
        if (this.f394p != z) {
            this.f394p = z;
            if (!this.f394p) {
                this.f395q = System.currentTimeMillis();
                m322b();
            }
        }
    }

    final synchronized void m323b(boolean z) {
        if (this.f393o != z) {
            this.f393o = z;
            if (this.f393o) {
                m322b();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized long m318d() {
        /*
        r8 = this;
        r0 = 0;
        monitor-enter(r8);
        r2 = r8.f392n;	 Catch:{ all -> 0x0017 }
        r4 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x0017 }
        r6 = r8.f395q;	 Catch:{ all -> 0x0017 }
        r4 = r4 - r6;
        r6 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1));
        if (r6 <= 0) goto L_0x001a;
    L_0x0010:
        r2 = r2 - r4;
        r4 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1));
        if (r4 >= 0) goto L_0x001a;
    L_0x0015:
        monitor-exit(r8);
        return r0;
    L_0x0017:
        r0 = move-exception;
        monitor-exit(r8);
        throw r0;
    L_0x001a:
        r0 = r2;
        goto L_0x0015;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.crittercism.internal.bu.d():long");
    }

    private static boolean m317a(@Nullable Future future) {
        return future == null || future.isDone();
    }

    public final String toString() {
        return this.f396r;
    }
}
