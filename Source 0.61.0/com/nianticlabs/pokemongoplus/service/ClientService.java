package com.nianticlabs.pokemongoplus.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.nianticlabs.pokemongoplus.bridge.ClientBridge;

public class ClientService {
    private static final String TAG;
    static ClientBridge pgpClientBridge;

    static {
        TAG = ClientService.class.getSimpleName();
        pgpClientBridge = null;
    }

    public static void startClientService(Context context, ClientBridge bridge) {
        pgpClientBridge = bridge;
        String packageName = context.getApplicationContext().getPackageName();
        Log.i(TAG, "package: " + packageName);
        if (context.bindService(new Intent("com.nianticlabs.pokemongoplus.service.BackgroundService").setPackage(packageName), bridge, 1)) {
            Log.i(TAG, "Started BackgroundService");
        } else {
            Log.e(TAG, "Failed to start BackgroundService");
        }
    }

    public static void stopClientService(Context context) {
        context.unbindService(pgpClientBridge);
        pgpClientBridge = null;
    }
}
