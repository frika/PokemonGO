package com.nianticlabs.pokemongoplus.bridge;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.location.places.Place;
import com.mopub.volley.Request.Method;
import com.nianticlabs.pokemongoplus.ble.BluetoothGattSupport;
import com.nianticlabs.pokemongoplus.bridge.BackgroundBridgeMessage.Action;
import com.nianticlabs.pokemongoplus.bridge.BackgroundBridgeMessage.MessageHandler;
import com.nianticlabs.pokemongoplus.service.BackgroundService;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import java.util.ArrayList;
import java.util.List;
import spacemadness.com.lunarconsole.C1628R;

public class ClientBridge implements ServiceConnection {
    private static final String TAG;
    private static Context currentContext;
    private static ClientBridge instance;
    private final List<MessageHandler> listeners;
    LoginDelegate loginDelegate;
    private Messenger messenger;
    private long nativeHandle;
    private Messenger replyMessenger;
    SfidaRegisterDelegate sfidaRegisterDelegate;

    /* renamed from: com.nianticlabs.pokemongoplus.bridge.ClientBridge.1 */
    static class C10131 implements Runnable {
        final /* synthetic */ ClientBridgeMessage val$message;

        C10131(ClientBridgeMessage clientBridgeMessage) {
            this.val$message = clientBridgeMessage;
        }

        public void run() {
            try {
                Log.i(ClientBridge.TAG, String.format("sendMessage: action:%s thread:%s", new Object[]{this.val$message.getAction(), Thread.currentThread().getName()}));
                this.val$message.message.replyTo = ClientBridge.instance.replyMessenger;
                ClientBridge.instance.messenger.send(this.val$message.message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.nianticlabs.pokemongoplus.bridge.ClientBridge.2 */
    class C10142 extends Handler {
        C10142() {
        }

        public void handleMessage(Message msg) {
            Log.i(ClientBridge.TAG, "Received a message: what:" + msg.what + " tid:" + Thread.currentThread().getName());
            BackgroundBridgeMessage bridgeMessage = new BackgroundBridgeMessage(msg);
            ClientBridge.this.onBackgroundMessage(bridgeMessage);
            for (int i = 0; i < ClientBridge.this.listeners.size(); i++) {
                ((MessageHandler) ClientBridge.this.listeners.get(i)).handleMessage(bridgeMessage);
            }
        }
    }

    /* renamed from: com.nianticlabs.pokemongoplus.bridge.ClientBridge.3 */
    static /* synthetic */ class C10153 {
        static final /* synthetic */ int[] f574xcb45735b;

        static {
            f574xcb45735b = new int[Action.values().length];
            try {
                f574xcb45735b[Action.UPDATE_TIMESTAMP_ACTION.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f574xcb45735b[Action.SFIDA_STATE_ACTION.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f574xcb45735b[Action.ENCOUNTER_ID_ACTION.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f574xcb45735b[Action.POKESTOP_ACTION.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f574xcb45735b[Action.CENTRAL_STATE_ACTION.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f574xcb45735b[Action.SCANNED_SFIDA_ACTION.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                f574xcb45735b[Action.PLUGIN_STATE_ACTION.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                f574xcb45735b[Action.IS_SCANNING_ACTION.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                f574xcb45735b[Action.XP_GAIN_ACTION.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                f574xcb45735b[Action.BATTERY_LEVEL_ACTION.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                f574xcb45735b[Action.CONFIRM_BRIDGE_SHUTDOWN_ACTION.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                f574xcb45735b[Action.SEND_NOTIFICATION_ACTION.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
        }
    }

    public interface LoginDelegate {
        void onLogin(boolean z);
    }

    public interface SfidaRegisterDelegate {
        void onSfidaRegistered(boolean z, String str);
    }

    private native void initialize();

    public static native void nativeInit();

    public native void connectDevice(String str, int i);

    public native void disconnectDevice();

    public native void dispose();

    public native void login();

    public native void logout();

    public native void pausePlugin();

    public native void registerDevice(String str);

    public native void requestPgpState();

    public native void resumePlugin();

    public native void sendBatteryLevel(double d);

    public native void sendCentralState(int i);

    public native void sendEncounterId(long j);

    public native void sendIsScanning(int i);

    public native void sendPluginState(int i);

    public native void sendPokestopId(String str);

    public native void sendScannedSfida(String str, int i);

    public native void sendSfidaState(int i);

    public native void sendUpdateTimestamp(long j);

    public native void sendXpGained(int i);

    public native void standaloneInit(long j);

    public native void standaloneUpdate();

    public native void startPlugin();

    public native void startScanning();

    public native void stopPlugin();

    public native void stopScanning();

    static {
        TAG = ClientBridge.class.getSimpleName();
    }

    private ClientBridge() {
        this.listeners = new ArrayList();
        Log.i(TAG, "Initialize();");
        initialize();
    }

    public static ClientBridge createBridge(Context context) {
        currentContext = context;
        return getInstance();
    }

    public static ClientBridge getInstance() {
        if (instance == null) {
            Log.i(TAG, "Create ClientBridge");
            nativeInit();
            instance = new ClientBridge();
        }
        return instance;
    }

    public void addListener(MessageHandler listener) {
        this.listeners.add(listener);
    }

    public void removeListener(MessageHandler listener) {
        this.listeners.remove(listener);
    }

    private static void sendMessage(ClientBridgeMessage message) {
        if (instance.messenger == null) {
            Log.e(TAG, String.format("Service is not connected yet. Action failed: %s", new Object[]{message.getAction()}));
            return;
        }
        new Handler(currentContext.getMainLooper()).post(new C10131(message));
    }

    public void onServiceConnected(ComponentName componentName, IBinder binder) {
        Log.i(TAG, "onServiceConnected");
        this.messenger = new Messenger(binder);
        this.replyMessenger = new Messenger(new C10142());
    }

    public void onServiceDisconnected(ComponentName name) {
        Log.i(TAG, "onServiceDisconnected");
        this.messenger = null;
        this.replyMessenger = null;
    }

    private void onBackgroundMessage(BackgroundBridgeMessage message) {
        switch (C10153.f574xcb45735b[message.getAction().ordinal()]) {
            case C1628R.styleable.RecyclerView_layoutManager /*1*/:
                sendUpdateTimestamp(message.getTimestamp());
            case C1628R.styleable.RecyclerView_spanCount /*2*/:
                sendSfidaState(message.message.arg1);
            case C1628R.styleable.RecyclerView_reverseLayout /*3*/:
                sendEncounterId(message.getEncounterId());
            case C1628R.styleable.RecyclerView_stackFromEnd /*4*/:
                sendPokestopId(message.getPokestopId());
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                sendCentralState(message.message.arg1);
            case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_RIGHT /*6*/:
                sendScannedSfida(message.getDeviceId(), message.message.arg1);
            case Method.PATCH /*7*/:
                sendPluginState(message.message.arg1);
            case BluetoothGattSupport.GATT_INSUF_AUTHENTICATION /*8*/:
                sendIsScanning(message.message.arg1);
            case Place.TYPE_BAR /*9*/:
                sendXpGained(message.message.arg1);
            case Place.TYPE_BEAUTY_SALON /*10*/:
                sendBatteryLevel(message.getBatteryLevel());
            case Place.TYPE_BICYCLE_STORE /*11*/:
                Log.i(TAG, "Confirmed bridge shut down");
            case Place.TYPE_BOOK_STORE /*12*/:
                int messageId = message.getArg1();
                Log.i(TAG, String.format("Notification received: %d", new Object[]{Integer.valueOf(messageId)}));
            default:
                Log.e(TAG, "Can't handle intent message: " + message.getAction());
        }
    }

    public static void shutdownBackgroundBridge() {
        Log.i(TAG, "shutdown background bridge PROCESS_LOCAL_VALUE = " + BackgroundService.PROCESS_LOCAL_VALUE);
        sendMessage(new ClientBridgeMessage().setAction(ClientBridgeMessage.Action.SHUTDOWN_ACTION));
    }

    public static void sendStart() {
        Log.i(TAG, "sendStart PROCESS_LOCAL_VALUE = " + BackgroundService.PROCESS_LOCAL_VALUE);
        sendMessage(new ClientBridgeMessage().setAction(ClientBridgeMessage.Action.START_ACTION));
    }

    public static void sendResume() {
        sendMessage(new ClientBridgeMessage().setAction(ClientBridgeMessage.Action.RESUME_ACTION));
    }

    public static void sendPause() {
        sendMessage(new ClientBridgeMessage().setAction(ClientBridgeMessage.Action.PAUSE_ACTION));
    }

    public static void sendStop() {
        sendMessage(new ClientBridgeMessage().setAction(ClientBridgeMessage.Action.STOP_ACTION));
    }

    public static void sendStartScanning() {
        sendMessage(new ClientBridgeMessage().setAction(ClientBridgeMessage.Action.START_SCANNING_ACTION));
    }

    public static void sendStopScanning() {
        sendMessage(new ClientBridgeMessage().setAction(ClientBridgeMessage.Action.STOP_SCANNING_ACTION));
    }

    public static void sendStartSession(String hostPort, String device, byte[] authToken, long encounterId, int notifications) {
        Log.i(TAG, String.format("send startSession intent %s %s %s %d", new Object[]{hostPort, device, authToken, Long.valueOf(encounterId)}));
        sendMessage(new ClientBridgeMessage().setAction(ClientBridgeMessage.Action.START_SESSION_ACTION).setDeviceId(device).setEncounterId(encounterId).setHostPort(hostPort).setAuthToken(authToken).setNotifications(notifications));
    }

    public static void sendStopSession() {
        sendMessage(new ClientBridgeMessage().setAction(ClientBridgeMessage.Action.STOP_SESSION_ACTION));
    }

    public static void sendUpdateNotifications(int notifications) {
        sendMessage(new ClientBridgeMessage().setAction(ClientBridgeMessage.Action.UPDATE_NOTIFICATIONS).setNotifications(notifications));
    }

    public static void sendRequestPgpState() {
        sendMessage(new ClientBridgeMessage().setAction(ClientBridgeMessage.Action.REQUEST_PGP_STATE));
    }

    public void standaloneLogin(LoginDelegate delegate) {
        this.loginDelegate = delegate;
        login();
    }

    public void standaloneSfidaRegister(String device, SfidaRegisterDelegate delegate) {
        this.sfidaRegisterDelegate = delegate;
        registerDevice(device);
    }

    public void onLogin(boolean success) {
        if (this.loginDelegate != null) {
            this.loginDelegate.onLogin(success);
            this.loginDelegate = null;
        }
    }

    public void onSfidaRegistered(boolean success, String device) {
        if (this.sfidaRegisterDelegate != null) {
            this.sfidaRegisterDelegate.onSfidaRegistered(success, device);
            this.sfidaRegisterDelegate = null;
        }
    }
}
