package com.upsight.android.unity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.upsight.android.Upsight;
import com.upsight.android.UpsightContext;
import com.upsight.android.UpsightException;
import com.upsight.android.analytics.UpsightGooglePlayHelper;
import com.upsight.android.analytics.UpsightLifeCycleTracker;
import com.upsight.android.analytics.UpsightLifeCycleTracker.ActivityState;
import com.upsight.android.analytics.event.UpsightCustomEvent;
import com.upsight.android.analytics.event.UpsightCustomEvent.Builder;
import com.upsight.android.analytics.event.UpsightPublisherData;
import com.upsight.android.analytics.event.install.UpsightInstallAttributionEvent;
import com.upsight.android.analytics.event.milestone.UpsightMilestoneEvent;
import com.upsight.android.analytics.event.monetization.UpsightMonetizationEvent;
import com.upsight.android.analytics.provider.UpsightLocationTracker;
import com.upsight.android.analytics.provider.UpsightLocationTracker.Data;
import com.upsight.android.analytics.provider.UpsightOptOutStatus;
import com.upsight.android.analytics.provider.UpsightUserAttributes;
import com.upsight.android.analytics.session.UpsightSessionInfo;
import com.upsight.android.logger.UpsightLogger.Level;
import com.upsight.android.managedvariables.type.UpsightManagedBoolean;
import com.upsight.android.managedvariables.type.UpsightManagedFloat;
import com.upsight.android.managedvariables.type.UpsightManagedInt;
import com.upsight.android.managedvariables.type.UpsightManagedString;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

public class UpsightPlugin {
    protected static final String TAG = "Upsight-Unity";
    @NonNull
    private Set<IUpsightExtensionManager> mExtensions;
    protected UpsightContext mUpsight;

    /* renamed from: com.upsight.android.unity.UpsightPlugin.10 */
    class AnonymousClass10 implements Runnable {
        final /* synthetic */ String val$eventName;
        final /* synthetic */ String val$properties;

        AnonymousClass10(String str, String str2) {
            this.val$eventName = str;
            this.val$properties = str2;
        }

        public void run() {
            Builder builder = UpsightCustomEvent.createBuilder(this.val$eventName);
            builder.put(UpsightPlugin.publisherDataFromJsonString(this.val$properties));
            builder.record(UpsightPlugin.this.mUpsight);
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.11 */
    class AnonymousClass11 implements Runnable {
        final /* synthetic */ String val$properties;
        final /* synthetic */ String val$scope;

        AnonymousClass11(String str, String str2) {
            this.val$scope = str;
            this.val$properties = str2;
        }

        public void run() {
            UpsightMilestoneEvent.Builder builder = UpsightMilestoneEvent.createBuilder(this.val$scope);
            builder.put(UpsightPlugin.publisherDataFromJsonString(this.val$properties));
            builder.record(UpsightPlugin.this.mUpsight);
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.12 */
    class AnonymousClass12 implements Runnable {
        final /* synthetic */ String val$currency;
        final /* synthetic */ double val$price;
        final /* synthetic */ String val$product;
        final /* synthetic */ String val$properties;
        final /* synthetic */ int val$quantity;
        final /* synthetic */ String val$resolution;
        final /* synthetic */ double val$totalPrice;

        AnonymousClass12(double d, String str, String str2, String str3, double d2, String str4, int i) {
            this.val$totalPrice = d;
            this.val$currency = str;
            this.val$properties = str2;
            this.val$product = str3;
            this.val$price = d2;
            this.val$resolution = str4;
            this.val$quantity = i;
        }

        public void run() {
            UpsightMonetizationEvent.Builder builder = UpsightMonetizationEvent.createBuilder(Double.valueOf(this.val$totalPrice), this.val$currency);
            builder.put(UpsightPlugin.publisherDataFromJsonString(this.val$properties));
            if (this.val$product != null) {
                builder.setProduct(this.val$product);
            }
            if (this.val$price >= 0.0d) {
                builder.setPrice(Double.valueOf(this.val$price));
            }
            if (this.val$resolution != null) {
                builder.setResolution(this.val$resolution);
            }
            if (this.val$quantity > 0) {
                builder.setQuantity(Integer.valueOf(this.val$quantity));
            }
            builder.record(UpsightPlugin.this.mUpsight);
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.13 */
    class AnonymousClass13 implements Runnable {
        final /* synthetic */ String val$currency;
        final /* synthetic */ String val$inAppDataSignature;
        final /* synthetic */ String val$inAppPurchaseData;
        final /* synthetic */ double val$price;
        final /* synthetic */ String val$product;
        final /* synthetic */ String val$properties;
        final /* synthetic */ int val$quantity;
        final /* synthetic */ int val$reponseCode;
        final /* synthetic */ double val$totalPrice;

        AnonymousClass13(String str, int i, String str2, String str3, int i2, String str4, double d, double d2, String str5) {
            this.val$properties = str;
            this.val$reponseCode = i;
            this.val$inAppPurchaseData = str2;
            this.val$inAppDataSignature = str3;
            this.val$quantity = i2;
            this.val$currency = str4;
            this.val$price = d;
            this.val$totalPrice = d2;
            this.val$product = str5;
        }

        public void run() {
            UpsightPublisherData.Builder builder = new UpsightPublisherData.Builder();
            builder.put(UpsightPlugin.publisherDataFromJsonString(this.val$properties));
            try {
                Intent responseData = new Intent();
                responseData.putExtra(UpsightGooglePlayHelper.PURCHASE_RESPONSE_CODE, this.val$reponseCode);
                responseData.putExtra(UpsightGooglePlayHelper.PURCHASE_INAPP_PURCHASE_DATA, this.val$inAppPurchaseData);
                responseData.putExtra(UpsightGooglePlayHelper.PURCHASE_INAPP_DATA_SIGNATURE, this.val$inAppDataSignature);
                UpsightGooglePlayHelper.trackPurchase(UpsightPlugin.this.mUpsight, this.val$quantity, this.val$currency, this.val$price, this.val$totalPrice, this.val$product, responseData, builder.build());
            } catch (UpsightException e) {
                Log.i(UpsightPlugin.TAG, "Failed to recordGooglePlayPurchase: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.14 */
    class AnonymousClass14 implements Runnable {
        final /* synthetic */ String val$campaign;
        final /* synthetic */ String val$creative;
        final /* synthetic */ String val$properties;
        final /* synthetic */ String val$source;

        AnonymousClass14(String str, String str2, String str3, String str4) {
            this.val$campaign = str;
            this.val$creative = str2;
            this.val$source = str3;
            this.val$properties = str4;
        }

        public void run() {
            UpsightInstallAttributionEvent.createBuilder().setAttributionCampaign(this.val$campaign).setAttributionCreative(this.val$creative).setAttributionSource(this.val$source).put(UpsightPlugin.publisherDataFromJsonString(this.val$properties)).record(UpsightPlugin.this.mUpsight);
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.1 */
    class C11971 implements Runnable {
        final /* synthetic */ Activity val$activity;

        C11971(Activity activity) {
            this.val$activity = activity;
        }

        public void run() {
            UpsightLifeCycleTracker.track(UpsightPlugin.this.mUpsight, this.val$activity, ActivityState.STARTED);
            Log.i(UpsightPlugin.TAG, "Upsight initialization finished");
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.2 */
    class C11982 implements Runnable {
        final /* synthetic */ double val$lat;
        final /* synthetic */ double val$lon;

        C11982(double d, double d2) {
            this.val$lat = d;
            this.val$lon = d2;
        }

        public void run() {
            UpsightLocationTracker.track(UpsightPlugin.this.mUpsight, Data.create(this.val$lat, this.val$lon));
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.3 */
    class C11993 implements Runnable {
        C11993() {
        }

        public void run() {
            UpsightLocationTracker.purge(UpsightPlugin.this.mUpsight);
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.4 */
    class C12004 implements Runnable {
        final /* synthetic */ String val$key;
        final /* synthetic */ String val$value;

        C12004(String str, String str2) {
            this.val$key = str;
            this.val$value = str2;
        }

        public void run() {
            UpsightUserAttributes.put(UpsightPlugin.this.mUpsight, this.val$key, this.val$value);
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.5 */
    class C12015 implements Runnable {
        final /* synthetic */ String val$key;
        final /* synthetic */ float val$value;

        C12015(String str, float f) {
            this.val$key = str;
            this.val$value = f;
        }

        public void run() {
            UpsightUserAttributes.put(UpsightPlugin.this.mUpsight, this.val$key, Float.valueOf(this.val$value));
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.6 */
    class C12026 implements Runnable {
        final /* synthetic */ String val$key;
        final /* synthetic */ int val$value;

        C12026(String str, int i) {
            this.val$key = str;
            this.val$value = i;
        }

        public void run() {
            UpsightUserAttributes.put(UpsightPlugin.this.mUpsight, this.val$key, Integer.valueOf(this.val$value));
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.7 */
    class C12037 implements Runnable {
        final /* synthetic */ String val$key;
        final /* synthetic */ boolean val$value;

        C12037(String str, boolean z) {
            this.val$key = str;
            this.val$value = z;
        }

        public void run() {
            UpsightUserAttributes.put(UpsightPlugin.this.mUpsight, this.val$key, Boolean.valueOf(this.val$value));
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.8 */
    class C12048 implements Runnable {
        final /* synthetic */ String val$key;
        final /* synthetic */ long val$value;

        C12048(long j, String str) {
            this.val$value = j;
            this.val$key = str;
        }

        public void run() {
            UpsightUserAttributes.put(UpsightPlugin.this.mUpsight, this.val$key, new Date(TimeUnit.MILLISECONDS.convert(this.val$value, TimeUnit.SECONDS)));
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightPlugin.9 */
    class C12059 implements Runnable {
        final /* synthetic */ String val$eventName;
        final /* synthetic */ String val$properties;

        C12059(String str, String str2) {
            this.val$eventName = str;
            this.val$properties = str2;
        }

        public void run() {
            Builder builder = UpsightCustomEvent.createBuilder(this.val$eventName);
            builder.put(UpsightPlugin.publisherDataFromJsonString(this.val$properties));
            builder.recordSessionless(UpsightPlugin.this.mUpsight);
        }
    }

    public static void setEnabled(boolean isEnabled) {
        Upsight.setEnabled(UnityBridge.getActivity(), isEnabled);
    }

    public static boolean isEnabled() {
        return Upsight.isEnabled(UnityBridge.getActivity());
    }

    public UpsightPlugin() {
        this.mExtensions = new HashSet(2);
        try {
            Activity activity = UnityBridge.getActivity();
            this.mUpsight = Upsight.createContext(activity);
            this.mUpsight.getLogger().setLogLevel(Upsight.LOG_TAG, EnumSet.of(Level.ERROR));
            UnityBridge.runSafelyOnUiThread(new C11971(activity));
        } catch (Exception e) {
            Log.e(TAG, "Critical Error: Exception thrown while initializing. Upsight will NOT work!", e);
            throw e;
        }
    }

    public void registerExtension(IUpsightExtensionManager extension) {
        if (this.mExtensions.add(extension)) {
            extension.init(this.mUpsight);
        }
    }

    @NonNull
    public String getAppToken() {
        return this.mUpsight.getApplicationToken();
    }

    @NonNull
    public String getPublicKey() {
        return this.mUpsight.getPublicKey();
    }

    @NonNull
    public String getSid() {
        return this.mUpsight.getSid();
    }

    public void setLoggerLevel(@NonNull String logLevel) {
        try {
            if (logLevel.toLowerCase().equals("verbose")) {
                Log.i(TAG, "enabling verbose logs");
                this.mUpsight.getLogger().setLogLevel(".*", EnumSet.allOf(Level.class));
                return;
            }
            this.mUpsight.getLogger().setLogLevel(Upsight.LOG_TAG, EnumSet.of(Level.valueOf(logLevel)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public String getPluginVersion() {
        return this.mUpsight.getSdkPlugin();
    }

    public boolean getOptOutStatus() {
        try {
            return UpsightOptOutStatus.get(this.mUpsight);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setOptOutStatus(boolean optOutStatus) {
        try {
            UpsightOptOutStatus.set(this.mUpsight, optOutStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setLocation(double lat, double lon) {
        UnityBridge.runSafelyOnUiThread(new C11982(lat, lon));
    }

    public void purgeLocation() {
        UnityBridge.runSafelyOnUiThread(new C11993());
    }

    public int getLatestSessionNumber() {
        try {
            return UpsightSessionInfo.getLatest(this.mUpsight).sessionNumber;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long getLatestSessionStartTimestamp() {
        try {
            return UpsightSessionInfo.getLatest(this.mUpsight).startTimestamp;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setUserAttributesString(@NonNull String key, @NonNull String value) {
        UnityBridge.runSafelyOnUiThread(new C12004(key, value));
    }

    public void setUserAttributesFloat(@NonNull String key, float value) {
        UnityBridge.runSafelyOnUiThread(new C12015(key, value));
    }

    public void setUserAttributesInt(@NonNull String key, int value) {
        UnityBridge.runSafelyOnUiThread(new C12026(key, value));
    }

    public void setUserAttributesBool(@NonNull String key, boolean value) {
        UnityBridge.runSafelyOnUiThread(new C12037(key, value));
    }

    public void setUserAttributesDatetime(@NonNull String key, long value) {
        UnityBridge.runSafelyOnUiThread(new C12048(value, key));
    }

    @Nullable
    public String getUserAttributesString(@NonNull String key) {
        try {
            return UpsightUserAttributes.getString(this.mUpsight, key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public float getUserAttributesFloat(@NonNull String key) {
        try {
            Float value = UpsightUserAttributes.getFloat(this.mUpsight, key);
            if (value != null) {
                return value.floatValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0f;
    }

    public int getUserAttributesInt(@NonNull String key) {
        try {
            Integer value = UpsightUserAttributes.getInteger(this.mUpsight, key);
            if (value != null) {
                return value.intValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean getUserAttributesBool(@NonNull String key) {
        try {
            Boolean value = UpsightUserAttributes.getBoolean(this.mUpsight, key);
            if (value != null) {
                return value.booleanValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public long getUserAttributesDatetime(@NonNull String key) {
        try {
            Date value = UpsightUserAttributes.getDatetime(this.mUpsight, key);
            if (value != null) {
                return TimeUnit.SECONDS.convert(value.getTime(), TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Nullable
    public String getManagedString(@NonNull String key) {
        try {
            UpsightManagedString managedString = UpsightManagedString.fetch(this.mUpsight, key);
            if (managedString != null) {
                return (String) managedString.get();
            }
            Log.e(TAG, "Unknown tag " + key + " for managed string, please check your UXM schema");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public float getManagedFloat(@NonNull String key) {
        try {
            UpsightManagedFloat managedFloat = UpsightManagedFloat.fetch(this.mUpsight, key);
            if (managedFloat != null) {
                return ((Float) managedFloat.get()).floatValue();
            }
            Log.e(TAG, "Unknown tag " + key + " for managed float, please check your UXM schema");
            return 0.0f;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    public int getManagedInt(@NonNull String key) {
        try {
            UpsightManagedInt managedInt = UpsightManagedInt.fetch(this.mUpsight, key);
            if (managedInt != null) {
                return ((Integer) managedInt.get()).intValue();
            }
            Log.e(TAG, "Unknown tag " + key + " for managed int, please check your UXM schema");
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean getManagedBool(@NonNull String key) {
        try {
            UpsightManagedBoolean managedBoolean = UpsightManagedBoolean.fetch(this.mUpsight, key);
            if (managedBoolean != null) {
                return ((Boolean) managedBoolean.get()).booleanValue();
            }
            Log.e(TAG, "Unknown tag " + key + " for managed bool, please check your UXM schema");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void recordSessionlessAnalyticsEvent(@NonNull String eventName, @NonNull String properties) {
        UnityBridge.runSafelyOnUiThread(new C12059(eventName, properties));
    }

    public void recordAnalyticsEvent(@NonNull String eventName, @NonNull String properties) {
        UnityBridge.runSafelyOnUiThread(new AnonymousClass10(eventName, properties));
    }

    public void recordMilestoneEvent(@NonNull String scope, @NonNull String properties) {
        UnityBridge.runSafelyOnUiThread(new AnonymousClass11(scope, properties));
    }

    public void recordMonetizationEvent(double totalPrice, @NonNull String currency, @Nullable String product, double price, @Nullable String resolution, int quantity, @Nullable String properties) {
        UnityBridge.runSafelyOnUiThread(new AnonymousClass12(totalPrice, currency, properties, product, price, resolution, quantity));
    }

    public void recordGooglePlayPurchase(int quantity, @NonNull String currency, double price, double totalPrice, @NonNull String product, int reponseCode, @NonNull String inAppPurchaseData, @NonNull String inAppDataSignature, @NonNull String properties) {
        UnityBridge.runSafelyOnUiThread(new AnonymousClass13(properties, reponseCode, inAppPurchaseData, inAppDataSignature, quantity, currency, price, totalPrice, product));
    }

    public void recordAttributionEvent(@Nullable String campaign, @Nullable String creative, @Nullable String source, @Nullable String properties) {
        UnityBridge.runSafelyOnUiThread(new AnonymousClass14(campaign, creative, source, properties));
    }

    @NonNull
    private static UpsightPublisherData publisherDataFromJsonString(@Nullable String json) {
        UpsightPublisherData.Builder pubData = new UpsightPublisherData.Builder();
        if (json != null && json.length() > 0) {
            try {
                JSONObject jObject = new JSONObject(json);
                Iterator<?> itr = jObject.keys();
                while (itr.hasNext()) {
                    String key = (String) itr.next();
                    try {
                        Object value = jObject.get(key);
                        if (value instanceof String) {
                            pubData.put(key, (String) value);
                        } else if (value instanceof Float) {
                            pubData.put(key, ((Float) value).floatValue());
                        } else if (value instanceof Double) {
                            pubData.put(key, ((Double) value).doubleValue());
                        } else if (value instanceof Long) {
                            pubData.put(key, ((Long) value).longValue());
                        } else if (value instanceof Boolean) {
                            pubData.put(key, ((Boolean) value).booleanValue());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        return pubData.build();
    }

    public void onApplicationPaused() {
        for (IUpsightExtensionManager extension : this.mExtensions) {
            extension.onApplicationPaused();
        }
    }

    public void onApplicationResumed() {
        for (IUpsightExtensionManager extension : this.mExtensions) {
            extension.onApplicationResumed();
        }
    }
}
