package com.crittercism.internal;

/* renamed from: com.crittercism.internal.v */
public abstract class C0177v {
    ac f32a;
    C0273w f33b;
    protected int f34c;
    private int f35d;

    public abstract boolean m30a(C0273w c0273w);

    public abstract C0177v m32b();

    public abstract C0177v m34c();

    protected abstract int m35d();

    protected abstract int m36e();

    public C0177v(ac acVar) {
        m25a(acVar, 0);
    }

    public C0177v(C0177v c0177v) {
        m25a(c0177v.f32a, c0177v.f34c);
    }

    private void m25a(ac acVar, int i) {
        this.f32a = acVar;
        this.f35d = m36e();
        this.f33b = new C0273w(m35d());
        this.f34c = i;
    }

    public boolean m29a(int i) {
        if (i == -1) {
            m26g();
            return true;
        }
        C0177v b;
        this.f34c++;
        char c = (char) i;
        if (c == '\n') {
            if (m30a(this.f33b)) {
                b = m32b();
            } else {
                b = aj.f45d;
            }
        } else if (this.f33b.f563b < this.f35d) {
            C0273w c0273w = this.f33b;
            int i2 = c0273w.f563b + 1;
            if (i2 > c0273w.f562a.length) {
                Object obj = new char[Math.max(c0273w.f562a.length << 1, i2)];
                System.arraycopy(c0273w.f562a, 0, obj, 0, c0273w.f563b);
                c0273w.f562a = obj;
            }
            c0273w.f562a[c0273w.f563b] = c;
            c0273w.f563b = i2;
            b = this;
        } else {
            b = m34c();
        }
        if (b != this) {
            this.f32a.m54a(b);
        }
        if (b == this) {
            return false;
        }
        return true;
    }

    public final void m28a(byte[] bArr, int i, int i2) {
        int b = m31b(bArr, i, i2);
        while (b > 0 && b < i2) {
            int b2 = this.f32a.m52a().m31b(bArr, i + b, i2 - b);
            if (b2 > 0) {
                b += b2;
            } else {
                return;
            }
        }
    }

    protected int m31b(byte[] bArr, int i, int i2) {
        boolean z = false;
        int i3 = -1;
        if (i2 == -1) {
            m26g();
        } else if (!(bArr == null || i2 == 0)) {
            i3 = 0;
            while (!z && i3 < i2) {
                z = m29a((char) bArr[i + i3]);
                i3++;
            }
        }
        return i3;
    }

    public final int m27a() {
        return this.f34c;
    }

    public final void m33b(int i) {
        this.f34c = i;
    }

    public String toString() {
        return this.f33b.toString();
    }

    public void m37f() {
        if (this.f32a != null) {
            this.f32a.m54a(aj.f45d);
        }
    }

    private void m26g() {
        this.f32a.m54a(aj.f45d);
    }
}
