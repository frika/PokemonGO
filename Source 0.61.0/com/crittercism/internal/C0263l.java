package com.crittercism.internal;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.apache.harmony.xnet.provider.jsse.SSLParametersImpl;

/* renamed from: com.crittercism.internal.l */
public final class C0263l extends C0262k {
    private static boolean f538a;
    private static SSLSocketFactory f539b;
    private SSLParametersImpl f540c;
    private C0254d f541d;
    private SSLSocketFactory delegate;
    private C0230c f542e;

    static {
        f538a = false;
    }

    public static boolean m420a(C0254d c0254d, C0230c c0230c) {
        if (f538a) {
            return f538a;
        }
        SSLSocketFactory defaultSSLSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
        try {
            SSLSocketFactory c0263l = new C0263l(defaultSSLSocketFactory, c0254d, c0230c);
            try {
                c0263l.createSocket(c0263l.createSocket(), "localhost", 6895, true);
            } catch (SocketException e) {
            }
            HttpsURLConnection.setDefaultSSLSocketFactory(c0263l);
            f539b = defaultSSLSocketFactory;
            f538a = true;
            return true;
        } catch (ThreadDeath e2) {
            throw e2;
        } catch (Throwable th) {
            cc.m354a("Unable to instrument https connections.", th);
            return false;
        }
    }

    public C0263l(SSLSocketFactory sSLSocketFactory, C0254d c0254d, C0230c c0230c) {
        this.delegate = sSLSocketFactory;
        this.f541d = c0254d;
        this.f542e = c0230c;
        this.f540c = C0263l.m418a(sSLSocketFactory);
    }

    public final String[] getDefaultCipherSuites() {
        return this.delegate.getDefaultCipherSuites();
    }

    public final String[] getSupportedCipherSuites() {
        return this.delegate.getSupportedCipherSuites();
    }

    public final Socket createSocket(Socket socket, String host, int port, boolean autoClose) {
        return new C0268p(this.f541d, this.f542e, socket, host, port, autoClose, C0263l.m419a(this.f540c));
    }

    public final Socket createSocket(String host, int port) {
        return new C0266n(this.f541d, this.f542e, host, port, C0263l.m419a(this.f540c));
    }

    public final Socket createSocket(String host, int port, InetAddress localHost, int localPort) {
        return new C0266n(this.f541d, this.f542e, host, port, localHost, localPort, C0263l.m419a(this.f540c));
    }

    public final Socket createSocket(InetAddress host, int port) {
        return new C0266n(this.f541d, this.f542e, host, port, C0263l.m419a(this.f540c));
    }

    public final Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) {
        return new C0266n(this.f541d, this.f542e, address, port, localAddress, localPort, C0263l.m419a(this.f540c));
    }

    public final Socket createSocket() {
        return new C0266n(this.f541d, this.f542e, C0263l.m419a(this.f540c));
    }

    private static SSLParametersImpl m418a(SSLSocketFactory sSLSocketFactory) {
        SSLParametersImpl sSLParametersImpl;
        try {
            sSLParametersImpl = (SSLParametersImpl) C0255e.m383a(C0255e.m384a(sSLSocketFactory.getClass(), SSLParametersImpl.class, false), sSLSocketFactory);
        } catch (Throwable e) {
            cc.m359b(e);
            sSLParametersImpl = null;
        }
        return C0263l.m419a(sSLParametersImpl);
    }

    private static SSLParametersImpl m419a(SSLParametersImpl sSLParametersImpl) {
        try {
            return C0263l.m421b(sSLParametersImpl);
        } catch (bh e) {
            return null;
        }
    }

    private static SSLParametersImpl m421b(SSLParametersImpl sSLParametersImpl) {
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

    public final SSLSocketFactory m422a() {
        return this.delegate;
    }
}
