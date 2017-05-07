package com.nianticlabs.nia.contextservice;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public abstract class ContextService {
    private static final Handler handler;
    private static final HandlerThread handlerThread;
    private static final Handler mainHandler;
    protected final long NULL_POINTER;
    protected final Object callbackLock;
    protected final Context context;
    protected long nativeClassPointer;
    private Runnable runOnPause;
    private Runnable runOnResume;
    private Runnable runOnStart;
    private Runnable runOnStop;

    /* renamed from: com.nianticlabs.nia.contextservice.ContextService.1 */
    class C09351 implements Runnable {
        C09351() {
        }

        public void run() {
            ContextService.this.onStart();
        }
    }

    /* renamed from: com.nianticlabs.nia.contextservice.ContextService.2 */
    class C09362 implements Runnable {
        C09362() {
        }

        public void run() {
            ContextService.this.onStop();
        }
    }

    /* renamed from: com.nianticlabs.nia.contextservice.ContextService.3 */
    class C09373 implements Runnable {
        C09373() {
        }

        public void run() {
            ContextService.this.onPause();
        }
    }

    /* renamed from: com.nianticlabs.nia.contextservice.ContextService.4 */
    class C09384 implements Runnable {
        C09384() {
        }

        public void run() {
            ContextService.this.onResume();
        }
    }

    public static native void setActivityProviderClass(String str);

    public ContextService(Context context, long nativeClassPointer) {
        this.NULL_POINTER = 0;
        this.callbackLock = new Object();
        this.runOnStart = new C09351();
        this.runOnStop = new C09362();
        this.runOnPause = new C09373();
        this.runOnResume = new C09384();
        this.context = context;
        this.nativeClassPointer = nativeClassPointer;
    }

    public void onStart() {
    }

    public void onStop() {
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public final void resetNativeClassPointer() {
        synchronized (this.callbackLock) {
            this.nativeClassPointer = 0;
        }
    }

    public Context getContext() {
        return this.context;
    }

    public static void runOnUiThread(Runnable runnable) {
        mainHandler.post(runnable);
    }

    public static void runOnServiceHandler(Runnable runnable) {
        handler.post(runnable);
    }

    public static boolean onUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static boolean onServiceThread() {
        return Looper.myLooper() == handlerThread.getLooper();
    }

    public static void assertOnServiceThread() {
        if (!onServiceThread()) {
            throw new RuntimeException("Must be on the service thread");
        }
    }

    public static Looper getServiceLooper() {
        return handlerThread.getLooper();
    }

    public static Handler getServiceHandler() {
        return handler;
    }

    private void invokeOnStart() {
        runOnServiceHandler(this.runOnStart);
    }

    private void invokeOnStop() {
        runOnServiceHandler(this.runOnStop);
    }

    private void invokeOnPause() {
        runOnServiceHandler(this.runOnPause);
    }

    private void invokeOnResume() {
        runOnServiceHandler(this.runOnResume);
    }

    static {
        handlerThread = new HandlerThread("ContextService");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(Looper.getMainLooper());
    }
}
