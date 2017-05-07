package com.crittercism.internal;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public final class ba implements bf {
    String f259a;
    String f260b;

    /* renamed from: com.crittercism.internal.ba.a */
    public static class C0213a extends bv {
        public C0213a(String str, String str2) {
            super(str, str2);
        }

        public final bs m240a(as asVar, List<? extends bf> list) {
            URL url = new URL(asVar.f178b, "/android_v2/update_user_metadata");
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("app_id", this.a);
                jSONObject.put("hashed_device_id", this.b);
                jSONObject.put("library_version", "5.8.2");
                JSONObject jSONObject2 = new JSONObject();
                for (bf bfVar : list) {
                    ba baVar = (ba) bfVar;
                    jSONObject2.put(baVar.f259a, baVar.f260b);
                }
                jSONObject.put(TtmlNode.TAG_METADATA, jSONObject2);
                return bs.m314a(url, this.a, jSONObject);
            } catch (JSONException e) {
                throw new IOException(e.getMessage());
            }
        }
    }

    public ba(String str, String str2) {
        this.f259a = str;
        this.f260b = str2;
    }

    public final String m241f() {
        return this.f259a;
    }

    public final Object m242g() {
        throw new UnsupportedOperationException();
    }
}
