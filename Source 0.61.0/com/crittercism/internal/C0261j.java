package com.crittercism.internal;

import android.os.Build.VERSION;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContextSpi;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/* renamed from: com.crittercism.internal.j */
public final class C0261j extends SSLContextSpi {
    private static Method[] f533a;
    private static boolean f534b;
    private SSLContextSpi f535c;
    private C0254d f536d;
    private C0230c f537e;

    static {
        f533a = new Method[7];
        f534b = false;
        try {
            f533a[0] = SSLContextSpi.class.getDeclaredMethod("engineCreateSSLEngine", new Class[0]);
            f533a[1] = SSLContextSpi.class.getDeclaredMethod("engineCreateSSLEngine", new Class[]{String.class, Integer.TYPE});
            f533a[2] = SSLContextSpi.class.getDeclaredMethod("engineGetClientSessionContext", new Class[0]);
            f533a[3] = SSLContextSpi.class.getDeclaredMethod("engineGetServerSessionContext", new Class[0]);
            f533a[4] = SSLContextSpi.class.getDeclaredMethod("engineGetServerSocketFactory", new Class[0]);
            f533a[5] = SSLContextSpi.class.getDeclaredMethod("engineGetSocketFactory", new Class[0]);
            f533a[6] = SSLContextSpi.class.getDeclaredMethod("engineInit", new Class[]{KeyManager[].class, TrustManager[].class, SecureRandom.class});
            Method[] methodArr = f533a;
            for (AccessibleObject accessibleObject : methodArr) {
                if (accessibleObject != null) {
                    accessibleObject.setAccessible(true);
                }
            }
            C0261j c0261j = new C0261j(new C0261j(), null, null);
            c0261j.engineCreateSSLEngine();
            c0261j.engineCreateSSLEngine(null, 0);
            c0261j.engineGetClientSessionContext();
            c0261j.engineGetServerSessionContext();
            c0261j.engineGetServerSocketFactory();
            c0261j.engineGetSocketFactory();
            c0261j.engineInit(null, null, null);
            f534b = true;
        } catch (Throwable th) {
            cc.m355a(th);
            f534b = false;
        }
    }

    private C0261j(SSLContextSpi sSLContextSpi, C0254d c0254d, C0230c c0230c) {
        this.f535c = sSLContextSpi;
        this.f536d = c0254d;
        this.f537e = c0230c;
    }

    public static C0261j m412a(SSLContextSpi sSLContextSpi, C0254d c0254d, C0230c c0230c) {
        if (f534b) {
            return new C0261j(sSLContextSpi, c0254d, c0230c);
        }
        return null;
    }

    private C0261j() {
    }

    public static boolean m415a() {
        return f534b;
    }

    private <T> T m413a(int i, Object... objArr) {
        Throwable e;
        if (this.f535c == null) {
            return null;
        }
        try {
            return f533a[i].invoke(this.f535c, objArr);
        } catch (Throwable e2) {
            throw new bg(e2);
        } catch (Throwable e22) {
            throw new bg(e22);
        } catch (Throwable e222) {
            Throwable th = e222;
            e222 = th.getTargetException();
            if (e222 == null) {
                throw new bg(th);
            } else if (e222 instanceof Exception) {
                throw ((Exception) e222);
            } else if (e222 instanceof Error) {
                throw ((Error) e222);
            } else {
                throw new bg(th);
            }
        } catch (Throwable e2222) {
            throw new bg(e2222);
        }
    }

    private <T> T m416b(int i, Object... objArr) {
        try {
            return m413a(i, objArr);
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e2) {
            throw new bg(e2);
        }
    }

    private <T> T m414a(Object... objArr) {
        try {
            return m413a(6, objArr);
        } catch (RuntimeException e) {
            throw e;
        } catch (KeyManagementException e2) {
            throw e2;
        } catch (Throwable e3) {
            throw new bg(e3);
        }
    }

    protected final SSLEngine engineCreateSSLEngine() {
        return (SSLEngine) m416b(0, new Object[0]);
    }

    protected final SSLEngine engineCreateSSLEngine(String host, int port) {
        return (SSLEngine) m416b(1, host, Integer.valueOf(port));
    }

    protected final SSLSessionContext engineGetClientSessionContext() {
        return (SSLSessionContext) m416b(2, new Object[0]);
    }

    protected final SSLSessionContext engineGetServerSessionContext() {
        return (SSLSessionContext) m416b(3, new Object[0]);
    }

    protected final SSLServerSocketFactory engineGetServerSocketFactory() {
        return (SSLServerSocketFactory) m416b(4, new Object[0]);
    }

    protected final SSLSocketFactory engineGetSocketFactory() {
        SSLSocketFactory sSLSocketFactory = (SSLSocketFactory) m416b(5, new Object[0]);
        if (sSLSocketFactory == null) {
            return sSLSocketFactory;
        }
        try {
            if (VERSION.SDK_INT >= 19) {
                return new C0264m(sSLSocketFactory, this.f536d, this.f537e);
            }
            if (VERSION.SDK_INT >= 14) {
                return new C0263l(sSLSocketFactory, this.f536d, this.f537e);
            }
            return sSLSocketFactory;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
            return sSLSocketFactory;
        }
    }

    protected final void engineInit(KeyManager[] km, TrustManager[] tm, SecureRandom sr) {
        m414a(km, tm, sr);
    }

    public final boolean equals(Object o) {
        return this.f535c.equals(o);
    }

    public final int hashCode() {
        return this.f535c.hashCode();
    }

    public final String toString() {
        return this.f535c.toString();
    }
}
