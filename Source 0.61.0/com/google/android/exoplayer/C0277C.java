package com.google.android.exoplayer;

import com.google.android.exoplayer.util.Util;
import com.google.android.gms.location.places.Place;

/* renamed from: com.google.android.exoplayer.C */
public final class C0277C {
    public static final int CHANNEL_OUT_7POINT1_SURROUND;
    public static final int CRYPTO_MODE_AES_CTR = 1;
    public static final int ENCODING_AC3 = 5;
    public static final int ENCODING_DTS = 7;
    public static final int ENCODING_DTS_HD = 8;
    public static final int ENCODING_E_AC3 = 6;
    public static final int LENGTH_UNBOUNDED = -1;
    public static final long MATCH_LONGEST_US = -2;
    public static final long MICROS_PER_SECOND = 1000000;
    public static final int RESULT_END_OF_INPUT = -1;
    public static final int RESULT_MAX_LENGTH_EXCEEDED = -2;
    public static final int SAMPLE_FLAG_DECODE_ONLY = 134217728;
    public static final int SAMPLE_FLAG_ENCRYPTED = 2;
    public static final int SAMPLE_FLAG_SYNC = 1;
    public static final long UNKNOWN_TIME_US = -1;
    public static final String UTF8_NAME = "UTF-8";

    static {
        CHANNEL_OUT_7POINT1_SURROUND = Util.SDK_INT < 23 ? Place.TYPE_ROUTE : 6396;
    }

    private C0277C() {
    }
}
