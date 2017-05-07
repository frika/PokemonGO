package com.crittercism.internal;

import android.app.Application;
import android.os.MessageQueue.IdleHandler;
import com.crittercism.internal.ca.C0231a;
import com.crittercism.internal.ca.C0232b;
import com.crittercism.internal.ca.C0233c;
import com.crittercism.internal.ca.C0234d;
import com.crittercism.internal.ca.C0235e;
import com.google.android.exoplayer.extractor.ts.PtsTimestampAdjuster;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class cb {
    public final HashMap<String, ca> f451a;
    public final ScheduledExecutorService f452b;
    final av<ca> f453c;
    public ap f454d;
    public boolean f455e;
    private ScheduledFuture f456f;
    private C0243a f457g;

    /* renamed from: com.crittercism.internal.cb.1 */
    class C02381 implements Runnable {
        final /* synthetic */ ca f440a;
        final /* synthetic */ cb f441b;

        C02381(cb cbVar, ca caVar) {
            this.f441b = cbVar;
            this.f440a = caVar;
        }

        public final void run() {
            if (((Boolean) this.f441b.f454d.m128a(ap.f140y)).booleanValue()) {
                this.f440a.f439j = ((Float) this.f441b.f454d.m128a(ap.f109B)).floatValue();
                this.f441b.f453c.m196a(this.f440a);
            }
        }
    }

    /* renamed from: com.crittercism.internal.cb.2 */
    public class C02392 implements Runnable {
        final /* synthetic */ ca f442a;
        final /* synthetic */ cb f443b;

        public C02392(cb cbVar, ca caVar) {
            this.f443b = cbVar;
            this.f442a = caVar;
        }

        public final void run() {
            if (((Boolean) this.f443b.f454d.m128a(ap.f140y)).booleanValue()) {
                this.f442a.f439j = ((Float) this.f443b.f454d.m128a(ap.f109B)).floatValue();
                this.f443b.f453c.m196a(this.f442a);
            }
        }
    }

    /* renamed from: com.crittercism.internal.cb.3 */
    public class C02403 implements Runnable {
        final /* synthetic */ Date f444a;
        final /* synthetic */ cb f445b;

        public C02403(cb cbVar, Date date) {
            this.f445b = cbVar;
            this.f444a = date;
        }

        public final void run() {
            C0231a c0231a = new C0231a();
            c0231a.f404a = "App Load";
            c0231a.f405b = this.f444a.getTime();
            c0231a.f406c = -1;
            c0231a.f407d = PtsTimestampAdjuster.DO_NOT_OFFSET;
            c0231a.f408e = C0235e.f426c;
            ca a = c0231a.m330a();
            this.f445b.f451a.put(a.f430a, a);
        }
    }

    /* renamed from: com.crittercism.internal.cb.4 */
    public class C02424 implements IdleHandler {
        final /* synthetic */ cb f447a;
        private boolean f448b;

        /* renamed from: com.crittercism.internal.cb.4.1 */
        class C02411 implements Runnable {
            final /* synthetic */ C02424 f446a;

            C02411(C02424 c02424) {
                this.f446a = c02424;
            }

            public final void run() {
                this.f446a.f447a.m347a("App Load");
            }
        }

        public C02424(cb cbVar) {
            this.f447a = cbVar;
            this.f448b = false;
        }

        public final boolean queueIdle() {
            if (!this.f448b) {
                this.f448b = true;
                this.f447a.f452b.submit(new C02411(this));
            }
            return true;
        }
    }

    /* renamed from: com.crittercism.internal.cb.a */
    class C0243a extends bm {
        final /* synthetic */ cb f449b;

        public C0243a(cb cbVar, Application application) {
            this.f449b = cbVar;
            super(application);
            m285a();
        }

        public final void m344b() {
            long currentTimeMillis = System.currentTimeMillis();
            synchronized (this.f449b.f451a) {
                for (ca caVar : this.f449b.f451a.values()) {
                    if (caVar.f435f == C0234d.f415b && currentTimeMillis >= caVar.f433d) {
                        caVar.f438i.add(new C0232b(C0233c.f412b, currentTimeMillis));
                    }
                }
            }
        }

        public final void m345c() {
            long currentTimeMillis = System.currentTimeMillis();
            synchronized (this.f449b.f451a) {
                for (ca caVar : this.f449b.f451a.values()) {
                    if (caVar.f435f == C0234d.f415b && currentTimeMillis >= caVar.f433d) {
                        caVar.f438i.add(new C0232b(C0233c.f411a, currentTimeMillis));
                    }
                }
            }
        }
    }

    /* renamed from: com.crittercism.internal.cb.b */
    class C0244b implements Runnable {
        final /* synthetic */ cb f450a;

        private C0244b(cb cbVar) {
            this.f450a = cbVar;
        }

        public final void run() {
            Map hashMap = new HashMap();
            synchronized (this.f450a.f451a) {
                for (Entry entry : this.f450a.f451a.entrySet()) {
                    String str = (String) entry.getKey();
                    ca caVar = (ca) entry.getValue();
                    if (caVar.m339a() >= caVar.f431b) {
                        hashMap.put(str, caVar);
                    }
                }
                for (Entry entry2 : hashMap.entrySet()) {
                    str = (String) entry2.getKey();
                    bf bfVar = (ca) entry2.getValue();
                    this.f450a.f451a.remove(str);
                    bfVar.m340a(C0234d.f419f, System.currentTimeMillis());
                    if (((Boolean) this.f450a.f454d.m128a(ap.f140y)).booleanValue()) {
                        bfVar.f439j = ((Float) this.f450a.f454d.m128a(ap.f109B)).floatValue();
                        this.f450a.f453c.m196a(bfVar);
                    }
                }
            }
        }
    }

    public cb(Application application, ScheduledExecutorService scheduledExecutorService, av<ca> avVar, ap apVar) {
        this.f451a = new HashMap();
        this.f455e = "true".equals(System.getProperty("com.crittercism.appLoadUserflowIsDisabled", "false"));
        this.f452b = scheduledExecutorService;
        this.f453c = avVar;
        this.f454d = apVar;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        if (this.f456f != null) {
            this.f456f.cancel(false);
        }
        if (this.f456f == null || this.f456f.isDone()) {
            this.f456f = this.f452b.scheduleAtFixedRate(new C0244b(), 10, 10, timeUnit);
        }
        this.f457g = new C0243a(this, application);
    }

    public final void m347a(String str) {
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (this.f451a) {
            ca caVar = (ca) this.f451a.remove(str);
            if (caVar == null) {
                cc.m357b("endUserflow(" + str + "): no such userflow");
                return;
            }
            caVar.m340a(C0234d.f416c, currentTimeMillis);
            this.f452b.submit(new C02381(this, caVar));
        }
    }

    public final int m348b(String str) {
        int i;
        synchronized (this.f451a) {
            ca caVar = (ca) this.f451a.get(str);
            if (caVar == null) {
                cc.m357b("getUserflowValue(" + str + "): no such userflow");
                i = -1;
            } else {
                i = caVar.f432c;
            }
        }
        return i;
    }

    public final Collection<ca> m346a() {
        Collection linkedList;
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (this.f451a) {
            linkedList = new LinkedList(this.f451a.values());
            this.f451a.clear();
            Iterator it = linkedList.iterator();
            while (it.hasNext()) {
                ((ca) it.next()).m340a(C0234d.f420g, currentTimeMillis);
            }
        }
        return linkedList;
    }
}
