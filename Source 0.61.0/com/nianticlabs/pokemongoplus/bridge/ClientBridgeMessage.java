package com.nianticlabs.pokemongoplus.bridge;

import android.os.Message;

public class ClientBridgeMessage {
    public final Message message;

    public enum Action {
        START_ACTION,
        RESUME_ACTION,
        PAUSE_ACTION,
        STOP_ACTION,
        START_SCANNING_ACTION,
        STOP_SCANNING_ACTION,
        START_SESSION_ACTION,
        STOP_SESSION_ACTION,
        START_SERVICE_ACTION,
        SHUTDOWN_ACTION,
        REQUEST_PGP_STATE,
        UPDATE_NOTIFICATIONS
    }

    public ClientBridgeMessage() {
        this(Message.obtain());
    }

    public ClientBridgeMessage(Message message) {
        this.message = message;
    }

    public String getDeviceId() {
        return this.message.getData().getString("deviceId");
    }

    public ClientBridgeMessage setDeviceId(String deviceId) {
        this.message.getData().putString("deviceId", deviceId);
        return this;
    }

    public long getEncounterId() {
        return this.message.getData().getLong("encounterId");
    }

    public ClientBridgeMessage setEncounterId(long encounterId) {
        this.message.getData().putLong("encounterId", encounterId);
        return this;
    }

    public Action getAction() {
        return Action.values()[this.message.what];
    }

    public ClientBridgeMessage setAction(Action action) {
        this.message.what = action.ordinal();
        return this;
    }

    public ClientBridgeMessage setArg1(int arg1) {
        this.message.arg1 = arg1;
        return this;
    }

    public int getArg1() {
        return this.message.arg1;
    }

    public ClientBridgeMessage setHostPort(String hostPort) {
        this.message.getData().putString("hostPort", hostPort);
        return this;
    }

    public String getHostPort() {
        return this.message.getData().getString("hostPort");
    }

    public ClientBridgeMessage setAuthToken(byte[] authToken) {
        this.message.getData().putByteArray("authToken", authToken);
        return this;
    }

    public byte[] getAuthToken() {
        return this.message.getData().getByteArray("authToken");
    }

    public ClientBridgeMessage setNotifications(int notifications) {
        this.message.getData().putInt("notifications", notifications);
        return this;
    }

    public int getNotifications() {
        return this.message.getData().getInt("notifications");
    }
}
