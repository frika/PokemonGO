package com.nianticlabs.nia.platform;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi.AttestationResult;
import com.nianticlabs.nia.contextservice.ContextService;
import com.nianticlabs.nia.contextservice.GoogleApiManager;
import com.nianticlabs.nia.contextservice.GoogleApiManager.Listener;
import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class SafetyNetService extends ContextService {
    private Listener googleApiListener;
    private final GoogleApiManager googleApiManager;
    private GoogleApiState googleApiState;
    private final Object lock;
    private byte[] queuedNonce;
    private final Map<ByteBuffer, PendingResult<AttestationResult>> requestMap;

    /* renamed from: com.nianticlabs.nia.platform.SafetyNetService.1 */
    class C09651 implements Listener {
        C09651() {
        }

        public void onConnected() {
            SafetyNetService.this.googleApiState = GoogleApiState.STARTED;
            synchronized (SafetyNetService.this.lock) {
                if (SafetyNetService.this.queuedNonce != null) {
                    byte[] nonce = SafetyNetService.this.queuedNonce;
                    SafetyNetService.this.queuedNonce = null;
                    SafetyNetService.this.attest(nonce);
                }
            }
        }

        public void onDisconnected() {
            SafetyNetService.this.googleApiState = GoogleApiState.STOPPED;
        }

        public void onConnectionFailed(ConnectionResult connectionResult) {
            SafetyNetService.this.googleApiState = GoogleApiState.STOPPED;
        }
    }

    /* renamed from: com.nianticlabs.nia.platform.SafetyNetService.2 */
    class C09672 implements Runnable {
        final /* synthetic */ byte[] val$nonce;

        /* renamed from: com.nianticlabs.nia.platform.SafetyNetService.2.1 */
        class C09661 implements ResultCallback<AttestationResult> {
            final /* synthetic */ ByteBuffer val$nonceBuffer;

            C09661(ByteBuffer byteBuffer) {
                this.val$nonceBuffer = byteBuffer;
            }

            public void onResult(AttestationResult result) {
                synchronized (SafetyNetService.this.lock) {
                    SafetyNetService.this.requestMap.remove(this.val$nonceBuffer);
                }
                if (result.getStatus().isSuccess()) {
                    String jwsResult = result.getJwsResult();
                    if (SafetyNetService.checkResult(jwsResult)) {
                        SafetyNetService.this.attestResponse(C09672.this.val$nonce, jwsResult);
                        return;
                    } else {
                        SafetyNetService.this.attestResponse(C09672.this.val$nonce, null);
                        return;
                    }
                }
                SafetyNetService.this.attestResponse(C09672.this.val$nonce, null);
            }
        }

        C09672(byte[] bArr) {
            this.val$nonce = bArr;
        }

        public void run() {
            if (SafetyNetService.this.googleApiState == GoogleApiState.STARTED) {
                ByteBuffer nonceBuffer = ByteBuffer.wrap(this.val$nonce);
                synchronized (SafetyNetService.this.lock) {
                    if (((PendingResult) SafetyNetService.this.requestMap.get(nonceBuffer)) == null) {
                        PendingResult<AttestationResult> result = SafetyNet.SafetyNetApi.attest(SafetyNetService.this.googleApiManager.getClient(), this.val$nonce);
                        SafetyNetService.this.requestMap.put(nonceBuffer, result);
                        result.setResultCallback(new C09661(nonceBuffer));
                    }
                }
                return;
            }
            boolean sendFailResponse = false;
            synchronized (SafetyNetService.this.lock) {
                if (SafetyNetService.this.queuedNonce == null) {
                    SafetyNetService.this.queuedNonce = this.val$nonce;
                } else {
                    sendFailResponse = true;
                }
            }
            if (sendFailResponse) {
                SafetyNetService.this.attestResponse(this.val$nonce, null);
            }
        }
    }

    /* renamed from: com.nianticlabs.nia.platform.SafetyNetService.3 */
    class C09683 implements Runnable {
        final /* synthetic */ byte[] val$nonce;

        C09683(byte[] bArr) {
            this.val$nonce = bArr;
        }

        public void run() {
            if (SafetyNetService.this.googleApiState == GoogleApiState.STARTED) {
                ByteBuffer nonceBuffer = ByteBuffer.wrap(this.val$nonce);
                synchronized (SafetyNetService.this.lock) {
                    boolean keepLooking = true;
                    if (1 != null) {
                        if (SafetyNetService.this.queuedNonce != null && Arrays.equals(SafetyNetService.this.queuedNonce, this.val$nonce)) {
                            SafetyNetService.this.queuedNonce = null;
                            keepLooking = false;
                        }
                    }
                    if (keepLooking) {
                        PendingResult<AttestationResult> result = (PendingResult) SafetyNetService.this.requestMap.get(nonceBuffer);
                        if (result != null) {
                            SafetyNetService.this.requestMap.remove(nonceBuffer);
                            result.cancel();
                        }
                    }
                }
                return;
            }
            synchronized (SafetyNetService.this.lock) {
                if (SafetyNetService.this.queuedNonce != null && Arrays.equals(SafetyNetService.this.queuedNonce, this.val$nonce)) {
                    SafetyNetService.this.queuedNonce = null;
                }
            }
        }
    }

    private enum GoogleApiState {
        STARTED,
        STOPPED
    }

    private native void nativeAttestResponse(byte[] bArr, String str);

    public SafetyNetService(Context context, long nativeClassPointer) {
        super(context, nativeClassPointer);
        this.googleApiState = GoogleApiState.STOPPED;
        this.queuedNonce = null;
        this.requestMap = new HashMap();
        this.lock = new Object();
        this.googleApiListener = new C09651();
        this.googleApiManager = new GoogleApiManager(context);
        this.googleApiManager.setListener(this.googleApiListener);
        this.googleApiManager.builder().addApi(SafetyNet.API);
        this.googleApiManager.build();
    }

    private static boolean checkResult(String jwsResult) {
        try {
            String[] parts = jwsResult.split("\\.");
            if (parts.length == 3) {
                JSONObject root = new JSONObject(new String(Base64.decode(parts[1], 0)));
                if (!root.has(GameServices.ERROR)) {
                    return true;
                }
                Log.e("SafetyNetService", "Got error from SafetyNet " + root.getString(GameServices.ERROR));
                return false;
            }
        } catch (Throwable th) {
        }
        return false;
    }

    public void attest(byte[] nonce) {
        ContextService.runOnServiceHandler(new C09672(nonce));
    }

    public void cancel(byte[] nonce) {
        ContextService.runOnServiceHandler(new C09683(nonce));
    }

    public void onStart() {
        this.googleApiManager.onStart();
    }

    public void onStop() {
        this.googleApiManager.onStop();
    }

    public void onResume() {
        this.googleApiManager.onResume();
    }

    public void onPause() {
        this.googleApiManager.onPause();
    }

    private void attestResponse(byte[] nonce, String result) {
        synchronized (this.callbackLock) {
            nativeAttestResponse(nonce, result);
        }
    }
}
