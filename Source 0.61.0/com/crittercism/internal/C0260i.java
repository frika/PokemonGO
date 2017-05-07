package com.crittercism.internal;

import android.os.Build.VERSION;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Provider.Service;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLContextSpi;

/* renamed from: com.crittercism.internal.i */
public final class C0260i extends Service {
    public static final String[] f529a;
    private C0254d f530b;
    private C0230c f531c;
    private Service f532d;

    static {
        f529a = new String[]{"Default", "SSL", "TLSv1.1", "TLSv1.2", "SSLv3", "TLSv1", "TLS"};
    }

    private C0260i(Service service, C0254d c0254d, C0230c c0230c) {
        super(service.getProvider(), service.getType(), service.getAlgorithm(), service.getClassName(), null, null);
        this.f530b = c0254d;
        this.f531c = c0230c;
        this.f532d = service;
    }

    private static C0260i m408a(Service service, C0254d c0254d, C0230c c0230c) {
        C0260i c0260i = new C0260i(service, c0254d, c0230c);
        try {
            Field[] fields = Service.class.getFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                fields[i].set(c0260i, fields[i].get(service));
            }
            return c0260i;
        } catch (Exception e) {
            return null;
        }
    }

    private static Provider m409a() {
        try {
            SSLContext instance = SSLContext.getInstance("TLS");
            if (instance != null) {
                return instance.getProvider();
            }
            return null;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static boolean m410a(C0254d c0254d, C0230c c0230c) {
        int i = 0;
        if (VERSION.SDK_INT < 17 || !C0261j.m415a()) {
            return false;
        }
        Provider a = C0260i.m409a();
        if (a == null) {
            return false;
        }
        boolean z = false;
        while (i < f529a.length) {
            Service service = a.getService("SSLContext", f529a[i]);
            if (!(service == null || (service instanceof C0260i))) {
                C0260i a2 = C0260i.m408a(service, c0254d, c0230c);
                if (a2 != null) {
                    z |= a2.m411b();
                }
            }
            i++;
        }
        return z;
    }

    private boolean m411b() {
        Provider provider = getProvider();
        if (provider == null) {
            return false;
        }
        try {
            Method declaredMethod = Provider.class.getDeclaredMethod("putService", new Class[]{Service.class});
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(provider, new Object[]{this});
            String str = "SSLContext.DummySSLAlgorithm";
            provider.put(str, getClassName());
            provider.remove(getType() + "." + getAlgorithm());
            provider.remove(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public final Object newInstance(Object constructorParameter) {
        Object newInstance = super.newInstance(constructorParameter);
        try {
            if (!(newInstance instanceof SSLContextSpi)) {
                return newInstance;
            }
            C0261j a = C0261j.m412a((SSLContextSpi) newInstance, this.f530b, this.f531c);
            return a != null ? a : newInstance;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
            return newInstance;
        }
    }
}
