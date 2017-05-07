package com.nianticlabs.nia.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.nianticlabs.nia.contextservice.ContextService;

public class NetworkConnectivity extends ContextService {
    private static final String TAG = "NetworkConnectivity";
    private ConnectivityManager connectivityManager;
    private boolean listening;
    private BroadcastReceiver networkChangeReceiver;

    /* renamed from: com.nianticlabs.nia.network.NetworkConnectivity.1 */
    class C09621 extends BroadcastReceiver {
        C09621() {
        }

        public void onReceive(Context context, Intent intent) {
            NetworkInfo info = NetworkConnectivity.this.connectivityManager.getActiveNetworkInfo();
            NetworkConnectivity.this.notifyNetworkStateChanged(info == null ? false : info.isConnected());
        }
    }

    private native void nativeNotifyNetworkStateChanged(boolean z);

    public NetworkConnectivity(Context context, long nativeClassPointer) {
        super(context, nativeClassPointer);
        this.connectivityManager = null;
        this.listening = false;
        this.networkChangeReceiver = new C09621();
    }

    public void onStart() {
        if (ContextCompat.checkSelfPermission(this.context, "android.permission.ACCESS_NETWORK_STATE") == 0) {
            this.connectivityManager = (ConnectivityManager) this.context.getSystemService("connectivity");
            this.listening = true;
        } else {
            this.listening = false;
            Log.e(TAG, "No permissions");
        }
        if (this.listening) {
            registerNetworkChangeReceiver();
        }
    }

    public void onStop() {
        if (this.listening) {
            unregisterNetworkChangeReceiver();
        }
    }

    private void registerNetworkChangeReceiver() {
        IntentFilter networkChanges = new IntentFilter();
        networkChanges.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getContext().registerReceiver(this.networkChangeReceiver, networkChanges);
    }

    private void unregisterNetworkChangeReceiver() {
        getContext().unregisterReceiver(this.networkChangeReceiver);
    }

    private void notifyNetworkStateChanged(boolean is_connected) {
        synchronized (this.callbackLock) {
            nativeNotifyNetworkStateChanged(is_connected);
        }
    }
}
