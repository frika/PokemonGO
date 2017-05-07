package com.upsight.android.analytics.internal.session;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import com.upsight.android.analytics.UpsightLifeCycleTracker.ActivityState;
import javax.inject.Inject;

@TargetApi(14)
public class ActivityLifecycleTracker implements ActivityLifecycleCallbacks {
    private SessionInitializer mSessionInitializer;
    private ManualTracker mTracker;

    @Inject
    public ActivityLifecycleTracker(ManualTracker tracker) {
        this.mTracker = tracker;
        this.mSessionInitializer = new StandardSessionInitializer();
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        this.mTracker.track(activity, ActivityState.CREATED, this.mSessionInitializer);
    }

    public void onActivityStarted(Activity activity) {
        this.mTracker.track(activity, ActivityState.STARTED, this.mSessionInitializer);
    }

    public void onActivityResumed(Activity activity) {
        this.mTracker.track(activity, ActivityState.RESUMED, this.mSessionInitializer);
    }

    public void onActivityPaused(Activity activity) {
        this.mTracker.track(activity, ActivityState.PAUSED, this.mSessionInitializer);
    }

    public void onActivityStopped(Activity activity) {
        this.mTracker.track(activity, ActivityState.STOPPED, this.mSessionInitializer);
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        this.mTracker.track(activity, ActivityState.SAVE_INSTANCE_STATE, this.mSessionInitializer);
    }

    public void onActivityDestroyed(Activity activity) {
        this.mTracker.track(activity, ActivityState.DESTROYED, this.mSessionInitializer);
    }
}
