package com.google.android.gms.internal;

import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdRequest.Gender;
import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.internal.client.AdRequestParcel;
import com.google.android.gms.ads.internal.client.AdSizeParcel;
import com.google.android.gms.ads.zza;
import java.util.Date;
import java.util.HashSet;
import spacemadness.com.lunarconsole.C1628R;

@zzgr
public final class zzfa {

    /* renamed from: com.google.android.gms.internal.zzfa.1 */
    static /* synthetic */ class C06971 {
        static final /* synthetic */ int[] zzzT;
        static final /* synthetic */ int[] zzzU;

        static {
            zzzU = new int[ErrorCode.values().length];
            try {
                zzzU[ErrorCode.INTERNAL_ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                zzzU[ErrorCode.INVALID_REQUEST.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                zzzU[ErrorCode.NETWORK_ERROR.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                zzzU[ErrorCode.NO_FILL.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            zzzT = new int[Gender.values().length];
            try {
                zzzT[Gender.FEMALE.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                zzzT[Gender.MALE.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            try {
                zzzT[Gender.UNKNOWN.ordinal()] = 3;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    public static int zza(ErrorCode errorCode) {
        switch (C06971.zzzU[errorCode.ordinal()]) {
            case C1628R.styleable.RecyclerView_spanCount /*2*/:
                return 1;
            case C1628R.styleable.RecyclerView_reverseLayout /*3*/:
                return 2;
            case C1628R.styleable.RecyclerView_stackFromEnd /*4*/:
                return 3;
            default:
                return 0;
        }
    }

    public static AdSize zzb(AdSizeParcel adSizeParcel) {
        int i = 0;
        AdSize[] adSizeArr = new AdSize[]{AdSize.SMART_BANNER, AdSize.BANNER, AdSize.IAB_MRECT, AdSize.IAB_BANNER, AdSize.IAB_LEADERBOARD, AdSize.IAB_WIDE_SKYSCRAPER};
        while (i < adSizeArr.length) {
            if (adSizeArr[i].getWidth() == adSizeParcel.width && adSizeArr[i].getHeight() == adSizeParcel.height) {
                return adSizeArr[i];
            }
            i++;
        }
        return new AdSize(zza.zza(adSizeParcel.width, adSizeParcel.height, adSizeParcel.zzte));
    }

    public static MediationAdRequest zzh(AdRequestParcel adRequestParcel) {
        return new MediationAdRequest(new Date(adRequestParcel.zzsB), zzr(adRequestParcel.zzsC), adRequestParcel.zzsD != null ? new HashSet(adRequestParcel.zzsD) : null, adRequestParcel.zzsE, adRequestParcel.zzsJ);
    }

    public static Gender zzr(int i) {
        switch (i) {
            case C1628R.styleable.RecyclerView_layoutManager /*1*/:
                return Gender.MALE;
            case C1628R.styleable.RecyclerView_spanCount /*2*/:
                return Gender.FEMALE;
            default:
                return Gender.UNKNOWN;
        }
    }
}
