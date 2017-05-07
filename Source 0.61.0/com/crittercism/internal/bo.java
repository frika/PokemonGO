package com.crittercism.internal;

import android.app.Activity;
import android.app.Application;
import com.crittercism.internal.at.C0201b;
import com.crittercism.internal.at.C0203d;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import org.json.JSONObject;

public final class bo extends bm {
    private ExecutorService f335b;
    private av<at> f336c;
    private ap f337d;

    /* renamed from: com.crittercism.internal.bo.1 */
    class C02181 implements Runnable {
        final /* synthetic */ bo f331a;

        C02181(bo boVar) {
            this.f331a = boVar;
        }

        public final void run() {
            if (((Boolean) this.f331a.f337d.m128a(ap.f110C)).booleanValue()) {
                av b = this.f331a.f336c;
                Map hashMap = new HashMap();
                hashMap.put(SendEvent.EVENT, "foregrounded");
                b.m196a(new at(C0201b.f184d, new JSONObject(hashMap)));
            }
        }
    }

    /* renamed from: com.crittercism.internal.bo.2 */
    class C02192 implements Runnable {
        final /* synthetic */ bo f332a;

        C02192(bo boVar) {
            this.f332a = boVar;
        }

        public final void run() {
            if (((Boolean) this.f332a.f337d.m128a(ap.f110C)).booleanValue()) {
                av b = this.f332a.f336c;
                Map hashMap = new HashMap();
                hashMap.put(SendEvent.EVENT, "backgrounded");
                b.m196a(new at(C0201b.f184d, new JSONObject(hashMap)));
            }
        }
    }

    /* renamed from: com.crittercism.internal.bo.3 */
    class C02203 implements Runnable {
        final /* synthetic */ Activity f333a;
        final /* synthetic */ bo f334b;

        C02203(bo boVar, Activity activity) {
            this.f334b = boVar;
            this.f333a = activity;
        }

        public final void run() {
            if (((Boolean) this.f334b.f337d.m128a(ap.f111D)).booleanValue()) {
                String name = this.f333a.getClass().getName();
                int i = C0203d.f196a;
                Map hashMap = new HashMap();
                hashMap.put(SendEvent.EVENT, Integer.valueOf(i - 1));
                hashMap.put("viewName", name);
                this.f334b.f336c.m196a(new at(C0201b.f186f, new JSONObject(hashMap)));
            }
        }
    }

    public bo(Application application, ExecutorService executorService, av<at> avVar, ap apVar) {
        super(application);
        this.f335b = executorService;
        this.f336c = avVar;
        this.f337d = apVar;
        m285a();
    }

    public final void m301b() {
        this.f335b.submit(new C02181(this));
    }

    public final void m302c() {
        this.f335b.submit(new C02192(this));
    }

    public final void m300a(Activity activity) {
        this.f335b.submit(new C02203(this, activity));
    }
}
