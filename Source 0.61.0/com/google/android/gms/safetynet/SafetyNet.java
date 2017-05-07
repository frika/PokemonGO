package com.google.android.gms.safetynet;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions.NoOptions;
import com.google.android.gms.common.api.Api.zza;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Api.zzc;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.internal.zzqn;
import com.google.android.gms.internal.zzqo;
import com.google.android.gms.internal.zzqp;

public final class SafetyNet {
    public static final Api<NoOptions> API;
    public static final SafetyNetApi SafetyNetApi;
    public static final zzc<zzqo> zzRk;
    public static final zza<zzqo, NoOptions> zzRl;
    public static final zzc zzaUC;

    /* renamed from: com.google.android.gms.safetynet.SafetyNet.1 */
    static class C08391 extends zza<zzqo, NoOptions> {
        C08391() {
        }

        public /* synthetic */ zzb zza(Context context, Looper looper, zzf com_google_android_gms_common_internal_zzf, Object obj, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return zzr(context, looper, com_google_android_gms_common_internal_zzf, (NoOptions) obj, connectionCallbacks, onConnectionFailedListener);
        }

        public zzqo zzr(Context context, Looper looper, zzf com_google_android_gms_common_internal_zzf, NoOptions noOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return new zzqo(context, looper, com_google_android_gms_common_internal_zzf, connectionCallbacks, onConnectionFailedListener);
        }
    }

    static {
        zzRk = new zzc();
        zzRl = new C08391();
        API = new Api("SafetyNet.API", zzRl, zzRk);
        SafetyNetApi = new zzqn();
        zzaUC = new zzqp();
    }

    private SafetyNet() {
    }
}
