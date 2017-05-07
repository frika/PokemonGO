package rx.internal.operators;

import rx.Observable;
import rx.Observable.Operator;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.internal.producers.ProducerArbiter;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.SerialSubscription;

public final class OperatorOnErrorResumeNextViaFunction<T> implements Operator<T, T> {
    final Func1<Throwable, ? extends Observable<? extends T>> resumeFunction;

    /* renamed from: rx.internal.operators.OperatorOnErrorResumeNextViaFunction.1 */
    static class C14601 implements Func1<Throwable, Observable<? extends T>> {
        final /* synthetic */ Func1 val$resumeFunction;

        C14601(Func1 func1) {
            this.val$resumeFunction = func1;
        }

        public Observable<? extends T> call(Throwable t) {
            return Observable.just(this.val$resumeFunction.call(t));
        }
    }

    /* renamed from: rx.internal.operators.OperatorOnErrorResumeNextViaFunction.2 */
    static class C14612 implements Func1<Throwable, Observable<? extends T>> {
        final /* synthetic */ Observable val$other;

        C14612(Observable observable) {
            this.val$other = observable;
        }

        public Observable<? extends T> call(Throwable t) {
            return this.val$other;
        }
    }

    /* renamed from: rx.internal.operators.OperatorOnErrorResumeNextViaFunction.3 */
    static class C14623 implements Func1<Throwable, Observable<? extends T>> {
        final /* synthetic */ Observable val$other;

        C14623(Observable observable) {
            this.val$other = observable;
        }

        public Observable<? extends T> call(Throwable t) {
            if (t instanceof Exception) {
                return this.val$other;
            }
            return Observable.error(t);
        }
    }

    /* renamed from: rx.internal.operators.OperatorOnErrorResumeNextViaFunction.4 */
    class C14644 extends Subscriber<T> {
        private boolean done;
        long produced;
        final /* synthetic */ Subscriber val$child;
        final /* synthetic */ ProducerArbiter val$pa;
        final /* synthetic */ SerialSubscription val$ssub;

        /* renamed from: rx.internal.operators.OperatorOnErrorResumeNextViaFunction.4.1 */
        class C14631 extends Subscriber<T> {
            C14631() {
            }

            public void onNext(T t) {
                C14644.this.val$child.onNext(t);
            }

            public void onError(Throwable e) {
                C14644.this.val$child.onError(e);
            }

            public void onCompleted() {
                C14644.this.val$child.onCompleted();
            }

            public void setProducer(Producer producer) {
                C14644.this.val$pa.setProducer(producer);
            }
        }

        C14644(Subscriber subscriber, ProducerArbiter producerArbiter, SerialSubscription serialSubscription) {
            this.val$child = subscriber;
            this.val$pa = producerArbiter;
            this.val$ssub = serialSubscription;
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                this.val$child.onCompleted();
            }
        }

        public void onError(Throwable e) {
            if (this.done) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                return;
            }
            this.done = true;
            try {
                unsubscribe();
                Subscriber<T> next = new C14631();
                this.val$ssub.set(next);
                long p = this.produced;
                if (p != 0) {
                    this.val$pa.produced(p);
                }
                ((Observable) OperatorOnErrorResumeNextViaFunction.this.resumeFunction.call(e)).unsafeSubscribe(next);
            } catch (Throwable e2) {
                Exceptions.throwOrReport(e2, this.val$child);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                this.produced++;
                this.val$child.onNext(t);
            }
        }

        public void setProducer(Producer producer) {
            this.val$pa.setProducer(producer);
        }
    }

    public static <T> OperatorOnErrorResumeNextViaFunction<T> withSingle(Func1<Throwable, ? extends T> resumeFunction) {
        return new OperatorOnErrorResumeNextViaFunction(new C14601(resumeFunction));
    }

    public static <T> OperatorOnErrorResumeNextViaFunction<T> withOther(Observable<? extends T> other) {
        return new OperatorOnErrorResumeNextViaFunction(new C14612(other));
    }

    public static <T> OperatorOnErrorResumeNextViaFunction<T> withException(Observable<? extends T> other) {
        return new OperatorOnErrorResumeNextViaFunction(new C14623(other));
    }

    public OperatorOnErrorResumeNextViaFunction(Func1<Throwable, ? extends Observable<? extends T>> f) {
        this.resumeFunction = f;
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        ProducerArbiter pa = new ProducerArbiter();
        SerialSubscription ssub = new SerialSubscription();
        Subscriber<T> parent = new C14644(child, pa, ssub);
        ssub.set(parent);
        child.add(ssub);
        child.setProducer(pa);
        return parent;
    }
}
