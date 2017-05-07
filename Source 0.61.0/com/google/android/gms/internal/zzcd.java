package com.google.android.gms.internal;

import android.text.TextUtils;
import java.util.Map;

@zzgr
public abstract class zzcd {
    @zzgr
    public static final zzcd zzvK;
    @zzgr
    public static final zzcd zzvL;
    @zzgr
    public static final zzcd zzvM;

    /* renamed from: com.google.android.gms.internal.zzcd.1 */
    static class C06501 extends zzcd {
        C06501() {
        }

        public String zzd(String str, String str2) {
            return str2;
        }
    }

    /* renamed from: com.google.android.gms.internal.zzcd.2 */
    static class C06512 extends zzcd {
        C06512() {
        }

        public String zzd(String str, String str2) {
            return str != null ? str : str2;
        }
    }

    /* renamed from: com.google.android.gms.internal.zzcd.3 */
    static class C06523 extends zzcd {
        C06523() {
        }

        private String zzS(String str) {
            if (TextUtils.isEmpty(str)) {
                return str;
            }
            int i = 0;
            int length = str.length();
            while (i < str.length() && str.charAt(i) == ',') {
                i++;
            }
            while (length > 0 && str.charAt(length - 1) == ',') {
                length--;
            }
            return (i == 0 && length == str.length()) ? str : str.substring(i, length);
        }

        public String zzd(String str, String str2) {
            String zzS = zzS(str);
            String zzS2 = zzS(str2);
            return TextUtils.isEmpty(zzS) ? zzS2 : TextUtils.isEmpty(zzS2) ? zzS : zzS + "," + zzS2;
        }
    }

    static {
        zzvK = new C06501();
        zzvL = new C06512();
        zzvM = new C06523();
    }

    public final void zza(Map<String, String> map, String str, String str2) {
        map.put(str, zzd((String) map.get(str), str2));
    }

    public abstract String zzd(String str, String str2);
}
