package com.crittercism.internal;

import android.support.v7.widget.RecyclerView.ItemAnimator;

public final class ae extends C0177v {
    public ae(ac acVar) {
        super(acVar);
    }

    public final C0177v m62b() {
        return new ad(this);
    }

    public final C0177v m63c() {
        return aj.f45d;
    }

    public final boolean m61a(C0273w c0273w) {
        String[] split = c0273w.toString().split(" ");
        if (split.length != 3) {
            return false;
        }
        this.f32a.m56a(split[0], split[1]);
        return true;
    }

    protected final int m64d() {
        return 64;
    }

    protected final int m65e() {
        return ItemAnimator.FLAG_MOVED;
    }
}
