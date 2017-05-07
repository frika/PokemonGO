package com.crittercism.internal;

public final class ai extends C0177v {
    private C0177v f44d;

    public ai(C0177v c0177v) {
        super(c0177v);
        this.f44d = c0177v;
    }

    public final boolean m77a(int i) {
        if (i == -1) {
            this.f32a.m54a(aj.f45d);
            return true;
        }
        this.c++;
        if (((char) i) != '\n') {
            return false;
        }
        this.f44d.m33b(m27a());
        this.f32a.m54a(this.f44d);
        return true;
    }

    public final C0177v m79b() {
        return this;
    }

    public final C0177v m80c() {
        return this;
    }

    public final boolean m78a(C0273w c0273w) {
        return true;
    }

    protected final int m81d() {
        return 0;
    }

    protected final int m82e() {
        return 0;
    }
}
