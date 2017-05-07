package com.crittercism.internal;

import android.app.Application;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.crittercism.internal.cb.C02403;
import com.crittercism.internal.cb.C02424;
import java.util.Date;
import java.util.concurrent.ExecutorService;

public final class bn extends bm {
    private final ExecutorService f324b;
    private final av<ar> f325c;
    private ar f326d;
    private ap f327e;
    private boolean f328f;
    private cb f329g;
    private Date f330h;

    /* renamed from: com.crittercism.internal.bn.1 */
    class C02171 implements Runnable {
        final /* synthetic */ ar f322a;
        final /* synthetic */ bn f323b;

        C02171(bn bnVar, ar arVar) {
            this.f323b = bnVar;
            this.f322a = arVar;
        }

        public final void run() {
            if (((Boolean) this.f323b.f327e.m128a(ap.f136u)).booleanValue()) {
                this.f322a.f175m = ((Float) this.f323b.f327e.m128a(ap.f139x)).floatValue();
                this.f323b.f325c.m196a(this.f322a);
            }
        }
    }

    public bn(@NonNull Application application, @NonNull ExecutorService executorService, @NonNull av<ar> avVar, @NonNull ar arVar, @NonNull ap apVar, boolean z, @NonNull cb cbVar, Date date) {
        super(application);
        this.f324b = executorService;
        this.f325c = avVar;
        this.f326d = arVar;
        this.f328f = z;
        this.f327e = apVar;
        this.f329g = cbVar;
        this.f330h = date;
        m285a();
    }

    public final synchronized void m296d() {
        if (!this.f328f) {
            m295h();
            if (this.f329g != null) {
                if (this.f330h != null) {
                    cb cbVar = this.f329g;
                    Date date = this.f330h;
                    if (!cbVar.f455e && Looper.myLooper() == Looper.getMainLooper()) {
                        cbVar.f452b.submit(new C02403(cbVar, date));
                        Looper.myQueue().addIdleHandler(new C02424(cbVar));
                    }
                }
                this.f329g = null;
                this.f330h = null;
            }
        }
    }

    private void m295h() {
        ar arVar = this.f326d;
        if (arVar != null) {
            this.f324b.submit(new C02171(this, arVar));
            this.f326d = null;
        }
    }

    public final synchronized void m297g() {
        this.f328f = false;
        if (this.f316a) {
            m295h();
        }
    }
}
