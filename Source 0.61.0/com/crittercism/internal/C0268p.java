package com.crittercism.internal;

import com.crittercism.internal.C0257f.C0256a;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.net.ssl.SSLSocket;
import org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImplWrapper;
import org.apache.harmony.xnet.provider.jsse.SSLParametersImpl;

/* renamed from: com.crittercism.internal.p */
public final class C0268p extends OpenSSLSocketImplWrapper implements C0265u {
    private C0270r f550a;

    protected C0268p(C0254d c0254d, C0230c c0230c, Socket socket, String str, int i, boolean z, SSLParametersImpl sSLParametersImpl) {
        super(socket, str, i, z, sSLParametersImpl);
        this.f550a = new C0270r(C0256a.HTTPS, c0254d, c0230c);
    }

    public final C0212b m437a() {
        return this.f550a.m443a(getInetAddress());
    }

    public final void m438a(C0212b c0212b) {
        this.f550a.m448a(c0212b);
    }

    public final C0212b m439b() {
        return this.f550a.m451b();
    }

    public final void startHandshake() {
        try {
            super.startHandshake();
        } catch (IOException e) {
            this.f550a.m450a(e, (SSLSocket) this);
            throw e;
        }
    }

    public final void close() {
        super.close();
        this.f550a.m447a();
    }

    public final InputStream getInputStream() {
        return this.f550a.m445a((C0265u) this, super.getInputStream());
    }

    public final OutputStream getOutputStream() {
        return this.f550a.m446a((C0265u) this, super.getOutputStream());
    }
}
