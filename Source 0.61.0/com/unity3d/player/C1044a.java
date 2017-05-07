package com.unity3d.player;

import android.os.Build.VERSION;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import spacemadness.com.lunarconsole.BuildConfig;

/* renamed from: com.unity3d.player.a */
public final class C1044a extends SSLSocketFactory {
    private static volatile SSLSocketFactory f670c;
    private static final Object[] f671d;
    private static final boolean f672e;
    private final SSLSocketFactory f673a;
    private final C1043a f674b;

    /* renamed from: com.unity3d.player.a.a */
    class C1043a implements HandshakeCompletedListener {
        final /* synthetic */ C1044a f669a;

        C1043a(C1044a c1044a) {
            this.f669a = c1044a;
        }

        public final void handshakeCompleted(HandshakeCompletedEvent handshakeCompletedEvent) {
            SSLSession session = handshakeCompletedEvent.getSession();
            String cipherSuite = session.getCipherSuite();
            String protocol = session.getProtocol();
            String str = BuildConfig.FLAVOR;
            try {
                str = session.getPeerPrincipal().getName();
            } catch (SSLPeerUnverifiedException e) {
            }
            C1046c.Log(2, "Connected to " + str + " using " + protocol + " protocol and " + cipherSuite + " cipher.");
        }
    }

    static {
        boolean z = false;
        f671d = new Object[0];
        if (VERSION.SDK_INT >= 16 && VERSION.SDK_INT < 20) {
            z = true;
        }
        f672e = z;
    }

    private C1044a() {
        SSLContext instance = SSLContext.getInstance("TLS");
        instance.init(null, null, null);
        this.f673a = instance.getSocketFactory();
        this.f674b = new C1043a(this);
    }

    private static Socket m545a(Socket socket) {
        if (socket != null && (socket instanceof SSLSocket) && f672e) {
            ((SSLSocket) socket).setEnabledProtocols(((SSLSocket) socket).getSupportedProtocols());
        }
        return socket;
    }

    public static SSLSocketFactory m546a() {
        synchronized (f671d) {
            if (f670c != null) {
                SSLSocketFactory sSLSocketFactory = f670c;
                return sSLSocketFactory;
            }
            try {
                sSLSocketFactory = new C1044a();
                f670c = sSLSocketFactory;
                return sSLSocketFactory;
            } catch (Exception e) {
                C1046c.Log(5, "CustomSSLSocketFactory: Failed to create SSLSocketFactory (" + e.getMessage() + ")");
                return null;
            }
        }
    }

    public final Socket createSocket() {
        return C1044a.m545a(this.f673a.createSocket());
    }

    public final Socket createSocket(String str, int i) {
        return C1044a.m545a(this.f673a.createSocket(str, i));
    }

    public final Socket createSocket(String str, int i, InetAddress inetAddress, int i2) {
        return C1044a.m545a(this.f673a.createSocket(str, i, inetAddress, i2));
    }

    public final Socket createSocket(InetAddress inetAddress, int i) {
        return C1044a.m545a(this.f673a.createSocket(inetAddress, i));
    }

    public final Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) {
        return C1044a.m545a(this.f673a.createSocket(inetAddress, i, inetAddress2, i2));
    }

    public final Socket createSocket(Socket socket, String str, int i, boolean z) {
        return C1044a.m545a(this.f673a.createSocket(socket, str, i, z));
    }

    public final String[] getDefaultCipherSuites() {
        return this.f673a.getDefaultCipherSuites();
    }

    public final String[] getSupportedCipherSuites() {
        return this.f673a.getSupportedCipherSuites();
    }
}
