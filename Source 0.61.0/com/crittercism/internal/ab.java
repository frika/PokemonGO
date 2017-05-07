package com.crittercism.internal;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.voxelbusters.nativeplugins.defines.Keys.WebView;

public abstract class ab extends C0177v {
    boolean f36d;
    int f37e;
    boolean f38f;
    private boolean f39g;
    private boolean f40h;

    protected abstract C0177v m51g();

    public ab(C0177v c0177v) {
        super(c0177v);
        this.f36d = false;
        this.f39g = false;
        this.f40h = false;
        this.f38f = false;
    }

    public final C0177v m47b() {
        if (this.f40h) {
            return m51g();
        }
        this.f33b.f563b = 0;
        return this;
    }

    public final boolean m46a(C0273w c0273w) {
        int i = this.f33b.f563b;
        if (i == 0 || (i == 1 && this.f33b.f562a[0] == '\r')) {
            i = true;
        } else {
            i = 0;
        }
        if (i != 0) {
            this.f40h = true;
            return true;
        }
        try {
            String[] split = c0273w.toString().split(UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR, 2);
            if (split.length != 2) {
                return false;
            }
            String trim = split[0].trim();
            String trim2 = split[1].trim();
            if (!this.f36d && trim.equalsIgnoreCase("content-length")) {
                i = Integer.parseInt(trim2);
                if (i < 0) {
                    return false;
                }
                this.f36d = true;
                this.f37e = i;
                return true;
            } else if (trim.equalsIgnoreCase("transfer-encoding")) {
                this.f38f = trim2.equalsIgnoreCase("chunked");
                return true;
            } else if (this.f39g || !trim.equalsIgnoreCase(WebView.HOST) || trim2 == null) {
                return true;
            } else {
                this.f39g = true;
                this.f32a.m55a(trim2);
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    protected final int m49d() {
        return 32;
    }

    protected final int m50e() {
        return AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS;
    }

    public final C0177v m48c() {
        this.f33b.f563b = 0;
        return new ai(this);
    }
}
