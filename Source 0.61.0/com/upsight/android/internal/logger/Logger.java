package com.upsight.android.internal.logger;

import android.text.TextUtils;
import com.upsight.android.UpsightException;
import com.upsight.android.logger.UpsightLogger;
import com.upsight.android.logger.UpsightLogger.Level;
import com.upsight.android.persistence.UpsightDataStore;
import com.upsight.android.persistence.UpsightDataStoreListener;
import com.upsight.android.persistence.UpsightSubscription;
import com.upsight.android.persistence.annotation.Created;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public final class Logger implements UpsightLogger {
    private final UpsightDataStore mDataStore;
    private final LogWriter mLogWriter;
    private final Map<String, UpsightSubscription> mSubscriptionsMap;

    /* renamed from: com.upsight.android.internal.logger.Logger.1 */
    class C11191 implements UpsightDataStoreListener<LogMessage> {
        C11191() {
        }

        public void onSuccess(LogMessage result) {
            Logger.this.mDataStore.remove(result);
        }

        public void onFailure(UpsightException exception) {
        }
    }

    public static final class LogSubscriber {
        private final EnumSet<Level> mLevels;
        private final Pattern mTag;
        private final LogWriter mWriter;

        public LogSubscriber(String tag, EnumSet<Level> levels, LogWriter writer) {
            this.mTag = Pattern.compile(tag);
            this.mLevels = levels;
            this.mWriter = writer;
        }

        @Created
        public void onLogMessageCreated(LogMessage message) {
            if (this.mTag.matcher(message.getTag()).matches() && this.mLevels.contains(message.getLevel())) {
                this.mWriter.write(message.getTag(), message.getLevel(), message.getMessage());
            }
        }
    }

    public static Logger create(UpsightDataStore dataStore, LogWriter writer) {
        return new Logger(dataStore, writer);
    }

    Logger(UpsightDataStore upsightDataStore, LogWriter logWriter) {
        this.mSubscriptionsMap = new ConcurrentHashMap();
        this.mDataStore = upsightDataStore;
        this.mLogWriter = logWriter;
    }

    public void m600v(String tag, String message, Object... args) {
        log(Level.VERBOSE, tag, null, message, args);
    }

    public void m601v(String tag, Throwable tr, String message, Object... args) {
        log(Level.VERBOSE, tag, tr, message, args);
    }

    public void m594d(String tag, String message, Object... args) {
        log(Level.DEBUG, tag, null, message, args);
    }

    public void m595d(String tag, Throwable tr, String message, Object... args) {
        log(Level.DEBUG, tag, tr, message, args);
    }

    public void m598i(String tag, String message, Object... args) {
        log(Level.INFO, tag, null, message, args);
    }

    public void m599i(String tag, Throwable tr, String message, Object... args) {
        log(Level.INFO, tag, tr, message, args);
    }

    public void m602w(String tag, String message, Object... args) {
        log(Level.WARN, tag, null, message, args);
    }

    public void m603w(String tag, Throwable tr, String message, Object... args) {
        log(Level.WARN, tag, tr, message, args);
    }

    public void m596e(String tag, String message, Object... args) {
        log(Level.ERROR, tag, null, message, args);
    }

    public void m597e(String tag, Throwable tr, String message, Object... args) {
        log(Level.ERROR, tag, tr, message, args);
    }

    public void setLogLevel(String logTagRegularExpression, EnumSet<Level> levels) {
        if (TextUtils.isEmpty(logTagRegularExpression)) {
            throw new IllegalArgumentException("Log tag can not be null or empty.");
        }
        UpsightSubscription previous = (UpsightSubscription) this.mSubscriptionsMap.put(logTagRegularExpression, this.mDataStore.subscribe(new LogSubscriber(logTagRegularExpression, levels, this.mLogWriter)));
        if (previous != null) {
            previous.unsubscribe();
        }
    }

    private void log(Level priority, String tag, Throwable t, String message, Object... args) {
        StringWriter writer = new StringWriter();
        if (t != null) {
            t.printStackTrace(new PrintWriter(writer));
        }
        this.mDataStore.store(new LogMessage(tag, priority, formatString(message, args), writer.getBuffer().toString()), new C11191());
    }

    private static String formatString(String message, Object... args) {
        return args.length == 0 ? message : String.format(message, args);
    }
}
