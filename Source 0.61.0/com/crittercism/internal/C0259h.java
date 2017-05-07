package com.crittercism.internal;

import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import com.crittercism.internal.C0212b.C0211c;
import java.io.InputStream;

/* renamed from: com.crittercism.internal.h */
public final class C0259h extends InputStream implements ac {
    C0212b f524a;
    InputStream f525b;
    C0177v f526c;
    private C0265u f527d;
    private C0254d f528e;

    public C0259h(C0265u c0265u, InputStream inputStream, C0254d c0254d) {
        if (c0265u == null) {
            throw new NullPointerException("socket was null");
        } else if (inputStream == null) {
            throw new NullPointerException("delegate was null");
        } else if (c0254d == null) {
            throw new NullPointerException("dispatch was null");
        } else {
            this.f527d = c0265u;
            this.f525b = inputStream;
            this.f528e = c0254d;
            this.f526c = m405b();
            if (this.f526c == null) {
                throw new NullPointerException("parser was null");
            }
        }
    }

    public final int available() {
        return this.f525b.available();
    }

    public final void close() {
        try {
            this.f526c.m37f();
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
        this.f525b.close();
    }

    public final void mark(int readlimit) {
        this.f525b.mark(readlimit);
    }

    public final boolean markSupported() {
        return this.f525b.markSupported();
    }

    public final int read() {
        try {
            int read = this.f525b.read();
            try {
                this.f526c.m29a(read);
            } catch (ThreadDeath e) {
                throw e;
            } catch (IllegalStateException e2) {
                this.f526c = aj.f45d;
            } catch (Throwable th) {
                this.f526c = aj.f45d;
                cc.m359b(th);
            }
            return read;
        } catch (Exception e3) {
            m397a(e3);
            throw e3;
        }
    }

    public final int read(byte[] buffer) {
        try {
            int bytesIn = this.f525b.read(buffer);
            m398a(buffer, 0, bytesIn);
            return bytesIn;
        } catch (Exception e) {
            m397a(e);
            throw e;
        }
    }

    public final int read(byte[] buffer, int offset, int length) {
        try {
            int bytesIn = this.f525b.read(buffer, offset, length);
            m398a(buffer, offset, bytesIn);
            return bytesIn;
        } catch (Exception e) {
            m397a(e);
            throw e;
        }
    }

    private void m398a(byte[] bArr, int i, int i2) {
        try {
            this.f526c.m28a(bArr, i, i2);
        } catch (ThreadDeath e) {
            throw e;
        } catch (IllegalStateException e2) {
            this.f526c = aj.f45d;
        } catch (Throwable th) {
            this.f526c = aj.f45d;
            cc.m359b(th);
        }
    }

    public final synchronized void reset() {
        this.f525b.reset();
    }

    public final long skip(long byteCount) {
        return this.f525b.skip(byteCount);
    }

    public final void m404a(String str, String str2) {
    }

    public final void m401a(int i) {
        C0212b d = m399d();
        d.m235d();
        d.f248i = i;
    }

    public final void m402a(C0177v c0177v) {
        this.f526c = c0177v;
    }

    public final C0177v m400a() {
        return this.f526c;
    }

    public final void m406b(int i) {
        C0212b c0212b = null;
        if (this.f524a != null) {
            int i2 = this.f524a.f248i;
            if (i2 >= 100 && i2 < Callback.DEFAULT_DRAG_ANIMATION_DURATION) {
                c0212b = new C0212b(this.f524a.m225a());
                c0212b.m234c(this.f524a.f240a);
                c0212b.m231b(this.f524a.f247h);
                c0212b.f249j = this.f524a.f249j;
            }
            this.f524a.m226a((long) i);
            this.f528e.m382a(this.f524a, C0211c.INPUT_STREAM_FINISHED);
        }
        this.f524a = c0212b;
    }

    private C0212b m399d() {
        if (this.f524a == null) {
            this.f524a = this.f527d.m430b();
        }
        if (this.f524a != null) {
            return this.f524a;
        }
        throw new IllegalStateException("No statistics were queued up.");
    }

    public final C0177v m405b() {
        return new ag(this);
    }

    public final String m407c() {
        return m399d().f249j;
    }

    public final void m403a(String str) {
    }

    private void m397a(Exception exception) {
        try {
            C0212b d = m399d();
            d.m229a((Throwable) exception);
            this.f528e.m382a(d, C0211c.PARSING_INPUT_STREAM_LOG_ERROR);
        } catch (ThreadDeath e) {
            throw e;
        } catch (IllegalStateException e2) {
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }
}
