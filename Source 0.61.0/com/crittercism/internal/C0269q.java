package com.crittercism.internal;

import com.android.org.conscrypt.OpenSSLSocketImplWrapper;
import com.android.org.conscrypt.SSLParametersImpl;
import com.crittercism.internal.C0257f.C0256a;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.net.ssl.SSLSocket;

/* renamed from: com.crittercism.internal.q */
public final class C0269q extends OpenSSLSocketImplWrapper implements C0265u {
    private C0270r f551a;

    protected C0269q(C0254d c0254d, C0230c c0230c, Socket socket, String str, int i, boolean z, SSLParametersImpl sSLParametersImpl) {
        super(socket, str, i, z, sSLParametersImpl);
        this.f551a = new C0270r(C0256a.HTTPS, c0254d, c0230c);
    }

    public final C0212b m440a() {
        return this.f551a.m443a(getInetAddress());
    }

    public final void m441a(C0212b c0212b) {
        this.f551a.m448a(c0212b);
    }

    public final C0212b m442b() {
        return this.f551a.m451b();
    }

    public final void startHandshake() {
        try {
            super.startHandshake();
        } catch (IOException e) {
            this.f551a.m450a(e, (SSLSocket) this);
            throw e;
        }
    }

    public final void close() {
        super.close();
        this.f551a.m447a();
    }

    public final InputStream getInputStream() {
        return this.f551a.m445a((C0265u) this, super.getInputStream());
    }

    public final OutputStream getOutputStream() {
        return this.f551a.m446a((C0265u) this, super.getOutputStream());
    }
}
