package com.crittercism.internal;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.NetworkRequest;
import android.net.NetworkRequest.Builder;
import android.os.Build.VERSION;
import android.os.Looper;
import android.os.Process;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.crittercism.app.CrashData;
import com.crittercism.app.CrittercismCallback;
import com.crittercism.app.CrittercismConfig;
import com.crittercism.app.CrittercismNDK;
import com.crittercism.internal.C0212b.C0209a;
import com.crittercism.internal.C0212b.C0210b;
import com.crittercism.internal.C0212b.C0211c;
import com.crittercism.internal.C0254d.C0253a;
import com.crittercism.internal.ap.C0190e;
import com.crittercism.internal.aq.C0196a;
import com.crittercism.internal.aq.C0197b;
import com.crittercism.internal.ar.C0198a;
import com.crittercism.internal.ar.C0199b;
import com.crittercism.internal.at.C0200a;
import com.crittercism.internal.at.C0201b;
import com.crittercism.internal.ba.C0213a;
import com.crittercism.internal.bc.C0214a;
import com.crittercism.internal.bc.C0215b;
import com.crittercism.internal.bu.C02284;
import com.crittercism.internal.ca.C0231a;
import com.crittercism.internal.ca.C0234d;
import com.crittercism.internal.ca.C0236f;
import com.crittercism.internal.ca.C0237g;
import com.crittercism.internal.cb.C02392;
import com.crittercism.webview.CritterJSInterface;
import com.voxelbusters.nativeplugins.defines.Keys.GameServices;
import com.voxelbusters.nativeplugins.defines.Keys.Twitter;
import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import org.json.JSONArray;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.BuildConfig;

public final class am {
    private Set<WebView> f73A;
    private Date f74B;
    private Date f75C;
    Application f76a;
    av<aq> f77b;
    av<bc> f78c;
    av<aq> f79d;
    av<at> f80e;
    av<ba> f81f;
    List<bu> f82g;
    ScheduledExecutorService f83h;
    public ScheduledExecutorService f84i;
    ap f85j;
    protected C0254d f86k;
    cb f87l;
    public bn f88m;
    au f89n;
    private String f90o;
    private av<ar> f91p;
    private av<C0212b> f92q;
    private av<ca> f93r;
    private bu f94s;
    private ak f95t;
    private bo f96u;
    private CrittercismConfig f97v;
    private as f98w;
    private bw f99x;
    private bp f100y;
    private bq f101z;

    /* renamed from: com.crittercism.internal.am.1 */
    class C01781 extends al {
        final /* synthetic */ am f49a;

        C01781(am amVar, UncaughtExceptionHandler uncaughtExceptionHandler) {
            this.f49a = amVar;
            super(uncaughtExceptionHandler);
        }

        public final void m91a(Throwable th) {
            this.f49a.m101a(th);
        }
    }

    /* renamed from: com.crittercism.internal.am.2 */
    class C01792 implements Runnable {
        final /* synthetic */ JSONObject f50a;
        final /* synthetic */ am f51b;

        C01792(am amVar, JSONObject jSONObject) {
            this.f51b = amVar;
            this.f50a = jSONObject;
        }

        public final void run() {
            if (((Boolean) this.f51b.f85j.m128a(ap.f132q)).booleanValue()) {
                ((bb) this.f51b.f81f).m251a(this.f50a);
            }
        }
    }

    /* renamed from: com.crittercism.internal.am.3 */
    class C01803 implements Runnable {
        final /* synthetic */ Throwable f52a;
        final /* synthetic */ long f53b;
        final /* synthetic */ Date f54c;
        final /* synthetic */ Map f55d;
        final /* synthetic */ am f56e;

        C01803(am amVar, Throwable th, long j, Date date, Map map) {
            this.f56e = amVar;
            this.f52a = th;
            this.f53b = j;
            this.f54c = date;
            this.f55d = map;
        }

        public final void run() {
            bf aqVar = new aq(this.f52a, this.f56e.f89n, this.f53b);
            aqVar.f157h = cf.f482a.m374a(this.f54c);
            Map map = this.f55d;
            aqVar.f156g = new JSONArray();
            for (Entry entry : map.entrySet()) {
                Map hashMap = new HashMap();
                Thread thread = (Thread) entry.getKey();
                if (thread.getId() != aqVar.f150a) {
                    hashMap.put(Twitter.NAME, thread.getName());
                    hashMap.put(TriggerIfContentAvailable.ID, Long.valueOf(thread.getId()));
                    hashMap.put(GameServices.STATE, thread.getState().name());
                    JSONArray jSONArray = new JSONArray();
                    StackTraceElement[] stackTraceElementArr = (StackTraceElement[]) entry.getValue();
                    for (StackTraceElement stackTraceElement : stackTraceElementArr) {
                        jSONArray.put(stackTraceElement.toString());
                    }
                    hashMap.put("stacktrace", jSONArray);
                    aqVar.f156g.put(new JSONObject(hashMap));
                }
            }
            aqVar.m143a(this.f56e.f80e);
            aqVar.m144a(this.f56e.f87l.m346a());
            if (((Boolean) this.f56e.f85j.m128a(ap.f120e)).booleanValue()) {
                aqVar.f162m = ((Float) this.f56e.f85j.m128a(ap.f123h)).floatValue();
                this.f56e.f79d.m196a(aqVar);
            }
            by.m329a(this.f56e.f76a, new CrashData(aqVar.f153d, aqVar.f154e, this.f54c));
        }
    }

    /* renamed from: com.crittercism.internal.am.4 */
    class C01814 implements Runnable {
        final /* synthetic */ Throwable f57a;
        final /* synthetic */ long f58b;
        final /* synthetic */ am f59c;

        C01814(am amVar, Throwable th, long j) {
            this.f59c = amVar;
            this.f57a = th;
            this.f58b = j;
        }

        public final void run() {
            if (((Boolean) this.f59c.f85j.m128a(ap.f124i)).booleanValue()) {
                bf aqVar = new aq(this.f57a, this.f59c.f89n, this.f58b);
                if (((Boolean) this.f59c.f85j.m128a(ap.f114G)).booleanValue()) {
                    Object obj = aqVar.f153d;
                    String str = aqVar.f154e;
                    Map hashMap = new HashMap();
                    String str2 = Twitter.NAME;
                    if (obj == null) {
                        obj = BuildConfig.FLAVOR;
                    }
                    hashMap.put(str2, obj);
                    hashMap.put("reason", str != null ? str : BuildConfig.FLAVOR);
                    this.f59c.f80e.m196a(new at(C0201b.f187g, new JSONObject(hashMap)));
                }
                aqVar.m143a(this.f59c.f80e);
                aqVar.f162m = ((Float) this.f59c.f85j.m128a(ap.f127l)).floatValue();
                this.f59c.f77b.m196a(aqVar);
            }
        }
    }

    /* renamed from: com.crittercism.internal.am.5 */
    class C01825 implements Runnable {
        final /* synthetic */ at f60a;
        final /* synthetic */ am f61b;

        C01825(am amVar, at atVar) {
            this.f61b = amVar;
            this.f60a = atVar;
        }

        public final void run() {
            this.f61b.f80e.m196a(this.f60a);
        }
    }

    /* renamed from: com.crittercism.internal.am.6 */
    class C01836 implements Callable<Boolean> {
        final /* synthetic */ am f62a;

        C01836(am amVar) {
            this.f62a = amVar;
        }

        public final /* synthetic */ Object call() {
            return Boolean.valueOf(new bx(this.f62a.f76a).m328a());
        }
    }

    /* renamed from: com.crittercism.internal.am.7 */
    public class C01847 implements Runnable {
        final /* synthetic */ boolean f63a;
        final /* synthetic */ am f64b;

        public C01847(am amVar, boolean z) {
            this.f64b = amVar;
            this.f63a = z;
        }

        public final void run() {
            boolean z;
            boolean z2 = true;
            new bx(this.f64b.f76a).f400a.edit().putBoolean("isOptedOut", this.f63a).commit();
            this.f64b.f85j.m129a(ap.f116a, Boolean.valueOf(!this.f63a));
            ap apVar = this.f64b.f85j;
            C0190e c0190e = ap.f120e;
            if (this.f63a) {
                z = false;
            } else {
                z = true;
            }
            apVar.m129a(c0190e, Boolean.valueOf(z));
            apVar = this.f64b.f85j;
            c0190e = ap.f128m;
            if (this.f63a) {
                z = false;
            } else {
                z = true;
            }
            apVar.m129a(c0190e, Boolean.valueOf(z));
            apVar = this.f64b.f85j;
            c0190e = ap.f124i;
            if (this.f63a) {
                z = false;
            } else {
                z = true;
            }
            apVar.m129a(c0190e, Boolean.valueOf(z));
            apVar = this.f64b.f85j;
            c0190e = ap.f132q;
            if (this.f63a) {
                z = false;
            } else {
                z = true;
            }
            apVar.m129a(c0190e, Boolean.valueOf(z));
            apVar = this.f64b.f85j;
            c0190e = ap.f136u;
            if (this.f63a) {
                z = false;
            } else {
                z = true;
            }
            apVar.m129a(c0190e, Boolean.valueOf(z));
            apVar = this.f64b.f85j;
            c0190e = ap.f140y;
            if (this.f63a) {
                z = false;
            } else {
                z = true;
            }
            apVar.m129a(c0190e, Boolean.valueOf(z));
            apVar = this.f64b.f85j;
            c0190e = ap.f114G;
            if (this.f63a) {
                z = false;
            } else {
                z = true;
            }
            apVar.m129a(c0190e, Boolean.valueOf(z));
            apVar = this.f64b.f85j;
            c0190e = ap.f110C;
            if (this.f63a) {
                z = false;
            } else {
                z = true;
            }
            apVar.m129a(c0190e, Boolean.valueOf(z));
            apVar = this.f64b.f85j;
            c0190e = ap.f111D;
            if (this.f63a) {
                z = false;
            } else {
                z = true;
            }
            apVar.m129a(c0190e, Boolean.valueOf(z));
            apVar = this.f64b.f85j;
            c0190e = ap.f112E;
            if (this.f63a) {
                z = false;
            } else {
                z = true;
            }
            apVar.m129a(c0190e, Boolean.valueOf(z));
            ap apVar2 = this.f64b.f85j;
            C0190e c0190e2 = ap.f113F;
            if (this.f63a) {
                z2 = false;
            }
            apVar2.m129a(c0190e2, Boolean.valueOf(z2));
            for (bu a : this.f64b.f82g) {
                a.m321a(this.f63a);
            }
        }
    }

    /* renamed from: com.crittercism.internal.am.8 */
    class C01858 implements Callable<Boolean> {
        final /* synthetic */ am f65a;

        C01858(am amVar) {
            this.f65a = amVar;
        }

        public final /* synthetic */ Object call() {
            boolean z;
            if (by.f401a != null) {
                z = true;
            } else {
                z = false;
            }
            return Boolean.valueOf(z);
        }
    }

    /* renamed from: com.crittercism.internal.am.9 */
    class C01869 implements Runnable {
        final /* synthetic */ CrittercismCallback f66a;
        final /* synthetic */ am f67b;

        C01869(am amVar, CrittercismCallback crittercismCallback) {
            this.f67b = amVar;
            this.f66a = crittercismCallback;
        }

        public final void run() {
            CrashData crashData = by.f401a;
            if (crashData != null) {
                crashData = crashData.copy();
            }
            this.f67b.f83h.execute(new C0189c(this.f66a, crashData));
        }
    }

    /* renamed from: com.crittercism.internal.am.a */
    class C0187a implements Runnable {
        final /* synthetic */ am f68a;

        private C0187a(am amVar) {
            this.f68a = amVar;
        }

        public final void run() {
            boolean z = false;
            try {
                boolean a;
                C0230c c0230c = new C0230c(this.f68a.f76a);
                C0254d c0254d = this.f68a.f86k;
                boolean a2 = C0272t.m455a(c0254d, c0230c);
                cc.m362d("Http network insights installation: " + a2);
                if (VERSION.SDK_INT >= 19) {
                    a = C0264m.m425a(c0254d, c0230c);
                } else if (VERSION.SDK_INT >= 14) {
                    a = C0263l.m420a(c0254d, c0230c);
                } else {
                    a = false;
                }
                cc.m362d("Https network insights installation: " + a);
                if (a) {
                    z = C0260i.m410a(c0254d, c0230c);
                    cc.m362d("Network insights provider service instrumented: " + z);
                }
                if (a2 || a || r0) {
                    cc.m360c("installed service monitoring");
                }
            } catch (Throwable e) {
                cc.m362d("Exception in installApm: " + e.getClass().getName());
                cc.m355a(e);
            }
        }
    }

    /* renamed from: com.crittercism.internal.am.b */
    class C0188b implements Runnable {
        final /* synthetic */ am f69a;
        private boolean f70b;

        public C0188b(am amVar, boolean z) {
            this.f69a = amVar;
            this.f70b = z;
        }

        private void m92a(av<at> avVar) {
            LinkedList linkedList;
            int i = 0;
            Collection b = avVar.m197b();
            if (b instanceof LinkedList) {
                linkedList = (LinkedList) b;
            } else {
                linkedList = new LinkedList(b);
            }
            Iterator descendingIterator = linkedList.descendingIterator();
            int i2 = 0;
            while (descendingIterator.hasNext()) {
                at atVar = (at) descendingIterator.next();
                if (i2 != 0) {
                    avVar.m194a(atVar.f199a);
                } else {
                    int i3;
                    if (atVar.f201c == C0201b.f181a) {
                        i3 = 1;
                    } else {
                        i3 = i2;
                    }
                    i2 = i3;
                }
            }
            String[] strArr = new String[]{"network_bcs", "previous_bcs", "current_bcs", "system_bcs"};
            while (i < 4) {
                cd.m364a(aw.m200a(this.f69a.f76a, strArr[i]));
                i++;
            }
        }

        public final void run() {
            CrashData crashData;
            this.f69a.f85j.m128a(ap.f137v);
            cd.m364a(new File(this.f69a.f76a.getFilesDir(), "com.crittercism/pending"));
            SharedPreferences sharedPreferences = this.f69a.f76a.getSharedPreferences("com.crittercism.usersettings", 0);
            if (sharedPreferences.getBoolean("crashedOnLastLoad", false)) {
                Date date;
                String string = sharedPreferences.getString("crashName", BuildConfig.FLAVOR);
                String string2 = sharedPreferences.getString("crashReason", BuildConfig.FLAVOR);
                long j = sharedPreferences.getLong("crashDate", 0);
                if (j != 0) {
                    date = new Date(j);
                } else {
                    date = null;
                }
                crashData = new CrashData(string, string2, date);
            } else {
                crashData = null;
            }
            by.f401a = crashData;
            by.m329a(this.f69a.f76a, null);
            bz bzVar = new bz(this.f69a.f76a);
            bzVar.f402a.edit().putInt("sessionIDSetting", bzVar.f402a.getInt("sessionIDSetting", 0) + 1).commit();
            if (!this.f70b) {
                bf a = bc.m258a(CrittercismNDK.crashDumpDirectory(this.f69a.f76a), this.f69a.f80e, this.f69a.f89n);
                if (a != null) {
                    if (((Boolean) this.f69a.f85j.m128a(ap.f128m)).booleanValue()) {
                        a.f268f = ((Float) this.f69a.f85j.m128a(ap.f131p)).floatValue();
                        this.f69a.f78c.m196a(a);
                    }
                    by.f401a = new CrashData("NDK crash", BuildConfig.FLAVOR, new Date());
                }
            }
            m92a(this.f69a.f80e);
            if (!this.f70b) {
                try {
                    CrittercismNDK.installNdkLib(this.f69a.f76a);
                } catch (Throwable th) {
                    cc.m362d("Exception installing ndk library: " + th.getClass().getName());
                }
            }
            this.f69a.f76a.getSharedPreferences("com.crittercism." + this.f69a.f89n.f207e + ".usermetadata", 0).edit().clear().commit();
            this.f69a.m96a(this.f69a.f85j);
        }
    }

    /* renamed from: com.crittercism.internal.am.c */
    static class C0189c implements Runnable {
        private CrittercismCallback<CrashData> f71a;
        private CrashData f72b;

        public C0189c(CrittercismCallback<CrashData> crittercismCallback, CrashData crashData) {
            this.f71a = crittercismCallback;
            this.f72b = crashData;
        }

        public final void run() {
            this.f71a.onDataReceived(this.f72b);
        }
    }

    public am(Application application, String str, CrittercismConfig crittercismConfig) {
        av bdVar;
        this.f90o = null;
        this.f81f = null;
        this.f82g = new LinkedList();
        this.f83h = ce.m367a("crittercism networking");
        this.f84i = ce.m368b("crittercism data");
        this.f73A = new HashSet();
        this.f75C = new Date();
        this.f90o = str;
        this.f76a = application;
        this.f98w = new as(str);
        this.f97v = new CrittercismConfig(crittercismConfig);
        this.f95t = new ak(this.f76a, this.f97v);
        this.f99x = new bw(this.f76a);
        boolean a = m93a(this.f76a);
        this.f85j = new ap(this.f76a, this.f90o);
        Context context = this.f76a;
        if (a) {
            bdVar = new bd();
        } else {
            bdVar = new aw(context, "app_loads_2", new C0199b(), 10);
        }
        this.f91p = bdVar;
        context = this.f76a;
        if (a) {
            bdVar = new az(10);
        } else {
            bdVar = new aw(context, "breadcrumbs", new C0200a(), Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
        }
        this.f80e = bdVar;
        context = this.f76a;
        if (a) {
            bdVar = new az(1);
        } else {
            bdVar = new aw(context, "exceptions", new C0196a(), 5);
        }
        this.f77b = bdVar;
        context = this.f76a;
        if (a) {
            bdVar = new az(1);
        } else {
            bdVar = new aw(context, "sdk_crashes", new C0196a(), 5);
        }
        this.f79d = bdVar;
        context = this.f76a;
        if (a) {
            bdVar = new az(5);
        } else {
            bdVar = new aw(context, "network_statistics", new C0210b(), 50);
        }
        this.f92q = bdVar;
        context = this.f76a;
        if (a) {
            bdVar = new bd();
        } else {
            bdVar = new aw(context, "ndk_crashes", new C0215b(), 5);
        }
        this.f78c = bdVar;
        context = this.f76a;
        if (a) {
            bdVar = new az(5);
        } else {
            bdVar = new aw(context, "finished_txns", new C0237g(), 50);
        }
        this.f93r = bdVar;
        context = this.f76a;
        String str2 = this.f90o;
        if (a) {
            bdVar = new bd();
        } else {
            bdVar = new bb(context, str2);
        }
        this.f81f = bdVar;
        List uRLBlacklistPatterns = crittercismConfig.getURLBlacklistPatterns();
        uRLBlacklistPatterns.add(this.f98w.f177a.getHost());
        uRLBlacklistPatterns.add(this.f98w.f178b.getHost());
        uRLBlacklistPatterns.add(this.f98w.f180d.getHost());
        uRLBlacklistPatterns.add(this.f98w.f179c.getHost());
        C0253a c0253a = new C0253a();
        c0253a.f497a = this.f84i;
        c0253a.f498b = uRLBlacklistPatterns;
        c0253a.f499c = crittercismConfig.getPreserveQueryStringPatterns();
        c0253a.f500d = this.f92q;
        c0253a.f501e = this.f80e;
        c0253a.f502f = this.f85j;
        this.f86k = new C0254d(c0253a.f498b, c0253a.f499c, c0253a.f500d, c0253a.f501e, c0253a.f502f, (byte) 0);
        this.f87l = new cb(this.f76a, this.f84i, this.f93r, this.f85j);
        this.f89n = new au(this.f95t, this.f76a, new ao(this.f76a, this.f97v), this.f90o);
        this.f84i.submit(new C0188b(this, a));
        try {
            this.f74B = new Date(bm.m281f());
        } catch (Throwable e) {
            cc.m355a(e);
        }
        if (this.f74B != null) {
            m97a(at.m160a(this.f74B));
        } else {
            m97a(at.m160a(this.f75C));
        }
        if (this.f97v.isServiceMonitoringEnabled()) {
            Thread thread = new Thread(new C0187a());
            thread.start();
            try {
                thread.join();
            } catch (Throwable e2) {
                cc.m359b(e2);
            }
        }
        Thread.setDefaultUncaughtExceptionHandler(new C01781(this, Thread.getDefaultUncaughtExceptionHandler()));
        this.f88m = new bn(this.f76a, this.f84i, this.f91p, new ar(this.f89n), this.f85j, crittercismConfig.delaySendingAppLoad(), this.f87l, this.f74B);
        this.f96u = new bo(this.f76a, this.f84i, this.f80e, this.f85j);
        this.f100y = new bp(this.f76a, this.f84i, this.f80e, this.f85j);
        if (VERSION.SDK_INT >= 14) {
            this.f101z = new bq(this.f76a, this.f89n);
        }
    }

    private static boolean m93a(Context context) {
        int myUid = Process.myUid();
        int myPid = Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        int i = 0;
        for (RunningAppProcessInfo runningAppProcessInfo : activityManager.getRunningAppProcesses()) {
            int i2;
            if (runningAppProcessInfo.uid == myUid) {
                i2 = i + 1;
            } else {
                i2 = i;
            }
            i = i2;
        }
        if (i <= 1) {
            return false;
        }
        for (RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (runningServiceInfo.pid == myPid) {
                return true;
            }
        }
        return false;
    }

    public final void m101a(Throwable th) {
        Throwable th2 = th;
        try {
            this.f84i.submit(new C01803(this, th2, Thread.currentThread().getId(), new Date(), Thread.getAllStackTraces())).get();
            bu buVar = this.f94s;
            ScheduledFuture scheduledFuture = buVar.f383e;
            if (scheduledFuture != null) {
                scheduledFuture.get();
            }
            Future future = buVar.f384f;
            if (future != null) {
                future.get();
            }
            Future future2 = buVar.f385g;
            if (future2 != null) {
                future2.get();
            }
        } catch (Throwable e) {
            cc.m359b(e);
        } catch (Throwable e2) {
            cc.m359b(e2);
        }
    }

    public final void m100a(String str, String str2, long j, long j2, long j3, int i, bj bjVar) {
        long currentTimeMillis = System.currentTimeMillis() - j;
        if (str == null) {
            cc.m353a("Null HTTP request method provided. Endpoint will not be logged.");
        } else if (str2 == null) {
            cc.m353a("Null url provided. Endpoint will not be logged");
        } else if (j2 < 0 || j3 < 0) {
            cc.m353a("Invalid byte values. Bytes need to be non-negative. Endpoint will not be logged.");
        } else if (j < 0 || currentTimeMillis < 0) {
            cc.m353a("Invalid latency '" + j + "'. Endpoint will not be logged.");
        } else {
            C0230c c0230c = new C0230c(this.f76a);
            C0212b c0212b = new C0212b();
            c0212b.f249j = str.toUpperCase();
            c0212b.m228a(str2);
            c0212b.m226a(j2);
            c0212b.m231b(j3);
            c0212b.f248i = i;
            c0212b.f254o = C0176a.m24a(c0230c.f403a);
            c0212b.m234c(currentTimeMillis);
            c0212b.m236d(currentTimeMillis + j);
            c0212b.f250k = bjVar;
            if (an.m112b()) {
                c0212b.m227a(an.m110a());
            }
            this.f86k.m382a(c0212b, C0211c.LOG_ENDPOINT);
        }
    }

    public final void m94a(WebView webView) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            cc.m353a("Crittercism.instrumentWebView(WebView) must be called on the main UI thread");
            return;
        }
        synchronized (this.f73A) {
            if (this.f73A.contains(webView)) {
                return;
            }
            this.f73A.add(webView);
            cj cjVar = new cj(this, this.f86k, this.f76a);
            try {
                ci ciVar = new ci();
                try {
                    WebViewClient a;
                    if (VERSION.SDK_INT <= 15) {
                        a = ci.m375a(webView);
                    } else if (VERSION.SDK_INT <= 18) {
                        a = ci.m376b(webView);
                    } else {
                        a = ci.m377c(webView);
                    }
                    webView.setWebViewClient(new ch(a, cjVar.f492b, cjVar.f493c, cjVar.f494d));
                    if (webView.getSettings().getJavaScriptEnabled()) {
                        webView.addJavascriptInterface(new CritterJSInterface(cjVar.f491a), "_crttr");
                    }
                } catch (Throwable e) {
                    cc.m359b(e);
                    cc.m353a("Failed to find WebViewClient. WebView will not be instrumented.");
                }
            } catch (bh e2) {
                cc.m357b(e2.getMessage());
            }
        }
    }

    public final synchronized void m105b(Throwable th) {
        if (th == null) {
            cc.m357b("Calling logHandledException with a null java.lang.Throwable. Nothing will be reported to Crittercism");
        } else {
            this.f84i.execute(new C01814(this, th, Thread.currentThread().getId()));
        }
    }

    public final void m97a(at atVar) {
        this.f84i.execute(new C01825(this, atVar));
    }

    public final void m96a(ap apVar) {
        ConnectivityManager connectivityManager;
        String a = this.f99x.m327a();
        boolean a2 = new bx(this.f76a).m328a();
        this.f82g.add(new bu(this.f98w, this.f84i, this.f83h, this.f77b, new C0197b(this.f90o, a, "exceptions", "/android_v2/handle_exceptions"), "EXCEPTIONS", apVar, ap.f125j, ap.f126k));
        this.f94s = new bu(this.f98w, this.f84i, this.f83h, this.f79d, new C0197b(this.f90o, a, "crashes", "/android_v2/handle_crashes"), "CRASHES", apVar, ap.f121f, ap.f122g);
        this.f82g.add(this.f94s);
        this.f82g.add(new bu(this.f98w, this.f84i, this.f83h, this.f78c, new C0214a(this.f90o, a), "NDK", apVar, ap.f129n, ap.f130o));
        this.f82g.add(new bu(this.f98w, this.f84i, this.f83h, this.f81f, new C0213a(this.f90o, a), "METADATA", apVar, ap.f133r, ap.f134s));
        this.f82g.add(new bu(this.f98w, this.f84i, this.f83h, this.f92q, new C0209a(this.f89n), "APM", apVar, ap.f117b, ap.f118c));
        bu buVar = new bu(this.f98w, this.f84i, this.f83h, this.f91p, new C0198a(this.f90o, a), "APPLOADS", apVar, ap.f137v, ap.f138w);
        buVar.f381c = new br(this.f98w.f178b, this.f76a, this.f89n, apVar);
        this.f82g.add(buVar);
        this.f82g.add(new bu(this.f98w, this.f84i, this.f83h, this.f93r, new C0236f(this.f80e, this.f89n), "USERFLOWS", apVar, ap.f141z, ap.f108A));
        if (ao.m113a(this.f76a, "android.permission.ACCESS_NETWORK_STATE")) {
            connectivityManager = (ConnectivityManager) this.f76a.getSystemService("connectivity");
        } else {
            connectivityManager = null;
        }
        for (bu buVar2 : this.f82g) {
            buVar2.f387i = connectivityManager;
            if (buVar2.f387i != null && VERSION.SDK_INT >= 21) {
                Builder builder = new Builder();
                builder.addCapability(12);
                if (!buVar2.f386h) {
                    builder.addTransportType(1);
                }
                NetworkRequest build = builder.build();
                buVar2.f388j = new C02284(buVar2);
                connectivityManager.registerNetworkCallback(build, (NetworkCallback) buVar2.f388j);
            }
            buVar2.f386h = this.f97v.allowsCellularAccess();
            buVar2.m321a(a2);
        }
    }

    public final boolean m103a() {
        try {
            return ((Boolean) this.f84i.submit(new C01836(this)).get()).booleanValue();
        } catch (Throwable e) {
            cc.m359b(e);
            return false;
        } catch (Throwable e2) {
            cc.m359b(e2);
            return false;
        }
    }

    public final boolean m106b() {
        try {
            return ((Boolean) this.f84i.submit(new C01858(this)).get()).booleanValue();
        } catch (Throwable e) {
            cc.m359b(e);
            return false;
        } catch (Throwable e2) {
            cc.m359b(e2);
            return false;
        }
    }

    public final void m95a(CrittercismCallback<CrashData> crittercismCallback) {
        this.f84i.execute(new C01869(this, crittercismCallback));
    }

    public final void m102a(JSONObject jSONObject) {
        this.f84i.execute(new C01792(this, jSONObject));
    }

    public final void m98a(String str) {
        cb cbVar = this.f87l;
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (cbVar.f451a) {
            cbVar.f451a.remove(str);
            if (cbVar.f451a.size() >= 50) {
                cc.m357b("Aborting beginUserflow(" + str + "). Maximum number of userflows exceeded.");
                return;
            }
            long longValue = ((Long) cbVar.f454d.m128a(ap.m127a(str, ((Long) cbVar.f454d.m128a(ap.f115H)).longValue()))).longValue();
            C0231a c0231a = new C0231a();
            c0231a.f404a = str;
            c0231a.f405b = currentTimeMillis;
            c0231a.f406c = -1;
            c0231a.f407d = longValue;
            cbVar.f451a.put(str, c0231a.m330a());
            cc.m362d("Added userflow: " + str);
        }
    }

    public final void m104b(String str) {
        this.f87l.m347a(str);
    }

    public final void m107c(String str) {
        cb cbVar = this.f87l;
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (cbVar.f451a) {
            ca caVar = (ca) cbVar.f451a.remove(str);
            if (caVar == null) {
                cc.m357b("failUserflow(" + str + "): no such userflow");
                return;
            }
            caVar.m340a(C0234d.f418e, currentTimeMillis);
            cbVar.f452b.submit(new C02392(cbVar, caVar));
        }
    }

    public final void m108d(String str) {
        cb cbVar = this.f87l;
        synchronized (cbVar.f451a) {
            cbVar.f451a.remove(str);
        }
    }

    public final void m99a(String str, int i) {
        cb cbVar = this.f87l;
        synchronized (cbVar.f451a) {
            ca caVar = (ca) cbVar.f451a.get(str);
            if (caVar == null) {
                cc.m357b("setUserflowValue(" + str + "): no such userflow");
                return;
            }
            caVar.f432c = i;
        }
    }

    public final int m109e(String str) {
        return this.f87l.m348b(str);
    }
}
