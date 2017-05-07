package com.crittercism.internal;

import com.crittercism.internal.C0257f.C0256a;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import javax.net.ssl.SSLSocket;
import org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl;
import org.apache.harmony.xnet.provider.jsse.SSLParametersImpl;

/* renamed from: com.crittercism.internal.n */
public final class C0266n extends OpenSSLSocketImpl implements C0265u {
    private C0270r f548a;

    protected C0266n(C0254d c0254d, C0230c c0230c, SSLParametersImpl sSLParametersImpl) {
        super(sSLParametersImpl);
        this.f548a = new C0270r(C0256a.HTTPS, c0254d, c0230c);
    }

    protected C0266n(C0254d c0254d, C0230c c0230c, String str, int i, SSLParametersImpl sSLParametersImpl) {
        super(str, i, sSLParametersImpl);
        this.f548a = new C0270r(C0256a.HTTPS, c0254d, c0230c);
    }

    protected C0266n(C0254d c0254d, C0230c c0230c, InetAddress inetAddress, int i, SSLParametersImpl sSLParametersImpl) {
        super(inetAddress, i, sSLParametersImpl);
        this.f548a = new C0270r(C0256a.HTTPS, c0254d, c0230c);
    }

    protected C0266n(C0254d c0254d, C0230c c0230c, String str, int i, InetAddress inetAddress, int i2, SSLParametersImpl sSLParametersImpl) {
        super(str, i, inetAddress, i2, sSLParametersImpl);
        this.f548a = new C0270r(C0256a.HTTPS, c0254d, c0230c);
    }

    protected C0266n(C0254d c0254d, C0230c c0230c, InetAddress inetAddress, int i, InetAddress inetAddress2, int i2, SSLParametersImpl sSLParametersImpl) {
        super(inetAddress, i, inetAddress2, i2, sSLParametersImpl);
        this.f548a = new C0270r(C0256a.HTTPS, c0254d, c0230c);
    }

    public final C0212b m431a() {
        return this.f548a.m443a(getInetAddress());
    }

    public final void m432a(C0212b c0212b) {
        this.f548a.m448a(c0212b);
    }

    public final C0212b m433b() {
        return this.f548a.m451b();
    }

    public final void startHandshake() {
        try {
            super.startHandshake();
        } catch (IOException e) {
            this.f548a.m450a(e, (SSLSocket) this);
            throw e;
        }
    }

    public final void close() {
        super.close();
        this.f548a.m447a();
    }

    public final InputStream getInputStream() {
        return this.f548a.m445a((C0265u) this, super.getInputStream());
    }

    public final OutputStream getOutputStream() {
        return this.f548a.m446a((C0265u) this, super.getOutputStream());
    }

    public final synchronized void setSoTimeout(int timeout) {
        super.setSoTimeout(timeout);
    }

    public final synchronized int getSoTimeout() {
        return super.getSoTimeout();
    }
}
