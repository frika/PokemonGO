package com.crittercism.internal;

import java.lang.Thread.UncaughtExceptionHandler;

public abstract class al implements UncaughtExceptionHandler {
    private UncaughtExceptionHandler f48a;

    public abstract void m90a(Throwable th);

    public al(UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.f48a = uncaughtExceptionHandler;
    }

    public final void uncaughtException(Thread thread, Throwable exception) {
        try {
            m90a(exception);
            if (this.f48a != null) {
                this.f48a.uncaughtException(thread, exception);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            if (this.f48a != null) {
                this.f48a.uncaughtException(thread, exception);
            }
        }
    }
}
