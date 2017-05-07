package rx.internal.operators;

import com.google.android.exoplayer.extractor.ts.PtsTimestampAdjuster;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action1;

public class OperatorOnBackpressureDrop<T> implements Operator<T, T> {
    final Action1<? super T> onDrop;

    /* renamed from: rx.internal.operators.OperatorOnBackpressureDrop.1 */
    class C14581 implements Producer {
        final /* synthetic */ AtomicLong val$requested;

        C14581(AtomicLong atomicLong) {
            this.val$requested = atomicLong;
        }

        public void request(long n) {
            BackpressureUtils.getAndAddRequest(this.val$requested, n);
        }
    }

    /* renamed from: rx.internal.operators.OperatorOnBackpressureDrop.2 */
    class C14592 extends Subscriber<T> {
        final /* synthetic */ Subscriber val$child;
        final /* synthetic */ AtomicLong val$requested;

        C14592(Subscriber x0, Subscriber subscriber, AtomicLong atomicLong) {
            this.val$child = subscriber;
            this.val$requested = atomicLong;
            super(x0);
        }

        public void onStart() {
            request(PtsTimestampAdjuster.DO_NOT_OFFSET);
        }

        public void onCompleted() {
            this.val$child.onCompleted();
        }

        public void onError(Throwable e) {
            this.val$child.onError(e);
        }

        public void onNext(T t) {
            if (this.val$requested.get() > 0) {
                this.val$child.onNext(t);
                this.val$requested.decrementAndGet();
            } else if (OperatorOnBackpressureDrop.this.onDrop != null) {
                try {
                    OperatorOnBackpressureDrop.this.onDrop.call(t);
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, this.val$child, t);
                }
            }
        }
    }

    private static final class Holder {
        static final OperatorOnBackpressureDrop<Object> INSTANCE;

        private Holder() {
        }

        static {
            INSTANCE = new OperatorOnBackpressureDrop();
        }
    }

    public static <T> OperatorOnBackpressureDrop<T> instance() {
        return Holder.INSTANCE;
    }

    OperatorOnBackpressureDrop() {
        this(null);
    }

    public OperatorOnBackpressureDrop(Action1<? super T> onDrop) {
        this.onDrop = onDrop;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        AtomicLong requested = new AtomicLong();
        child.setProducer(new C14581(requested));
        return new C14592(child, child, requested);
    }
}
