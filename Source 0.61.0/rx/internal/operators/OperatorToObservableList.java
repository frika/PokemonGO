package rx.internal.operators;

import com.google.android.exoplayer.extractor.ts.PtsTimestampAdjuster;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import rx.Observable.Operator;
import rx.Observer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.internal.producers.SingleDelayedProducer;

public final class OperatorToObservableList<T> implements Operator<List<T>, T> {

    /* renamed from: rx.internal.operators.OperatorToObservableList.1 */
    class C15291 extends Subscriber<T> {
        boolean completed;
        List<T> list;
        final /* synthetic */ Subscriber val$o;
        final /* synthetic */ SingleDelayedProducer val$producer;

        C15291(SingleDelayedProducer singleDelayedProducer, Subscriber subscriber) {
            this.val$producer = singleDelayedProducer;
            this.val$o = subscriber;
            this.completed = false;
            this.list = new LinkedList();
        }

        public void onStart() {
            request(PtsTimestampAdjuster.DO_NOT_OFFSET);
        }

        public void onCompleted() {
            if (!this.completed) {
                this.completed = true;
                try {
                    List<T> result = new ArrayList(this.list);
                    this.list = null;
                    this.val$producer.setValue(result);
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, (Observer) this);
                }
            }
        }

        public void onError(Throwable e) {
            this.val$o.onError(e);
        }

        public void onNext(T value) {
            if (!this.completed) {
                this.list.add(value);
            }
        }
    }

    private static final class Holder {
        static final OperatorToObservableList<Object> INSTANCE;

        private Holder() {
        }

        static {
            INSTANCE = new OperatorToObservableList();
        }
    }

    public static <T> OperatorToObservableList<T> instance() {
        return Holder.INSTANCE;
    }

    OperatorToObservableList() {
    }

    public Subscriber<? super T> call(Subscriber<? super List<T>> o) {
        SingleDelayedProducer<List<T>> producer = new SingleDelayedProducer(o);
        Subscriber<T> result = new C15291(producer, o);
        o.add(result);
        o.setProducer(producer);
        return result;
    }
}