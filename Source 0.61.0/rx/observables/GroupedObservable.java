package rx.observables;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

public class GroupedObservable<K, T> extends Observable<T> {
    private final K key;

    /* renamed from: rx.observables.GroupedObservable.1 */
    static class C15881 implements OnSubscribe<T> {
        final /* synthetic */ Observable val$o;

        C15881(Observable observable) {
            this.val$o = observable;
        }

        public void call(Subscriber<? super T> s) {
            this.val$o.unsafeSubscribe(s);
        }
    }

    public static <K, T> GroupedObservable<K, T> from(K key, Observable<T> o) {
        return new GroupedObservable(key, new C15881(o));
    }

    public static <K, T> GroupedObservable<K, T> create(K key, OnSubscribe<T> f) {
        return new GroupedObservable(key, f);
    }

    protected GroupedObservable(K key, OnSubscribe<T> onSubscribe) {
        super(onSubscribe);
        this.key = key;
    }

    public K getKey() {
        return this.key;
    }
}
