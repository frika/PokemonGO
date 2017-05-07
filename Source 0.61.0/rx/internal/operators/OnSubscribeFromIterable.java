package rx.internal.operators;

import com.google.android.exoplayer.extractor.ts.PtsTimestampAdjuster;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable.OnSubscribe;
import rx.Producer;
import rx.Subscriber;

public final class OnSubscribeFromIterable<T> implements OnSubscribe<T> {
    final Iterable<? extends T> is;

    private static final class IterableProducer<T> extends AtomicLong implements Producer {
        private static final long serialVersionUID = -8730475647105475802L;
        private final Iterator<? extends T> it;
        private final Subscriber<? super T> f757o;

        IterableProducer(Subscriber<? super T> o, Iterator<? extends T> it) {
            this.f757o = o;
            this.it = it;
        }

        public void request(long n) {
            if (get() != PtsTimestampAdjuster.DO_NOT_OFFSET) {
                if (n == PtsTimestampAdjuster.DO_NOT_OFFSET && compareAndSet(0, PtsTimestampAdjuster.DO_NOT_OFFSET)) {
                    fastpath();
                } else if (n > 0 && BackpressureUtils.getAndAddRequest(this, n) == 0) {
                    slowpath(n);
                }
            }
        }

        void slowpath(long n) {
            Subscriber<? super T> o = this.f757o;
            Iterator<? extends T> it = this.it;
            long r = n;
            do {
                long numToEmit = r;
                while (!o.isUnsubscribed()) {
                    if (it.hasNext()) {
                        numToEmit--;
                        if (numToEmit >= 0) {
                            o.onNext(it.next());
                        } else {
                            r = addAndGet(-r);
                        }
                    } else if (!o.isUnsubscribed()) {
                        o.onCompleted();
                        return;
                    } else {
                        return;
                    }
                }
                return;
            } while (r != 0);
        }

        void fastpath() {
            Subscriber<? super T> o = this.f757o;
            Iterator<? extends T> it = this.it;
            while (!o.isUnsubscribed()) {
                if (it.hasNext()) {
                    o.onNext(it.next());
                } else if (!o.isUnsubscribed()) {
                    o.onCompleted();
                    return;
                } else {
                    return;
                }
            }
        }
    }

    public OnSubscribeFromIterable(Iterable<? extends T> iterable) {
        if (iterable == null) {
            throw new NullPointerException("iterable must not be null");
        }
        this.is = iterable;
    }

    public void call(Subscriber<? super T> o) {
        Iterator<? extends T> it = this.is.iterator();
        if (it.hasNext() || o.isUnsubscribed()) {
            o.setProducer(new IterableProducer(o, it));
        } else {
            o.onCompleted();
        }
    }
}
