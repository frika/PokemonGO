package com.google.android.gms.internal;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.internal.zzlb.zza;
import com.google.android.gms.safetynet.SafetyNet;

abstract class zzqk<R extends Result> extends zza<R, zzqo> {
    public zzqk(GoogleApiClient googleApiClient) {
        super(SafetyNet.zzRk, googleApiClient);
    }
}
