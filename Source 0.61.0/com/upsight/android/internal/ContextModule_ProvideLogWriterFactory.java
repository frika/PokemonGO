package com.upsight.android.internal;

import com.upsight.android.internal.logger.LogWriter;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class ContextModule_ProvideLogWriterFactory implements Factory<LogWriter> {
    static final /* synthetic */ boolean $assertionsDisabled;
    private final ContextModule module;

    static {
        $assertionsDisabled = !ContextModule_ProvideLogWriterFactory.class.desiredAssertionStatus();
    }

    public ContextModule_ProvideLogWriterFactory(ContextModule module) {
        if ($assertionsDisabled || module != null) {
            this.module = module;
            return;
        }
        throw new AssertionError();
    }

    public LogWriter get() {
        return (LogWriter) Preconditions.checkNotNull(this.module.provideLogWriter(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<LogWriter> create(ContextModule module) {
        return new ContextModule_ProvideLogWriterFactory(module);
    }
}
