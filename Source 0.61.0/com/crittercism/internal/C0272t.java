package com.crittercism.internal;

import java.net.Socket;
import java.net.SocketImpl;
import java.net.SocketImplFactory;

/* renamed from: com.crittercism.internal.t */
public final class C0272t implements SocketImplFactory {
    private static boolean f559a;
    private C0254d f560b;
    private C0230c f561c;

    static {
        f559a = false;
    }

    public static boolean m455a(C0254d c0254d, C0230c c0230c) {
        if (f559a) {
            return f559a;
        }
        SocketImplFactory c0272t = new C0272t(c0254d, c0230c);
        try {
            c0272t.createSocketImpl();
            Socket.setSocketImplFactory(c0272t);
            f559a = true;
            return true;
        } catch (Throwable th) {
            return f559a;
        }
    }

    private C0272t(C0254d c0254d, C0230c c0230c) {
        this.f560b = c0254d;
        this.f561c = c0230c;
    }

    public final SocketImpl createSocketImpl() {
        return new C0271s(this.f560b, this.f561c);
    }
}
