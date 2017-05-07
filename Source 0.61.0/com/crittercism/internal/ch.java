package com.crittercism.internal;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build.VERSION;
import android.os.Message;
import android.util.Base64;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import spacemadness.com.lunarconsole.BuildConfig;

public final class ch extends WebViewClient {
    private WebViewClient f486a;
    private C0230c f487b;
    private final String f488c;
    private C0254d f489d;
    private C0212b f490e;

    public ch(WebViewClient webViewClient, C0254d c0254d, C0230c c0230c, String str) {
        this.f486a = webViewClient;
        this.f489d = c0254d;
        this.f487b = c0230c;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("javascript:(function() {");
        stringBuilder.append("  if (typeof(Crittercism) !== \"undefined\") {");
        stringBuilder.append("    Crittercism.init({");
        stringBuilder.append("      \"platform\": \"android\"});");
        stringBuilder.append("  } else {");
        stringBuilder.append("    (");
        stringBuilder.append("      function() {");
        stringBuilder.append("        var parent = document.getElementsByTagName('head').item(0);");
        stringBuilder.append("        var script = document.createElement('script');");
        stringBuilder.append("        script.type = 'text/javascript';");
        stringBuilder.append("        script.innerHTML = window.atob('");
        stringBuilder.append(Base64.encodeToString(str.getBytes(), 2));
        stringBuilder.append("                                     ');");
        stringBuilder.append("        parent.appendChild(script)");
        stringBuilder.append("      }");
        stringBuilder.append("    )();");
        stringBuilder.append("    if (typeof(BasicCrittercism) !== \"undefined\") {");
        stringBuilder.append("      BasicCrittercism.instrumentOnError({");
        stringBuilder.append("        errorCallback: function(errorMsg, stackStr) {");
        stringBuilder.append("          _crttr.logError(errorMsg, stackStr);");
        stringBuilder.append("          }, ");
        stringBuilder.append("        platform: \"android\"");
        stringBuilder.append("      });");
        stringBuilder.append("      BasicCrittercism.initApm();");
        stringBuilder.append("    } ");
        stringBuilder.append("  }");
        stringBuilder.append("})()");
        this.f488c = stringBuilder.toString();
    }

    @TargetApi(21)
    public final WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        WebResourceResponse shouldInterceptRequest;
        long currentTimeMillis = System.currentTimeMillis();
        try {
            if (VERSION.SDK_INT >= 23 && request != null && request.isForMainFrame()) {
                cc.m362d("******** shouldInterceptRequest (Lollipop) part 1");
                synchronized (this) {
                    this.f490e = new C0212b();
                    this.f490e.m228a(request.getUrl().toString());
                    this.f490e.f249j = request.getMethod();
                    this.f490e.m234c(currentTimeMillis);
                    this.f490e.f254o = C0176a.m24a(this.f487b.f403a);
                    if (an.m112b()) {
                        this.f490e.m227a(an.m110a());
                    }
                }
                return shouldInterceptRequest;
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
        if (this.f486a != null) {
            shouldInterceptRequest = this.f486a.shouldInterceptRequest(view, request);
        } else {
            shouldInterceptRequest = null;
        }
        try {
            if (VERSION.SDK_INT >= 23 && request != null && request.isForMainFrame()) {
                cc.m362d("******** shouldInterceptRequest (Lollipop) part 2");
                if (shouldInterceptRequest != null) {
                    synchronized (this) {
                        if (this.f490e != null) {
                            this.f490e.f248i = shouldInterceptRequest.getStatusCode();
                        }
                    }
                }
            }
        } catch (ThreadDeath e2) {
            throw e2;
        } catch (Throwable th2) {
            cc.m359b(th2);
        }
        return shouldInterceptRequest;
    }

    public final void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        if (this.f486a != null) {
            this.f486a.doUpdateVisitedHistory(view, url, isReload);
        }
    }

    public final void onFormResubmission(WebView view, Message dontResend, Message resend) {
        if (this.f486a != null) {
            this.f486a.onFormResubmission(view, dontResend, resend);
        }
    }

    public final void onLoadResource(WebView view, String url) {
        if (this.f486a != null) {
            this.f486a.onLoadResource(view, url);
        }
    }

    public final void onPageStarted(WebView view, String url, Bitmap favicon) {
        cc.m362d("******** onPageStarted: " + url);
        if (this.f486a != null) {
            this.f486a.onPageStarted(view, url, favicon);
        }
    }

    @TargetApi(21)
    public final void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
        if (this.f486a != null) {
            this.f486a.onReceivedClientCertRequest(view, request);
        }
    }

    @TargetApi(23)
    public final void onPageCommitVisible(WebView view, String url) {
        if (this.f486a != null) {
            this.f486a.onPageCommitVisible(view, url);
        }
    }

    public final void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (this.f486a != null) {
            this.f486a.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    public final void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        if (this.f486a != null) {
            this.f486a.onReceivedHttpAuthRequest(view, handler, host, realm);
        }
    }

    @TargetApi(12)
    public final void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
        if (this.f486a != null) {
            this.f486a.onReceivedLoginRequest(view, realm, account, args);
        }
    }

    @TargetApi(8)
    public final void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if (this.f486a != null) {
            this.f486a.onReceivedSslError(view, handler, error);
        }
    }

    public final void onScaleChanged(WebView view, float oldScale, float newScale) {
        if (this.f486a != null) {
            this.f486a.onScaleChanged(view, oldScale, newScale);
        }
    }

    public final void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
        if (this.f486a != null) {
            this.f486a.onTooManyRedirects(view, cancelMsg, continueMsg);
        }
    }

    @TargetApi(21)
    public final void onUnhandledInputEvent(WebView view, InputEvent event) {
        if (this.f486a != null) {
            this.f486a.onUnhandledInputEvent(view, event);
        }
    }

    public final void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        if (this.f486a != null) {
            this.f486a.onUnhandledKeyEvent(view, event);
        }
    }

    @TargetApi(11)
    public final WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        if (this.f486a != null) {
            return this.f486a.shouldInterceptRequest(view, url);
        }
        return null;
    }

    public final boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        if (this.f486a != null) {
            return this.f486a.shouldOverrideKeyEvent(view, event);
        }
        return false;
    }

    public final boolean shouldOverrideUrlLoading(WebView view, String url) {
        cc.m362d("******** shouldOverrideUrlLoading: " + url);
        if (this.f486a != null) {
            return this.f486a.shouldOverrideUrlLoading(view, url);
        }
        return false;
    }

    public final void onPageFinished(WebView view, String url) {
        try {
            cc.m362d("******** onPageFinished: " + url);
            if (VERSION.SDK_INT >= 23) {
                synchronized (this) {
                    if (this.f490e != null) {
                        this.f490e.m236d(System.currentTimeMillis());
                        this.f489d.m381a(this.f490e);
                        this.f490e = null;
                    }
                }
            }
            if (view.getSettings().getJavaScriptEnabled()) {
                view.loadUrl(this.f488c);
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
        if (this.f486a != null) {
            this.f486a.onPageFinished(view, url);
        }
    }

    @TargetApi(23)
    public final void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        try {
            if (VERSION.SDK_INT >= 23) {
                cc.m362d("******** onReceivedHttpError (Marshmallow)");
                boolean isForMainFrame = request.isForMainFrame();
                cc.m362d((isForMainFrame ? BuildConfig.FLAVOR : "not ") + "main frame");
                if (isForMainFrame) {
                    if (errorResponse == null) {
                        cc.m362d("null response (no status code)");
                    } else {
                        synchronized (this) {
                            if (this.f490e != null) {
                                this.f490e.f248i = errorResponse.getStatusCode();
                            }
                        }
                    }
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
        if (this.f486a != null) {
            this.f486a.onReceivedHttpError(view, request, errorResponse);
        }
    }

    @TargetApi(23)
    public final void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        try {
            if (VERSION.SDK_INT >= 23) {
                cc.m362d("******** onReceivedError (Marshmallow, no http)");
                if (request == null) {
                    cc.m362d("null request");
                } else {
                    boolean isForMainFrame = request.isForMainFrame();
                    cc.m362d((isForMainFrame ? BuildConfig.FLAVOR : "not ") + "main frame");
                    if (isForMainFrame) {
                        if (error == null) {
                            cc.m362d("null error (no error code)");
                        } else {
                            synchronized (this) {
                                if (this.f490e != null) {
                                    this.f490e.f250k = new bj(bk.f310e - 1, error.getErrorCode());
                                }
                            }
                        }
                    }
                }
            }
        } catch (ThreadDeath e) {
            throw e;
        } catch (Throwable th) {
            cc.m359b(th);
        }
        if (this.f486a != null) {
            this.f486a.onReceivedError(view, request, error);
        }
    }
}
