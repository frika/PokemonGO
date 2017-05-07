package com.crittercism.internal;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;

public final class ah extends C0177v {
    private boolean f43d;

    public ah(C0177v c0177v) {
        super(c0177v);
        this.f43d = false;
    }

    public final C0177v m73b() {
        if (this.f43d) {
            this.f32a.m58b(m27a());
            return this.f32a.m57b();
        }
        this.f33b.f563b = 0;
        return this;
    }

    public final boolean m72a(C0273w c0273w) {
        this.f43d = c0273w.m457a(c0273w.f563b).length() == 0;
        return true;
    }

    protected final int m75d() {
        return 8;
    }

    protected final int m76e() {
        return AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS;
    }

    public final C0177v m74c() {
        this.f33b.f563b = 0;
        return new ai(this);
    }
}
