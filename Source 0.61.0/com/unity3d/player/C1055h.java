package com.unity3d.player;

/* renamed from: com.unity3d.player.h */
final class C1055h {
    private static boolean f691a;
    private boolean f692b;
    private boolean f693c;
    private boolean f694d;
    private boolean f695e;

    static {
        f691a = false;
    }

    C1055h() {
        this.f692b = !C1049e.f681b;
        this.f693c = false;
        this.f694d = false;
        this.f695e = true;
    }

    static void m559a() {
        f691a = true;
    }

    static void m560b() {
        f691a = false;
    }

    static boolean m561c() {
        return f691a;
    }

    final void m562a(boolean z) {
        this.f693c = z;
    }

    final void m563b(boolean z) {
        this.f695e = z;
    }

    final void m564c(boolean z) {
        this.f694d = z;
    }

    final void m565d() {
        this.f692b = true;
    }

    final boolean m566e() {
        return this.f695e;
    }

    final boolean m567f() {
        return f691a && this.f693c && this.f692b && !this.f695e && !this.f694d;
    }

    final boolean m568g() {
        return this.f694d;
    }

    public final String toString() {
        return super.toString();
    }
}
