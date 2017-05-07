package rx.internal.operators;

import rx.Observable.Operator;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

public final class OperatorFilter<T> implements Operator<T, T> {
    final Func1<? super T, Boolean> predicate;

    /* renamed from: rx.internal.operators.OperatorFilter.1 */
    class C14451 extends Subscriber<T> {
        final /* synthetic */ Subscriber val$child;

        C14451(Subscriber x0, Subscriber subscriber) {
            this.val$child = subscriber;
            super(x0);
        }

        public void onCompleted() {
            this.val$child.onCompleted();
        }

        public void onError(Throwable e) {
            this.val$child.onError(e);
        }

        public void onNext(T t) {
            try {
                if (((Boolean) OperatorFilter.this.predicate.call(t)).booleanValue()) {
                    this.val$child.onNext(t);
                } else {
                    request(1);
                }
            } catch (Throwable e) {
                Exceptions.throwOrReport(e, this.val$child, t);
            }
        }
    }

    public OperatorFilter(Func1<? super T, Boolean> predicate) {
        this.predicate = predicate;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        return new C14451(child, child);
    }
}
