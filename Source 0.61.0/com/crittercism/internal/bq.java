package com.crittercism.internal;

import android.app.Activity;
import android.app.Application;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import com.google.android.exoplayer.chunk.FormatEvaluator.AdaptiveEvaluator;
import java.util.ArrayDeque;
import java.util.Deque;
import spacemadness.com.lunarconsole.BuildConfig;

public final class bq extends bm {
    public au f355b;
    public Deque<String> f356c;

    public bq(@NonNull Application application, @NonNull au auVar) {
        super(application);
        this.f356c = new ArrayDeque();
        if (VERSION.SDK_INT < 9) {
            throw new IllegalStateException("Activity monitoring is only supported on API 9 and later");
        }
        this.f355b = auVar;
        m285a();
    }

    private static String m305d(Activity activity) {
        if (activity == null) {
            return null;
        }
        return activity.getComponentName().flattenToShortString().replace("/", BuildConfig.FLAVOR);
    }

    private void m306g() {
        this.f355b.f209g = (String) this.f356c.peekFirst();
    }

    public final void m307b(Activity activity) {
        if (this.f356c.size() >= AdaptiveEvaluator.DEFAULT_MIN_DURATION_FOR_QUALITY_INCREASE_MS) {
            this.f356c.removeLast();
        }
        String d = m305d(activity);
        if (d != null) {
            this.f356c.addFirst(d);
            m306g();
        }
    }

    public final void m308c(Activity activity) {
        String d = m305d(activity);
        if (d != null) {
            this.f356c.removeFirstOccurrence(d);
            m306g();
        }
    }

    public final void m309e() {
    }
}
