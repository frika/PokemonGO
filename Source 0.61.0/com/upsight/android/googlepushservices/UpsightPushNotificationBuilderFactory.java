package com.upsight.android.googlepushservices;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v4.app.NotificationCompat.BigPictureStyle;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationCompat.Style;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ImageView.ScaleType;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NoCache;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

public interface UpsightPushNotificationBuilderFactory {

    public static class Default implements UpsightPushNotificationBuilderFactory {
        public static final float HTTP_REQUEST_BACKOFF_MULT = 2.0f;
        public static final int HTTP_REQUEST_MAX_RETRIES = 3;
        public static final int HTTP_REQUEST_TIMEOUT_MS = 5000;
        private RequestQueue mRequestQueue;

        /* renamed from: com.upsight.android.googlepushservices.UpsightPushNotificationBuilderFactory.Default.1 */
        class C11031 implements OnSubscribe<Bitmap> {
            final /* synthetic */ String val$imageUrl;

            /* renamed from: com.upsight.android.googlepushservices.UpsightPushNotificationBuilderFactory.Default.1.1 */
            class C11011 implements Listener<Bitmap> {
                final /* synthetic */ Subscriber val$subscriber;

                C11011(Subscriber subscriber) {
                    this.val$subscriber = subscriber;
                }

                public void onResponse(Bitmap response) {
                    this.val$subscriber.onNext(response);
                    this.val$subscriber.onCompleted();
                }
            }

            /* renamed from: com.upsight.android.googlepushservices.UpsightPushNotificationBuilderFactory.Default.1.2 */
            class C11022 implements ErrorListener {
                final /* synthetic */ Subscriber val$subscriber;

                C11022(Subscriber subscriber) {
                    this.val$subscriber = subscriber;
                }

                public void onErrorResponse(VolleyError error) {
                    this.val$subscriber.onError(error);
                }
            }

            C11031(String str) {
                this.val$imageUrl = str;
            }

            public void call(Subscriber<? super Bitmap> subscriber) {
                Default.this.mRequestQueue.add(new ImageRequest(this.val$imageUrl, new C11011(subscriber), 0, 0, ScaleType.CENTER_INSIDE, Config.ARGB_8888, new C11022(subscriber)).setRetryPolicy(new DefaultRetryPolicy(Default.HTTP_REQUEST_TIMEOUT_MS, Default.HTTP_REQUEST_MAX_RETRIES, Default.HTTP_REQUEST_BACKOFF_MULT)));
            }
        }

        public Default() {
            this.mRequestQueue = new RequestQueue(new NoCache(), new BasicNetwork(new HurlStack()));
            this.mRequestQueue.start();
        }

        public Builder getNotificationBuilder(UpsightContext upsight, String title, String message, String imageUrl) {
            Style style;
            Bitmap picture = null;
            if (!TextUtils.isEmpty(imageUrl)) {
                if (isImageUrlValid(imageUrl)) {
                    try {
                        picture = (Bitmap) getImageObservable(imageUrl).toBlocking().first();
                    } catch (Throwable t) {
                        upsight.getLogger().m576e(Upsight.LOG_TAG, "Failed to download notification picture, displaying notification without", t);
                    }
                } else {
                    upsight.getLogger().m576e(Upsight.LOG_TAG, "Malformed notification picture URL, displaying notification without", new Object[0]);
                }
            }
            if (picture != null) {
                style = new BigPictureStyle().bigPicture(picture).setSummaryText(message);
            } else {
                style = new BigTextStyle().bigText(message);
            }
            return new Builder(upsight.getApplicationContext()).setSmallIcon(upsight.getApplicationInfo().icon).setStyle(style).setContentTitle(title).setContentText(message);
        }

        protected boolean isImageUrlValid(String imageUrl) {
            return Patterns.WEB_URL.matcher(imageUrl).matches();
        }

        protected Observable<Bitmap> getImageObservable(String imageUrl) {
            return Observable.create(new C11031(imageUrl));
        }
    }

    Builder getNotificationBuilder(UpsightContext upsightContext, String str, String str2, String str3);
}
