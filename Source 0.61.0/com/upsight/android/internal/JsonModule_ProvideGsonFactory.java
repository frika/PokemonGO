package com.upsight.android.internal;

import com.google.gson.Gson;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class JsonModule_ProvideGsonFactory implements Factory<Gson> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final JsonModule module;

    static {
        $assertionsDisabled = !JsonModule_ProvideGsonFactory.class.desiredAssertionStatus();
    }

    public JsonModule_ProvideGsonFactory(JsonModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public Gson get() {
        return (Gson) Preconditions.checkNotNull(this.module.provideGson(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Gson> create(JsonModule module) {
        return new JsonModule_ProvideGsonFactory(module);
    }
}
