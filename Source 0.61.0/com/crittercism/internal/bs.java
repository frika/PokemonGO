package com.crittercism.internal;

import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import com.mopub.volley.DefaultRetryPolicy;
import com.upsight.mediation.vast.VASTPlayer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import org.json.JSONObject;

public final class bs {
    private final URL f362a;
    private final String f363b;
    private final byte[] f364c;
    private final Map<String, String> f365d;

    public bs(String str, URL url, byte[] bArr, String str2, String str3) {
        this.f363b = str;
        this.f362a = url;
        this.f364c = bArr;
        this.f365d = new HashMap();
        this.f365d.put("Content-type", str2);
        this.f365d.put("CRPlatform", "android");
        this.f365d.put("CRVersion", "5.8.2");
        this.f365d.put("CRAppId", str3);
    }

    public static bs m314a(URL url, String str, JSONObject jSONObject) {
        return new bs("POST", url, jSONObject.toString().getBytes("UTF8"), "application/json", str);
    }

    public final bt m315a() {
        bt btVar;
        Exception e;
        HttpURLConnection httpURLConnection;
        Throwable th;
        HttpURLConnection httpURLConnection2 = null;
        cc.m351a();
        try {
            HttpURLConnection httpURLConnection3 = (HttpURLConnection) this.f362a.openConnection();
            for (Entry entry : this.f365d.entrySet()) {
                httpURLConnection3.addRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }
            httpURLConnection3.setConnectTimeout(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS);
            httpURLConnection3.setReadTimeout(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS);
            httpURLConnection3.setDoOutput(this.f363b.equals("POST"));
            httpURLConnection3.setRequestMethod(this.f363b);
            if (httpURLConnection3 instanceof HttpsURLConnection) {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection3;
                SSLContext instance = SSLContext.getInstance("TLS");
                instance.init(null, null, null);
                SSLSocketFactory socketFactory = instance.getSocketFactory();
                if (socketFactory instanceof C0262k) {
                    socketFactory = ((C0262k) socketFactory).m417a();
                }
                httpsURLConnection.setSSLSocketFactory(socketFactory);
            }
            try {
                byte[] a;
                httpURLConnection3.getOutputStream().write(this.f364c);
                int responseCode = httpURLConnection3.getResponseCode();
                if (responseCode < Callback.DEFAULT_DRAG_ANIMATION_DURATION || responseCode >= VASTPlayer.ERROR_GENERAL_WRAPPER) {
                    a = cd.m365a(httpURLConnection3.getErrorStream());
                } else {
                    a = cd.m365a(httpURLConnection3.getInputStream());
                }
                bt btVar2 = new bt(responseCode, a);
                if (httpURLConnection3 != null) {
                    httpURLConnection3.disconnect();
                    btVar = btVar2;
                } else {
                    btVar = btVar2;
                }
            } catch (Exception e2) {
                e = e2;
                httpURLConnection = httpURLConnection3;
                try {
                    btVar = new bt(e);
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                    cc.m356b();
                    return btVar;
                } catch (Throwable th2) {
                    th = th2;
                    httpURLConnection2 = httpURLConnection;
                    if (httpURLConnection2 != null) {
                        httpURLConnection2.disconnect();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                httpURLConnection2 = httpURLConnection3;
                th = th3;
                if (httpURLConnection2 != null) {
                    httpURLConnection2.disconnect();
                }
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            httpURLConnection = null;
            btVar = new bt(e);
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            cc.m356b();
            return btVar;
        } catch (Throwable th4) {
            th = th4;
            if (httpURLConnection2 != null) {
                httpURLConnection2.disconnect();
            }
            throw th;
        }
        cc.m356b();
        return btVar;
    }
}
