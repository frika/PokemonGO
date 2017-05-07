package com.crittercism.internal;

import android.support.annotation.NonNull;
import com.crittercism.internal.C0212b.C0211c;
import com.crittercism.internal.at.C0201b;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

/* renamed from: com.crittercism.internal.d */
public final class C0254d {
    final av<at> f503a;
    final av<C0212b> f504b;
    final ap f505c;
    private final List<String> f506d;
    private final List<String> f507e;
    private final Executor f508f;

    /* renamed from: com.crittercism.internal.d.1 */
    class C02521 implements Runnable {
        final /* synthetic */ C0212b f495a;
        final /* synthetic */ C0254d f496b;

        C02521(C0254d c0254d, C0212b c0212b) {
            this.f496b = c0254d;
            this.f495a = c0212b;
        }

        public final void run() {
            if (((Boolean) this.f496b.f505c.m128a(ap.f116a)).booleanValue()) {
                this.f495a.f244e = ((Float) this.f496b.f505c.m128a(ap.f119d)).floatValue();
                if (((Boolean) this.f496b.f505c.m128a(ap.f113F)).booleanValue()) {
                    this.f496b.f503a.m196a(new at(C0201b.f183c, this.f495a.m237e()));
                }
                this.f496b.f504b.m196a(this.f495a);
            }
        }
    }

    /* renamed from: com.crittercism.internal.d.a */
    public static class C0253a {
        public Executor f497a;
        public List<String> f498b;
        public List<String> f499c;
        public av<C0212b> f500d;
        public av<at> f501e;
        public ap f502f;

        public C0253a() {
            this.f498b = new LinkedList();
            this.f499c = new LinkedList();
            this.f500d = new bd();
            this.f501e = new bd();
        }
    }

    private C0254d(@NonNull Executor executor, @NonNull List<String> list, @NonNull List<String> list2, @NonNull av<C0212b> avVar, @NonNull av<at> avVar2, @NonNull ap apVar) {
        this.f508f = executor;
        this.f504b = avVar;
        this.f503a = avVar2;
        this.f506d = new LinkedList(list);
        this.f506d.remove(null);
        this.f507e = new LinkedList(list2);
        this.f507e.remove(null);
        this.f505c = apVar;
    }

    @Deprecated
    public final void m381a(C0212b c0212b) {
        m382a(c0212b, C0211c.LEGACY_JAVANET);
    }

    private boolean m380b(C0212b c0212b) {
        String a = c0212b.m225a();
        synchronized (this.f506d) {
            for (String contains : this.f506d) {
                if (a.contains(contains)) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean m379a(String str) {
        synchronized (this.f507e) {
            for (String contains : this.f507e) {
                if (str.contains(contains)) {
                    return false;
                }
            }
            return true;
        }
    }

    public final void m382a(C0212b c0212b, C0211c c0211c) {
        if (!c0212b.f242c) {
            c0212b.f242c = true;
            c0212b.f245f = c0211c;
            if (!m380b(c0212b)) {
                String a = c0212b.m225a();
                if (m379a(a)) {
                    int indexOf = a.indexOf("?");
                    if (indexOf != -1) {
                        c0212b.m228a(a.substring(0, indexOf));
                    }
                }
                try {
                    this.f508f.execute(new C02521(this, c0212b));
                } catch (RejectedExecutionException e) {
                }
            }
        }
    }
}
