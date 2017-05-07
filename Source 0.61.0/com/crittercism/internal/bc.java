package com.crittercism.internal;

import android.os.Build;
import android.os.Build.VERSION;
import android.util.Base64;
import com.crittercism.internal.aw.C0195b;
import com.mopub.volley.DefaultRetryPolicy;
import com.voxelbusters.nativeplugins.defines.Keys.Twitter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class bc implements bf {
    JSONObject f263a;
    JSONArray f264b;
    String f265c;
    String f266d;
    String f267e;
    public float f268f;

    /* renamed from: com.crittercism.internal.bc.a */
    public static class C0214a extends bv {
        public C0214a(String str, String str2) {
            super(str, str2);
        }

        public final bs m254a(as asVar, List<? extends bf> list) {
            URL url = new URL(asVar.f178b, "/android_v2/handle_ndk_crashes");
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("app_id", this.a);
                jSONObject.put("hashed_device_id", this.b);
                jSONObject.put("library_version", "5.8.2");
                JSONArray jSONArray = new JSONArray();
                for (bf g : list) {
                    jSONArray.put(g.m139g());
                }
                jSONObject.put("crashes", jSONArray);
                return bs.m314a(url, this.a, jSONObject);
            } catch (JSONException e) {
                throw new IOException(e.getMessage());
            }
        }
    }

    /* renamed from: com.crittercism.internal.bc.b */
    public static class C0215b implements C0195b<bc> {
        private C0215b() {
        }

        public final /* synthetic */ bf m256a(File file) {
            return C0215b.m255b(file);
        }

        public final /* synthetic */ void m257a(bf bfVar, OutputStream outputStream) {
            bc bcVar = (bc) bfVar;
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("appState", bcVar.f263a);
                jSONObject.put("breadcrumbs", bcVar.f264b);
                jSONObject.put("crashDumpFileName", bcVar.f265c);
                jSONObject.put("base64EncodedCrash", bcVar.f266d);
                jSONObject.put("fileName", bcVar.f267e);
                jSONObject.put("rate", (double) bcVar.f268f);
                outputStream.write(jSONObject.toString().getBytes());
            } catch (JSONException e) {
                throw new IOException(e.getMessage());
            }
        }

        private static bc m255b(File file) {
            try {
                JSONObject jSONObject = new JSONObject(cd.m366b(file));
                bc bcVar = new bc();
                bcVar.f263a = jSONObject.getJSONObject("appState");
                bcVar.f264b = jSONObject.getJSONArray("breadcrumbs");
                bcVar.f265c = jSONObject.getString("crashDumpFileName");
                bcVar.f266d = jSONObject.getString("base64EncodedCrash");
                bcVar.f267e = jSONObject.getString("fileName");
                bcVar.f268f = (float) jSONObject.getDouble("rate");
                return bcVar;
            } catch (JSONException e) {
                throw new IOException(e.getMessage());
            }
        }
    }

    private bc(File file, av<at> avVar, au auVar) {
        this.f268f = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
        this.f267e = be.f269a.m269a();
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        byte[] bArr = new byte[((int) randomAccessFile.length())];
        randomAccessFile.read(bArr);
        this.f266d = new String(Base64.encode(bArr, 0));
        this.f265c = file.getName();
        this.f263a = new JSONObject();
        try {
            this.f263a.putOpt("app_version", auVar.f203a.f46a).putOpt("app_version_code", auVar.m177a()).putOpt("arch", System.getProperty("os.arch")).putOpt("carrier", auVar.m179b()).putOpt("mobile_country_code", auVar.m180c()).putOpt("mobile_network_code", auVar.m181d()).putOpt("disk_space_total", auVar.m188k()).putOpt("dpi", auVar.m182e()).putOpt("xdpi", Float.valueOf(auVar.m183f())).putOpt("ydpi", Float.valueOf(auVar.m184g())).putOpt("locale", auVar.m186i()).putOpt(Models.CONTENT_DIRECTORY, Build.MODEL).putOpt("memory_total", au.m176l()).putOpt(Twitter.NAME, new String()).putOpt("system", "android").putOpt("system_version", VERSION.RELEASE);
        } catch (JSONException e) {
        }
        this.f264b = avVar.m192a();
    }

    private bc() {
        this.f268f = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
    }

    public static bc m258a(File file, av<at> avVar, au auVar) {
        if (!file.exists() || !file.isDirectory()) {
            return null;
        }
        File[] listFiles = file.listFiles();
        if (listFiles == null || listFiles.length == 0) {
            return null;
        }
        bc bcVar;
        if (listFiles.length == 1) {
            File file2 = listFiles[0];
            if (file2.isFile()) {
                try {
                    bcVar = new bc(file2, avVar, auVar);
                } catch (ThreadDeath e) {
                    throw e;
                } catch (Throwable th) {
                }
                for (File a : listFiles) {
                    cd.m364a(a);
                }
                return bcVar;
            }
        }
        bcVar = null;
        while (r1 < r4) {
            cd.m364a(a);
        }
        return bcVar;
    }

    public final String m259f() {
        return this.f267e;
    }

    public final /* synthetic */ Object m260g() {
        Map hashMap = new HashMap();
        hashMap.put("app_state", this.f263a);
        hashMap.put("breadcrumbs", new JSONObject());
        hashMap.put("endpoints", new JSONArray());
        hashMap.put("systemBreadcrumbs", this.f264b);
        hashMap.put("rate", Float.valueOf(this.f268f));
        Map hashMap2 = new HashMap();
        hashMap2.put("dmp_name", this.f265c);
        hashMap2.put("dmp_file", this.f266d);
        hashMap.put("ndk_dmp_info", new JSONObject(hashMap2));
        return new JSONObject(hashMap);
    }
}
