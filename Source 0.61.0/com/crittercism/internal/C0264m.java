package com.crittercism.internal;

import com.android.org.conscrypt.SSLParametersImpl;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/* renamed from: com.crittercism.internal.m */
public final class C0264m extends C0262k {
    private static boolean f543a;
    private static SSLSocketFactory f544b;
    private SSLParametersImpl f545c;
    private C0254d f546d;
    private SSLSocketFactory delegate;
    private C0230c f547e;

    static {
        f543a = false;
    }

    public static boolean m425a(C0254d c0254d, C0230c c0230c) {
        if (f543a) {
            return f543a;
        }
        SSLSocketFactory defaultSSLSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
        try {
            SSLSocketFactory c0264m = new C0264m(defaultSSLSocketFactory, c0254d, c0230c);
            try {
                c0264m.createSocket(c0264m.createSocket(), "localhost", 6895, true);
            } catch (SocketException e) {
            }
            HttpsURLConnection.setDefaultSSLSocketFactory(c0264m);
            f544b = defaultSSLSocketFactory;
            f543a = true;
            return true;
        } catch (ThreadDeath e2) {
            throw e2;
        } catch (Throwable th) {
            cc.m354a("Unable to instrument https connections.", th);
            return false;
        }
    }

    public C0264m(SSLSocketFactory sSLSocketFactory, C0254d c0254d, C0230c c0230c) {
        this.delegate = sSLSocketFactory;
        this.f546d = c0254d;
        this.f547e = c0230c;
        this.f545c = C0264m.m424a(sSLSocketFactory);
    }

    public final String[] getDefaultCipherSuites() {
        return this.delegate.getDefaultCipherSuites();
    }

    public final String[] getSupportedCipherSuites() {
        return this.delegate.getSupportedCipherSuites();
    }

    public final Socket createSocket(Socket socket, String host, int port, boolean autoClose) {
        return new C0269q(this.f546d, this.f547e, socket, host, port, autoClose, C0264m.m423a(this.f545c));
    }

    public final Socket createSocket(String host, int port) {
        return new C0267o(this.f546d, this.f547e, host, port, C0264m.m423a(this.f545c));
    }

    public final Socket createSocket(String host, int port, InetAddress localHost, int localPort) {
        return new C0267o(this.f546d, this.f547e, host, port, localHost, localPort, C0264m.m423a(this.f545c));
    }

    public final Socket createSocket(InetAddress host, int port) {
        return new C0267o(this.f546d, this.f547e, host, port, C0264m.m423a(this.f545c));
    }

    public final Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) {
        return new C0267o(this.f546d, this.f547e, address, port, localAddress, localPort, C0264m.m423a(this.f545c));
    }

    public final Socket createSocket() {
        return new C0267o(this.f546d, this.f547e, C0264m.m423a(this.f545c));
    }

    private static SSLParametersImpl m424a(SSLSocketFactory sSLSocketFactory) {
        SSLParametersImpl sSLParametersImpl;
        try {
            sSLParametersImpl = (SSLParametersImpl) C0255e.m383a(C0255e.m384a(sSLSocketFactory.getClass(), SSLParametersImpl.class, false), sSLSocketFactory);
        } catch (Throwable e) {
            cc.m359b(e);
            sSLParametersImpl = null;
        }
        return C0264m.m423a(sSLParametersImpl);
    }

    private static SSLParametersImpl m423a(SSLParametersImpl sSLParametersImpl) {
        try {
            return C0264m.m426b(sSLParametersImpl);
        } catch (bh e) {
            return null;
        }
    }

    private static SSLParametersImpl m426b(SSLParametersImpl sSLParametersImpl) {
        try {
            Method declaredMethod = SSLParametersImpl.class.getDeclaredMethod("clone", new Class[0]);
            declaredMethod.setAccessible(true);
            return (SSLParametersImpl) declaredMethod.invoke(sSLParametersImpl, new Object[0]);
        } catch (Throwable e) {
            throw new bh(e);
        } catch (Throwable e2) {
            throw new bh(e2);
        } catch (Throwable e22) {
            throw new bh(e22);
        } catch (Throwable e222) {
            throw new bh(e222);
        }
    }

    public final SSLSocketFactory m427a() {
        return this.delegate;
    }
}
