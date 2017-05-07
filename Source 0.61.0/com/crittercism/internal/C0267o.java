package com.crittercism.internal;

import com.android.org.conscrypt.OpenSSLSocketImpl;
import com.android.org.conscrypt.SSLParametersImpl;
import com.crittercism.internal.C0257f.C0256a;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import javax.net.ssl.SSLSocket;

/* renamed from: com.crittercism.internal.o */
public final class C0267o extends OpenSSLSocketImpl implements C0265u {
    private C0270r f549a;

    protected C0267o(C0254d c0254d, C0230c c0230c, SSLParametersImpl sSLParametersImpl) {
        super(sSLParametersImpl);
        this.f549a = new C0270r(C0256a.HTTPS, c0254d, c0230c);
    }

    protected C0267o(C0254d c0254d, C0230c c0230c, String str, int i, SSLParametersImpl sSLParametersImpl) {
        super(str, i, sSLParametersImpl);
        this.f549a = new C0270r(C0256a.HTTPS, c0254d, c0230c);
    }

    protected C0267o(C0254d c0254d, C0230c c0230c, InetAddress inetAddress, int i, SSLParametersImpl sSLParametersImpl) {
        super(inetAddress, i, sSLParametersImpl);
        this.f549a = new C0270r(C0256a.HTTPS, c0254d, c0230c);
    }

    protected C0267o(C0254d c0254d, C0230c c0230c, String str, int i, InetAddress inetAddress, int i2, SSLParametersImpl sSLParametersImpl) {
        super(str, i, inetAddress, i2, sSLParametersImpl);
        this.f549a = new C0270r(C0256a.HTTPS, c0254d, c0230c);
    }

    protected C0267o(C0254d c0254d, C0230c c0230c, InetAddress inetAddress, int i, InetAddress inetAddress2, int i2, SSLParametersImpl sSLParametersImpl) {
        super(inetAddress, i, inetAddress2, i2, sSLParametersImpl);
        this.f549a = new C0270r(C0256a.HTTPS, c0254d, c0230c);
    }

    public final C0212b m434a() {
        return this.f549a.m443a(getInetAddress());
    }

    public final void m435a(C0212b c0212b) {
        this.f549a.m448a(c0212b);
    }

    public final C0212b m436b() {
        return this.f549a.m451b();
    }

    public final void startHandshake() {
        try {
            super.startHandshake();
        } catch (IOException e) {
            this.f549a.m450a(e, (SSLSocket) this);
            throw e;
        }
    }

    public final void close() {
        super.close();
        this.f549a.m447a();
    }

    public final InputStream getInputStream() {
        return this.f549a.m445a((C0265u) this, super.getInputStream());
    }

    public final OutputStream getOutputStream() {
        return this.f549a.m446a((C0265u) this, super.getOutputStream());
    }

    public final synchronized void setSoTimeout(int timeout) {
        super.setSoTimeout(timeout);
    }

    public final synchronized int getSoTimeout() {
        return super.getSoTimeout();
    }
}
