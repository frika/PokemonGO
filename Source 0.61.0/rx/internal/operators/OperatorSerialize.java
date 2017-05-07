package rx.internal.operators;

import rx.Observable.Operator;
import rx.Subscriber;
import rx.observers.SerializedSubscriber;

public final class OperatorSerialize<T> implements Operator<T, T> {

    /* renamed from: rx.internal.operators.OperatorSerialize.1 */
    class C14881 extends Subscriber<T> {
        final /* synthetic */ Subscriber val$s;

        C14881(Subscriber x0, Subscriber subscriber) {
            this.val$s = subscriber;
            super(x0);
        }

        public void onCompleted() {
            this.val$s.onCompleted();
        }

        public void onError(Throwable e) {
            this.val$s.onError(e);
        }

        public void onNext(T t) {
            this.val$s.onNext(t);
        }
    }

    private static final class Holder {
        static final OperatorSerialize<Object> INSTANCE;

        private Holder() {
        }

        static {
            INSTANCE = new OperatorSerialize();
        }
    }

    public static <T> OperatorSerialize<T> instance() {
        return Holder.INSTANCE;
    }

    OperatorSerialize() {
    }

    public Subscriber<? super T> call(Subscriber<? super T> s) {
        return new SerializedSubscriber(new C14881(s, s));
    }
}
