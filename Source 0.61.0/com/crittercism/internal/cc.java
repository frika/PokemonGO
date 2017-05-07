package com.crittercism.internal;

import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.Log;
import com.crittercism.app.Crittercism.LoggingLevel;
import com.upsight.mediation.vast.VASTPlayer;
import spacemadness.com.lunarconsole.C1628R;

public final class cc {
    public static int f472a;
    private static C0247b f473b;
    private static cc f474c;

    /* renamed from: com.crittercism.internal.cc.1 */
    static /* synthetic */ class C02451 {
        static final /* synthetic */ int[] f458a;
        static final /* synthetic */ int[] f459b;

        static {
            f459b = new int[LoggingLevel.values().length];
            try {
                f459b[LoggingLevel.Silent.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f459b[LoggingLevel.Error.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f459b[LoggingLevel.Warning.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f459b[LoggingLevel.Info.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            f458a = new int[C0247b.values().length];
            try {
                f458a[C0247b.Silent.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f458a[C0247b.Error.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            try {
                f458a[C0247b.Warning.ordinal()] = 3;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    /* renamed from: com.crittercism.internal.cc.a */
    public enum C0246a {
        ;

        static {
            f460a = 1;
            f461b = 2;
            f462c = 3;
            f463d = new int[]{f460a, f461b, f462c};
        }
    }

    /* renamed from: com.crittercism.internal.cc.b */
    public enum C0247b {
        Silent(0),
        Error(100),
        Warning(Callback.DEFAULT_DRAG_ANIMATION_DURATION),
        Info(VASTPlayer.ERROR_GENERAL_WRAPPER),
        Debug(VASTPlayer.ERROR_GENERAL_LINEAR),
        Verbose(500);
        
        private int f471g;

        private C0247b(int i) {
            this.f471g = i;
        }

        public final boolean m350a(C0247b c0247b) {
            return this.f471g >= c0247b.f471g;
        }

        public static C0247b m349a(LoggingLevel loggingLevel) {
            switch (C02451.f459b[loggingLevel.ordinal()]) {
                case C1628R.styleable.RecyclerView_layoutManager /*1*/:
                    return Silent;
                case C1628R.styleable.RecyclerView_spanCount /*2*/:
                    return Error;
                case C1628R.styleable.RecyclerView_reverseLayout /*3*/:
                    return Warning;
                case C1628R.styleable.RecyclerView_stackFromEnd /*4*/:
                    return Info;
                default:
                    return Warning;
            }
        }
    }

    static {
        f472a = C0246a.f460a;
        f473b = C0247b.Info;
        f474c = new cc();
    }

    public static void m352a(LoggingLevel loggingLevel) {
        f473b = C0247b.m349a(loggingLevel);
    }

    public static void m353a(String str) {
        if (f473b.m350a(C0247b.Error)) {
            Log.e("Crittercism", str);
        }
    }

    public static void m354a(String str, Throwable th) {
        if (f473b.m350a(C0247b.Error)) {
            Log.e("Crittercism", str, th);
        }
    }

    public static void m357b(String str) {
        if (f473b.m350a(C0247b.Warning)) {
            Log.w("Crittercism", str);
        }
    }

    public static void m358b(String str, Throwable th) {
        if (f473b.m350a(C0247b.Warning)) {
            Log.w("Crittercism", str, th);
        }
    }

    public static void m360c(String str) {
        if (f473b.m350a(C0247b.Info)) {
            Log.i("Crittercism", str);
        }
    }

    public static void m362d(String str) {
        if (f473b.m350a(C0247b.Debug)) {
            Log.d("Crittercism", str);
        }
    }

    public static void m361c(String str, Throwable th) {
        if (f473b.m350a(C0247b.Debug)) {
            Log.d("Crittercism", str, th);
        }
    }

    public static void m355a(Throwable th) {
        if (f473b.m350a(C0247b.Debug)) {
            Log.d("Crittercism", th.getMessage(), th);
        }
    }

    public static void m351a() {
    }

    public static void m356b() {
    }

    public static void m359b(Throwable th) {
        try {
            m355a(th);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th2) {
        }
    }
}
