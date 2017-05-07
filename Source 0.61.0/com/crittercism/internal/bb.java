package com.crittercism.internal;

import android.content.Context;
import android.content.SharedPreferences;
import com.crittercism.internal.av.C0207a;
import com.google.android.exoplayer.extractor.ts.PtsTimestampAdjuster;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class bb implements av<ba> {
    private SharedPreferences f261a;
    private final List<C0207a> f262b;

    public bb(Context context, String str) {
        this.f262b = new LinkedList();
        this.f261a = context.getSharedPreferences("com.crittercism." + str + ".usermetadata.v2", 0);
    }

    private boolean m244d() {
        return this.f261a.getBoolean("dirty", false);
    }

    private boolean m245e() {
        Map all = this.f261a.getAll();
        String str = null;
        long j = PtsTimestampAdjuster.DO_NOT_OFFSET;
        for (String str2 : all.keySet()) {
            if (str2.startsWith("__timestamp_")) {
                String str3;
                long j2;
                long j3;
                if (str == null || j > this.f261a.getLong(str2, j)) {
                    j3 = this.f261a.getLong(str2, j);
                    str3 = str2;
                    j2 = j3;
                } else {
                    j3 = j;
                    str3 = str;
                    j2 = j3;
                }
                str = str3;
                j = j2;
            }
        }
        if (str == null) {
            return false;
        }
        return this.f261a.edit().remove(str).remove(str.substring(12)).commit();
    }

    private boolean m243a(ba baVar) {
        String string = this.f261a.getString(baVar.f259a, null);
        String str = baVar.f259a;
        String str2 = "__timestamp_" + baVar.f259a;
        if (baVar.f260b.equals(string)) {
            this.f261a.edit().putLong(str2, System.nanoTime()).commit();
            return true;
        } else if ((this.f261a.getAll().size() - 1) / 2 >= 25 && !this.f261a.contains(baVar.f259a) && !m245e()) {
            return false;
        } else {
            boolean commit = this.f261a.edit().putString(str, baVar.f260b).putLong(str2, System.nanoTime()).putBoolean("dirty", true).commit();
            if (commit) {
                for (C0207a a : this.f262b) {
                    a.m191a();
                }
            }
            return commit;
        }
    }

    public final boolean m251a(JSONObject jSONObject) {
        Iterator keys = jSONObject.keys();
        boolean z = true;
        while (keys.hasNext()) {
            try {
                String str = (String) keys.next();
                z = m243a(new ba(str, jSONObject.get(str).toString())) & z;
            } catch (JSONException e) {
            }
        }
        return z;
    }

    public final void m248a(String str) {
        throw new UnsupportedOperationException();
    }

    public final JSONArray m246a() {
        JSONArray jSONArray = new JSONArray();
        Iterator it = m252b().iterator();
        if (!it.hasNext()) {
            return jSONArray;
        }
        it.next();
        throw new UnsupportedOperationException();
    }

    public final List<ba> m252b() {
        Map all = this.f261a.getAll();
        List<ba> linkedList = new LinkedList();
        for (String str : all.keySet()) {
            if (!(str.startsWith("__timestamp_") || str.equals("dirty"))) {
                linkedList.add(new ba(str, (String) all.get(str)));
            }
        }
        return linkedList;
    }

    public final List<ba> m253c() {
        if (m244d()) {
            return m252b();
        }
        return new LinkedList();
    }

    public final void m247a(C0207a c0207a) {
        synchronized (this.f262b) {
            this.f262b.add(c0207a);
        }
        if (m244d()) {
            c0207a.m191a();
        }
    }

    public final void m249a(List<ba> list) {
        this.f261a.edit().putBoolean("dirty", false).commit();
    }
}
