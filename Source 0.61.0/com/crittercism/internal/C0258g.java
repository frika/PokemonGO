package com.crittercism.internal;

import java.io.OutputStream;

/* renamed from: com.crittercism.internal.g */
public final class C0258g extends OutputStream implements ac {
    OutputStream f520a;
    private C0265u f521b;
    private C0212b f522c;
    private C0177v f523d;

    public C0258g(C0265u c0265u, OutputStream outputStream) {
        if (c0265u == null) {
            throw new NullPointerException("socket was null");
        } else if (outputStream == null) {
            throw new NullPointerException("output stream was null");
        } else {
            this.f521b = c0265u;
            this.f520a = outputStream;
            this.f523d = m394b();
            if (this.f523d == null) {
                throw new NullPointerException("parser was null");
            }
        }
    }

    public final void flush() {
        this.f520a.flush();
    }

    public final void close() {
        this.f520a.close();
    }

    public final void write(int oneByte) {
        this.f520a.write(oneByte);
        try {
            this.f523d.m29a(oneByte);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
            this.f523d = aj.f45d;
        }
    }

    public final void write(byte[] buffer) {
        this.f520a.write(buffer);
        if (buffer != null) {
            m387a(buffer, 0, buffer.length);
        }
    }

    public final void write(byte[] buffer, int offset, int byteCount) {
        this.f520a.write(buffer, offset, byteCount);
        if (buffer != null) {
            m387a(buffer, offset, byteCount);
        }
    }

    public final void m393a(String str, String str2) {
        C0212b d = m388d();
        d.m233c();
        d.f249j = str;
        d.f253n = null;
        C0257f c0257f = d.f252m;
        if (str2 != null) {
            c0257f.f516c = str2;
        }
        this.f521b.m429a(d);
    }

    public final void m390a(int i) {
    }

    public final void m391a(C0177v c0177v) {
        this.f523d = c0177v;
    }

    public final C0177v m389a() {
        return this.f523d;
    }

    public final void m395b(int i) {
        C0212b c0212b = this.f522c;
        this.f522c = null;
        if (c0212b != null) {
            c0212b.m231b((long) i);
        }
    }

    private C0212b m388d() {
        if (this.f522c == null) {
            this.f522c = this.f521b.m428a();
        }
        return this.f522c;
    }

    public final C0177v m394b() {
        return new ae(this);
    }

    public final String m396c() {
        C0212b d = m388d();
        if (d != null) {
            return d.f249j;
        }
        return null;
    }

    public final void m392a(String str) {
        C0212b d = m388d();
        if (d != null) {
            d.m232b(str);
        }
    }

    private void m387a(byte[] bArr, int i, int i2) {
        try {
            this.f523d.m28a(bArr, i, i2);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
            this.f523d = aj.f45d;
        }
    }
}
