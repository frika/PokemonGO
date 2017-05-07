package com.upsight.android.internal.persistence.subscription;

import com.squareup.otto.Bus;
import com.upsight.android.internal.persistence.subscription.DataStoreEvent.Action;
import com.upsight.android.persistence.UpsightSubscription;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public class Subscriptions {

    /* renamed from: com.upsight.android.internal.persistence.subscription.Subscriptions.1 */
    static class C11301 implements Action1<T> {
        final /* synthetic */ Bus val$bus;
        final /* synthetic */ String val$type;

        C11301(Bus bus, String str) {
            this.val$bus = bus;
            this.val$type = str;
        }

        public void call(T t) {
            this.val$bus.post(new DataStoreEvent(Action.Created, this.val$type, t));
        }
    }

    /* renamed from: com.upsight.android.internal.persistence.subscription.Subscriptions.2 */
    static class C11312 implements Action1<T> {
        final /* synthetic */ Bus val$bus;
        final /* synthetic */ String val$type;

        C11312(Bus bus, String str) {
            this.val$bus = bus;
            this.val$type = str;
        }

        public void call(T t) {
            this.val$bus.post(new DataStoreEvent(Action.Updated, this.val$type, t));
        }
    }

    /* renamed from: com.upsight.android.internal.persistence.subscription.Subscriptions.3 */
    static class C11323 implements Action1<T> {
        final /* synthetic */ Bus val$bus;
        final /* synthetic */ String val$type;

        C11323(Bus bus, String str) {
            this.val$bus = bus;
            this.val$type = str;
        }

        public void call(T t) {
            this.val$bus.post(new DataStoreEvent(Action.Removed, this.val$type, t));
        }
    }

    public static <T> Action1<T> publishCreated(Bus bus, String type) {
        return new C11301(bus, type);
    }

    public static <T> Action1<T> publishUpdated(Bus bus, String type) {
        return new C11312(bus, type);
    }

    public static <T> Action1<T> publishRemoved(Bus bus, String type) {
        return new C11323(bus, type);
    }

    public static Observable<DataStoreEvent> toObservable(Bus bus) {
        return Observable.create(new OnSubscribeBus(bus)).onBackpressureBuffer();
    }

    public static UpsightSubscription from(Subscription subscription) {
        return new SubscriptionAdapter(subscription);
    }

    public static AnnotatedSubscriber create(Object target) {
        SubscriptionHandlerVisitor visitor = new SubscriptionHandlerVisitor(target);
        new ClassSubscriptionReader(target.getClass()).accept(visitor);
        return new AnnotatedSubscriber(visitor.getHandlers());
    }

    private Subscriptions() {
    }
}
