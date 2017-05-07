package com.google.android.gms.internal;

import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import com.google.android.exoplayer.util.NalUnitUtil;
import java.security.MessageDigest;

public class zzbo extends zzbl {
    private MessageDigest zzsw;

    byte[] zza(String[] strArr) {
        byte[] bArr = new byte[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            bArr[i] = zzj(zzbn.zzC(strArr[i]));
        }
        return bArr;
    }

    byte zzj(int i) {
        return (byte) ((((i & NalUnitUtil.EXTENDED_SAR) ^ ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i) >> 8)) ^ ((16711680 & i) >> 16)) ^ ((ViewCompat.MEASURED_STATE_MASK & i) >> 24));
    }

    public byte[] zzz(String str) {
        byte[] zza = zza(str.split(" "));
        this.zzsw = zzcy();
        synchronized (this.zzpd) {
            if (this.zzsw == null) {
                zza = new byte[0];
            } else {
                this.zzsw.reset();
                this.zzsw.update(zza);
                Object digest = this.zzsw.digest();
                int i = 4;
                if (digest.length <= 4) {
                    i = digest.length;
                }
                zza = new byte[i];
                System.arraycopy(digest, 0, zza, 0, zza.length);
            }
        }
        return zza;
    }
}
