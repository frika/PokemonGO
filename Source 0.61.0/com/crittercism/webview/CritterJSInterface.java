package com.crittercism.webview;

import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.webkit.JavascriptInterface;
import com.crittercism.internal.am;
import com.crittercism.internal.at;
import com.crittercism.internal.bj;
import com.crittercism.internal.bk;
import com.crittercism.internal.bl;
import com.crittercism.internal.cc;
import com.mopub.volley.toolbox.HttpClientStack.HttpPatch;
import com.upsight.mediation.vast.VASTPlayer;
import com.voxelbusters.nativeplugins.defines.Keys;
import com.voxelbusters.nativeplugins.defines.Keys.Twitter;
import com.voxelbusters.nativeplugins.defines.Keys.Ui;
import org.json.JSONException;
import org.json.JSONObject;
import spacemadness.com.lunarconsole.BuildConfig;

public class CritterJSInterface {
    private am f570a;

    public CritterJSInterface(am instance) {
        if (instance == null) {
            m479a("CritterJSInterface");
        }
        this.f570a = instance;
    }

    @JavascriptInterface
    public void logError(String errorMsg, String stackStr) {
        try {
            if (this.f570a == null) {
                m479a("logError");
            } else if (errorMsg != null && errorMsg.length() != 0 && stackStr != null && stackStr.length() != 0) {
                String str = BuildConfig.FLAVOR;
                String str2 = BuildConfig.FLAVOR;
                String[] split = errorMsg.split(UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR, 2);
                if (split.length > 0) {
                    if (split[0].indexOf("Uncaught ") < 0) {
                        str = split[0];
                    } else {
                        str = split[0].substring(9);
                    }
                    str = str.trim();
                }
                if (split.length > 1) {
                    str2 = split[1].trim();
                }
                this.f570a.m105b(new bl(str, str2, stackStr, false));
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    @JavascriptInterface
    public int getTransactionValue(String transactionName) {
        int i = -1;
        try {
            if (this.f570a == null) {
                m479a("getTransactionValue");
            } else if (m481a(transactionName, "getTransactionValue", "transactionName")) {
                i = this.f570a.m109e(transactionName);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
        return i;
    }

    private static boolean m481a(String str, String str2, String str3) {
        if (str == null) {
            cc.m358b(CritterJSInterface.class.getName() + "." + str2 + "() given invalid " + (str3.length() > 0 ? str3 + " " : BuildConfig.FLAVOR) + "parameter: null string or invalid javascript type. Request is being ignored...", new NullPointerException());
            return false;
        } else if (str.length() != 0) {
            return true;
        } else {
            m482b(str2, str3, "empty string");
            return false;
        }
    }

    private static void m479a(String str) {
        cc.m358b(CritterJSInterface.class.getName() + "." + str + "() badly initialized: null instance.", new NullPointerException());
    }

    private static void m480a(String str, String str2, long j) {
        m482b(str, str2, "negative long integer: " + j);
    }

    private static void m482b(String str, String str2, String str3) {
        String str4 = (str2 == null || str2.length() <= 0) ? BuildConfig.FLAVOR : str2 + " ";
        cc.m358b(CritterJSInterface.class.getName() + "." + str + "() given invalid " + str4 + "parameter: " + str3 + ". Request is being ignored.", new IllegalArgumentException());
    }

    @JavascriptInterface
    public void logHandledException(String name, String reason, String stackStr) {
        try {
            if (this.f570a == null) {
                m479a("logHandledException");
            } else if (m481a(name, "logHandledException", Twitter.NAME) && m481a(reason, "logHandledException", "reason") && m481a(stackStr, "logHandledException", "stack")) {
                this.f570a.m105b(new bl(name, reason, stackStr, true));
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    @JavascriptInterface
    public void logUnhandledException(String name, String reason, String stackStr) {
        try {
            if (this.f570a == null) {
                m479a("logUnhandledException");
            } else if (m481a(name, "logUnhandledException", Twitter.NAME) && m481a(reason, "logUnhandledException", "reason") && m481a(stackStr, "logUnhandledException", "stack")) {
                this.f570a.m105b(new bl(name, reason, stackStr, false));
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    @JavascriptInterface
    public void leaveBreadcrumb(String breadcrumb) {
        try {
            if (this.f570a == null) {
                m479a("leaveBreadcrumb");
            } else if (m481a(breadcrumb, "leaveBreadcrumb", "breadcrumb")) {
                this.f570a.m97a(at.m159a(breadcrumb));
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    @JavascriptInterface
    public void beginTransaction(String name) {
        try {
            if (this.f570a == null) {
                m479a("beginTransaction");
            } else if (m481a(name, "beginTransaction", Twitter.NAME)) {
                this.f570a.m98a(name);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    @JavascriptInterface
    public void endTransaction(String name) {
        try {
            if (this.f570a == null) {
                m479a("endTransaction");
            } else if (m481a(name, "endTransaction", Twitter.NAME)) {
                this.f570a.m104b(name);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    @JavascriptInterface
    public void failTransaction(String name) {
        try {
            if (this.f570a == null) {
                m479a("failTransaction");
            } else if (m481a(name, "failTransaction", Twitter.NAME)) {
                this.f570a.m107c(name);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    @JavascriptInterface
    public void cancelTransaction(String name) {
        try {
            if (this.f570a == null) {
                m479a("cancelTransaction");
            } else if (m481a(name, "cancelTransaction", Twitter.NAME)) {
                this.f570a.m108d(name);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    @JavascriptInterface
    public void setTransactionValue(String transactionName, int transactionValue) {
        try {
            if (this.f570a == null) {
                m479a("setTransactionValue");
            } else if (m481a(transactionName, "setTransactionValue", "transactionName")) {
                this.f570a.m99a(transactionName, transactionValue);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    @JavascriptInterface
    public void setMetadata(String metadataJsonStr) {
        try {
            if (this.f570a == null) {
                m479a("setMetadata");
            } else if (m481a(metadataJsonStr, "setMetadata", "metadataJson")) {
                try {
                    this.f570a.m102a(new JSONObject(metadataJsonStr));
                } catch (JSONException e) {
                    m482b("setMetadata", BuildConfig.FLAVOR, "badly formatted json string. " + metadataJsonStr);
                }
            }
        } catch (ThreadDeath e2) {
            throw e2;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }

    @JavascriptInterface
    public void setUsername(String username) {
        try {
            if (this.f570a == null) {
                m479a("setUsername");
            } else if (m481a(username, "setUsername", Ui.USER_NAME)) {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.putOpt(Ui.USER_NAME, username);
                    this.f570a.m102a(jSONObject);
                } catch (Throwable e) {
                    cc.m358b("Crittercism.setUsername()", e);
                }
            }
        } catch (ThreadDeath e2) {
            throw e2;
        } catch (Throwable e3) {
            cc.m359b(e3);
        }
    }

    @JavascriptInterface
    public void logNetworkRequest(String method, String url, long latency, long bytesRead, long bytesSent, int responseCode, int errorCode) {
        try {
            if (this.f570a == null) {
                m479a("logNetworkRequest");
                return;
            }
            int i;
            Object obj;
            if (m481a(method, "logNetworkRequest", "httpMethod")) {
                String[] strArr = new String[]{"GET", "HEAD", "POST", "PUT", "DELETE", "TRACE", "CONNECT", "OPTIONS", HttpPatch.METHOD_NAME};
                for (i = 0; i < 9; i++) {
                    if (strArr[i].equals(method)) {
                        obj = 1;
                        break;
                    }
                }
                m482b("logNetworkRequest", "httpMethod", method);
                obj = null;
            } else {
                obj = null;
            }
            if (obj != null) {
                if (!m481a(url, "logNetworkRequest", Keys.URL)) {
                    return;
                }
                if (bytesRead < 0) {
                    m480a("logNetworkRequest", "bytesRead", bytesRead);
                } else if (bytesSent < 0) {
                    m480a("logNetworkRequest", "bytesSent", bytesSent);
                } else {
                    if (responseCode < 0) {
                        m482b("logNetworkRequest", "responseCode", "negative integer: " + responseCode);
                        obj = null;
                    } else {
                        int[] iArr = new int[]{0, 100, VASTPlayer.ERROR_SCHEMA_VALIDATION, Callback.DEFAULT_DRAG_ANIMATION_DURATION, 201, 202, 203, 204, 205, 206, VASTPlayer.ERROR_GENERAL_WRAPPER, VASTPlayer.ERROR_WRAPPER_TIMEOUT, VASTPlayer.ERROR_EXCEEDED_WRAPPER_LIMIT, VASTPlayer.ERROR_NO_VAST_IN_WRAPPER, 304, 305, 306, 307, VASTPlayer.ERROR_GENERAL_LINEAR, VASTPlayer.ERROR_FILE_NOT_FOUND, VASTPlayer.ERROR_VIDEO_TIMEOUT, VASTPlayer.ERROR_NO_COMPATIBLE_MEDIA_FILE, 404, VASTPlayer.ERROR_VIDEO_PLAYBACK, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 417, 500, 501, 502, 503, 504, 505};
                        for (i = 0; i < 42; i++) {
                            if (iArr[i] == responseCode) {
                                obj = 1;
                                break;
                            }
                        }
                        m482b("logNetworkRequest", "responseCode", "the given HTTP response is not allowed: " + responseCode);
                        obj = null;
                    }
                    if (obj != null) {
                        this.f570a.m100a(method, url, latency, bytesRead, bytesSent, responseCode, new bj(bk.f310e - 1, errorCode));
                    }
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
    }
}
