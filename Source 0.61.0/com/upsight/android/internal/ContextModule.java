package com.upsight.android.internal;

import android.content.Context;
import android.util.Log;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import com.upsight.android.internal.logger.LogWriter;
import com.upsight.android.internal.persistence.storable.StorableIdFactory;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.logger.UpsightLogger.Level;
import com.upsight.mediation.mraid.properties.MRAIDResizeProperties;
import dagger.Module;
import dagger.Provides;
import java.util.UUID;
import javax.inject.Singleton;
import spacemadness.com.lunarconsole.C1628R;

@Module
public final class ContextModule {
    private final Context mApplicationContext;

    /* renamed from: com.upsight.android.internal.ContextModule.1 */
    class C11151 implements LogWriter {
        C11151() {
        }

        public void write(String tag, Level level, String message) {
            if (message.length() > UpsightLogger.MAX_LENGTH) {
                int chunkCount = message.length() / UpsightLogger.MAX_LENGTH;
                for (int i = 0; i <= chunkCount; i++) {
                    int max = (i + 1) * UpsightLogger.MAX_LENGTH;
                    if (max >= message.length()) {
                        logMessage(tag, level, message.substring(i * UpsightLogger.MAX_LENGTH));
                    } else {
                        logMessage(tag, level, message.substring(i * UpsightLogger.MAX_LENGTH, max));
                    }
                }
                return;
            }
            logMessage(tag, level, message);
        }

        private void logMessage(String tag, Level level, String message) {
            switch (C11173.$SwitchMap$com$upsight$android$logger$UpsightLogger$Level[level.ordinal()]) {
                case C1628R.styleable.RecyclerView_layoutManager /*1*/:
                    Log.v(tag, message);
                case C1628R.styleable.RecyclerView_spanCount /*2*/:
                    Log.d(tag, message);
                case C1628R.styleable.RecyclerView_reverseLayout /*3*/:
                    Log.i(tag, message);
                case C1628R.styleable.RecyclerView_stackFromEnd /*4*/:
                    Log.w(tag, message);
                case MRAIDResizeProperties.CUSTOM_CLOSE_POSITION_BOTTOM_CENTER /*5*/:
                    Log.e(tag, message);
                default:
            }
        }
    }

    /* renamed from: com.upsight.android.internal.ContextModule.2 */
    class C11162 implements StorableIdFactory {
        C11162() {
        }

        public String createObjectID() {
            return UUID.randomUUID().toString();
        }
    }

    /* renamed from: com.upsight.android.internal.ContextModule.3 */
    static /* synthetic */ class C11173 {
        static final /* synthetic */ int[] $SwitchMap$com$upsight$android$logger$UpsightLogger$Level;

        static {
            $SwitchMap$com$upsight$android$logger$UpsightLogger$Level = new int[Level.values().length];
            try {
                $SwitchMap$com$upsight$android$logger$UpsightLogger$Level[Level.VERBOSE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$upsight$android$logger$UpsightLogger$Level[Level.DEBUG.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$upsight$android$logger$UpsightLogger$Level[Level.INFO.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$upsight$android$logger$UpsightLogger$Level[Level.WARN.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$upsight$android$logger$UpsightLogger$Level[Level.ERROR.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public ContextModule(Context context) {
        this.mApplicationContext = context.getApplicationContext();
    }

    @Singleton
    @Provides
    Context provideApplicationContext() {
        return this.mApplicationContext;
    }

    @Singleton
    @Provides
    Bus provideBus() {
        return new Bus(ThreadEnforcer.ANY);
    }

    @Singleton
    @Provides
    LogWriter provideLogWriter() {
        return new C11151();
    }

    @Singleton
    @Provides
    StorableIdFactory provideTypeIdGenerator() {
        return new C11162();
    }
}
