package com.crittercism.internal;

import com.crittercism.internal.aw.C0195b;
import com.upsight.android.marketing.internal.content.MarketingContent.PendingDialog;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class at implements bf {
    public String f199a;
    String f200b;
    public int f201c;
    Object f202d;

    /* renamed from: com.crittercism.internal.at.a */
    public static class C0200a implements C0195b<at> {
        private C0200a() {
        }

        public final /* synthetic */ bf m155a(File file) {
            return C0200a.m154b(file);
        }

        public final /* synthetic */ void m156a(bf bfVar, OutputStream outputStream) {
            at atVar = (at) bfVar;
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put(GameServices.USER_TIME_STAMP, atVar.f200b);
                jSONObject.put("filename", atVar.f199a);
                jSONObject.put(Keys.TYPE, atVar.f201c - 1);
                jSONObject.put("payload", atVar.f202d);
                outputStream.write(jSONObject.toString().getBytes());
            } catch (JSONException e) {
                throw new IOException(e.getMessage());
            }
        }

        private static at m154b(File file) {
            try {
                JSONObject jSONObject = new JSONObject(cd.m366b(file));
                return new at(jSONObject.getString(GameServices.USER_TIME_STAMP), C0201b.m157a()[jSONObject.getInt(Keys.TYPE)], jSONObject.has("payload") ? jSONObject.get("payload") : null, (byte) 0);
            } catch (JSONException e) {
                throw new IOException(e.getMessage());
            } catch (IndexOutOfBoundsException e2) {
                throw new IOException(e2.getMessage());
            }
        }
    }

    /* renamed from: com.crittercism.internal.at.b */
    public enum C0201b {
        ;

        public static int[] m157a() {
            return (int[]) f189i.clone();
        }

        static {
            f181a = 1;
            f182b = 2;
            f183c = 3;
            f184d = 4;
            f185e = 5;
            f186f = 6;
            f187g = 7;
            f188h = 8;
            f189i = new int[]{f181a, f182b, f183c, f184d, f185e, f186f, f187g, f188h};
        }
    }

    /* renamed from: com.crittercism.internal.at.c */
    public enum C0202c {
        ;

        static {
            f190a = 1;
            f191b = 2;
            f192c = 3;
            f193d = 4;
            f194e = 5;
            f195f = new int[]{f190a, f191b, f192c, f193d, f194e};
        }
    }

    /* renamed from: com.crittercism.internal.at.d */
    public enum C0203d {
        ;

        static {
            f196a = 1;
            f197b = 2;
            f198c = new int[]{f196a, f197b};
        }
    }

    public final /* synthetic */ Object m163g() {
        return m161a();
    }

    public at(int i, Object obj) {
        this(be.f269a.m269a(), cf.f482a.m373a(), i, obj);
    }

    private at(Date date, int i) {
        this(be.f269a.m269a(), cf.f482a.m374a(date), i, null);
    }

    private at(String str, String str2, int i, Object obj) {
        this.f199a = str;
        this.f200b = str2;
        this.f201c = i;
        this.f202d = obj;
    }

    public static at m160a(Date date) {
        return new at(date, C0201b.f181a);
    }

    public static at m159a(String str) {
        if (str.length() > 140) {
            str = str.substring(0, 140);
        }
        Map hashMap = new HashMap();
        hashMap.put(PendingDialog.TEXT, str);
        hashMap.put("level", Integer.valueOf(0));
        return new at(C0201b.f182b, new JSONObject(hashMap));
    }

    public static at m158a(int i, String str) {
        if (i != C0202c.f192c) {
            int i2 = C0202c.f193d;
        }
        Map hashMap = new HashMap();
        hashMap.put("change", Integer.valueOf(i - 1));
        hashMap.put(Keys.TYPE, str);
        return new at(C0201b.f185e, new JSONObject(hashMap));
    }

    public final String m162f() {
        return this.f199a;
    }

    private JSONArray m161a() {
        JSONArray put = new JSONArray().put(this.f200b).put(this.f201c - 1);
        if (this.f202d != null) {
            put.put(this.f202d);
        }
        return put;
    }

    public final String toString() {
        try {
            return m161a().toString(4);
        } catch (JSONException e) {
            return e.toString();
        }
    }
}
