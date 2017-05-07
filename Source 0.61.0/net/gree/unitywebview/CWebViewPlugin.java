package net.gree.unitywebview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build.VERSION;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import com.unity3d.player.UnityPlayer;

public class CWebViewPlugin {
    private static FrameLayout layout;
    private boolean canGoBack;
    private boolean canGoForward;
    private WebView mWebView;
    private CWebViewPluginInterface mWebViewPlugin;

    /* renamed from: net.gree.unitywebview.CWebViewPlugin.1 */
    class C12681 implements Runnable {
        final /* synthetic */ Activity val$a;
        final /* synthetic */ String val$gameObject;
        final /* synthetic */ CWebViewPlugin val$self;
        final /* synthetic */ boolean val$transparent;

        /* renamed from: net.gree.unitywebview.CWebViewPlugin.1.1 */
        class C12671 extends WebViewClient {
            final /* synthetic */ WebView val$webView;

            C12671(WebView webView) {
                this.val$webView = webView;
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                this.val$webView.loadUrl("about:blank");
                CWebViewPlugin.this.canGoBack = this.val$webView.canGoBack();
                CWebViewPlugin.this.canGoForward = this.val$webView.canGoForward();
                CWebViewPlugin.this.mWebViewPlugin.call("CallOnError", errorCode + "\t" + description + "\t" + failingUrl);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                CWebViewPlugin.this.canGoBack = this.val$webView.canGoBack();
                CWebViewPlugin.this.canGoForward = this.val$webView.canGoForward();
            }

            public void onPageFinished(WebView view, String url) {
                CWebViewPlugin.this.canGoBack = this.val$webView.canGoBack();
                CWebViewPlugin.this.canGoForward = this.val$webView.canGoForward();
                CWebViewPlugin.this.mWebViewPlugin.call("CallOnLoaded", url);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file://") || url.startsWith("javascript:")) {
                    return false;
                }
                if (url.startsWith("unity:")) {
                    CWebViewPlugin.this.mWebViewPlugin.call("CallFromJS", url.substring(6));
                    return true;
                }
                view.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                return true;
            }
        }

        C12681(Activity activity, CWebViewPlugin cWebViewPlugin, String str, boolean z) {
            this.val$a = activity;
            this.val$self = cWebViewPlugin;
            this.val$gameObject = str;
            this.val$transparent = z;
        }

        public void run() {
            if (CWebViewPlugin.this.mWebView == null) {
                WebView webView = new WebView(this.val$a);
                webView.setVisibility(8);
                webView.setFocusable(true);
                webView.setFocusableInTouchMode(true);
                webView.setWebChromeClient(new WebChromeClient());
                CWebViewPlugin.this.mWebViewPlugin = new CWebViewPluginInterface(this.val$self, this.val$gameObject);
                webView.setWebViewClient(new C12671(webView));
                webView.addJavascriptInterface(CWebViewPlugin.this.mWebViewPlugin, "Unity");
                WebSettings webSettings = webView.getSettings();
                webSettings.setSupportZoom(false);
                webSettings.setJavaScriptEnabled(true);
                if (VERSION.SDK_INT >= 16) {
                    webSettings.setAllowUniversalAccessFromFileURLs(true);
                }
                webSettings.setDatabaseEnabled(true);
                webSettings.setDomStorageEnabled(true);
                webSettings.setDatabasePath(webView.getContext().getDir("databases", 0).getPath());
                webSettings.setUseWideViewPort(true);
                if (this.val$transparent) {
                    webView.setBackgroundColor(0);
                }
                if (CWebViewPlugin.layout == null) {
                    CWebViewPlugin.layout = new FrameLayout(this.val$a);
                    this.val$a.addContentView(CWebViewPlugin.layout, new LayoutParams(-1, -1));
                    CWebViewPlugin.layout.setFocusable(true);
                    CWebViewPlugin.layout.setFocusableInTouchMode(true);
                }
                CWebViewPlugin.layout.addView(webView, new FrameLayout.LayoutParams(-1, -1, 0));
                CWebViewPlugin.this.mWebView = webView;
            }
        }
    }

    /* renamed from: net.gree.unitywebview.CWebViewPlugin.2 */
    class C12692 implements OnGlobalLayoutListener {
        final /* synthetic */ Activity val$a;
        final /* synthetic */ View val$activityRootView;
        final /* synthetic */ String val$gameObject;

        C12692(View view, Activity activity, String str) {
            this.val$activityRootView = view;
            this.val$a = activity;
            this.val$gameObject = str;
        }

        public void onGlobalLayout() {
            Rect r = new Rect();
            this.val$activityRootView.getWindowVisibleDisplayFrame(r);
            Display display = this.val$a.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            if (this.val$activityRootView.getRootView().getHeight() - (r.bottom - r.top) > size.y / 3) {
                UnityPlayer.UnitySendMessage(this.val$gameObject, "SetKeyboardVisible", "true");
            } else {
                UnityPlayer.UnitySendMessage(this.val$gameObject, "SetKeyboardVisible", "false");
            }
        }
    }

    /* renamed from: net.gree.unitywebview.CWebViewPlugin.3 */
    class C12703 implements Runnable {
        C12703() {
        }

        public void run() {
            if (CWebViewPlugin.this.mWebView != null) {
                CWebViewPlugin.layout.removeView(CWebViewPlugin.this.mWebView);
                CWebViewPlugin.this.mWebView = null;
            }
        }
    }

    /* renamed from: net.gree.unitywebview.CWebViewPlugin.4 */
    class C12714 implements Runnable {
        final /* synthetic */ String val$url;

        C12714(String str) {
            this.val$url = str;
        }

        public void run() {
            if (CWebViewPlugin.this.mWebView != null) {
                CWebViewPlugin.this.mWebView.loadUrl(this.val$url);
            }
        }
    }

    /* renamed from: net.gree.unitywebview.CWebViewPlugin.5 */
    class C12725 implements Runnable {
        final /* synthetic */ String val$js;

        C12725(String str) {
            this.val$js = str;
        }

        public void run() {
            if (CWebViewPlugin.this.mWebView != null) {
                CWebViewPlugin.this.mWebView.loadUrl("javascript:" + this.val$js);
            }
        }
    }

    /* renamed from: net.gree.unitywebview.CWebViewPlugin.6 */
    class C12736 implements Runnable {
        C12736() {
        }

        public void run() {
            if (CWebViewPlugin.this.mWebView != null) {
                CWebViewPlugin.this.mWebView.goBack();
            }
        }
    }

    /* renamed from: net.gree.unitywebview.CWebViewPlugin.7 */
    class C12747 implements Runnable {
        C12747() {
        }

        public void run() {
            if (CWebViewPlugin.this.mWebView != null) {
                CWebViewPlugin.this.mWebView.goForward();
            }
        }
    }

    /* renamed from: net.gree.unitywebview.CWebViewPlugin.8 */
    class C12758 implements Runnable {
        final /* synthetic */ FrameLayout.LayoutParams val$params;

        C12758(FrameLayout.LayoutParams layoutParams) {
            this.val$params = layoutParams;
        }

        public void run() {
            if (CWebViewPlugin.this.mWebView != null) {
                CWebViewPlugin.this.mWebView.setLayoutParams(this.val$params);
            }
        }
    }

    /* renamed from: net.gree.unitywebview.CWebViewPlugin.9 */
    class C12769 implements Runnable {
        final /* synthetic */ boolean val$visibility;

        C12769(boolean z) {
            this.val$visibility = z;
        }

        public void run() {
            if (CWebViewPlugin.this.mWebView != null) {
                if (this.val$visibility) {
                    CWebViewPlugin.this.mWebView.setVisibility(0);
                    CWebViewPlugin.layout.requestFocus();
                    CWebViewPlugin.this.mWebView.requestFocus();
                    return;
                }
                CWebViewPlugin.this.mWebView.setVisibility(8);
            }
        }
    }

    static {
        layout = null;
    }

    public boolean IsInitialized() {
        return this.mWebView != null;
    }

    public void Init(String gameObject, boolean transparent) {
        Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new C12681(a, this, gameObject, transparent));
        View activityRootView = a.getWindow().getDecorView().getRootView();
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new C12692(activityRootView, a, gameObject));
    }

    public void Destroy() {
        UnityPlayer.currentActivity.runOnUiThread(new C12703());
    }

    public void LoadURL(String url) {
        UnityPlayer.currentActivity.runOnUiThread(new C12714(url));
    }

    public void EvaluateJS(String js) {
        UnityPlayer.currentActivity.runOnUiThread(new C12725(js));
    }

    public void GoBack() {
        UnityPlayer.currentActivity.runOnUiThread(new C12736());
    }

    public void GoForward() {
        UnityPlayer.currentActivity.runOnUiThread(new C12747());
    }

    public void SetMargins(int left, int top, int right, int bottom) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -1, 0);
        params.setMargins(left, top, right, bottom);
        UnityPlayer.currentActivity.runOnUiThread(new C12758(params));
    }

    public void SetVisibility(boolean visibility) {
        UnityPlayer.currentActivity.runOnUiThread(new C12769(visibility));
    }
}
