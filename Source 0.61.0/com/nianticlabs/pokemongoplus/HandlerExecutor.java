package com.nianticlabs.pokemongoplus;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import java.util.concurrent.Executor;

class HandlerExecutor implements Executor {
    private static final boolean reallyAssertOnThread = false;
    private final Handler handler;
    private final HandlerThread handlerThread;

    HandlerExecutor(String name) {
        this.handlerThread = new HandlerThread(name);
        this.handlerThread.start();
        this.handler = new Handler(this.handlerThread.getLooper());
    }

    public void execute(Runnable command) {
        this.handler.post(command);
    }

    public boolean onThread() {
        return Looper.myLooper() == this.handlerThread.getLooper();
    }

    public void maybeAssertOnThread() {
        if (!onThread()) {
        }
    }

    public void stop() {
        this.handlerThread.quitSafely();
    }
}
