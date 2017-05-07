package com.crittercism.internal;

import android.location.Location;
import android.os.Build;
import android.os.Build.VERSION;
import com.crittercism.internal.aw.C0195b;
import com.google.android.exoplayer.extractor.ts.PtsTimestampAdjuster;
import com.mopub.volley.DefaultRetryPolicy;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.BuildConfig;

/* renamed from: com.crittercism.internal.b */
public final class C0212b implements bf {
    public long f240a;
    long f241b;
    boolean f242c;
    String f243d;
    float f244e;
    C0211c f245f;
    long f246g;
    public long f247h;
    public int f248i;
    public String f249j;
    public bj f250k;
    double[] f251l;
    public C0257f f252m;
    public String f253n;
    public C0176a f254o;
    private boolean f255p;
    private boolean f256q;
    private boolean f257r;
    private boolean f258s;

    /* renamed from: com.crittercism.internal.b.a */
    public static class C0209a extends bv {
        private au f223c;

        public final bs m221a(as asVar, List<? extends bf> list) {
            if (list.size() == 0) {
                throw new IOException("No events provided");
            }
            JSONObject jSONObject = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            JSONArray jSONArray2 = new JSONArray();
            jSONArray2.put(this.f223c.f207e);
            jSONArray2.put(this.f223c.f203a.f46a);
            jSONArray2.put(this.f223c.m185h());
            jSONArray2.put("5.8.2");
            jSONArray2.put(this.f223c.f208f.f402a.getInt("sessionIDSetting", 0));
            jSONArray.put(jSONArray2);
            jSONArray2 = new JSONArray();
            jSONArray2.put(cf.f482a.m373a());
            jSONArray2.put(this.f223c.m179b());
            jSONArray2.put(Build.MODEL);
            jSONArray2.put("Android");
            jSONArray2.put(VERSION.RELEASE);
            jSONArray2.put(this.f223c.m180c());
            jSONArray2.put(this.f223c.m181d());
            jSONArray.put(jSONArray2);
            JSONArray jSONArray3 = new JSONArray();
            for (bf bfVar : list) {
                jSONArray3.put(((C0212b) bfVar).m237e());
            }
            jSONArray.put(jSONArray3);
            try {
                jSONObject.put("d", jSONArray);
                return bs.m314a(new URL(asVar.f177a, "/api/apm/network"), this.a, jSONObject);
            } catch (JSONException e) {
                throw new IOException(e.getMessage());
            }
        }

        public C0209a(au auVar) {
            super(auVar.f207e, auVar.m185h());
            this.f223c = auVar;
        }
    }

    /* renamed from: com.crittercism.internal.b.b */
    public static class C0210b implements C0195b<C0212b> {
        private C0210b() {
        }

        public final /* synthetic */ bf m223a(File file) {
            return C0210b.m222b(file);
        }

        public final /* synthetic */ void m224a(bf bfVar, OutputStream outputStream) {
            C0212b c0212b = (C0212b) bfVar;
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("Sequence Number", c0212b.f243d).put("rate", (double) c0212b.f244e).put("Request Method", c0212b.f249j).put("Uri", c0212b.m225a()).put("Time Stamp", cf.f482a.m374a(new Date(c0212b.f240a))).put("Response Time", c0212b.m230b()).put("Network Status", c0212b.f254o.f31e).put("Bytes In", c0212b.f246g).put("Bytes Out", c0212b.f247h).put("Return Code", c0212b.f248i).put("Error Type", c0212b.f250k.f304a).put("Error Code", c0212b.f250k.f305b);
                if (c0212b.f251l != null) {
                    JSONArray jSONArray = new JSONArray();
                    if (c0212b.f251l.length == 2) {
                        jSONArray.put(c0212b.f251l[0]);
                        jSONArray.put(c0212b.f251l[1]);
                        jSONObject.put("Location", jSONArray);
                    }
                }
                outputStream.write(jSONObject.toString().getBytes());
            } catch (JSONException e) {
                throw new IOException("Bad values pased to write to stream");
            }
        }

        private static C0212b m222b(File file) {
            String b = cd.m366b(file);
            C0212b c0212b = new C0212b();
            try {
                JSONObject jSONObject = new JSONObject(b);
                c0212b.f243d = jSONObject.getString("Sequence Number");
                c0212b.f249j = jSONObject.getString("Request Method");
                c0212b.f253n = jSONObject.getString("Uri");
                c0212b.f240a = cf.f482a.m372a(jSONObject.getString("Time Stamp"));
                c0212b.f241b = c0212b.f240a + jSONObject.getLong("Response Time");
                c0212b.f254o = C0176a.m23a(jSONObject.getInt("Network Status"));
                c0212b.f246g = jSONObject.getLong("Bytes In");
                c0212b.f247h = jSONObject.getLong("Bytes Out");
                c0212b.f248i = jSONObject.getInt("Return Code");
                c0212b.f250k = new bj(jSONObject.getInt("Error Type"), jSONObject.getInt("Error Code"));
                if (jSONObject.has("Location")) {
                    JSONArray jSONArray = jSONObject.getJSONArray("Location");
                    c0212b.f251l = new double[]{jSONArray.getDouble(0), jSONArray.getDouble(1)};
                }
                c0212b.f244e = (float) jSONObject.getDouble("rate");
                return c0212b;
            } catch (JSONException e) {
                throw new IOException(e.getMessage());
            } catch (ParseException e2) {
                throw new IOException(e2.getMessage());
            }
        }
    }

    /* renamed from: com.crittercism.internal.b.c */
    public enum C0211c {
        NOT_LOGGED_YET("Not logged"),
        INPUT_STREAM_READ("InputStream.read()"),
        INPUT_STREAM_CLOSE("InputStream.close()"),
        SOCKET_CLOSE("Socket.close()"),
        LEGACY_JAVANET("Legacy java.net"),
        HTTP_CONTENT_LENGTH_PARSER("parse()"),
        INPUT_STREAM_FINISHED("finishedMessage()"),
        PARSING_INPUT_STREAM_LOG_ERROR("logError()"),
        SOCKET_IMPL_CONNECT("MonitoredSocketImpl.connect()"),
        SSL_SOCKET_START_HANDSHAKE("MonitoredSSLSocketKK.startHandshake"),
        UNIT_TEST("Unit test"),
        LOG_ENDPOINT("logEndpoint"),
        WEBVIEW_CLIENT_ON_PAGE_FINISHED("onPageFinished"),
        WEBVIEW_CLIENT_ON_RECEIVED_ERROR("onReceivedError");
        
        private String f239o;

        private C0211c(String str) {
            this.f239o = str;
        }

        public final String toString() {
            return this.f239o;
        }
    }

    public final /* synthetic */ Object m239g() {
        return m237e();
    }

    public C0212b() {
        this.f240a = PtsTimestampAdjuster.DO_NOT_OFFSET;
        this.f241b = PtsTimestampAdjuster.DO_NOT_OFFSET;
        this.f255p = false;
        this.f256q = false;
        this.f242c = false;
        this.f244e = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        this.f245f = C0211c.NOT_LOGGED_YET;
        this.f246g = 0;
        this.f247h = 0;
        this.f257r = false;
        this.f258s = false;
        this.f248i = 0;
        this.f249j = BuildConfig.FLAVOR;
        this.f250k = new bj(null);
        this.f252m = new C0257f();
        this.f254o = C0176a.MOBILE;
        this.f243d = be.f269a.m269a();
    }

    public C0212b(String str) {
        this.f240a = PtsTimestampAdjuster.DO_NOT_OFFSET;
        this.f241b = PtsTimestampAdjuster.DO_NOT_OFFSET;
        this.f255p = false;
        this.f256q = false;
        this.f242c = false;
        this.f244e = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        this.f245f = C0211c.NOT_LOGGED_YET;
        this.f246g = 0;
        this.f247h = 0;
        this.f257r = false;
        this.f258s = false;
        this.f248i = 0;
        this.f249j = BuildConfig.FLAVOR;
        this.f250k = new bj(null);
        this.f252m = new C0257f();
        this.f254o = C0176a.MOBILE;
        this.f243d = be.f269a.m269a();
        if (str != null) {
            this.f253n = str;
        }
    }

    public final void m226a(long j) {
        this.f257r = true;
        this.f246g = j;
    }

    public final void m231b(long j) {
        this.f258s = true;
        this.f247h = j;
    }

    public final String m225a() {
        boolean z = true;
        String str = this.f253n;
        if (str == null) {
            C0257f c0257f = this.f252m;
            String str2 = "unknown-host";
            if (c0257f.f515b != null) {
                str = c0257f.f515b;
            } else if (c0257f.f514a != null) {
                str = c0257f.f514a.getHostName();
            } else {
                str = str2;
            }
            if (c0257f.f519f) {
                int i = c0257f.f518e;
                if (i > 0) {
                    str2 = new StringBuilder(UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR).append(i).toString();
                    if (!str.endsWith(str2)) {
                        str = str + str2;
                    }
                }
            } else {
                str2 = c0257f.f516c;
                String str3 = BuildConfig.FLAVOR;
                if (str2 == null || !(str2.regionMatches(true, 0, "http:", 0, 5) || str2.regionMatches(true, 0, "https:", 0, 6))) {
                    z = false;
                }
                if (z) {
                    str = str2;
                } else {
                    String str4;
                    if (c0257f.f517d != null) {
                        str4 = str3 + c0257f.f517d.f512c + UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR;
                    } else {
                        str4 = str3;
                    }
                    if (str2.startsWith("//")) {
                        str = str4 + str2;
                    } else {
                        String str5 = str4 + "//";
                        if (str2.startsWith(str)) {
                            str = str5 + str2;
                        } else {
                            str4 = BuildConfig.FLAVOR;
                            if (c0257f.f518e > 0 && (c0257f.f517d == null || c0257f.f517d.f513d != c0257f.f518e)) {
                                String stringBuilder = new StringBuilder(UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR).append(c0257f.f518e).toString();
                                if (!str.endsWith(stringBuilder)) {
                                    str4 = stringBuilder;
                                }
                            }
                            str = str5 + str + str4 + str2;
                        }
                    }
                }
            }
            this.f253n = str;
        }
        return str;
    }

    public final void m228a(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        this.f253n = str;
    }

    public final long m230b() {
        if (this.f240a == PtsTimestampAdjuster.DO_NOT_OFFSET || this.f241b == PtsTimestampAdjuster.DO_NOT_OFFSET) {
            return PtsTimestampAdjuster.DO_NOT_OFFSET;
        }
        return this.f241b - this.f240a;
    }

    public final void m234c(long j) {
        this.f240a = j;
        this.f255p = true;
    }

    public final void m233c() {
        if (!this.f255p && this.f240a == PtsTimestampAdjuster.DO_NOT_OFFSET) {
            this.f240a = System.currentTimeMillis();
        }
    }

    public final void m236d(long j) {
        this.f241b = j;
        this.f256q = true;
    }

    public final void m235d() {
        if (!this.f256q && this.f241b == PtsTimestampAdjuster.DO_NOT_OFFSET) {
            this.f241b = System.currentTimeMillis();
        }
    }

    public final void m227a(Location location) {
        this.f251l = new double[]{location.getLatitude(), location.getLongitude()};
    }

    public final String toString() {
        String str = (((((((((((((((BuildConfig.FLAVOR + "URI            : " + this.f253n + IOUtils.LINE_SEPARATOR_UNIX) + "URI Builder    : " + this.f252m.toString() + IOUtils.LINE_SEPARATOR_UNIX) + IOUtils.LINE_SEPARATOR_UNIX) + "Logged by      : " + this.f245f.toString() + IOUtils.LINE_SEPARATOR_UNIX) + "Error type:         : " + this.f250k.f304a + IOUtils.LINE_SEPARATOR_UNIX) + "Error code:         : " + this.f250k.f305b + IOUtils.LINE_SEPARATOR_UNIX) + IOUtils.LINE_SEPARATOR_UNIX) + "Response time  : " + m230b() + IOUtils.LINE_SEPARATOR_UNIX) + "Start time     : " + this.f240a + IOUtils.LINE_SEPARATOR_UNIX) + "End time       : " + this.f241b + IOUtils.LINE_SEPARATOR_UNIX) + IOUtils.LINE_SEPARATOR_UNIX) + "Bytes out    : " + this.f247h + IOUtils.LINE_SEPARATOR_UNIX) + "Bytes in     : " + this.f246g + IOUtils.LINE_SEPARATOR_UNIX) + IOUtils.LINE_SEPARATOR_UNIX) + "Response code  : " + this.f248i + IOUtils.LINE_SEPARATOR_UNIX) + "Request method : " + this.f249j + IOUtils.LINE_SEPARATOR_UNIX;
        if (this.f251l != null) {
            return str + "Location       : " + Arrays.toString(this.f251l) + IOUtils.LINE_SEPARATOR_UNIX;
        }
        return str;
    }

    public final JSONArray m237e() {
        JSONArray jSONArray = new JSONArray();
        try {
            jSONArray.put(this.f249j);
            jSONArray.put(m225a());
            jSONArray.put(cf.f482a.m374a(new Date(this.f240a)));
            jSONArray.put(m230b());
            jSONArray.put(this.f254o.f31e);
            jSONArray.put(this.f246g);
            jSONArray.put(this.f247h);
            jSONArray.put(this.f248i);
            jSONArray.put(this.f250k.f304a);
            jSONArray.put(this.f250k.f305b);
            if (this.f251l == null) {
                return jSONArray;
            }
            JSONArray jSONArray2 = new JSONArray();
            jSONArray2.put(this.f251l[0]);
            jSONArray2.put(this.f251l[1]);
            jSONArray.put(jSONArray2);
            return jSONArray;
        } catch (Exception e) {
            Exception exception = e;
            System.out.println("Failed to create statsArray");
            exception.printStackTrace();
            return null;
        }
    }

    public final void m229a(Throwable th) {
        this.f250k = new bj(th);
    }

    public final void m232b(String str) {
        this.f253n = null;
        this.f252m.f515b = str;
    }

    public final String m238f() {
        return this.f243d;
    }
}
