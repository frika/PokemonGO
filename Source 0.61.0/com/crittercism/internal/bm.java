package com.crittercism.internal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Process;
import android.os.SystemClock;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressLint({"NewApi"})
public abstract class bm {
    private static final int[] f315g;
    boolean f316a;
    private int f317b;
    private boolean f318c;
    private boolean f319d;
    private Application f320e;
    private C0216a f321f;

    /* renamed from: com.crittercism.internal.bm.a */
    class C0216a implements ActivityLifecycleCallbacks {
        final /* synthetic */ bm f314a;

        private C0216a(bm bmVar) {
            this.f314a = bmVar;
        }

        public final void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        public final void onActivityStarted(Activity activity) {
        }

        public final void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        public final void onActivityDestroyed(Activity activity) {
        }

        public final void onActivityResumed(Activity activity) {
            if (activity != null) {
                try {
                    this.f314a.m288b(activity);
                    this.f314a.m283g();
                    if (!this.f314a.f319d) {
                        this.f314a.f319d = true;
                    }
                    if (this.f314a.f318c) {
                        cc.m362d("not a foreground. rotation event.");
                        this.f314a.f318c = false;
                    } else {
                        if (this.f314a.f317b == 0) {
                            this.f314a.m287b();
                        }
                        this.f314a.m286a(activity);
                    }
                    this.f314a.f317b = this.f314a.f317b + 1;
                } catch (ThreadDeath e) {
                    throw e;
                } catch (Throwable th) {
                    cc.m359b(th);
                }
            }
        }

        public final void onActivityPaused(Activity activity) {
            if (activity != null) {
                try {
                    this.f314a.m290c(activity);
                    if (!this.f314a.f319d) {
                        this.f314a.f317b;
                        this.f314a.f317b = 1;
                        this.f314a.f319d = true;
                    }
                    this.f314a.m283g();
                } catch (ThreadDeath e) {
                    throw e;
                } catch (Throwable th) {
                    cc.m359b(th);
                }
            }
        }

        public final void onActivityStopped(Activity activity) {
            if (activity != null) {
                try {
                    this.f314a.m292e();
                    if (!this.f314a.f319d) {
                        this.f314a.f317b;
                        this.f314a.f317b = 1;
                        this.f314a.f319d = true;
                    }
                    this.f314a.f317b = this.f314a.f317b - 1;
                    if (activity.isChangingConfigurations()) {
                        cc.m362d("not a background. rotation event.");
                        this.f314a.f318c = true;
                    } else if (this.f314a.f317b == 0) {
                        this.f314a.m289c();
                    }
                } catch (ThreadDeath e) {
                    throw e;
                } catch (Throwable th) {
                    cc.m359b(th);
                }
            }
        }
    }

    public bm(Application application) {
        this.f317b = 0;
        this.f318c = false;
        this.f319d = false;
        this.f316a = false;
        this.f320e = null;
        this.f321f = null;
        if (VERSION.SDK_INT < 14) {
            throw new IllegalStateException("App lifecycle monitoring is only supported on API 14 and later");
        }
        this.f320e = application;
    }

    public final void m285a() {
        if (this.f321f == null) {
            this.f321f = new C0216a();
            this.f320e.registerActivityLifecycleCallbacks(this.f321f);
            if (m274a(this.f320e)) {
                m283g();
            }
        }
    }

    private void m283g() {
        if (!this.f316a) {
            this.f316a = true;
            m291d();
        }
    }

    public void m287b() {
    }

    public void m289c() {
    }

    public void m291d() {
    }

    public void m286a(Activity activity) {
    }

    public void m288b(Activity activity) {
    }

    public void m290c(Activity activity) {
    }

    public void m292e() {
    }

    private static boolean m274a(Context context) {
        if (context instanceof Activity) {
            return true;
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            String str = "currentActivityThread";
            Method method = cls.getMethod(str, new Class[0]);
            method.setAccessible(true);
            if (cls.isAssignableFrom(method.getReturnType())) {
                Object invoke = method.invoke(null, new Object[0]);
                String str2 = "mNumVisibleActivities";
                Class cls2 = Integer.TYPE;
                Field declaredField = cls.getDeclaredField(str2);
                declaredField.setAccessible(true);
                if (cls2.isAssignableFrom(declaredField.getType())) {
                    return ((Integer) declaredField.get(invoke)).intValue() > 0;
                } else {
                    throw new NoSuchFieldException(str2);
                }
            }
            throw new NoSuchMethodException(str);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m354a("Unable to detect if app has finished launching", th);
            return false;
        }
    }

    static {
        f315g = new int[]{32, 544, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 8224};
    }

    public static long m281f() {
        Boolean bool;
        Throwable th;
        long[] jArr = new long[1];
        String str = "/proc/" + Process.myPid() + "/stat";
        Boolean valueOf = Boolean.valueOf(false);
        try {
            bool = (Boolean) Process.class.getDeclaredMethod("readProcFile", new Class[]{String.class, int[].class, String[].class, long[].class, float[].class}).invoke(null, new Object[]{str, f315g, null, jArr, null});
            th = null;
        } catch (NoSuchMethodException e) {
            th = e;
            bool = valueOf;
        } catch (IllegalAccessException e2) {
            th = e2;
            bool = valueOf;
        } catch (InvocationTargetException e3) {
            th = e3;
            bool = valueOf;
        }
        if (bool.booleanValue()) {
            return System.currentTimeMillis() - (SystemClock.elapsedRealtime() - (jArr[0] * 10));
        }
        throw new IOException("Unable to determine process start time", th);
    }
}
