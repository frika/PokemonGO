package com.crittercism.internal;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;

/* renamed from: com.crittercism.internal.z */
public final class C0276z extends C0177v {
    private int f569d;

    public C0276z(C0177v c0177v) {
        super(c0177v);
        this.f569d = -1;
    }

    public final C0177v m474b() {
        if (this.f569d == 0) {
            return new ah(this);
        }
        this.f33b.f563b = 0;
        return new C0275y(this, this.f569d);
    }

    public final C0177v m475c() {
        return aj.f45d;
    }

    public final boolean m473a(C0273w c0273w) {
        int i;
        int i2 = c0273w.f563b;
        if (i2 > c0273w.f563b) {
            i = c0273w.f563b;
        } else {
            i = i2;
        }
        if (i >= 0) {
            i2 = 0;
            while (i2 < i) {
                if (c0273w.f562a[i2] == ';') {
                    break;
                }
                i2++;
            }
        }
        i2 = -1;
        i = c0273w.f563b;
        if (i2 <= 0) {
            i2 = i;
        }
        try {
            this.f569d = Integer.parseInt(c0273w.m457a(i2), 16);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    protected final int m476d() {
        return 16;
    }

    protected final int m477e() {
        return AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
    }

    public final void m478f() {
        this.f32a.m58b(m27a());
        this.f32a.m54a(aj.f45d);
    }
}
