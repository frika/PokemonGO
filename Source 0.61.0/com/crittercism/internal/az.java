package com.crittercism.internal;

import com.crittercism.internal.av.C0207a;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import org.json.JSONArray;

public final class az<T extends bf> implements av<T> {
    private final SortedMap<String, T> f220a;
    private final List<C0207a> f221b;
    private int f222c;

    public az(int i) {
        this.f220a = new TreeMap();
        this.f221b = new LinkedList();
        this.f222c = i;
    }

    public final synchronized boolean m218a(T t) {
        if (this.f220a.size() >= this.f222c) {
            this.f220a.remove(this.f220a.firstKey());
        }
        this.f220a.put(t.m138f(), t);
        synchronized (this.f221b) {
            for (C0207a a : this.f221b) {
                a.m191a();
            }
        }
        return true;
    }

    public final void m216a(String str) {
        this.f220a.remove(str);
    }

    public final JSONArray m214a() {
        JSONArray jSONArray = new JSONArray();
        for (bf g : m219b()) {
            jSONArray.put(g.m139g());
        }
        return jSONArray;
    }

    public final List<T> m219b() {
        return new LinkedList(this.f220a.values());
    }

    public final List<T> m220c() {
        return m219b();
    }

    public final void m217a(List<T> list) {
        for (T f : list) {
            this.f220a.remove(f.m138f());
        }
    }

    public final void m215a(C0207a c0207a) {
        synchronized (this.f221b) {
            this.f221b.add(c0207a);
        }
    }
}
