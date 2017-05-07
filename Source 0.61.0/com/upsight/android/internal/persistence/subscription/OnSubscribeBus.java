package com.upsight.android.internal.persistence.subscription;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

class OnSubscribeBus implements OnSubscribe<DataStoreEvent> {
    private final Bus mBus;

    /* renamed from: com.upsight.android.internal.persistence.subscription.OnSubscribeBus.1 */
    class C11291 implements Action0 {
        final /* synthetic */ BusAdapter val$adapter;

        C11291(BusAdapter busAdapter) {
            this.val$adapter = busAdapter;
        }

        public void call() {
            OnSubscribeBus.this.mBus.unregister(this.val$adapter);
        }
    }

    private static class BusAdapter {
        private final Subscriber<? super DataStoreEvent> mChild;

        private BusAdapter(Subscriber<? super DataStoreEvent> child) {
            this.mChild = child;
        }

        @Subscribe
        public void onPersistenceEvent(DataStoreEvent event) {
            if (!this.mChild.isUnsubscribed()) {
                this.mChild.onNext(event);
            }
        }
    }

    OnSubscribeBus(Bus bus) {
        this.mBus = bus;
    }

    public void call(Subscriber<? super DataStoreEvent> subscriber) {
        BusAdapter adapter = new BusAdapter(null);
        this.mBus.register(adapter);
        subscriber.add(Subscriptions.create(new C11291(adapter)));
    }
}
