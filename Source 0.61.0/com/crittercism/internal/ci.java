package com.crittercism.internal;

import android.os.Build.VERSION;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.lang.reflect.Field;

public final class ci {
    public ci() {
        if (VERSION.SDK_INT < 14 || VERSION.SDK_INT > 23) {
            throw new bh("API Level " + VERSION.SDK_INT + " does not supportWebView monitoring. Skipping instrumentation.");
        }
    }

    public static WebViewClient m375a(WebView webView) {
        try {
            Object obj = C0255e.m384a(WebView.class, Class.forName("android.webkit.CallbackProxy"), true).get(webView);
            return (WebViewClient) obj.getClass().getMethod("getWebViewClient", new Class[0]).invoke(obj, new Object[0]);
        } catch (Throwable e) {
            throw new bh(e);
        } catch (Throwable e2) {
            throw new bh(e2);
        } catch (Throwable e22) {
            throw new bh(e22);
        } catch (Throwable e222) {
            throw new bh(e222);
        } catch (Throwable e2222) {
            throw new bh(e2222);
        }
    }

    public static WebViewClient m376b(WebView webView) {
        try {
            Object invoke = WebView.class.getMethod("getWebViewProvider", new Class[0]).invoke(webView, new Object[0]);
            return (WebViewClient) invoke.getClass().getMethod("getWebViewClient", new Class[0]).invoke(invoke, new Object[0]);
        } catch (Throwable e) {
            throw new bh(e);
        } catch (Throwable e2) {
            throw new bh(e2);
        } catch (Throwable e22) {
            throw new bh(e22);
        } catch (Throwable e222) {
            throw new bh(e222);
        }
    }

    public static WebViewClient m377c(WebView webView) {
        try {
            Object invoke = WebView.class.getMethod("getWebViewProvider", new Class[0]).invoke(webView, new Object[0]);
            Field declaredField = invoke.getClass().getDeclaredField("mContentsClientAdapter");
            declaredField.setAccessible(true);
            invoke = declaredField.get(invoke);
            declaredField = invoke.getClass().getDeclaredField("mWebViewClient");
            declaredField.setAccessible(true);
            return (WebViewClient) declaredField.get(invoke);
        } catch (Throwable e) {
            throw new bh(e);
        } catch (Throwable e2) {
            throw new bh(e2);
        } catch (Throwable e22) {
            throw new bh(e22);
        } catch (Throwable e222) {
            throw new bh(e222);
        } catch (Throwable e2222) {
            throw new bh(e2222);
        }
    }
}
