package com.unity3d.player;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;
import com.google.android.exoplayer.util.NalUnitUtil;
import com.google.android.gms.location.places.Place;
import com.unity3d.player.C1056i.C1028a;
import com.upsight.android.marketing.internal.BaseMarketingModule;
import com.voxelbusters.nativeplugins.defines.Keys.Twitter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class UnityPlayer extends FrameLayout {
    public static Activity currentActivity;
    private static boolean f643j;
    C1042b f644a;
    C1053f f645b;
    private boolean f646c;
    private boolean f647d;
    private C1055h f648e;
    private final ConcurrentLinkedQueue f649f;
    private BroadcastReceiver f650g;
    private ContextWrapper f651h;
    private SurfaceView f652i;
    private boolean f653k;
    private Bundle f654l;
    private C1056i f655m;
    private boolean f656n;
    private ProgressBar f657o;
    private Runnable f658p;
    private Runnable f659q;

    /* renamed from: com.unity3d.player.UnityPlayer.10 */
    class AnonymousClass10 implements Runnable {
        final /* synthetic */ AtomicInteger f587a;
        final /* synthetic */ Semaphore f588b;
        final /* synthetic */ String f589c;
        final /* synthetic */ int f590d;
        final /* synthetic */ int f591e;
        final /* synthetic */ int f592f;
        final /* synthetic */ boolean f593g;
        final /* synthetic */ int f594h;
        final /* synthetic */ int f595i;
        final /* synthetic */ UnityPlayer f596j;

        /* renamed from: com.unity3d.player.UnityPlayer.10.1 */
        class C10291 implements C1028a {
            final /* synthetic */ AnonymousClass10 f586a;

            C10291(AnonymousClass10 anonymousClass10) {
                this.f586a = anonymousClass10;
            }

            public final void m498a(int i) {
                this.f586a.f587a.set(i);
                if (i == 3) {
                    if (this.f586a.f596j.f652i.getParent() == null) {
                        this.f586a.f596j.addView(this.f586a.f596j.f652i);
                    }
                    if (this.f586a.f596j.f655m != null) {
                        this.f586a.f596j.f655m.destroyPlayer();
                        this.f586a.f596j.removeView(this.f586a.f596j.f655m);
                        this.f586a.f596j.f655m = null;
                    }
                    this.f586a.f596j.resume();
                }
                if (i != 0) {
                    this.f586a.f588b.release();
                }
            }
        }

        AnonymousClass10(UnityPlayer unityPlayer, AtomicInteger atomicInteger, Semaphore semaphore, String str, int i, int i2, int i3, boolean z, int i4, int i5) {
            this.f596j = unityPlayer;
            this.f587a = atomicInteger;
            this.f588b = semaphore;
            this.f589c = str;
            this.f590d = i;
            this.f591e = i2;
            this.f592f = i3;
            this.f593g = z;
            this.f594h = i4;
            this.f595i = i5;
        }

        public final void run() {
            if (this.f596j.f655m != null) {
                C1046c.Log(5, "Video already playing");
                this.f587a.set(2);
                this.f588b.release();
                return;
            }
            this.f596j.f655m = new C1056i(this.f596j.f651h, this.f589c, this.f590d, this.f591e, this.f592f, this.f593g, (long) this.f594h, (long) this.f595i, new C10291(this));
            this.f596j.addView(this.f596j.f655m);
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.16 */
    class AnonymousClass16 implements Runnable {
        final /* synthetic */ Semaphore f602a;
        final /* synthetic */ UnityPlayer f603b;

        AnonymousClass16(UnityPlayer unityPlayer, Semaphore semaphore) {
            this.f603b = unityPlayer;
            this.f602a = semaphore;
        }

        public final void run() {
            this.f603b.m525d();
            this.f602a.release();
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.17 */
    class AnonymousClass17 implements Runnable {
        final /* synthetic */ Semaphore f604a;
        final /* synthetic */ UnityPlayer f605b;

        AnonymousClass17(UnityPlayer unityPlayer, Semaphore semaphore) {
            this.f605b = unityPlayer;
            this.f604a = semaphore;
        }

        public final void run() {
            if (this.f605b.nativePause()) {
                this.f605b.f653k = true;
                this.f605b.m525d();
                this.f604a.release(2);
                return;
            }
            this.f604a.release();
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.19 */
    class AnonymousClass19 implements Runnable {
        final /* synthetic */ boolean f607a;
        final /* synthetic */ UnityPlayer f608b;

        AnonymousClass19(UnityPlayer unityPlayer, boolean z) {
            this.f608b = unityPlayer;
            this.f607a = z;
        }

        public final void run() {
            this.f608b.nativeFocusChanged(this.f607a);
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.1 */
    class C10301 implements Runnable {
        final /* synthetic */ UnityPlayer f609a;

        /* renamed from: com.unity3d.player.UnityPlayer.1.1 */
        class C10271 implements Runnable {
            final /* synthetic */ C10301 f585a;

            C10271(C10301 c10301) {
                this.f585a = c10301;
            }

            public final void run() {
                this.f585a.f609a.f648e.m565d();
                this.f585a.f609a.m527e();
            }
        }

        C10301(UnityPlayer unityPlayer) {
            this.f609a = unityPlayer;
        }

        public final void run() {
            this.f609a.m544a(new C10271(this));
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.2 */
    class C10312 implements Runnable {
        final /* synthetic */ UnityPlayer f610a;

        C10312(UnityPlayer unityPlayer) {
            this.f610a = unityPlayer;
        }

        public final void run() {
            int k = this.f610a.nativeActivityIndicatorStyle();
            if (k >= 0) {
                if (this.f610a.f657o == null) {
                    this.f610a.f657o = new ProgressBar(this.f610a.f651h, null, new int[]{16842874, 16843401, 16842873, 16843400}[k]);
                    this.f610a.f657o.setIndeterminate(true);
                    this.f610a.f657o.setLayoutParams(new LayoutParams(-2, -2, 51));
                    this.f610a.addView(this.f610a.f657o);
                }
                this.f610a.f657o.setVisibility(0);
                this.f610a.bringChildToFront(this.f610a.f657o);
            }
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.3 */
    class C10323 implements Runnable {
        final /* synthetic */ UnityPlayer f611a;

        C10323(UnityPlayer unityPlayer) {
            this.f611a = unityPlayer;
        }

        public final void run() {
            if (this.f611a.f657o != null) {
                this.f611a.f657o.setVisibility(8);
                this.f611a.removeView(this.f611a.f657o);
                this.f611a.f657o = null;
            }
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.4 */
    class C10334 extends BroadcastReceiver {
        final /* synthetic */ UnityPlayer f612a;

        public void onReceive(Context context, Intent intent) {
            this.f612a.m519b();
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.5 */
    class C10345 implements Runnable {
        final /* synthetic */ UnityPlayer f613a;
        final /* synthetic */ String f614b;
        final /* synthetic */ int f615c;
        final /* synthetic */ boolean f616d;
        final /* synthetic */ boolean f617e;
        final /* synthetic */ boolean f618f;
        final /* synthetic */ boolean f619g;
        final /* synthetic */ String f620h;
        final /* synthetic */ UnityPlayer f621i;

        C10345(UnityPlayer unityPlayer, UnityPlayer unityPlayer2, String str, int i, boolean z, boolean z2, boolean z3, boolean z4, String str2) {
            this.f621i = unityPlayer;
            this.f613a = unityPlayer2;
            this.f614b = str;
            this.f615c = i;
            this.f616d = z;
            this.f617e = z2;
            this.f618f = z3;
            this.f619g = z4;
            this.f620h = str2;
        }

        public final void run() {
            this.f621i.f645b = new C1053f(this.f621i.f651h, this.f613a, this.f614b, this.f615c, this.f616d, this.f617e, this.f618f, this.f620h);
            this.f621i.f645b.show();
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.6 */
    class C10356 implements Runnable {
        final /* synthetic */ UnityPlayer f622a;

        C10356(UnityPlayer unityPlayer) {
            this.f622a = unityPlayer;
        }

        public final void run() {
            if (this.f622a.f645b != null) {
                this.f622a.f645b.dismiss();
                this.f622a.f645b = null;
            }
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.c */
    private abstract class C1036c implements Runnable {
        final /* synthetic */ UnityPlayer f623e;

        private C1036c(UnityPlayer unityPlayer) {
            this.f623e = unityPlayer;
        }

        public abstract void m499a();

        public final void run() {
            if (!this.f623e.isFinishing()) {
                m499a();
            }
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.7 */
    class C10377 extends C1036c {
        final /* synthetic */ Runnable f624a;
        final /* synthetic */ UnityPlayer f625b;

        C10377(UnityPlayer unityPlayer, Runnable runnable) {
            this.f625b = unityPlayer;
            this.f624a = runnable;
            super((byte) 0);
        }

        public final void m500a() {
            this.f625b.m544a(this.f624a);
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.8 */
    class C10388 implements Runnable {
        final /* synthetic */ String f626a;
        final /* synthetic */ UnityPlayer f627b;

        C10388(UnityPlayer unityPlayer, String str) {
            this.f627b = unityPlayer;
            this.f626a = str;
        }

        public final void run() {
            if (this.f627b.f645b != null && this.f626a != null) {
                this.f627b.f645b.m557a(this.f626a);
            }
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.9 */
    class C10399 extends C1036c {
        final /* synthetic */ boolean f628a;
        final /* synthetic */ String f629b;
        final /* synthetic */ int f630c;
        final /* synthetic */ UnityPlayer f631d;

        C10399(UnityPlayer unityPlayer, boolean z, String str, int i) {
            this.f631d = unityPlayer;
            this.f628a = z;
            this.f629b = str;
            this.f630c = i;
            super((byte) 0);
        }

        public final void m501a() {
            if (this.f628a) {
                this.f631d.nativeSetInputCanceled(true);
            } else if (this.f629b != null) {
                this.f631d.nativeSetInputString(this.f629b);
            }
            if (this.f630c == 1) {
                this.f631d.nativeSoftInputClosed();
            }
        }
    }

    /* renamed from: com.unity3d.player.UnityPlayer.a */
    enum C1040a {
        PAUSE,
        RESUME,
        QUIT,
        FOCUS_GAINED,
        FOCUS_LOST,
        NEXT_FRAME
    }

    /* renamed from: com.unity3d.player.UnityPlayer.b */
    private class C1042b extends Thread {
        Handler f640a;
        boolean f641b;
        final /* synthetic */ UnityPlayer f642c;

        /* renamed from: com.unity3d.player.UnityPlayer.b.1 */
        class C10411 implements Callback {
            final /* synthetic */ C1042b f639a;

            C10411(C1042b c1042b) {
                this.f639a = c1042b;
            }

            public final boolean handleMessage(Message message) {
                if (message.what != 2269) {
                    return false;
                }
                C1040a c1040a = (C1040a) message.obj;
                if (c1040a == C1040a.QUIT) {
                    Looper.myLooper().quit();
                } else if (c1040a == C1040a.RESUME) {
                    this.f639a.f641b = true;
                } else if (c1040a == C1040a.PAUSE) {
                    this.f639a.f641b = false;
                    this.f639a.f642c.executeGLThreadJobs();
                } else if (c1040a == C1040a.FOCUS_LOST) {
                    if (!this.f639a.f641b) {
                        this.f639a.f642c.executeGLThreadJobs();
                    }
                } else if (c1040a == C1040a.NEXT_FRAME) {
                    this.f639a.f642c.executeGLThreadJobs();
                    if (!(this.f639a.f642c.isFinishing() || this.f639a.f642c.nativeRender())) {
                        this.f639a.f642c.m519b();
                    }
                }
                if (this.f639a.f641b) {
                    Message.obtain(this.f639a.f640a, 2269, C1040a.NEXT_FRAME).sendToTarget();
                }
                return true;
            }
        }

        private C1042b(UnityPlayer unityPlayer) {
            this.f642c = unityPlayer;
            this.f641b = false;
        }

        private void m502a(C1040a c1040a) {
            Message.obtain(this.f640a, 2269, c1040a).sendToTarget();
        }

        public final void m503a() {
            m502a(C1040a.QUIT);
        }

        public final void m504a(boolean z) {
            m502a(z ? C1040a.FOCUS_GAINED : C1040a.FOCUS_LOST);
        }

        public final void m505b() {
            m502a(C1040a.RESUME);
        }

        public final void m506c() {
            m502a(C1040a.PAUSE);
        }

        public final void run() {
            setName("UnityMain");
            Looper.prepare();
            this.f640a = new Handler(new C10411(this));
            Looper.loop();
        }
    }

    static {
        currentActivity = null;
        new C1054g().m558a();
        f643j = false;
        f643j = loadLibraryStatic(BaseMarketingModule.SCHEDULER_MAIN);
    }

    public UnityPlayer(ContextWrapper contextWrapper) {
        super(contextWrapper);
        this.f646c = false;
        this.f647d = true;
        this.f648e = new C1055h();
        this.f649f = new ConcurrentLinkedQueue();
        this.f650g = null;
        this.f644a = new C1042b();
        this.f654l = new Bundle();
        this.f656n = false;
        this.f645b = null;
        this.f657o = null;
        this.f658p = new C10312(this);
        this.f659q = new C10323(this);
        if (contextWrapper instanceof Activity) {
            currentActivity = (Activity) contextWrapper;
        }
        this.f651h = contextWrapper;
        m510a();
        if (C1049e.f681b) {
            if (currentActivity != null) {
                C1049e.f682c.m547a(currentActivity, new C10301(this));
            } else {
                this.f648e.m565d();
            }
        }
        m512a(this.f651h.getApplicationInfo());
        if (C1055h.m561c()) {
            initJni(contextWrapper);
            nativeFile(this.f651h.getPackageCodePath());
            m531g();
            this.f652i = new SurfaceView(contextWrapper);
            this.f652i.getHolder().setFormat(2);
            this.f652i.getHolder().addCallback(new SurfaceHolder.Callback() {
                final /* synthetic */ UnityPlayer f600a;

                {
                    this.f600a = r1;
                }

                public final void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
                    this.f600a.m511a(0, surfaceHolder.getSurface());
                }

                public final void surfaceCreated(SurfaceHolder surfaceHolder) {
                    this.f600a.m511a(0, surfaceHolder.getSurface());
                }

                public final void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    this.f600a.m511a(0, null);
                }
            });
            this.f652i.setFocusable(true);
            this.f652i.setFocusableInTouchMode(true);
            addView(this.f652i);
            this.f653k = false;
            nativeInitWWW(WWW.class);
            nativeInitWebRequest(UnityWebRequest.class);
            m533h();
            this.f644a.start();
            return;
        }
        AlertDialog create = new Builder(this.f651h).setTitle("Failure to initialize!").setPositiveButton("OK", new OnClickListener() {
            final /* synthetic */ UnityPlayer f599a;

            {
                this.f599a = r1;
            }

            public final void onClick(DialogInterface dialogInterface, int i) {
                this.f599a.m519b();
            }
        }).setMessage("Your hardware does not support this application, sorry!").create();
        create.setCancelable(false);
        create.show();
    }

    public static void UnitySendMessage(String str, String str2, String str3) {
        if (C1055h.m561c()) {
            nativeUnitySendMessage(str, str2, str3);
        } else {
            C1046c.Log(5, "Native libraries not loaded - dropping message for " + str + "." + str2);
        }
    }

    private static String m509a(String str) {
        byte[] digest;
        int i = 0;
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            FileInputStream fileInputStream = new FileInputStream(str);
            long length = new File(str).length();
            fileInputStream.skip(length - Math.min(length, 65558));
            byte[] bArr = new byte[Place.TYPE_SUBLOCALITY_LEVEL_2];
            for (int i2 = 0; i2 != -1; i2 = fileInputStream.read(bArr)) {
                instance.update(bArr, 0, i2);
            }
            digest = instance.digest();
        } catch (FileNotFoundException e) {
            digest = null;
        } catch (IOException e2) {
            digest = null;
        } catch (NoSuchAlgorithmException e3) {
            digest = null;
        }
        if (digest == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        while (i < digest.length) {
            stringBuffer.append(Integer.toString((digest[i] & NalUnitUtil.EXTENDED_SAR) + AccessibilityNodeInfoCompat.ACTION_NEXT_AT_MOVEMENT_GRANULARITY, 16).substring(1));
            i++;
        }
        return stringBuffer.toString();
    }

    private void m510a() {
        try {
            File file = new File(this.f651h.getPackageCodePath(), "assets/bin/Data/settings.xml");
            InputStream fileInputStream = file.exists() ? new FileInputStream(file) : this.f651h.getAssets().open("bin/Data/settings.xml");
            XmlPullParserFactory newInstance = XmlPullParserFactory.newInstance();
            newInstance.setNamespaceAware(true);
            XmlPullParser newPullParser = newInstance.newPullParser();
            newPullParser.setInput(fileInputStream, null);
            String str = null;
            String str2 = null;
            for (int eventType = newPullParser.getEventType(); eventType != 1; eventType = newPullParser.next()) {
                if (eventType == 2) {
                    str2 = newPullParser.getName();
                    String str3 = str;
                    for (int i = 0; i < newPullParser.getAttributeCount(); i++) {
                        if (newPullParser.getAttributeName(i).equalsIgnoreCase(Twitter.NAME)) {
                            str3 = newPullParser.getAttributeValue(i);
                        }
                    }
                    str = str3;
                } else if (eventType == 3) {
                    str2 = null;
                } else if (eventType == 4 && str != null) {
                    if (str2.equalsIgnoreCase("integer")) {
                        this.f654l.putInt(str, Integer.parseInt(newPullParser.getText()));
                    } else if (str2.equalsIgnoreCase("string")) {
                        this.f654l.putString(str, newPullParser.getText());
                    } else if (str2.equalsIgnoreCase("bool")) {
                        this.f654l.putBoolean(str, Boolean.parseBoolean(newPullParser.getText()));
                    } else if (str2.equalsIgnoreCase("float")) {
                        this.f654l.putFloat(str, Float.parseFloat(newPullParser.getText()));
                    }
                    str = null;
                }
            }
        } catch (Exception e) {
            C1046c.Log(6, "Unable to locate player settings. " + e.getLocalizedMessage());
            m519b();
        }
    }

    private void m511a(int i, Surface surface) {
        if (!this.f646c) {
            m522b(0, surface);
        }
    }

    private static void m512a(ApplicationInfo applicationInfo) {
        if (f643j && NativeLoader.load(applicationInfo.nativeLibraryDir)) {
            C1055h.m559a();
        }
    }

    private void m513a(C1036c c1036c) {
        if (!isFinishing()) {
            m521b((Runnable) c1036c);
        }
    }

    private static String[] m518a(Context context) {
        String packageName = context.getPackageName();
        Vector vector = new Vector();
        try {
            int i = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
            if (Environment.getExternalStorageState().equals("mounted")) {
                File file = new File(Environment.getExternalStorageDirectory().toString() + "/Android/obb/" + packageName);
                if (file.exists()) {
                    if (i > 0) {
                        String str = file + File.separator + "main." + i + "." + packageName + ".obb";
                        if (new File(str).isFile()) {
                            vector.add(str);
                        }
                    }
                    if (i > 0) {
                        packageName = file + File.separator + "patch." + i + "." + packageName + ".obb";
                        if (new File(packageName).isFile()) {
                            vector.add(packageName);
                        }
                    }
                }
            }
            String[] strArr = new String[vector.size()];
            vector.toArray(strArr);
            return strArr;
        } catch (NameNotFoundException e) {
            return new String[0];
        }
    }

    private void m519b() {
        if ((this.f651h instanceof Activity) && !((Activity) this.f651h).isFinishing()) {
            ((Activity) this.f651h).finish();
        }
    }

    private void m521b(Runnable runnable) {
        if (!C1055h.m561c()) {
            return;
        }
        if (Thread.currentThread() == this.f644a) {
            runnable.run();
        } else {
            this.f649f.add(runnable);
        }
    }

    private boolean m522b(int i, Surface surface) {
        if (!C1055h.m561c()) {
            return false;
        }
        nativeRecreateGfxState(i, surface);
        return true;
    }

    private void m524c() {
        reportSoftInputStr(null, 1, true);
        if (this.f648e.m568g()) {
            if (C1055h.m561c()) {
                Semaphore semaphore = new Semaphore(0);
                if (isFinishing()) {
                    m521b(new AnonymousClass16(this, semaphore));
                } else {
                    m521b(new AnonymousClass17(this, semaphore));
                }
                try {
                    if (!semaphore.tryAcquire(4, TimeUnit.SECONDS)) {
                        C1046c.Log(5, "Timeout while trying to pause the Unity Engine.");
                    }
                } catch (InterruptedException e) {
                    C1046c.Log(5, "UI thread got interrupted while trying to pause the Unity Engine.");
                }
                if (semaphore.drainPermits() > 0) {
                    quit();
                }
            }
            this.f648e.m564c(false);
            this.f648e.m563b(true);
            this.f644a.m506c();
        }
    }

    private void m525d() {
        nativeDone();
    }

    private void m527e() {
        if (this.f648e.m567f()) {
            this.f648e.m564c(true);
            if (C1055h.m561c()) {
                m531g();
            }
            m521b(new Runnable() {
                final /* synthetic */ UnityPlayer f606a;

                {
                    this.f606a = r1;
                }

                public final void run() {
                    this.f606a.nativeResume();
                }
            });
            this.f644a.m505b();
        }
    }

    private static void m530f() {
        if (!C1055h.m561c()) {
            return;
        }
        if (NativeLoader.unload()) {
            C1055h.m560b();
            return;
        }
        throw new UnsatisfiedLinkError("Unable to unload libraries from libmain.so");
    }

    private void m531g() {
        if (this.f654l.getBoolean("useObb")) {
            for (String str : m518a(this.f651h)) {
                String a = m509a(str);
                if (this.f654l.getBoolean(a)) {
                    nativeFile(str);
                }
                this.f654l.remove(a);
            }
        }
    }

    private void m533h() {
        if (this.f651h instanceof Activity) {
            ((Activity) this.f651h).getWindow().setFlags(Place.TYPE_SUBLOCALITY_LEVEL_2, Place.TYPE_SUBLOCALITY_LEVEL_2);
        }
    }

    private final native void initJni(Context context);

    protected static boolean loadLibraryStatic(String str) {
        try {
            System.loadLibrary(str);
            return true;
        } catch (UnsatisfiedLinkError e) {
            C1046c.Log(6, "Unable to find " + str);
            return false;
        } catch (Exception e2) {
            C1046c.Log(6, "Unknown error " + e2);
            return false;
        }
    }

    private final native int nativeActivityIndicatorStyle();

    private final native void nativeDone();

    private final native void nativeFile(String str);

    private final native void nativeFocusChanged(boolean z);

    private final native void nativeInitWWW(Class cls);

    private final native void nativeInitWebRequest(Class cls);

    private final native boolean nativeInjectEvent(InputEvent inputEvent);

    private final native boolean nativePause();

    private final native void nativeRecreateGfxState(int i, Surface surface);

    private final native boolean nativeRender();

    private final native void nativeResume();

    private final native void nativeSetInputCanceled(boolean z);

    private final native void nativeSetInputString(String str);

    private final native void nativeSoftInputClosed();

    private static native void nativeUnitySendMessage(String str, String str2, String str3);

    final void m544a(Runnable runnable) {
        if (this.f651h instanceof Activity) {
            ((Activity) this.f651h).runOnUiThread(runnable);
        } else {
            C1046c.Log(5, "Not running Unity from an Activity; ignored...");
        }
    }

    public void configurationChanged(Configuration configuration) {
        if (this.f652i instanceof SurfaceView) {
            this.f652i.getHolder().setSizeFromLayout();
        }
        if (this.f655m != null) {
            this.f655m.updateVideoLayout();
        }
    }

    protected void disableLogger() {
        C1046c.f675a = true;
    }

    public boolean displayChanged(int i, Surface surface) {
        if (i == 0) {
            this.f646c = surface != null;
            m544a(new Runnable() {
                final /* synthetic */ UnityPlayer f601a;

                {
                    this.f601a = r1;
                }

                public final void run() {
                    if (this.f601a.f646c) {
                        this.f601a.removeView(this.f601a.f652i);
                    } else {
                        this.f601a.addView(this.f601a.f652i);
                    }
                }
            });
        }
        return m522b(i, surface);
    }

    protected void executeGLThreadJobs() {
        while (true) {
            Runnable runnable = (Runnable) this.f649f.poll();
            if (runnable != null) {
                runnable.run();
            } else {
                return;
            }
        }
    }

    public Bundle getSettings() {
        return this.f654l;
    }

    protected int getSplashMode() {
        return this.f654l.getInt("splash_mode");
    }

    public View getView() {
        return this;
    }

    protected void hideSoftInput() {
        Runnable c10356 = new C10356(this);
        if (C1049e.f680a) {
            m513a(new C10377(this, c10356));
        } else {
            m544a(c10356);
        }
    }

    public void init(int i, boolean z) {
    }

    public boolean injectEvent(InputEvent inputEvent) {
        return nativeInjectEvent(inputEvent);
    }

    protected boolean isFinishing() {
        if (!this.f653k) {
            boolean z = (this.f651h instanceof Activity) && ((Activity) this.f651h).isFinishing();
            this.f653k = z;
            if (!z) {
                return false;
            }
        }
        return true;
    }

    protected void kill() {
        Process.killProcess(Process.myPid());
    }

    protected boolean loadLibrary(String str) {
        return loadLibraryStatic(str);
    }

    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        return injectEvent(motionEvent);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return injectEvent(keyEvent);
    }

    public boolean onKeyMultiple(int i, int i2, KeyEvent keyEvent) {
        return injectEvent(keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        return injectEvent(keyEvent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return injectEvent(motionEvent);
    }

    public void pause() {
        if (this.f655m != null) {
            this.f656n = this.f655m.m573a();
            if (!this.f656n) {
                this.f655m.pause();
                return;
            }
            return;
        }
        m524c();
    }

    public void quit() {
        this.f653k = true;
        if (!this.f648e.m566e()) {
            pause();
        }
        this.f644a.m503a();
        try {
            this.f644a.join(4000);
        } catch (InterruptedException e) {
            this.f644a.interrupt();
        }
        if (this.f650g != null) {
            this.f651h.unregisterReceiver(this.f650g);
        }
        this.f650g = null;
        if (C1055h.m561c()) {
            removeAllViews();
        }
        kill();
        m530f();
    }

    protected void reportSoftInputStr(String str, int i, boolean z) {
        if (i == 1) {
            hideSoftInput();
        }
        m513a(new C10399(this, z, str, i));
    }

    public void resume() {
        this.f648e.m563b(false);
        if (this.f655m == null) {
            m527e();
        } else if (!this.f656n) {
            this.f655m.start();
        }
    }

    protected void setSoftInputStr(String str) {
        m544a(new C10388(this, str));
    }

    protected void showSoftInput(String str, int i, boolean z, boolean z2, boolean z3, boolean z4, String str2) {
        m544a(new C10345(this, this, str, i, z, z2, z3, z4, str2));
    }

    protected boolean showVideoPlayer(String str, int i, int i2, int i3, boolean z, int i4, int i5) {
        Semaphore semaphore = new Semaphore(0);
        AtomicInteger atomicInteger = new AtomicInteger(-1);
        m544a(new AnonymousClass10(this, atomicInteger, semaphore, str, i, i2, i3, z, i4, i5));
        boolean z2 = false;
        try {
            semaphore.acquire();
            z2 = atomicInteger.get() != 2;
        } catch (InterruptedException e) {
        }
        C1046c.Log(2, "Video returned " + (z2 ? "OK" : "FAIL"));
        if (!z2) {
            C1046c.Log(4, "Video failed");
            if (this.f655m != null) {
                m544a(new Runnable() {
                    final /* synthetic */ UnityPlayer f598a;

                    {
                        this.f598a = r1;
                    }

                    public final void run() {
                        if (this.f598a.f652i.getParent() == null) {
                            this.f598a.addView(this.f598a.f652i);
                        }
                        if (this.f598a.f655m != null) {
                            this.f598a.f655m.destroyPlayer();
                            this.f598a.removeView(this.f598a.f655m);
                            this.f598a.f655m = null;
                        }
                        this.f598a.resume();
                    }
                });
            }
        } else if (this.f655m != null) {
            m544a(new Runnable() {
                final /* synthetic */ UnityPlayer f597a;

                {
                    this.f597a = r1;
                }

                public final void run() {
                    if (this.f597a.f655m != null) {
                        this.f597a.m524c();
                        this.f597a.f655m.requestFocus();
                        this.f597a.removeView(this.f597a.f652i);
                    }
                }
            });
        }
        return z2;
    }

    protected void startActivityIndicator() {
        m544a(this.f658p);
    }

    protected void stopActivityIndicator() {
        m544a(this.f659q);
    }

    public void windowFocusChanged(boolean z) {
        this.f648e.m562a(z);
        if (z && this.f645b != null) {
            reportSoftInputStr(null, 1, false);
        }
        m521b(new AnonymousClass19(this, z));
        this.f644a.m504a(z);
        m527e();
    }
}
