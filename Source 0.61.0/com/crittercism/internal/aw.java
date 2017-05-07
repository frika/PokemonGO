package com.crittercism.internal;

import android.content.Context;
import android.support.annotation.NonNull;
import com.crittercism.internal.av.C0207a;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;

public final class aw<T extends bf> implements av<T> {
    private final C0208a f215a;
    private C0195b<T> f216b;
    private int f217c;
    private final List<C0207a> f218d;

    /* renamed from: com.crittercism.internal.aw.b */
    public interface C0195b<T extends bf> {
        T m131a(File file);

        void m132a(T t, OutputStream outputStream);
    }

    /* renamed from: com.crittercism.internal.aw.a */
    static class C0208a {
        private Context f212a;
        private String f213b;
        private File f214c;

        public C0208a(Context context, String str) {
            this.f212a = context;
            this.f213b = str;
        }

        public final File m199a() {
            if (this.f214c != null) {
                return this.f214c;
            }
            this.f214c = aw.m200a(this.f212a, this.f213b);
            if (!this.f214c.isDirectory()) {
                this.f214c.mkdirs();
            }
            return this.f214c;
        }
    }

    public aw(Context context, String str, C0195b<T> c0195b, int i) {
        this.f218d = new LinkedList();
        this.f216b = c0195b;
        this.f217c = i;
        this.f215a = new C0208a(context, str);
    }

    public final boolean m208a(T t) {
        if (m203e().length >= this.f217c) {
            Object obj;
            File[] d = m202d();
            int length = (this.f217c - d.length) + 1;
            int i = 0;
            int i2 = 0;
            while (i < length && i < d.length) {
                if (d[i].delete()) {
                    i2++;
                }
                i++;
            }
            if (i2 == length) {
                obj = 1;
            } else {
                obj = null;
            }
            if (obj == null) {
                return false;
            }
        }
        boolean b = m201b(t);
        synchronized (this.f218d) {
            for (C0207a a : this.f218d) {
                a.m191a();
            }
        }
        return b;
    }

    private boolean m201b(T t) {
        File file = new File(this.f215a.m199a(), t.m138f());
        try {
            OutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            try {
                this.f216b.m132a(t, bufferedOutputStream);
                return true;
            } catch (Throwable e) {
                file.delete();
                cc.m354a("Unable to write to " + file.getAbsolutePath(), e);
                return false;
            } finally {
                try {
                    bufferedOutputStream.close();
                } catch (Throwable e2) {
                    file.delete();
                    cc.m354a("Unable to close " + file.getAbsolutePath(), e2);
                    return false;
                }
            }
        } catch (Throwable e22) {
            cc.m361c("Could not open output stream to : " + file, e22);
            return false;
        }
    }

    private File[] m202d() {
        File[] e = m203e();
        Arrays.sort(e);
        return e;
    }

    private File[] m203e() {
        File[] listFiles = this.f215a.m199a().listFiles();
        if (listFiles == null) {
            return new File[0];
        }
        return listFiles;
    }

    public final void m206a(@NonNull String str) {
        File file = new File(this.f215a.m199a(), str);
        if (file.exists()) {
            file.delete();
        }
    }

    public final JSONArray m204a() {
        List<bf> b = m209b();
        JSONArray jSONArray = new JSONArray();
        for (bf g : b) {
            jSONArray.put(g.m139g());
        }
        return jSONArray;
    }

    public final List<T> m209b() {
        List<T> arrayList = new ArrayList();
        for (File file : m202d()) {
            try {
                arrayList.add(this.f216b.m131a(file));
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                m206a(file.getName());
            }
        }
        return arrayList;
    }

    public final List<T> m210c() {
        return m209b();
    }

    public final void m207a(List<T> list) {
        for (T f : list) {
            m206a(f.m138f());
        }
    }

    public final void m205a(C0207a c0207a) {
        if (c0207a != null) {
            synchronized (this.f218d) {
                this.f218d.add(c0207a);
            }
        }
    }

    public static File m200a(Context context, String str) {
        return new File(context.getFilesDir() + "/com.crittercism/" + str);
    }
}
