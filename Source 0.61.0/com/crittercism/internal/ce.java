package com.crittercism.internal;

import android.support.annotation.NonNull;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOUtils;

public final class ce implements ScheduledExecutorService {
    private ScheduledThreadPoolExecutor f479a;
    private String f480b;

    /* renamed from: com.crittercism.internal.ce.a */
    static class C0248a implements ThreadFactory {
        private String f475a;

        public C0248a(String str) {
            this.f475a = str;
        }

        public final Thread newThread(Runnable r) {
            return new Thread(r, this.f475a);
        }
    }

    /* renamed from: com.crittercism.internal.ce.b */
    static class C0249b<T> implements Callable<T> {
        private Callable<T> f476a;

        public C0249b(Callable<T> callable) {
            this.f476a = callable;
        }

        public final T call() {
            try {
                return this.f476a.call();
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                cc.m359b(th);
                return null;
            }
        }
    }

    /* renamed from: com.crittercism.internal.ce.c */
    static class C0250c implements Runnable {
        private Runnable f477a;
        private String f478b;

        public C0250c(Runnable runnable) {
            this.f477a = runnable;
        }

        public final void run() {
            try {
                this.f477a.run();
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                cc.m359b(th);
            }
        }

        public final String toString() {
            if (this.f478b != null) {
                return this.f478b;
            }
            return this.f477a.toString();
        }
    }

    private ce(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor, String str) {
        this.f479a = scheduledThreadPoolExecutor;
        this.f480b = str;
    }

    public static ScheduledExecutorService m367a(String str) {
        return new ce(new ScheduledThreadPoolExecutor(3, new C0248a(str)), str);
    }

    public static ScheduledExecutorService m368b(String str) {
        return new ce(new ScheduledThreadPoolExecutor(1, new C0248a(str)), str);
    }

    @NonNull
    public final ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return this.f479a.schedule(new C0250c(command), delay, unit);
    }

    @NonNull
    public final <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return this.f479a.schedule(new C0249b(callable), delay, unit);
    }

    @NonNull
    public final ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return this.f479a.scheduleAtFixedRate(new C0250c(command), initialDelay, period, unit);
    }

    @NonNull
    public final ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return this.f479a.scheduleWithFixedDelay(new C0250c(command), initialDelay, delay, unit);
    }

    public final void shutdown() {
        cc.m353a("Shutting down queue. " + toString());
        this.f479a.shutdown();
    }

    @NonNull
    public final List<Runnable> shutdownNow() {
        return this.f479a.shutdownNow();
    }

    public final boolean isShutdown() {
        return this.f479a.isShutdown();
    }

    public final boolean isTerminated() {
        return this.f479a.isTerminated();
    }

    public final boolean awaitTermination(long timeout, TimeUnit unit) {
        return this.f479a.awaitTermination(timeout, unit);
    }

    @NonNull
    public final <T> Future<T> submit(Callable<T> task) {
        return this.f479a.submit(new C0249b(task));
    }

    @NonNull
    public final <T> Future<T> submit(Runnable task, T result) {
        return this.f479a.submit(new C0250c(task), result);
    }

    @NonNull
    public final Future<?> submit(Runnable task) {
        return this.f479a.submit(new C0250c(task));
    }

    @NonNull
    public final <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    public final <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection, long timeout, TimeUnit unit) {
        throw new UnsupportedOperationException();
    }

    @NonNull
    public final <T> T invokeAny(Collection<? extends Callable<T>> collection) {
        throw new UnsupportedOperationException();
    }

    public final <T> T invokeAny(Collection<? extends Callable<T>> collection, long timeout, TimeUnit unit) {
        throw new UnsupportedOperationException();
    }

    public final void execute(Runnable command) {
        this.f479a.execute(new C0250c(command));
    }

    public final String toString() {
        BlockingQueue<Runnable> queue = this.f479a.getQueue();
        String str = "ProtectedExecutorService(" + this.f480b + ") size = " + queue.size() + IOUtils.LINE_SEPARATOR_UNIX;
        for (Runnable obj : queue) {
            str = str + obj.toString() + IOUtils.LINE_SEPARATOR_UNIX;
        }
        return str;
    }
}
