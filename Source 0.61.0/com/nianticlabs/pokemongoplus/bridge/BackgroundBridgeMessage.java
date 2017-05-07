package com.nianticlabs.pokemongoplus.bridge;

import android.os.Message;
import com.voxelbusters.nativeplugins.defines.Keys.GameServices;

public class BackgroundBridgeMessage {
    public final Message message;

    public enum Action {
        UPDATE_TIMESTAMP_ACTION,
        SFIDA_STATE_ACTION,
        ENCOUNTER_ID_ACTION,
        POKESTOP_ACTION,
        CENTRAL_STATE_ACTION,
        SCANNED_SFIDA_ACTION,
        BATTERY_LEVEL_ACTION,
        PLUGIN_STATE_ACTION,
        IS_SCANNING_ACTION,
        SEND_NOTIFICATION_ACTION,
        STOP_NOTIFICATION_ACTION,
        CONFIRM_BRIDGE_SHUTDOWN_ACTION,
        XP_GAIN_ACTION
    }

    public interface MessageHandler {
        void handleMessage(BackgroundBridgeMessage backgroundBridgeMessage);
    }

    public BackgroundBridgeMessage() {
        this(Message.obtain());
    }

    public BackgroundBridgeMessage(Message message) {
        this.message = message;
    }

    public Action getAction() {
        return Action.values()[this.message.what];
    }

    public BackgroundBridgeMessage setAction(Action action) {
        this.message.what = action.ordinal();
        return this;
    }

    public String getDeviceId() {
        return this.message.getData().getString("deviceId");
    }

    public BackgroundBridgeMessage setDeviceId(String deviceId) {
        this.message.getData().putString("deviceId", deviceId);
        return this;
    }

    public String getPokestopId() {
        return this.message.getData().getString("pokestopId");
    }

    public BackgroundBridgeMessage setPokestopId(String pokestopId) {
        this.message.getData().putString("pokestopId", pokestopId);
        return this;
    }

    public long getEncounterId() {
        return this.message.getData().getLong("encounterId");
    }

    public BackgroundBridgeMessage setEncounterId(long encounterId) {
        this.message.getData().putLong("encounterId", encounterId);
        return this;
    }

    public long getTimestamp() {
        return this.message.getData().getLong(GameServices.USER_TIME_STAMP);
    }

    public BackgroundBridgeMessage setTimestamp(long timestamp) {
        this.message.getData().putLong(GameServices.USER_TIME_STAMP, timestamp);
        return this;
    }

    public double getBatteryLevel() {
        return this.message.getData().getDouble("battery");
    }

    public BackgroundBridgeMessage setBatteryLevel(double batteryLevel) {
        this.message.getData().putDouble("battery", batteryLevel);
        return this;
    }

    public BackgroundBridgeMessage setArg1(int arg1) {
        this.message.arg1 = arg1;
        return this;
    }

    public int getArg1() {
        return this.message.arg1;
    }

    public BackgroundBridgeMessage setNotification(String notification) {
        this.message.getData().putString("notification", notification);
        return this;
    }

    public String getNotification() {
        return this.message.getData().getString("notification");
    }
}
