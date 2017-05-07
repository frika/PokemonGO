package com.google.android.gms.internal;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.internal.zzqm.zza;
import java.util.List;

public class zzqo extends zzj<zzqm> {
    private final Context mContext;

    public zzqo(Context context, Looper looper, zzf com_google_android_gms_common_internal_zzf, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 45, com_google_android_gms_common_internal_zzf, connectionCallbacks, onConnectionFailedListener);
        this.mContext = context;
    }

    private String zzmi() {
        try {
            PackageManager packageManager = this.mContext.getPackageManager();
            if (packageManager == null) {
                return null;
            }
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.mContext.getPackageName(), AccessibilityNodeInfoCompat.ACTION_CLEAR_ACCESSIBILITY_FOCUS);
            if (applicationInfo == null) {
                return null;
            }
            Bundle bundle = applicationInfo.metaData;
            return bundle == null ? null : (String) bundle.get("com.google.android.safetynet.API_KEY");
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    protected /* synthetic */ IInterface zzW(IBinder iBinder) {
        return zzdI(iBinder);
    }

    public void zza(zzql com_google_android_gms_internal_zzql, List<Integer> list, int i, String str) throws RemoteException {
        int[] iArr = new int[list.size()];
        for (int i2 = 0; i2 < list.size(); i2++) {
            iArr[i2] = ((Integer) list.get(i2)).intValue();
        }
        ((zzqm) zzpc()).zza(com_google_android_gms_internal_zzql, zzmi(), iArr, i, str);
    }

    public void zza(zzql com_google_android_gms_internal_zzql, byte[] bArr) throws RemoteException {
        ((zzqm) zzpc()).zza(com_google_android_gms_internal_zzql, bArr);
    }

    protected zzqm zzdI(IBinder iBinder) {
        return zza.zzdH(iBinder);
    }

    protected String zzfK() {
        return "com.google.android.gms.safetynet.service.START";
    }

    protected String zzfL() {
        return "com.google.android.gms.safetynet.internal.ISafetyNetService";
    }
}
