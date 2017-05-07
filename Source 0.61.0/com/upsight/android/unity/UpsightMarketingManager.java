package com.upsight.android.unity;

import android.support.annotation.NonNull;
import android.util.Log;
import com.upsight.android.UpsightContext;
import com.upsight.android.marketing.UpsightBillboard;
import com.upsight.android.marketing.UpsightMarketingContentStore;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UpsightMarketingManager implements IUpsightExtensionManager {
    protected static final String TAG = "Upsight-UnityMarketing";
    @NonNull
    private Map<String, BillboardInfo> mBillboardMap;
    @NonNull
    private Set<String> mPreparedBillboards;
    private UpsightContext mUpsight;

    /* renamed from: com.upsight.android.unity.UpsightMarketingManager.1 */
    class C11931 implements Runnable {
        final /* synthetic */ String val$scope;

        C11931(String str) {
            this.val$scope = str;
        }

        public void run() {
            if (!UpsightMarketingManager.this.mBillboardMap.containsKey(this.val$scope) && !UpsightMarketingManager.this.mPreparedBillboards.contains(this.val$scope)) {
                BillboardHandler handler = new BillboardHandler(UnityBridge.getActivity());
                UpsightMarketingManager.this.mBillboardMap.put(this.val$scope, new BillboardInfo(UpsightBillboard.create(UpsightMarketingManager.this.mUpsight, this.val$scope, handler), handler));
            }
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightMarketingManager.2 */
    class C11942 implements Runnable {
        final /* synthetic */ String val$scope;

        C11942(String str) {
            this.val$scope = str;
        }

        public void run() {
            Log.i(UpsightMarketingManager.TAG, "Destroying billboard for scope: " + this.val$scope);
            ((BillboardInfo) UpsightMarketingManager.this.mBillboardMap.remove(this.val$scope)).billboard.destroy();
            UpsightMarketingManager.this.mPreparedBillboards.remove(this.val$scope);
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightMarketingManager.3 */
    class C11953 implements Runnable {
        final /* synthetic */ String val$currentScope;

        C11953(String str) {
            this.val$currentScope = str;
        }

        public void run() {
            if (this.val$currentScope != null) {
                UpsightMarketingManager.this.mBillboardMap.remove(this.val$currentScope);
            }
            UpsightMarketingManager.this.mPreparedBillboards.addAll(UpsightMarketingManager.this.mBillboardMap.keySet());
            for (String scope : UpsightMarketingManager.this.mBillboardMap.keySet()) {
                ((BillboardInfo) UpsightMarketingManager.this.mBillboardMap.get(scope)).billboard.destroy();
            }
            UpsightMarketingManager.this.mBillboardMap.clear();
        }
    }

    /* renamed from: com.upsight.android.unity.UpsightMarketingManager.4 */
    class C11964 implements Runnable {
        C11964() {
        }

        public void run() {
            for (String scope : UpsightMarketingManager.this.mPreparedBillboards) {
                BillboardHandler handler = new BillboardHandler(UnityBridge.getActivity());
                UpsightMarketingManager.this.mBillboardMap.put(scope, new BillboardInfo(UpsightBillboard.create(UpsightMarketingManager.this.mUpsight, scope, handler), handler));
            }
            UpsightMarketingManager.this.mPreparedBillboards.clear();
        }
    }

    private static class BillboardInfo {
        @NonNull
        public final UpsightBillboard billboard;
        @NonNull
        public final BillboardHandler handler;

        public BillboardInfo(@NonNull UpsightBillboard billboard, @NonNull BillboardHandler handler) {
            this.billboard = billboard;
            this.handler = handler;
        }
    }

    public UpsightMarketingManager() {
        this.mPreparedBillboards = new HashSet();
        this.mBillboardMap = new HashMap();
    }

    public void init(UpsightContext context) {
        this.mUpsight = context;
    }

    public boolean isContentReadyForBillboardWithScope(@NonNull String scope) {
        boolean z = false;
        if (this.mUpsight != null) {
            try {
                z = UpsightMarketingContentStore.isContentReady(this.mUpsight, scope);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return z;
    }

    public void prepareBillboard(@NonNull String scope) {
        if (this.mUpsight != null) {
            UnityBridge.runSafelyOnUiThread(new C11931(scope));
        }
    }

    public void destroyBillboard(@NonNull String scope) {
        if (this.mUpsight != null) {
            UnityBridge.runSafelyOnUiThread(new C11942(scope));
        }
    }

    public void onApplicationPaused() {
        if (this.mUpsight != null) {
            UnityBridge.runSafelyOnUiThread(new C11953(BillboardHandler.getCurrentScope()));
        }
    }

    public void onApplicationResumed() {
        if (this.mUpsight != null) {
            UnityBridge.runSafelyOnUiThread(new C11964());
        }
    }
}
