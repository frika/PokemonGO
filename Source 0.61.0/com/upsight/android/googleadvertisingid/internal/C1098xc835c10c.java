package com.upsight.android.googleadvertisingid.internal;

import dagger.internal.Factory;
import dagger.internal.Preconditions;

/* renamed from: com.upsight.android.googleadvertisingid.internal.GoogleAdvertisingProviderModule_ProvideGooglePlayAdvertisingProviderFactory */
public final class C1098xc835c10c implements Factory<GooglePlayAdvertisingProvider> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final GoogleAdvertisingProviderModule module;

    static {
        $assertionsDisabled = !C1098xc835c10c.class.desiredAssertionStatus();
    }

    public C1098xc835c10c(GoogleAdvertisingProviderModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public GooglePlayAdvertisingProvider get() {
        return (GooglePlayAdvertisingProvider) Preconditions.checkNotNull(this.module.provideGooglePlayAdvertisingProvider(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<GooglePlayAdvertisingProvider> create(GoogleAdvertisingProviderModule module) {
        return new C1098xc835c10c(module);
    }
}
