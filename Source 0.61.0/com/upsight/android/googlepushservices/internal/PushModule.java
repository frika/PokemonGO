package com.upsight.android.googlepushservices.internal;

import android.support.annotation.NonNull;
import com.upsight.android.UpsightAnalyticsExtension;
import com.upsight.android.UpsightContext;
import com.upsight.android.analytics.UpsightAnalyticsComponent;
import com.upsight.android.analytics.internal.session.Session;
import com.upsight.android.analytics.internal.session.SessionInitializer;
import com.upsight.android.analytics.internal.session.SessionManager;
import com.upsight.android.analytics.session.UpsightSessionInfo;
import com.upsight.android.googlepushservices.UpsightGooglePushServicesApi;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public final class PushModule {
    private final UpsightContext mUpsight;

    /* renamed from: com.upsight.android.googlepushservices.internal.PushModule.1 */
    class C11141 implements SessionManager {
        C11141() {
        }

        public UpsightSessionInfo getLatestSessionInfo() {
            return UpsightSessionInfo.NONE;
        }

        public Session getBackgroundSession() {
            return null;
        }

        public Session getSession() {
            return null;
        }

        public Session startSession(@NonNull SessionInitializer sessionInitializer) {
            return null;
        }

        public void stopSession() {
        }
    }

    public PushModule(UpsightContext upsight) {
        this.mUpsight = upsight;
    }

    @Singleton
    @Provides
    UpsightContext provideUpsightContext() {
        return this.mUpsight;
    }

    @Singleton
    @Provides
    SessionManager provideSessionManager(UpsightContext upsight) {
        UpsightAnalyticsExtension extension = (UpsightAnalyticsExtension) upsight.getUpsightExtension(UpsightAnalyticsExtension.EXTENSION_NAME);
        if (extension != null) {
            return ((UpsightAnalyticsComponent) extension.getComponent()).sessionManager();
        }
        return new C11141();
    }

    @Singleton
    @Provides
    public UpsightGooglePushServicesApi provideGooglePushServicesApi(GooglePushServices googlePushServices) {
        return googlePushServices;
    }

    @Singleton
    @Provides
    public PushConfigManager providePushConfigManager(UpsightContext upsight) {
        return new PushConfigManager(upsight);
    }
}
