package com.crittercism.internal;

/* renamed from: com.crittercism.internal.x */
public final class C0274x extends C0177v {
    private int f564d;
    private int f565e;

    public C0274x(C0177v c0177v, int i) {
        super(c0177v);
        this.f565e = 0;
        this.f564d = i;
    }

    public final boolean m458a(int i) {
        if (i == -1) {
            this.f32a.m54a(aj.f45d);
            return true;
        }
        this.f565e++;
        this.c++;
        if (this.f565e != this.f564d) {
            return false;
        }
        this.f32a.m58b(m27a());
        this.f32a.m54a(this.f32a.m57b());
        return true;
    }

    public final int m460b(byte[] bArr, int i, int i2) {
        if (i2 == -1) {
            this.f32a.m54a(aj.f45d);
            return -1;
        } else if (this.f565e + i2 < this.f564d) {
            this.f565e += i2;
            this.c += i2;
            return i2;
        } else {
            i2 = this.f564d - this.f565e;
            this.c += i2;
            this.f32a.m58b(m27a());
            this.f32a.m54a(this.f32a.m57b());
            return i2;
        }
    }

    public final C0177v m461b() {
        return aj.f45d;
    }

    public final C0177v m462c() {
        return aj.f45d;
    }

    public final boolean m459a(C0273w c0273w) {
        return true;
    }

    protected final int m463d() {
        return 0;
    }

    protected final int m464e() {
        return 0;
    }

    public final void m465f() {
        this.f32a.m58b(m27a());
        this.f32a.m54a(aj.f45d);
    }
}
