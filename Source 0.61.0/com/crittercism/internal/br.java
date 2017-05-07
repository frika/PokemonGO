package com.crittercism.internal;

import android.content.Context;
import android.support.annotation.NonNull;
import com.crittercism.internal.ap.C0190e;
import com.crittercism.internal.ap.C0191a;
import com.crittercism.internal.ap.C0192b;
import com.crittercism.internal.ap.C0194d;
import com.crittercism.internal.bu.C0224a;
import com.crittercism.internal.cc.C0246a;
import com.upsight.mediation.ads.adapters.NetworkWrapper;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

public final class br implements C0224a {
    private au f357a;
    private Context f358b;
    private URL f359c;
    private boolean f360d;
    private ap f361e;

    public br(@NonNull URL url, @NonNull Context context, @NonNull au auVar, @NonNull ap apVar) {
        this.f359c = url;
        this.f358b = context;
        this.f357a = auVar;
        this.f361e = apVar;
    }

    private void m312a(JSONObject jSONObject, C0194d c0194d, C0191a c0191a, C0191a c0191a2, C0192b c0192b) {
        try {
            this.f361e.m129a((C0190e) c0194d, Long.valueOf(jSONObject.getLong("interval") * 1000));
        } catch (JSONException e) {
        }
        try {
            boolean z = jSONObject.getBoolean("enabled");
            this.f361e.m129a((C0190e) c0191a2, Boolean.valueOf(z));
            this.f361e.m129a((C0190e) c0191a, Boolean.valueOf(z));
        } catch (JSONException e2) {
        }
        try {
            this.f361e.m129a((C0190e) c0192b, Float.valueOf((float) jSONObject.getDouble("rate")));
        } catch (JSONException e3) {
        }
    }

    private void m311a(JSONObject jSONObject) {
        try {
            JSONObject jSONObject2 = jSONObject.getJSONObject("txnConfig");
            m312a(jSONObject2, ap.f108A, ap.f141z, ap.f140y, ap.f109B);
            long optLong = jSONObject2.optLong("defaultTimeout", ((Long) ap.f115H.m118b()).longValue());
            this.f361e.m129a(ap.f115H, Long.valueOf(optLong));
            jSONObject2 = jSONObject2.optJSONObject("transactions");
            if (jSONObject2 != null) {
                Iterator keys = jSONObject2.keys();
                while (keys.hasNext()) {
                    String str = (String) keys.next();
                    this.f361e.m130b(str, jSONObject2.getJSONObject(str).optLong(NetworkWrapper.TIMEOUT, optLong));
                }
            }
        } catch (JSONException e) {
        }
    }

    public final void m313a(bt btVar) {
        if (btVar != null && btVar.f367b != null) {
            try {
                JSONObject jSONObject = new JSONObject(new String(btVar.f367b));
                if (jSONObject.optBoolean("internalExceptionReporting", false)) {
                    cc.f472a = C0246a.f461b;
                } else {
                    cc.f472a = C0246a.f462c;
                }
                if (jSONObject.optInt("needPkg", 0) == 1) {
                    try {
                        JSONObject jSONObject2 = new JSONObject();
                        jSONObject2.putOpt("device_name", "Android");
                        jSONObject2.putOpt("pkg", this.f358b.getPackageName());
                        jSONObject2.putOpt("app_id", this.f357a.f207e);
                        jSONObject2.putOpt("hashed_device_id", this.f357a.m185h());
                        jSONObject2.putOpt("library_version", "5.8.2");
                        bs.m314a(new URL(this.f359c + "/android_v2/update_package_name"), this.f357a.f207e, jSONObject2).m315a();
                    } catch (JSONException e) {
                    } catch (IOException e2) {
                    }
                    this.f360d = true;
                }
                try {
                    m312a(jSONObject.getJSONObject("crashConfig"), ap.f122g, ap.f121f, ap.f120e, ap.f123h);
                } catch (JSONException e3) {
                }
                try {
                    m312a(jSONObject.getJSONObject("ndkConfig"), ap.f130o, ap.f129n, ap.f128m, ap.f131p);
                } catch (JSONException e4) {
                }
                try {
                    m312a(jSONObject.getJSONObject("heConfig"), ap.f126k, ap.f125j, ap.f124i, ap.f127l);
                } catch (JSONException e5) {
                }
                try {
                    m312a(jSONObject.getJSONObject("metadataConfig"), ap.f134s, ap.f133r, ap.f132q, ap.f135t);
                } catch (JSONException e6) {
                }
                try {
                    m312a(jSONObject.getJSONObject("apm").getJSONObject("net"), ap.f118c, ap.f117b, ap.f116a, ap.f119d);
                } catch (JSONException e7) {
                }
                m311a(jSONObject);
            } catch (JSONException e8) {
            }
        }
    }
}
