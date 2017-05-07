package com.crittercism.internal;

import android.os.Build;
import android.os.Build.VERSION;
import com.crittercism.internal.aw.C0195b;
import com.mopub.volley.DefaultRetryPolicy;
import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class ar implements bf {
    String f163a;
    String f164b;
    String f165c;
    String f166d;
    String f167e;
    String f168f;
    String f169g;
    int f170h;
    int f171i;
    String f172j;
    String f173k;
    long f174l;
    public float f175m;

    /* renamed from: com.crittercism.internal.ar.a */
    public static class C0198a extends bv {
        public C0198a(String str, String str2) {
            super(str, str2);
        }

        public final bs m147a(as asVar, List<? extends bf> list) {
            Map hashMap = new HashMap();
            ar arVar = null;
            for (bf bfVar : list) {
                int intValue;
                ar arVar2 = (ar) bfVar;
                if (hashMap.containsKey(arVar2)) {
                    intValue = ((Integer) hashMap.get(arVar2)).intValue();
                } else {
                    intValue = 0;
                }
                hashMap.put(arVar2, Integer.valueOf(intValue + 1));
                if (arVar2.f174l <= 0) {
                    arVar2 = arVar;
                }
                arVar = arVar2;
            }
            JSONArray jSONArray = new JSONArray();
            try {
                for (ar arVar22 : hashMap.keySet()) {
                    int intValue2 = ((Integer) hashMap.get(arVar22)).intValue();
                    boolean z = arVar != null && arVar.equals(arVar22);
                    jSONArray.put(new JSONObject().put("appLoads", arVar22.m151a()).put("count", intValue2).put("current", z));
                }
                return new bs("POST", new URL(asVar.f180d, "/v0/appload"), jSONArray.toString().getBytes("UTF8"), "application/json", this.a);
            } catch (JSONException e) {
                throw new IOException(e.getMessage());
            }
        }
    }

    /* renamed from: com.crittercism.internal.ar.b */
    public static class C0199b implements C0195b<ar> {
        private C0199b() {
        }

        public final /* synthetic */ bf m149a(File file) {
            return C0199b.m148b(file);
        }

        public final /* synthetic */ void m150a(bf bfVar, OutputStream outputStream) {
            ar arVar = (ar) bfVar;
            try {
                outputStream.write(new JSONObject().putOpt("fileName", arVar.f163a).putOpt("appId", arVar.f164b).putOpt("deviceId", arVar.f165c).putOpt("sdkVersion", arVar.f166d).putOpt("rate", Float.valueOf(arVar.f175m)).putOpt(Models.CONTENT_DIRECTORY, arVar.f167e).putOpt("osVersion", arVar.f168f).putOpt("carrier", arVar.f169g).putOpt("mobileCountryCode", Integer.valueOf(arVar.f170h)).putOpt("mobileNetworkCode", Integer.valueOf(arVar.f171i)).putOpt("appVersion", arVar.f172j).putOpt("locale", arVar.f173k).putOpt(GameServices.USER_TIME_STAMP, Long.valueOf(arVar.f174l)).toString().getBytes("UTF8"));
            } catch (JSONException e) {
                throw new IOException(e.getMessage());
            }
        }

        private static ar m148b(File file) {
            try {
                JSONObject jSONObject = new JSONObject(cd.m366b(file));
                ar arVar = new ar();
                arVar.f163a = jSONObject.getString("fileName");
                arVar.f164b = jSONObject.getString("appId");
                arVar.f165c = jSONObject.getString("deviceId");
                arVar.f166d = jSONObject.getString("sdkVersion");
                arVar.f175m = (float) jSONObject.getDouble("rate");
                arVar.f167e = jSONObject.getString(Models.CONTENT_DIRECTORY);
                arVar.f168f = jSONObject.getString("osVersion");
                arVar.f169g = jSONObject.getString("carrier");
                arVar.f170h = jSONObject.getInt("mobileCountryCode");
                arVar.f171i = jSONObject.getInt("mobileNetworkCode");
                arVar.f172j = jSONObject.getString("appVersion");
                arVar.f173k = jSONObject.getString("locale");
                arVar.f174l = jSONObject.getLong(GameServices.USER_TIME_STAMP);
                return arVar;
            } catch (JSONException e) {
                throw new IOException(e.getMessage());
            }
        }
    }

    public final /* synthetic */ Object m153g() {
        return m151a();
    }

    public ar(au auVar) {
        this.f175m = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        this.f163a = be.f269a.m269a();
        this.f164b = auVar.f207e;
        this.f165c = auVar.m185h();
        this.f166d = "5.8.2";
        this.f167e = Build.MODEL;
        this.f168f = VERSION.RELEASE;
        this.f169g = auVar.m179b();
        this.f170h = auVar.m180c().intValue();
        this.f171i = auVar.m181d().intValue();
        this.f172j = auVar.f203a.f46a;
        this.f173k = auVar.m186i();
        this.f174l = System.nanoTime();
    }

    private ar() {
        this.f175m = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
    }

    public final JSONObject m151a() {
        try {
            return new JSONObject().putOpt("appID", this.f164b).putOpt("deviceID", this.f165c).putOpt("crPlatform", "android").putOpt("crVersion", this.f166d).putOpt("rate", Float.valueOf(this.f175m)).putOpt("deviceModel", this.f167e).putOpt("osName", "android").putOpt("osVersion", this.f168f).putOpt("carrier", this.f169g).putOpt("mobileCountryCode", Integer.valueOf(this.f170h)).putOpt("mobileNetworkCode", Integer.valueOf(this.f171i)).putOpt("appVersion", this.f172j).putOpt("locale", this.f173k);
        } catch (JSONException e) {
            return null;
        }
    }

    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ar arVar = (ar) o;
        if (this.f170h == arVar.f170h && this.f171i == arVar.f171i && this.f164b.equals(arVar.f164b) && this.f165c.equals(arVar.f165c) && this.f166d.equals(arVar.f166d) && Float.compare(this.f175m, arVar.f175m) == 0 && this.f167e.equals(arVar.f167e) && this.f168f.equals(arVar.f168f) && this.f169g.equals(arVar.f169g) && this.f172j.equals(arVar.f172j)) {
            return this.f173k.equals(arVar.f173k);
        }
        return false;
    }

    public final int hashCode() {
        return (((((((((((((((((((this.f164b.hashCode() * 31) + this.f165c.hashCode()) * 31) + this.f166d.hashCode()) * 31) + Float.floatToIntBits(this.f175m)) * 31) + this.f167e.hashCode()) * 31) + this.f168f.hashCode()) * 31) + this.f169g.hashCode()) * 31) + this.f170h) * 31) + this.f171i) * 31) + this.f172j.hashCode()) * 31) + this.f173k.hashCode();
    }

    public final String m152f() {
        return this.f163a;
    }
}
