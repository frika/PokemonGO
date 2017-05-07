package com.crittercism.internal;

import com.voxelbusters.nativeplugins.defines.Keys.Scheme;
import java.net.InetAddress;

/* renamed from: com.crittercism.internal.f */
public final class C0257f {
    public InetAddress f514a;
    String f515b;
    public String f516c;
    public C0256a f517d;
    public int f518e;
    public boolean f519f;

    /* renamed from: com.crittercism.internal.f.a */
    public enum C0256a {
        HTTP(Scheme.HTTP, 80),
        HTTPS(Scheme.HTTPS, 443);
        
        private String f512c;
        private int f513d;

        private C0256a(String str, int i) {
            this.f512c = str;
            this.f513d = i;
        }
    }

    public C0257f() {
        this.f516c = "/";
        this.f517d = null;
        this.f518e = -1;
        this.f519f = false;
    }
}
