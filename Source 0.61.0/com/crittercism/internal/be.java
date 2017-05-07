package com.crittercism.internal;

import java.util.Locale;

public final class be {
    public static final be f269a;
    private volatile int f270b;
    private final long f271c;

    static {
        f269a = new be();
    }

    private be() {
        this.f270b = 1;
        this.f271c = System.currentTimeMillis();
    }

    private synchronized int m268b() {
        int i;
        i = this.f270b;
        this.f270b = i + 1;
        return i;
    }

    public final String m269a() {
        return String.format(Locale.US, "%d.%d.%09d", new Object[]{Integer.valueOf(1), Long.valueOf(this.f271c), Integer.valueOf(m268b())});
    }
}
