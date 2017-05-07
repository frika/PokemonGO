package com.crittercism.internal;

/* renamed from: com.crittercism.internal.w */
public final class C0273w {
    char[] f562a;
    int f563b;

    public C0273w(int i) {
        this.f562a = new char[i];
    }

    public final String m457a(int i) {
        if (i > this.f563b) {
            throw new IndexOutOfBoundsException("endIndex: " + i + " > length: " + this.f563b);
        } else if (i < 0) {
            throw new IndexOutOfBoundsException("beginIndex: 0 > endIndex: " + i);
        } else {
            int i2 = 0;
            while (i2 < i && C0273w.m456a(this.f562a[i2])) {
                i2++;
            }
            while (i > i2 && C0273w.m456a(this.f562a[i - 1])) {
                i--;
            }
            return new String(this.f562a, i2, i - i2);
        }
    }

    private static boolean m456a(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    public final String toString() {
        return new String(this.f562a, 0, this.f563b);
    }
}
