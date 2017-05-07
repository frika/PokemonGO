package com.crittercism.internal;

/* renamed from: com.crittercism.internal.y */
public final class C0275y extends C0177v {
    private C0276z f566d;
    private int f567e;
    private int f568f;

    public C0275y(C0276z c0276z, int i) {
        super((C0177v) c0276z);
        this.f568f = 0;
        this.f566d = c0276z;
        this.f567e = i;
    }

    public final boolean m466a(int i) {
        if (this.f568f >= this.f567e + 2) {
            return false;
        }
        if (i == -1) {
            this.f32a.m58b(m27a());
            this.f32a.m54a(aj.f45d);
            return true;
        }
        this.c++;
        char c = (char) i;
        this.f568f++;
        if (this.f568f <= this.f567e) {
            return false;
        }
        if (c == '\n') {
            this.f566d.m33b(m27a());
            this.f32a.m54a(this.f566d);
            return true;
        } else if (this.f568f != this.f567e + 2 || c == '\n') {
            return false;
        } else {
            this.f32a.m54a(aj.f45d);
            return true;
        }
    }

    public final C0177v m468b() {
        return this.f566d;
    }

    public final C0177v m469c() {
        return null;
    }

    public final boolean m467a(C0273w c0273w) {
        return true;
    }

    protected final int m470d() {
        return 0;
    }

    protected final int m471e() {
        return 0;
    }

    public final void m472f() {
        this.f32a.m58b(m27a());
        this.f32a.m54a(aj.f45d);
    }
}
