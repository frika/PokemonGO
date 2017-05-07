package com.crittercism.internal;

public final class af extends ab {
    private int f41g;

    public af(C0177v c0177v, int i) {
        super(c0177v);
        this.f41g = i;
    }

    protected final C0177v m66g() {
        Object obj;
        if (this.f32a.m59c().equals("HEAD") || ((this.f41g >= 100 && this.f41g <= 199) || this.f41g == 204 || this.f41g == 304)) {
            obj = 1;
        } else {
            obj = null;
        }
        if (obj != null) {
            this.f32a.m58b(m27a());
            return this.f32a.m57b();
        } else if (this.f38f) {
            return new C0276z(this);
        } else {
            if (this.f36d) {
                if (this.f37e > 0) {
                    return new C0274x(this, this.f37e);
                }
                this.f32a.m58b(m27a());
                return this.f32a.m57b();
            } else if (!this.f32a.m59c().equals("CONNECT")) {
                return new aa(this);
            } else {
                this.f32a.m58b(m27a());
                return this.f32a.m57b();
            }
        }
    }
}
