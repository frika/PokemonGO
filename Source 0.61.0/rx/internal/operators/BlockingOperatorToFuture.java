package rx.internal.operators;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

public final class BlockingOperatorToFuture {

    /* renamed from: rx.internal.operators.BlockingOperatorToFuture.1 */
    static class C13761 extends Subscriber<T> {
        final /* synthetic */ AtomicReference val$error;
        final /* synthetic */ CountDownLatch val$finished;
        final /* synthetic */ AtomicReference val$value;

        C13761(CountDownLatch countDownLatch, AtomicReference atomicReference, AtomicReference atomicReference2) {
            this.val$finished = countDownLatch;
            this.val$error = atomicReference;
            this.val$value = atomicReference2;
        }

        public void onCompleted() {
            this.val$finished.countDown();
        }

        public void onError(Throwable e) {
            this.val$error.compareAndSet(null, e);
            this.val$finished.countDown();
        }

        public void onNext(T v) {
            this.val$value.set(v);
        }
    }

    /* renamed from: rx.internal.operators.BlockingOperatorToFuture.2 */
    static class C13772 implements Future<T> {
        private volatile boolean cancelled;
        final /* synthetic */ AtomicReference val$error;
        final /* synthetic */ CountDownLatch val$finished;
        final /* synthetic */ Subscription val$s;
        final /* synthetic */ AtomicReference val$value;

        C13772(CountDownLatch countDownLatch, Subscription subscription, AtomicReference atomicReference, AtomicReference atomicReference2) {
            this.val$finished = countDownLatch;
            this.val$s = subscription;
            this.val$error = atomicReference;
            this.val$value = atomicReference2;
            this.cancelled = false;
        }

        public boolean cancel(boolean mayInterruptIfRunning) {
            if (this.val$finished.getCount() <= 0) {
                return false;
            }
            this.cancelled = true;
            this.val$s.unsubscribe();
            this.val$finished.countDown();
            return true;
        }

        public boolean isCancelled() {
            return this.cancelled;
        }

        public boolean isDone() {
            return this.val$finished.getCount() == 0;
        }

        public T get() throws InterruptedException, ExecutionException {
            this.val$finished.await();
            return getValue();
        }

        public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            if (this.val$finished.await(timeout, unit)) {
                return getValue();
            }
            throw new TimeoutException("Timed out after " + unit.toMillis(timeout) + "ms waiting for underlying Observable.");
        }

        private T getValue() throws ExecutionException {
            Throwable throwable = (Throwable) this.val$error.get();
            if (throwable != null) {
                throw new ExecutionException("Observable onError", throwable);
            } else if (!this.cancelled) {
                return this.val$value.get();
            } else {
                throw new CancellationException("Subscription unsubscribed");
            }
        }
    }

    private BlockingOperatorToFuture() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Future<T> toFuture(Observable<? extends T> that) {
        CountDownLatch finished = new CountDownLatch(1);
        AtomicReference<T> value = new AtomicReference();
        AtomicReference<Throwable> error = new AtomicReference();
        return new C13772(finished, that.single().subscribe(new C13761(finished, error, value)), error, value);
    }
}
