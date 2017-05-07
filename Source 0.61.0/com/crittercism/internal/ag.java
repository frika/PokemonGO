package com.crittercism.internal;

public final class ag extends C0177v {
    private int f42d;

    public ag(ac acVar) {
        super(acVar);
        this.f42d = -1;
    }

    public final C0177v m68b() {
        return new af(this, this.f42d);
    }

    public final C0177v m69c() {
        return aj.f45d;
    }

    public final boolean m67a(C0273w c0273w) {
        String[] split = c0273w.toString().split(" ");
        if (split.length >= 3) {
            try {
                this.f42d = Integer.parseInt(split[1]);
                this.f32a.m53a(this.f42d);
                return true;
            } catch (NumberFormatException e) {
            }
        }
        return false;
    }

    protected final int m70d() {
        return 20;
    }

    protected final int m71e() {
        return 64;
    }
}
