package com.crittercism.internal;

import com.crittercism.internal.C0257f.C0256a;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.Queue;
import javax.net.ssl.SSLSocket;

/* renamed from: com.crittercism.internal.r */
public final class C0270r {
    C0256a f552a;
    private C0254d f553b;
    private C0230c f554c;
    private final Queue<C0212b> f555d;
    private C0258g f556e;
    private C0259h f557f;

    public C0270r(C0256a c0256a, C0254d c0254d, C0230c c0230c) {
        this.f552a = c0256a;
        this.f553b = c0254d;
        this.f554c = c0230c;
        this.f555d = new LinkedList();
    }

    public final void m447a() {
        try {
            if (this.f557f != null) {
                C0259h c0259h = this.f557f;
                if (c0259h.f524a != null) {
                    Object obj;
                    bj bjVar = c0259h.f524a.f250k;
                    bi biVar = bi.OK;
                    if (bjVar.f304a == bk.f309d - 1 && bjVar.f305b == biVar.f302C) {
                        obj = 1;
                    } else {
                        obj = null;
                    }
                    if (obj != null && c0259h.f526c != null) {
                        c0259h.f526c.m37f();
                    }
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    public final void m450a(IOException iOException, SSLSocket sSLSocket) {
        try {
            m449a(iOException, sSLSocket.getInetAddress(), sSLSocket.getPort(), null, this.f552a);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    final void m449a(IOException iOException, InetAddress inetAddress, int i, String str, C0256a c0256a) {
        C0212b a = m444a(inetAddress, Integer.valueOf(i), c0256a);
        if (str != null) {
            a.m232b(str);
        }
        a.m233c();
        a.m235d();
        a.f252m.f519f = true;
        a.m229a((Throwable) iOException);
        this.f553b.m381a(a);
    }

    public final C0212b m443a(InetAddress inetAddress) {
        return m444a(inetAddress, null, this.f552a);
    }

    final C0212b m444a(InetAddress inetAddress, Integer num, C0256a c0256a) {
        C0212b c0212b = new C0212b();
        if (inetAddress != null) {
            c0212b.f253n = null;
            c0212b.f252m.f514a = inetAddress;
        }
        if (num != null && num.intValue() > 0) {
            int intValue = num.intValue();
            C0257f c0257f = c0212b.f252m;
            if (intValue > 0) {
                c0257f.f518e = intValue;
            }
        }
        if (c0256a != null) {
            c0212b.f252m.f517d = c0256a;
        }
        if (this.f554c != null) {
            c0212b.f254o = C0176a.m24a(this.f554c.f403a);
        }
        if (an.m112b()) {
            c0212b.m227a(an.m110a());
        }
        return c0212b;
    }

    public final void m448a(C0212b c0212b) {
        synchronized (this.f555d) {
            this.f555d.add(c0212b);
        }
    }

    public final C0212b m451b() {
        C0212b c0212b;
        synchronized (this.f555d) {
            c0212b = (C0212b) this.f555d.poll();
        }
        return c0212b;
    }

    public final InputStream m445a(C0265u c0265u, InputStream inputStream) {
        if (inputStream == null) {
            return inputStream;
        }
        try {
            if (this.f557f != null) {
                Object obj;
                if (this.f557f.f525b == inputStream) {
                    obj = 1;
                } else {
                    obj = null;
                }
                if (obj != null) {
                    return this.f557f;
                }
            }
            this.f557f = new C0259h(c0265u, inputStream, this.f553b);
            return this.f557f;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
            return inputStream;
        }
    }

    public final OutputStream m446a(C0265u c0265u, OutputStream outputStream) {
        if (outputStream == null) {
            return outputStream;
        }
        try {
            if (this.f556e != null) {
                Object obj;
                if (this.f556e.f520a == outputStream) {
                    obj = 1;
                } else {
                    obj = null;
                }
                if (obj != null) {
                    return this.f556e;
                }
            }
            this.f556e = new C0258g(c0265u, outputStream);
            return this.f556e;
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
            return outputStream;
        }
    }
}
