package com.crittercism.internal;

public final class bj {
    public int f304a;
    public int f305b;

    public bj(int i, int i2) {
        this.f304a = bk.f309d - 1;
        this.f305b = bi.OK.ordinal();
        this.f304a = i;
        this.f305b = i2;
    }

    public bj(Throwable th) {
        this.f304a = bk.f309d - 1;
        this.f305b = bi.OK.ordinal();
        if (th != null) {
            this.f304a = bk.m272a(th);
            if (this.f304a == bk.f309d - 1) {
                this.f305b = bi.m270a(th).f302C;
            } else {
                this.f305b = Integer.parseInt(th.getMessage());
            }
        }
    }
}
