package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable;
import rx.Observable.Operator;
import rx.Subscriber;
import rx.observers.SerializedSubscriber;

public final class OperatorSkipUntil<T, U> implements Operator<T, T> {
    final Observable<U> other;

    /* renamed from: rx.internal.operators.OperatorSkipUntil.1 */
    class C14951 extends Subscriber<U> {
        final /* synthetic */ AtomicBoolean val$gate;
        final /* synthetic */ SerializedSubscriber val$s;

        C14951(AtomicBoolean atomicBoolean, SerializedSubscriber serializedSubscriber) {
            this.val$gate = atomicBoolean;
            this.val$s = serializedSubscriber;
        }

        public void onNext(U u) {
            this.val$gate.set(true);
            unsubscribe();
        }

        public void onError(Throwable e) {
            this.val$s.onError(e);
            this.val$s.unsubscribe();
        }

        public void onCompleted() {
            unsubscribe();
        }
    }

    /* renamed from: rx.internal.operators.OperatorSkipUntil.2 */
    class C14962 extends Subscriber<T> {
        final /* synthetic */ AtomicBoolean val$gate;
        final /* synthetic */ SerializedSubscriber val$s;

        C14962(Subscriber x0, AtomicBoolean atomicBoolean, SerializedSubscriber serializedSubscriber) {
            this.val$gate = atomicBoolean;
            this.val$s = serializedSubscriber;
            super(x0);
        }

        public void onNext(T t) {
            if (this.val$gate.get()) {
                this.val$s.onNext(t);
            } else {
                request(1);
            }
        }

        public void onError(Throwable e) {
            this.val$s.onError(e);
            unsubscribe();
        }

        public void onCompleted() {
            this.val$s.onCompleted();
            unsubscribe();
        }
    }

    public OperatorSkipUntil(Observable<U> other) {
        this.other = other;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        SerializedSubscriber<T> s = new SerializedSubscriber(child);
        AtomicBoolean gate = new AtomicBoolean();
        Subscriber<U> u = new C14951(gate, s);
        child.add(u);
        this.other.unsafeSubscribe(u);
        return new C14962(child, gate, s);
    }
}
