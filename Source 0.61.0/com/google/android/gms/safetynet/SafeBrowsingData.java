package com.google.android.gms.safetynet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class SafeBrowsingData implements SafeParcelable {
    public static final Creator<SafeBrowsingData> CREATOR;
    public final int mVersionCode;
    private String zzaUB;

    static {
        CREATOR = new zzb();
    }

    SafeBrowsingData(int versionCode, String metadata) {
        this.mVersionCode = versionCode;
        this.zzaUB = metadata;
    }

    public SafeBrowsingData(String metadata) {
        this(1, metadata);
    }

    public int describeContents() {
        return 0;
    }

    public String getMetadata() {
        return this.zzaUB;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzb.zza(this, out, flags);
    }
}
