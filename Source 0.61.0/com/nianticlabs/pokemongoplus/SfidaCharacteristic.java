package com.nianticlabs.pokemongoplus;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.util.Log;
import com.nianticlabs.pokemongoplus.ble.Characteristic;
import com.nianticlabs.pokemongoplus.ble.SfidaConstant;
import com.nianticlabs.pokemongoplus.ble.SfidaConstant.BluetoothError;
import com.nianticlabs.pokemongoplus.ble.callback.CompletionCallback;
import com.nianticlabs.pokemongoplus.ble.callback.ValueChangeCallback;
import java.util.ArrayDeque;

public class SfidaCharacteristic extends Characteristic {
    private static final String TAG;
    private final int RETRIES;
    private final long SLEEP_DELAY_MS;
    private BluetoothGattCharacteristic characteristic;
    private BluetoothGatt gatt;
    private long nativeHandle;
    private CompletionCallback onDisableNotifyCallback;
    private CompletionCallback onEnableNotifyCallback;
    private CompletionCallback onReadCallback;
    private ValueChangeCallback onValueChangedCallback;
    private CompletionCallback onWriteCallback;
    private volatile ArrayDeque<byte[]> queue;
    private final HandlerExecutor serialExecutor;

    /* renamed from: com.nianticlabs.pokemongoplus.SfidaCharacteristic.1 */
    class C09811 implements ValueChangeCallback {
        C09811() {
        }

        public void OnValueChange(boolean success, boolean valueChanged, BluetoothError error) {
            SfidaCharacteristic.this.nativeValueChangedCallback(success, valueChanged, error.getInt());
        }
    }

    /* renamed from: com.nianticlabs.pokemongoplus.SfidaCharacteristic.2 */
    class C09822 implements Runnable {
        final /* synthetic */ byte[] val$byteArray;
        final /* synthetic */ CompletionCallback val$callback;

        C09822(CompletionCallback completionCallback, byte[] bArr) {
            this.val$callback = completionCallback;
            this.val$byteArray = bArr;
        }

        public void run() {
            SfidaCharacteristic.this.onWriteCallback = this.val$callback;
            SfidaCharacteristic.this.characteristic.setValue(this.val$byteArray);
            boolean result = false;
            for (int i = 0; i < 7; i++) {
                result = SfidaCharacteristic.this.gatt.writeCharacteristic(SfidaCharacteristic.this.characteristic);
                if (result) {
                    break;
                }
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                }
            }
            if (!result) {
                SfidaCharacteristic.this.onWriteCallback.onCompletion(false, BluetoothError.Unknown);
                SfidaCharacteristic.this.onWriteCallback = null;
            }
        }
    }

    /* renamed from: com.nianticlabs.pokemongoplus.SfidaCharacteristic.3 */
    class C09843 implements CompletionCallback {

        /* renamed from: com.nianticlabs.pokemongoplus.SfidaCharacteristic.3.1 */
        class C09831 implements Runnable {
            final /* synthetic */ BluetoothError val$error;
            final /* synthetic */ boolean val$success;

            C09831(boolean z, BluetoothError bluetoothError) {
                this.val$success = z;
                this.val$error = bluetoothError;
            }

            public void run() {
                SfidaCharacteristic.this.nativeWriteCompleteCallback(this.val$success, this.val$error.getInt());
            }
        }

        C09843() {
        }

        public void onCompletion(boolean success, BluetoothError error) {
            SfidaCharacteristic.this.serialExecutor.execute(new C09831(success, error));
        }
    }

    /* renamed from: com.nianticlabs.pokemongoplus.SfidaCharacteristic.4 */
    class C09854 implements Runnable {
        final /* synthetic */ CompletionCallback val$callback;

        C09854(CompletionCallback completionCallback) {
            this.val$callback = completionCallback;
        }

        public void run() {
            SfidaCharacteristic.this.onReadCallback = this.val$callback;
            if (!SfidaCharacteristic.this.gatt.readCharacteristic(SfidaCharacteristic.this.characteristic)) {
                SfidaCharacteristic.this.onReadCallback.onCompletion(false, BluetoothError.Unknown);
            }
        }
    }

    /* renamed from: com.nianticlabs.pokemongoplus.SfidaCharacteristic.5 */
    class C09875 implements CompletionCallback {

        /* renamed from: com.nianticlabs.pokemongoplus.SfidaCharacteristic.5.1 */
        class C09861 implements Runnable {
            final /* synthetic */ BluetoothError val$error;
            final /* synthetic */ boolean val$success;

            C09861(boolean z, BluetoothError bluetoothError) {
                this.val$success = z;
                this.val$error = bluetoothError;
            }

            public void run() {
                SfidaCharacteristic.this.nativeReadCompleteCallback(this.val$success, this.val$error.getInt());
            }
        }

        C09875() {
        }

        public void onCompletion(boolean success, BluetoothError error) {
            SfidaCharacteristic.this.serialExecutor.execute(new C09861(success, error));
        }
    }

    /* renamed from: com.nianticlabs.pokemongoplus.SfidaCharacteristic.6 */
    class C09886 implements Runnable {
        final /* synthetic */ CompletionCallback val$callback;

        C09886(CompletionCallback completionCallback) {
            this.val$callback = completionCallback;
        }

        public void run() {
            int i;
            SfidaCharacteristic.this.onEnableNotifyCallback = this.val$callback;
            SfidaCharacteristic.this.onDisableNotifyCallback = null;
            for (i = 0; i < 7; i++) {
                boolean success = SfidaCharacteristic.this.gatt.setCharacteristicNotification(SfidaCharacteristic.this.characteristic, true);
                Log.d(SfidaCharacteristic.TAG, String.format("setCharacteristicNotification success: %b", new Object[]{Boolean.valueOf(success)}));
                if (success) {
                    break;
                }
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                }
            }
            if ((SfidaCharacteristic.this.characteristic.getProperties() & 16) == 0) {
                Log.w(SfidaCharacteristic.TAG, "Enable Notify not supported");
            }
            byte[] previousValue = SfidaCharacteristic.this.characteristic.getValue();
            BluetoothGattDescriptor descriptor = SfidaCharacteristic.this.characteristic.getDescriptor(SfidaConstant.UUID_CLIENT_CHARACTERISTIC_CONFIG);
            Log.d(SfidaCharacteristic.TAG, String.format("Config characteristic:%s descriptor:%s", new Object[]{SfidaCharacteristic.this.getUuid(), descriptor}));
            if (descriptor != null) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                boolean result = false;
                for (i = 0; i < 7; i++) {
                    result = SfidaCharacteristic.this.gatt.writeDescriptor(descriptor);
                    Log.d(SfidaCharacteristic.TAG, String.format("Write descriptor success: %b", new Object[]{Boolean.valueOf(result)}));
                    if (result) {
                        break;
                    }
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e2) {
                    }
                }
                if (!result) {
                    SfidaCharacteristic.this.onEnableNotifyCallback.onCompletion(false, BluetoothError.Unknown);
                    SfidaCharacteristic.this.onEnableNotifyCallback = null;
                }
            }
        }
    }

    /* renamed from: com.nianticlabs.pokemongoplus.SfidaCharacteristic.7 */
    class C09907 implements CompletionCallback {

        /* renamed from: com.nianticlabs.pokemongoplus.SfidaCharacteristic.7.1 */
        class C09891 implements Runnable {
            final /* synthetic */ BluetoothError val$error;
            final /* synthetic */ boolean val$success;

            C09891(boolean z, BluetoothError bluetoothError) {
                this.val$success = z;
                this.val$error = bluetoothError;
            }

            public void run() {
                Log.d(SfidaCharacteristic.TAG, String.format("enableNotify callback success: %b error[%d]:%s UUID:%s", new Object[]{Boolean.valueOf(this.val$success), Integer.valueOf(this.val$error.getInt()), this.val$error.toString(), SfidaCharacteristic.this.getUuid()}));
                SfidaCharacteristic.this.nativeEnableNotifyCallback(this.val$success, this.val$error.getInt());
            }
        }

        C09907() {
        }

        public void onCompletion(boolean success, BluetoothError error) {
            SfidaCharacteristic.this.serialExecutor.execute(new C09891(success, error));
        }
    }

    /* renamed from: com.nianticlabs.pokemongoplus.SfidaCharacteristic.8 */
    class C09918 implements Runnable {
        final /* synthetic */ CompletionCallback val$callback;

        C09918(CompletionCallback completionCallback) {
            this.val$callback = completionCallback;
        }

        public void run() {
            SfidaCharacteristic.this.onEnableNotifyCallback = null;
            SfidaCharacteristic.this.onDisableNotifyCallback = this.val$callback;
            boolean success = SfidaCharacteristic.this.gatt.setCharacteristicNotification(SfidaCharacteristic.this.characteristic, false);
            byte[] currentValue = SfidaCharacteristic.this.characteristic.getValue();
            BluetoothGattDescriptor descriptor = SfidaCharacteristic.this.characteristic.getDescriptor(SfidaConstant.UUID_CLIENT_CHARACTERISTIC_CONFIG);
            Log.d(SfidaCharacteristic.TAG, String.format("disableNotify Config characteristic:%s descriptor:%s", new Object[]{SfidaCharacteristic.this.getUuid(), descriptor}));
            if (descriptor != null) {
                descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                boolean result = false;
                for (int i = 0; i < 7; i++) {
                    result = SfidaCharacteristic.this.gatt.writeDescriptor(descriptor);
                    Log.d(SfidaCharacteristic.TAG, String.format("disableNotify Write descriptor success: %b", new Object[]{Boolean.valueOf(result)}));
                    if (result) {
                        break;
                    }
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                    }
                }
                if (!result) {
                    SfidaCharacteristic.this.onDisableNotifyCallback.onCompletion(false, BluetoothError.Unknown);
                    SfidaCharacteristic.this.onDisableNotifyCallback = null;
                }
            }
        }
    }

    /* renamed from: com.nianticlabs.pokemongoplus.SfidaCharacteristic.9 */
    class C09939 implements CompletionCallback {

        /* renamed from: com.nianticlabs.pokemongoplus.SfidaCharacteristic.9.1 */
        class C09921 implements Runnable {
            final /* synthetic */ BluetoothError val$error;
            final /* synthetic */ boolean val$success;

            C09921(boolean z, BluetoothError bluetoothError) {
                this.val$success = z;
                this.val$error = bluetoothError;
            }

            public void run() {
                Log.d(SfidaCharacteristic.TAG, String.format("disableNotify callback success: %b error[%d]:%s UUID:%s", new Object[]{Boolean.valueOf(this.val$success), Integer.valueOf(this.val$error.getInt()), this.val$error.toString(), SfidaCharacteristic.this.getUuid()}));
                SfidaCharacteristic.this.nativeDisableNotifyCallback(this.val$success, this.val$error.getInt());
            }
        }

        C09939() {
        }

        public void onCompletion(boolean success, BluetoothError error) {
            SfidaCharacteristic.this.serialExecutor.execute(new C09921(success, error));
        }
    }

    private native void nativeDisableNotifyCallback(boolean z, int i);

    private native void nativeEnableNotifyCallback(boolean z, int i);

    private native void nativeReadCompleteCallback(boolean z, int i);

    private native void nativeSaveValueChangedCallback(byte[] bArr);

    private native void nativeValueChangedCallback(boolean z, boolean z2, int i);

    private native void nativeWriteCompleteCallback(boolean z, int i);

    static {
        TAG = SfidaCharacteristic.class.getSimpleName();
    }

    public SfidaCharacteristic(HandlerExecutor serialExecutor, BluetoothGattCharacteristic characteristic, BluetoothGatt gatt) {
        this.RETRIES = 7;
        this.SLEEP_DELAY_MS = 250;
        this.queue = new ArrayDeque();
        this.gatt = gatt;
        this.characteristic = characteristic;
        this.serialExecutor = serialExecutor;
    }

    public void onDestroy() {
        this.serialExecutor.maybeAssertOnThread();
        this.onEnableNotifyCallback = null;
        this.onDisableNotifyCallback = null;
    }

    public String getUuid() {
        this.serialExecutor.maybeAssertOnThread();
        return this.characteristic.getUuid().toString();
    }

    public long getLongValue() {
        this.serialExecutor.maybeAssertOnThread();
        return 0;
    }

    public byte[] getValue() {
        this.serialExecutor.maybeAssertOnThread();
        return (byte[]) this.queue.pollFirst();
    }

    public void cancelNotify() {
        this.onValueChangedCallback = null;
    }

    public void notifyValueChanged() {
        this.onValueChangedCallback = new C09811();
    }

    public void writeByteArray(byte[] byteArray, CompletionCallback callback) {
        this.serialExecutor.execute(new C09822(callback, byteArray));
    }

    public void writeByteArray(byte[] byteArray) {
        writeByteArray(byteArray, new C09843());
    }

    public void readValue(CompletionCallback callback) {
        this.serialExecutor.execute(new C09854(callback));
    }

    public void readValue() {
        readValue(new C09875());
    }

    public void enableNotify(CompletionCallback callback) {
        this.serialExecutor.execute(new C09886(callback));
    }

    public void enableNotify() {
        enableNotify(new C09907());
    }

    public void disableNotify(CompletionCallback callback) {
        this.serialExecutor.execute(new C09918(callback));
    }

    public void disableNotify() {
        disableNotify(new C09939());
    }

    public void onCharacteristicChanged() {
        this.serialExecutor.maybeAssertOnThread();
        Log.d(TAG, String.format("onCharacteristicChanged: %s", new Object[]{this.characteristic.getUuid().toString()}));
        byte[] receivedValue = this.characteristic.getValue();
        if (this.onValueChangedCallback != null) {
            nativeSaveValueChangedCallback(receivedValue);
            this.queue.add(receivedValue);
            this.onValueChangedCallback.OnValueChange(true, true, BluetoothError.Unknown);
        }
    }

    public void onCharacteristicWrite(int status) {
        this.serialExecutor.maybeAssertOnThread();
        if (this.onWriteCallback == null) {
            return;
        }
        if (status == 0) {
            this.onWriteCallback.onCompletion(true, BluetoothError.Unknown);
        } else {
            this.onWriteCallback.onCompletion(false, BluetoothError.Unknown);
        }
    }

    public void onCharacteristicRead(int status) {
        this.serialExecutor.maybeAssertOnThread();
        if (status == 0) {
            Log.d(TAG, String.format("onCharacteristicRead: %s", new Object[]{this.characteristic.getUuid().toString()}));
            nativeSaveValueChangedCallback(this.characteristic.getValue());
            this.onReadCallback.onCompletion(true, BluetoothError.Unknown);
            return;
        }
        this.onReadCallback.onCompletion(false, BluetoothError.Unknown);
    }

    public void onDescriptorWrite(BluetoothGattDescriptor descriptor, int status) {
        boolean succeeded = true;
        Log.d(TAG, String.format("onDescriptorWrite status:%d", new Object[]{Integer.valueOf(status)}));
        this.serialExecutor.maybeAssertOnThread();
        if (status != 0) {
            succeeded = false;
        }
        if (this.onEnableNotifyCallback != null) {
            this.onEnableNotifyCallback.onCompletion(succeeded, BluetoothError.Unknown);
            this.onEnableNotifyCallback = null;
        } else if (this.onDisableNotifyCallback != null) {
            this.onDisableNotifyCallback.onCompletion(succeeded, BluetoothError.Unknown);
            this.onDisableNotifyCallback = null;
        }
    }
}
