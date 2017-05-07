package com.crittercism.internal;

import com.crittercism.internal.C0257f.C0256a;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PlainSocketImpl;
import java.net.SocketAddress;

/* renamed from: com.crittercism.internal.s */
public final class C0271s extends PlainSocketImpl implements C0265u {
    private C0270r f558a;

    public C0271s(C0254d c0254d, C0230c c0230c) {
        this.f558a = new C0270r(C0256a.HTTP, c0254d, c0230c);
    }

    public final void close() {
        super.close();
        this.f558a.m447a();
    }

    public final void connect(String host, int port) {
        try {
            super.connect(host, port);
        } catch (IOException e) {
            InetAddress inetAddress = getInetAddress();
            C0270r c0270r = this.f558a;
            if (host != null) {
                try {
                    c0270r.m449a(e, inetAddress, port, host, null);
                } catch (ThreadDeath e2) {
                    throw e2;
                } catch (Throwable th) {
                    cc.m359b(th);
                }
            }
            throw e;
        }
    }

    public final void connect(InetAddress ipAddr, int port) {
        try {
            super.connect(ipAddr, port);
        } catch (IOException e) {
            C0270r c0270r = this.f558a;
            if (ipAddr != null) {
                try {
                    c0270r.m449a(e, ipAddr, port, null, null);
                } catch (ThreadDeath e2) {
                    throw e2;
                } catch (Throwable th) {
                    cc.m359b(th);
                }
            }
            throw e;
        }
    }

    public final void connect(SocketAddress addr, int timeout) {
        try {
            super.connect(addr, timeout);
        } catch (IOException e) {
            C0270r c0270r = this.f558a;
            if (addr != null) {
                try {
                    if (addr instanceof InetSocketAddress) {
                        InetSocketAddress inetSocketAddress = (InetSocketAddress) addr;
                        InetAddress address = inetSocketAddress.getAddress();
                        int port = inetSocketAddress.getPort();
                        if (address != null) {
                            c0270r.m449a(e, address, port, null, null);
                        }
                    }
                } catch (ThreadDeath e2) {
                    throw e2;
                } catch (Throwable th) {
                    cc.m359b(th);
                }
            }
            throw e;
        }
    }

    public final InputStream getInputStream() {
        return this.f558a.m445a((C0265u) this, super.getInputStream());
    }

    public final OutputStream getOutputStream() {
        return this.f558a.m446a((C0265u) this, super.getOutputStream());
    }

    public final C0212b m452a() {
        InetAddress inetAddress = getInetAddress();
        int port = getPort();
        C0270r c0270r = this.f558a;
        return c0270r.m444a(inetAddress, Integer.valueOf(port), c0270r.f552a);
    }

    public final void m453a(C0212b c0212b) {
        this.f558a.m448a(c0212b);
    }

    public final C0212b m454b() {
        return this.f558a.m451b();
    }

    public final Object getOption(int option) {
        return super.getOption(option);
    }

    public final void setOption(int option, Object value) {
        super.setOption(option, value);
    }
}
