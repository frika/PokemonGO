package com.nianticlabs.pokemongoplus.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.RemoteViews;
import com.google.android.exoplayer.C0277C;
import com.nianticlabs.pokemongoplus.C0973R;
import com.nianticlabs.pokemongoplus.bridge.BackgroundBridge;
import com.nianticlabs.pokemongoplus.bridge.BackgroundBridgeMessage;
import com.nianticlabs.pokemongoplus.bridge.BackgroundBridgeMessage.MessageHandler;
import com.nianticlabs.pokemongoplus.bridge.BridgeConstants.PgpState;
import com.nianticlabs.pokemongoplus.bridge.BridgeConstants.SfidaState;
import com.nianticlabs.pokemongoplus.bridge.ClientBridgeMessage;
import com.nianticlabs.pokemongoplus.bridge.ClientBridgeMessage.Action;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import spacemadness.com.lunarconsole.BuildConfig;

public class BackgroundService extends Service {
    public static int PROCESS_LOCAL_VALUE = 0;
    private static final String STOP_FROM_NOTIFICATION = "stopFromNotif";
    private static final String TAG;
    private static final int intentRequestCode = 1000;
    private static final int kCapturedPokemon = 1;
    private static final int kEmptyMessage = 0;
    private static final int kItemInventoryFull = 9;
    private static final int kOutOfPokeballs = 7;
    private static final int kPokemonEscaped = 2;
    private static final int kPokemonInventoryFull = 8;
    private static final int kPokestopCooldown = 6;
    private static final int kPokestopOutOfRange = 5;
    private static final int kRetrievedItems = 4;
    private static final int kRetrievedOneItem = 3;
    private static final int kSessionEnded = 12;
    private static final int kTrackedPokemonFound = 10;
    private static final int kTrackedPokemonLost = 11;
    private static final int notificationId = 2000;
    private static final Map<Integer, Integer> notificationMap;
    private static boolean serviceStopped;
    private double batteryLevel;
    private RemoteViews contentView;
    private boolean isScanning;
    private MessageHandler messageHandler;
    private Messenger messenger;
    private BackgroundBridge pgpBackgroundBridge;
    private PgpState pluginState;
    private Messenger replyMessenger;
    private SfidaState sfidaState;

    /* renamed from: com.nianticlabs.pokemongoplus.service.BackgroundService.1 */
    class C10161 extends Handler {
        C10161() {
        }

        public void handleMessage(Message message) {
            ClientBridgeMessage bridgeMessage = new ClientBridgeMessage(message);
            Action action = bridgeMessage.getAction();
            if (BackgroundService.this.pgpBackgroundBridge == null) {
                BackgroundService.this.replyMessenger = message.replyTo;
                BackgroundService.this.initBackgroundBridge();
                BackgroundService.serviceStopped = false;
            }
            BackgroundService.this.pgpBackgroundBridge.onClientMessage(bridgeMessage);
        }
    }

    /* renamed from: com.nianticlabs.pokemongoplus.service.BackgroundService.2 */
    class C10172 implements MessageHandler {
        C10172() {
        }

        public void handleMessage(BackgroundBridgeMessage message) {
            if (BackgroundService.this.replyMessenger != null) {
                try {
                    BackgroundService.this.replyMessenger.send(message.message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(BackgroundService.TAG, "replyMessenger not found");
            }
            BackgroundService.this.onHandleBackgroundMessage(message);
        }
    }

    /* renamed from: com.nianticlabs.pokemongoplus.service.BackgroundService.3 */
    static /* synthetic */ class C10183 {
        static final /* synthetic */ int[] f575xcb45735b;
        static final /* synthetic */ int[] f576xd558c8ad;

        static {
            f576xd558c8ad = new int[SfidaState.values().length];
            try {
                f576xd558c8ad[SfidaState.Disconnecting.ordinal()] = BackgroundService.kCapturedPokemon;
            } catch (NoSuchFieldError e) {
            }
            try {
                f576xd558c8ad[SfidaState.Disconnected.ordinal()] = BackgroundService.kPokemonEscaped;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f576xd558c8ad[SfidaState.Connected.ordinal()] = BackgroundService.kRetrievedOneItem;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f576xd558c8ad[SfidaState.Certified.ordinal()] = BackgroundService.kRetrievedItems;
            } catch (NoSuchFieldError e4) {
            }
            f575xcb45735b = new int[BackgroundBridgeMessage.Action.values().length];
            try {
                f575xcb45735b[BackgroundBridgeMessage.Action.SEND_NOTIFICATION_ACTION.ordinal()] = BackgroundService.kCapturedPokemon;
            } catch (NoSuchFieldError e5) {
            }
            try {
                f575xcb45735b[BackgroundBridgeMessage.Action.STOP_NOTIFICATION_ACTION.ordinal()] = BackgroundService.kPokemonEscaped;
            } catch (NoSuchFieldError e6) {
            }
            try {
                f575xcb45735b[BackgroundBridgeMessage.Action.BATTERY_LEVEL_ACTION.ordinal()] = BackgroundService.kRetrievedOneItem;
            } catch (NoSuchFieldError e7) {
            }
            try {
                f575xcb45735b[BackgroundBridgeMessage.Action.SFIDA_STATE_ACTION.ordinal()] = BackgroundService.kRetrievedItems;
            } catch (NoSuchFieldError e8) {
            }
            try {
                f575xcb45735b[BackgroundBridgeMessage.Action.PLUGIN_STATE_ACTION.ordinal()] = BackgroundService.kPokestopOutOfRange;
            } catch (NoSuchFieldError e9) {
            }
        }
    }

    static {
        notificationMap = new HashMap();
        notificationMap.put(Integer.valueOf(kCapturedPokemon), Integer.valueOf(C0973R.string.Captured_Pokemon));
        notificationMap.put(Integer.valueOf(kPokemonEscaped), Integer.valueOf(C0973R.string.Pokemon_Escaped));
        notificationMap.put(Integer.valueOf(kRetrievedOneItem), Integer.valueOf(C0973R.string.Retrieved_an_Item));
        notificationMap.put(Integer.valueOf(kRetrievedItems), Integer.valueOf(C0973R.string.Retrieved_Items));
        notificationMap.put(Integer.valueOf(kPokestopOutOfRange), Integer.valueOf(C0973R.string.Pokestop_Out_Of_Range));
        notificationMap.put(Integer.valueOf(kPokestopCooldown), Integer.valueOf(C0973R.string.Pokestop_Cooldown));
        notificationMap.put(Integer.valueOf(kOutOfPokeballs), Integer.valueOf(C0973R.string.Out_Of_Pokeballs));
        notificationMap.put(Integer.valueOf(kPokemonInventoryFull), Integer.valueOf(C0973R.string.Pokemon_Inventory_Full));
        notificationMap.put(Integer.valueOf(kItemInventoryFull), Integer.valueOf(C0973R.string.Item_Inventory_Full));
        notificationMap.put(Integer.valueOf(kTrackedPokemonFound), Integer.valueOf(C0973R.string.Tracked_Pokemon_Found));
        notificationMap.put(Integer.valueOf(kTrackedPokemonLost), Integer.valueOf(C0973R.string.Tracked_Pokemon_Lost));
        notificationMap.put(Integer.valueOf(kSessionEnded), Integer.valueOf(C0973R.string.Session_Ended));
        TAG = BackgroundService.class.getSimpleName();
        PROCESS_LOCAL_VALUE = new Random().nextInt();
        serviceStopped = false;
    }

    public BackgroundService() {
        this.pgpBackgroundBridge = null;
    }

    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "BackgroundService onCreate() PROCESS_LOCAL_VALUE = " + PROCESS_LOCAL_VALUE);
        System.loadLibrary("pgpplugin");
        this.messenger = new Messenger(new C10161());
        this.messageHandler = new C10172();
    }

    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "BackgroundService onDestroy PROCESS_LOCAL_VALUE = " + PROCESS_LOCAL_VALUE);
    }

    public void initBackgroundBridge() {
        Log.i(TAG, "BackgroundService onCreate PROCESS_LOCAL_VALUE = " + PROCESS_LOCAL_VALUE);
        this.pgpBackgroundBridge = BackgroundBridge.createBridge(this, this.messageHandler);
    }

    private static void sendClientIntent(Context context, String serviceAction) {
        Intent i = new Intent(context, ClientService.class);
        i.setPackage("com.nianticlabs.pokemongoplus.bridge");
        i.putExtra("action", serviceAction);
        context.startService(i);
    }

    public void shutdownBackgroundBridge() {
        Log.i(TAG, "BackgroundService shutdownBackgroundBridge() PROCESS_LOCAL_VALUE = " + PROCESS_LOCAL_VALUE);
        sendClientIntent(this, "confirmBridgeShutdown");
        if (this.pgpBackgroundBridge != null) {
            Log.i(TAG, "BackgroundService destroy the bridge ");
            this.pgpBackgroundBridge.destroyBridge();
            this.pgpBackgroundBridge = null;
        }
        Log.i(TAG, "DONE BackgroundService shutdownBackgroundBridge() PROCESS_LOCAL_VALUE = " + PROCESS_LOCAL_VALUE);
        serviceStopped = true;
        stopSelf();
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind()");
        return this.messenger.getBinder();
    }

    public boolean onUnbind(Intent intent) {
        stopBackgroundService();
        return false;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        onHandleIntent(intent);
        return kPokemonEscaped;
    }

    private void onHandleIntent(Intent intent) {
        if (intent == null) {
            Log.w(TAG, "BackgroundService onHandleIntent (intent == null)");
            return;
        }
        String action = intent.getStringExtra("action");
        if (action == null) {
            Log.w(TAG, "BackgroundService onHandleIntent (action == null)");
            return;
        }
        Log.i(TAG, "BackgroundService onHandleBridgedIntent action = " + action);
        int i = -1;
        switch (action.hashCode()) {
            case -1946846268:
                if (action.equals(STOP_FROM_NOTIFICATION)) {
                    i = kEmptyMessage;
                    break;
                }
                break;
        }
        switch (i) {
            case kEmptyMessage /*0*/:
                stopBackgroundService();
            default:
                String str = TAG;
                Object[] objArr = new Object[kCapturedPokemon];
                objArr[kEmptyMessage] = action;
                Log.e(str, String.format("Unknown intent passed to BackgroundService: %s", objArr));
        }
    }

    private void stopBackgroundService() {
        if (this.sfidaState == SfidaState.Disconnecting || this.sfidaState == SfidaState.Disconnected) {
            stopPlayerNotification();
            return;
        }
        if (this.contentView != null) {
            this.contentView.setBoolean(C0973R.id.stopPgp, "setEnabled", false);
        }
        if (this.pgpBackgroundBridge != null) {
            createPlayerNotification(getResources().getString(C0973R.string.Disconnecting_GO_Plus));
            this.pgpBackgroundBridge.stopSession();
            return;
        }
        stopPlayerNotification();
    }

    private void forceStopService() {
        stopPlayerNotification();
        stopSelf();
    }

    private void stopPlayerNotification() {
        Log.i(TAG, String.format("stopping notification", new Object[kEmptyMessage]));
        this.contentView = null;
        stopForeground(true);
    }

    private void updateBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    private String formatNotification(int message, String arg) {
        Integer tag = (Integer) notificationMap.get(Integer.valueOf(message));
        if (tag == null) {
            return BuildConfig.FLAVOR;
        }
        String format = getResources().getString(tag.intValue());
        Object[] objArr = new Object[kCapturedPokemon];
        objArr[kEmptyMessage] = arg;
        return String.format(format, objArr);
    }

    private void createPlayerNotification(String message) {
        if (VERSION.SDK_INT > 20) {
            createNewStylePlayerNotification(message);
        } else {
            createOldStylePlayerNotification(message);
        }
    }

    private PendingIntent createStopSelfPendingIntent() {
        Intent stopSelf = new Intent(this, BackgroundService.class);
        stopSelf.setPackage("com.nianticlabs.pokemongoplus.bridge");
        stopSelf.putExtra("action", STOP_FROM_NOTIFICATION);
        return PendingIntent.getService(this, kEmptyMessage, stopSelf, C0277C.SAMPLE_FLAG_DECODE_ONLY);
    }

    private void createNewStylePlayerNotification(String message) {
        Log.i(TAG, "BackgroundService createNewStylePlayerNotification message = " + message);
        finishPlayerNotificationCreation(new Builder(this).setSmallIcon(C0973R.drawable.ic_pgp_white).setContentTitle(getResources().getString(C0973R.string.Pokemon_Go_Plus)).setContentText(message).setPriority(kPokemonEscaped).setVisibility(kCapturedPokemon).addAction(new NotificationCompat.Action.Builder(C0973R.drawable.ic_close_black_24dp, getResources().getString(C0973R.string.Stop_button_label), createStopSelfPendingIntent()).build()));
    }

    private void createOldStylePlayerNotification(String message) {
        Log.i(TAG, "BackgroundService createOldStylePlayerNotification message = " + message);
        PendingIntent pendingStopSelf = createStopSelfPendingIntent();
        Builder builder = new Builder(this).setSmallIcon(C0973R.drawable.ic_pgp_white).setPriority(kPokemonEscaped).setVisibility(kCapturedPokemon);
        this.contentView = new RemoteViews(getPackageName(), C0973R.layout.pgp_status_notif);
        this.contentView.setOnClickPendingIntent(C0973R.id.stopPgp, pendingStopSelf);
        this.contentView.setTextViewText(C0973R.id.pgpStatusTitle, getResources().getString(C0973R.string.Pokemon_Go_Plus));
        this.contentView.setTextViewText(C0973R.id.pgpStatusDetail, message);
        builder.setContent(this.contentView);
        finishPlayerNotificationCreation(builder);
    }

    private void finishPlayerNotificationCreation(Builder builder) {
        builder.setContentIntent(PendingIntent.getActivity(this, intentRequestCode, Intent.makeMainActivity(new ComponentName(this, GetLauncherActivity(this))), C0277C.SAMPLE_FLAG_DECODE_ONLY));
        startForeground(notificationId, builder.build());
    }

    public Class<?> GetLauncherActivity(Context context) {
        String className = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent().getClassName();
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Launcher class not found: " + className);
            return null;
        }
    }

    public void startNotification(int message, String arg) {
        createPlayerNotification(formatNotification(message, arg));
    }

    public void stopNotification() {
        stopPlayerNotification();
    }

    public void onHandleBackgroundMessage(BackgroundBridgeMessage message) {
        switch (C10183.f575xcb45735b[message.getAction().ordinal()]) {
            case kCapturedPokemon /*1*/:
                createPlayerNotification(formatNotification(message.getArg1(), message.getNotification()));
            case kPokemonEscaped /*2*/:
                stopPlayerNotification();
            case kRetrievedOneItem /*3*/:
                updateBatteryLevel(message.getBatteryLevel());
            case kRetrievedItems /*4*/:
                SfidaState newSfidaState = SfidaState.fromInt(message.getArg1());
                updateNotificationForSfidaState(newSfidaState, this.sfidaState);
                this.sfidaState = newSfidaState;
            case kPokestopOutOfRange /*5*/:
                this.pluginState = PgpState.fromInt(message.getArg1());
            default:
        }
    }

    private void updateNotificationForSfidaState(SfidaState newSfidaState, SfidaState sfidaState) {
        Log.e(TAG, "New state: " + newSfidaState.toString());
        if (newSfidaState != sfidaState) {
            switch (C10183.f576xd558c8ad[newSfidaState.ordinal()]) {
                case kPokemonEscaped /*2*/:
                    if (sfidaState == SfidaState.Disconnecting) {
                        stopPlayerNotification();
                    }
                case kRetrievedOneItem /*3*/:
                case kRetrievedItems /*4*/:
                    createPlayerNotification(BuildConfig.FLAVOR);
                default:
            }
        }
    }
}
