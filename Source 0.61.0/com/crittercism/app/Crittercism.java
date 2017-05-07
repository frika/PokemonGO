package com.crittercism.app;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.location.Location;
import android.os.Build.VERSION;
import android.webkit.WebView;
import com.crittercism.integrations.PluginException;
import com.crittercism.internal.am;
import com.crittercism.internal.am.C01847;
import com.crittercism.internal.an;
import com.crittercism.internal.ao;
import com.crittercism.internal.as;
import com.crittercism.internal.at;
import com.crittercism.internal.bj;
import com.crittercism.internal.bk;
import com.crittercism.internal.cc;
import com.voxelbusters.nativeplugins.defines.Keys.Ui;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class Crittercism {
    private static volatile am f14a;
    private static final List<CrittercismCallback<CrashData>> f15b;

    public enum LoggingLevel {
        Silent,
        Error,
        Warning,
        Info
    }

    static {
        f14a = null;
        f15b = new ArrayList();
    }

    private Crittercism() {
    }

    public static synchronized void initialize(Context context, String appID, CrittercismConfig config) {
        synchronized (Crittercism.class) {
            try {
                cc.m360c("Initializing Crittercism 5.8.2 for App ID " + appID);
                if (context == null) {
                    cc.m357b("Crittercism.initialize() given a null context parameter");
                } else {
                    Context context2;
                    if (context instanceof Application) {
                        context2 = (Application) context;
                    } else if (context instanceof Activity) {
                        context2 = ((Activity) context).getApplication();
                    } else if (context instanceof Service) {
                        context2 = ((Service) context).getApplication();
                    } else if (context.getApplicationContext() instanceof Application) {
                        Application application = (Application) context.getApplicationContext();
                    } else {
                        context2 = null;
                    }
                    if (context2 == null) {
                        cc.m357b("Crittercism.initialize() expects the input Context to be an instanceof Application. Received '" + context.getClass().getName() + "'. Crittercism will no be initialized.");
                    } else if (appID == null) {
                        cc.m357b("Crittercism.initialize() given a null appID parameter");
                    } else if (config == null) {
                        cc.m357b("Crittercism.initialize() given a null CrittercismConfiguration. Crittercism will not be initialized");
                    } else if (VERSION.SDK_INT < 14) {
                        cc.m357b("Crittercism is not supported for Android API less than 14 (ICS). Crittercism will not be enabled");
                    } else if (!ao.m113a(context2, "android.permission.INTERNET")) {
                        cc.m353a("Crittercism requires INTERNET permission. Please add android.permission.INTERNET to your AndroidManifest.xml. Crittercism will not be initialized.");
                    } else if (config.allowsCellularAccess() || ao.m113a(context2, "android.permission.ACCESS_NETWORK_STATE")) {
                        try {
                            as asVar = new as(appID);
                            synchronized (Crittercism.class) {
                                if (f14a != null) {
                                    cc.m357b("Crittercism has already been initialized. Subsequent calls to initialize() are ignored.");
                                } else {
                                    long currentTimeMillis = System.currentTimeMillis();
                                    f14a = new am(context2, appID, config);
                                    cc.m362d("Crittercism initialized in " + (System.currentTimeMillis() - currentTimeMillis) + "ms");
                                    synchronized (f15b) {
                                        for (CrittercismCallback a : f15b) {
                                            f14a.m95a(a);
                                        }
                                        f15b.clear();
                                    }
                                }
                            }
                        } catch (IllegalArgumentException e) {
                            cc.m353a("Crittercism.initialize() given an invalid app ID '" + appID + "': " + e.getMessage());
                        }
                    } else {
                        cc.m353a("Crittercism requires adding android.permission.ACCESS_NETWORK_STATE to your AndroidManifest.xml when setting CrittercismConfig.setAllowsCellularAccess(false). Crittercism will not be initialized.");
                    }
                }
            } catch (ThreadDeath e2) {
                throw e2;
            } catch (Throwable th) {
                cc.m359b(th);
            }
        }
    }

    public static synchronized void initialize(Context context, String appID) {
        synchronized (Crittercism.class) {
            initialize(context, appID, new CrittercismConfig());
        }
    }

    public static void sendAppLoadData() {
        if (f14a == null) {
            m20a("sendAppLoadData");
            return;
        }
        try {
            am amVar = f14a;
            if (amVar.f88m != null) {
                amVar.f88m.m297g();
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    public static void logHandledException(Throwable t) {
        if (f14a == null) {
            m20a("logHandledException");
        } else if (t == null) {
            cc.m357b("Invalid input to Crittercism.logHandledException(): null exception parameter");
        } else {
            try {
                f14a.m105b(t);
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                cc.m359b(th);
            }
        }
    }

    public static void _logHandledException(String name, String msg, String stack) {
        try {
            cc.m362d("_logHandledException(name, msg, stack) called: " + name + " " + msg + " " + stack);
            if (name == null || msg == null || stack == null) {
                cc.m357b("Calling logHandledException with null parameter(s). Nothing will be reported to Crittercism");
            } else {
                logHandledException(new PluginException(name, msg, stack));
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    public static void _logHandledException(String name, String msg, String[] classStackElems, String[] methodStackElems, String[] fileStackElems, int[] lineNumberStackElems) {
        try {
            cc.m362d("_logHandledException(name, msg, classes, methods, files, lines) called: " + name);
            if (name == null || msg == null || classStackElems == null) {
                cc.m357b("Calling logHandledException with null parameter(s). Nothing will be reported to Crittercism");
            } else {
                logHandledException(new PluginException(name, msg, classStackElems, methodStackElems, fileStackElems, lineNumberStackElems));
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    public static void logNetworkRequest(String method, URL url, long latency, long bytesRead, long bytesSent, int responseCode, Exception error) {
        if (f14a == null) {
            m20a("logNetworkRequest");
        } else if (url == null) {
            cc.m353a("Null URL provided. Endpoint will not be logged");
        } else {
            try {
                String str = method;
                f14a.m100a(str, url.toExternalForm(), latency, bytesRead, bytesSent, responseCode, new bj(error));
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                cc.m359b(th);
            }
        }
    }

    public static void logNetworkRequest(String method, String url, long latency, long bytesRead, long bytesSent, int responseCode, int errorCode) {
        if (f14a == null) {
            m20a("logNetworkRequest");
            return;
        }
        try {
            f14a.m100a(method, url, latency, bytesRead, bytesSent, responseCode, new bj(bk.f310e - 1, errorCode));
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    public static boolean didCrashOnLastLoad() {
        boolean z = false;
        if (f14a == null) {
            m20a("didCrashOnLoad");
        } else {
            try {
                z = f14a.m106b();
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                cc.m359b(th);
            }
        }
        return z;
    }

    public static void getPreviousSessionCrashData(CrittercismCallback<CrashData> crashListener) {
        if (crashListener == null) {
            cc.m357b("Crittercism.getPreviousSessionCrashData() given invalid input parameter: null crashListener");
            return;
        }
        try {
            if (f14a == null) {
                synchronized (f15b) {
                    f15b.add(crashListener);
                }
                return;
            }
            f14a.m95a((CrittercismCallback) crashListener);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    public static void _logCrashException(String name, String msg, String stack) {
        if (name == null || msg == null || stack == null) {
            try {
                cc.m353a("Unable to handle application crash. Missing parameters");
                return;
            } catch (Throwable th) {
                cc.m359b(th);
                return;
            }
        }
        cc.m362d("_logCrashException(name, msg, stack) called: " + name + " " + msg + " " + stack);
        _logCrashException(new PluginException(name, msg, stack));
    }

    public static void _logCrashException(String msg, String stack) {
        if (msg == null || stack == null) {
            try {
                cc.m353a("Unable to handle application crash. Missing parameters");
                return;
            } catch (Throwable th) {
                cc.m359b(th);
                return;
            }
        }
        cc.m362d("_logCrashException(msg, stack) called: " + msg + " " + stack);
        _logCrashException(new PluginException(msg, stack));
    }

    public static void _logCrashException(String name, String msg, String[] classStackElems, String[] methodStackElems, String[] fileStackElems, int[] lineNumberStackElems) {
        try {
            cc.m362d("_logCrashException(String, String, String[], String[], String[], int[]) called: " + name + " " + msg);
            if (name == null || msg == null || classStackElems == null || methodStackElems == null || fileStackElems == null || lineNumberStackElems == null) {
                cc.m353a("Unable to handle application crash. Missing parameters");
                return;
            }
            if (m21a(classStackElems, methodStackElems, fileStackElems, lineNumberStackElems)) {
                _logCrashException(new PluginException(name, msg, classStackElems, methodStackElems, fileStackElems, lineNumberStackElems));
            } else {
                cc.m353a("Unable to handle application crash. Missing stack elements");
            }
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    private static boolean m21a(Object... objArr) {
        if (objArr[0] == null) {
            return false;
        }
        int length = Array.getLength(objArr[0]);
        int i = 1;
        while (i < 4) {
            if (objArr[i] == null || Array.getLength(objArr[i]) != length) {
                return false;
            }
            i++;
        }
        return true;
    }

    public static void _logCrashException(Throwable exception) {
        if (f14a == null) {
            m20a("_logCrashException");
            return;
        }
        try {
            cc.m362d("_logCrashException(Throwable) called with throwable: " + exception);
            f14a.m101a(exception);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    public static void instrumentWebView(WebView webView) {
        if (f14a == null) {
            m20a("instrumentWebView");
        } else if (webView == null) {
            cc.m357b("WebView was null. Skipping instrumentation.");
        } else {
            try {
                f14a.m94a(webView);
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                cc.m359b(th);
            }
        }
    }

    public static boolean getOptOutStatus() {
        boolean z = false;
        if (f14a != null) {
            try {
                z = f14a.m103a();
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                cc.m359b(th);
            }
        }
        return z;
    }

    public static void setOptOutStatus(boolean isOptedOut) {
        if (f14a == null) {
            m20a("setOptOutStatus");
            return;
        }
        try {
            am amVar = f14a;
            amVar.f84i.submit(new C01847(amVar, isOptedOut));
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    public static void setMetadata(JSONObject metadata) {
        if (f14a == null) {
            m20a("setMetadata");
        } else if (metadata == null) {
            cc.m357b("Invalid input to Crittercism.setMetadata(): null metadata parameter");
        } else {
            try {
                f14a.m102a(metadata);
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                cc.m359b(th);
            }
        }
    }

    public static void setUsername(String username) {
        if (f14a == null) {
            m20a("setUsername");
        } else if (username == null) {
            cc.m357b("Crittercism.setUsername() given invalid parameter: null");
        } else {
            try {
                f14a.m102a(new JSONObject().put(Ui.USER_NAME, username));
            } catch (Throwable e) {
                cc.m358b("Crittercism.setUsername()", e);
            } catch (ThreadDeath e2) {
                throw e2;
            } catch (Throwable e3) {
                cc.m359b(e3);
            }
        }
    }

    public static void leaveBreadcrumb(String text) {
        if (f14a == null) {
            m20a("leaveBreadcrumb");
        } else if (text == null) {
            cc.m358b("Cannot leave null breadcrumb", new NullPointerException());
        } else {
            try {
                f14a.m97a(at.m159a(text));
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                cc.m359b(th);
            }
        }
    }

    public static void beginUserflow(String name) {
        if (f14a == null) {
            m20a("beginUserflow");
        } else if (name == null) {
            cc.m357b("Invalid input to beginUserflow(): null userflow name");
        } else {
            try {
                f14a.m98a(name);
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                cc.m359b(th);
            }
        }
    }

    @Deprecated
    public static void beginTransaction(String name) {
        if (f14a == null) {
            m20a("beginTransaction");
        } else if (name == null) {
            cc.m357b("Invalid input to beginTransaction(): null name");
        } else {
            beginUserflow(name);
        }
    }

    public static void endUserflow(String name) {
        if (f14a == null) {
            m20a("endUserflow");
        } else if (name == null) {
            cc.m357b("Invalid input to endUserflow(): null userflow name");
        } else {
            try {
                f14a.m104b(name);
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                cc.m359b(th);
            }
        }
    }

    @Deprecated
    public static void endTransaction(String name) {
        if (f14a == null) {
            m20a("endTransaction");
        } else if (name == null) {
            cc.m357b("Invalid input to endTransaction(): null name");
        } else {
            endUserflow(name);
        }
    }

    public static void failUserflow(String name) {
        if (f14a == null) {
            m20a("failUserflow");
        } else if (name == null) {
            cc.m357b("Invalid input to failUserflow(): null name");
        } else {
            try {
                f14a.m107c(name);
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                cc.m359b(th);
            }
        }
    }

    @Deprecated
    public static void failTransaction(String name) {
        if (f14a == null) {
            m20a("failTransaction");
        } else if (name == null) {
            cc.m357b("Invalid input to failTransaction(): null name");
        } else {
            failUserflow(name);
        }
    }

    public static void cancelUserflow(String name) {
        if (f14a == null) {
            m20a("cancelUserflow");
        } else if (name == null) {
            cc.m357b("Invalid input to cancelUserflow(): null name");
        } else {
            try {
                f14a.m108d(name);
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                cc.m359b(th);
            }
        }
    }

    @Deprecated
    public static void cancelTransaction(String name) {
        if (f14a == null) {
            m20a("cancelTransaction");
        } else if (name == null) {
            cc.m357b("Invalid input to cancelTransaction(): null name");
        } else {
            cancelUserflow(name);
        }
    }

    public static void setUserflowValue(String name, int value) {
        if (f14a == null) {
            m20a("setUserflowValue");
        } else if (name == null) {
            cc.m357b("Invalid input to setUserflowValue(): null name");
        } else {
            try {
                f14a.m99a(name, value);
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                cc.m359b(th);
            }
        }
    }

    @Deprecated
    public static void setTransactionValue(String name, int value) {
        if (f14a == null) {
            m20a("setTransactionValue");
        } else if (name == null) {
            cc.m357b("Invalid input to setTransactionValue(): null name");
        } else {
            setUserflowValue(name, value);
        }
    }

    public static int getUserflowValue(String name) {
        int i = -1;
        if (f14a == null) {
            m20a("getUserflowValue");
        } else if (name == null) {
            cc.m357b("Invalid input to getUserflowValue(): null name");
        } else {
            try {
                i = f14a.m109e(name);
            } catch (ThreadDeath e) {
                throw e;
            } catch (Throwable th) {
                cc.m359b(th);
            }
        }
        return i;
    }

    @Deprecated
    public static int getTransactionValue(String name) {
        if (f14a == null) {
            m20a("getTransactionValue");
            return -1;
        } else if (name != null) {
            return getUserflowValue(name);
        } else {
            cc.m357b("Invalid input to getTransactionValue(): null name");
            return -1;
        }
    }

    public static void updateLocation(Location curLocation) {
        try {
            an.m111a(curLocation);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    public static void setLoggingLevel(LoggingLevel loggingLevel) {
        try {
            cc.m352a(loggingLevel);
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    private static void m20a(String str) {
        cc.m358b("Must initialize Crittercism before calling " + Crittercism.class.getName() + "." + str + "(). Request is being ignored...", new IllegalStateException());
    }
}
